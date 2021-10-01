package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceUnit;
import java.util.List;

public class PollUserDAO {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory;


    @PersistenceUnit
    public void setEmf(EntityManagerFactory emf) {
        this.factory = emf;
    }

    public PollUser findById(Long pollUserId) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        if(pollUserId!=null) {
            PollUser pollUser = em.find(PollUser.class, pollUserId);
            if (pollUser == null) {
                throw new EntityNotFoundException("Can't find Artist for ID "
                        + pollUserId);
            }
            return pollUser;
        }
        return null;
    }

    public PollUser persistPollUser(PollUser pollUser){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        PollUser savedPollUser = em.merge(pollUser);
        em.getTransaction().commit();
        return savedPollUser;
    }

    public PollUser updatePollList(PollUser pollUser, Poll poll){
        if(pollUser.getId()!=null) {
            PollUser user = findById(pollUser.getId());
            EntityManager em = factory.createEntityManager();
            em.getTransaction().begin();
            List polls = pollUser.getPollList();
            polls.add(poll);
            user.setPollList(polls);
            PollUser savedPollUser = em.merge(user);
            em.getTransaction().commit();
            return savedPollUser;
        }
        return null;
    }

}
