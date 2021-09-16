package no.hvl.dat250.jpa.basicexample.Poll;

import no.hvl.dat250.jpa.basicexample.Poll.Poll;
import no.hvl.dat250.jpa.basicexample.Poll.User;
import no.hvl.dat250.jpa.basicexample.Poll.Vote;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import static org.junit.Assert.assertTrue;

public class PollMain {
    private static final String PERSISTENCE_UNIT_NAME = "user";
    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        // Begin a new local transaction so that we can persist a new entity
        em.getTransaction().begin();

        // read the existing entries
        Query q = em.createQuery("select u from User u");
        // Persons should be empty

        // do we have entries?
        boolean createNewEntries = (q.getResultList().size() == 0);

        // No, so lets create new entries
        if (createNewEntries) {
            assertTrue(q.getResultList().size() == 0);

            //Create user
            User user = new User();
            user.setName("Max Musterman");
            user.setEmail("m.musterman@gmail.com");

            //Create poll & votes

            Poll poll = new Poll();
            poll.setName("Simple poll");
            poll.setQuestion("Is this a question?");

            List<Vote> votes = new ArrayList<>();
            Vote vote = new Vote();
            vote.setAnswer(1);
            Vote vote2 = new Vote();
            vote.setAnswer(0);
            votes.add(vote);
            votes.add(vote2);
            poll.setVotes(votes);

            em.persist(user);
            em.persist(poll);
            em.persist(vote);
            em.persist(vote2);
            em.persist(votes);

        }

        // Commit the transaction, which will cause the entity to
        // be stored in the database
        em.getTransaction().commit();

        // It is always good practice to close the EntityManager so that
        // resources are conserved.
        // read the existing entries and write to console

        em.close();

    }
}

