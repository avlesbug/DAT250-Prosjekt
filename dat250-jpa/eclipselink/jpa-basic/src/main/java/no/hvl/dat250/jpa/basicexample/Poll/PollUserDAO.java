package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;
import java.util.List;

public class PollUserDAO {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public PollUser findById(Long pollUserId) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        if(pollUserId!=null) {
            PollUser pollUser = em.find(PollUser.class, pollUserId);
            if (pollUser == null) {
                throw new EntityNotFoundException("Can't find pollUser for ID: "
                        + pollUserId);
            }
            return pollUser;
        }
        return null;
    }

    public void persistPollUser(PollUser pollUser){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(pollUser);
        em.getTransaction().commit();
        em.close();
    }

    public void updatePollUser(PollUser pollUser){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.merge(pollUser);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteUser(PollUser pollUser){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        if(em.contains(pollUser)) {
            em.remove(em.merge(pollUser));
            em.getTransaction().commit();
        }
        em.close();

    }

}
