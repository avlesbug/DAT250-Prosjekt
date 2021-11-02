package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class PollUserDAO {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);


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
        em.remove(em.merge(pollUser));
        em.getTransaction().commit();
        em.close();

    }

    public List<PollUser> getUsers(){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("select u from PollUser u");
        List<PollUser> allUsers = q.getResultList();
        em.getTransaction().commit();
        em.close();
        return allUsers;
    }

    public PollUser findById(Long id){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        PollUser user = em.find(PollUser.class,id);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    public List<Integer> getPollIds(Long id){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        PollUser user = em.find(PollUser.class,id);
        List<Integer> pollIds = new ArrayList<>();
        List<Poll> polls = user.getPollList();
        for(Poll p : polls){
            pollIds.add(p.getId().intValue());
        }
        em.getTransaction().commit();
        em.close();
        return pollIds;
    }

}
