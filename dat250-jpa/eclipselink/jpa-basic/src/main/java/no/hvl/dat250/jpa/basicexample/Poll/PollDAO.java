
package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import java.util.List;

public class PollDAO {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory;


    @PersistenceUnit
    public void setEmf(EntityManagerFactory emf){
        this.factory = emf;
    }

    public Poll persistPoll(Poll poll) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Poll savedPoll = em.merge(poll);
        em.getTransaction().commit();
        return savedPoll;
    }

    public Poll updateVote(Poll poll, Vote vote){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        poll.addVote(vote);
        Poll savedPoll = em.merge(poll);
        em.getTransaction().commit();
        return savedPoll;
    }

    public Poll updateVotes(Poll poll, List<Vote> newVotes){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        List<Vote> votes = poll.getVotes();
        votes.addAll(newVotes);
        poll.setVotes(votes);
        Poll savedPoll = em.merge(poll);
        em.getTransaction().commit();
        return savedPoll;
    }


    public void deletePoll(){

    }
}