package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class PollUserDAO{
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);


    public EntityManager start(){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        return  em;
    }

    public void stop(EntityManager em){
        em.getTransaction().commit();
        em.close();
    }

    public void persistPollUser(PollUser pollUser){
        EntityManager em = start();
        em.persist(pollUser);
        stop(em);
    }

    public void updatePollUser(PollUser pollUser){
        EntityManager em = start();
        em.merge(pollUser);
        stop(em);
    }

    public void deleteUser(PollUser pollUser){
        EntityManager em = start();
        em.remove(em.merge(pollUser));
        stop(em);

    }

    public List<PollUser> getUsers(){
        EntityManager em = start();
        Query q = em.createQuery("select u from PollUser u");
        List<PollUser> allUsers = q.getResultList();
        stop(em);
        return allUsers;
    }

    public PollUser findById(Long id){
        EntityManager em = start();
        PollUser user = em.find(PollUser.class,id);
        stop(em);
        return user;
    }

    public List<Integer> getPollIds(Long id){
        EntityManager em = start();
        PollUser user = findById(id);
        List<Integer> pollIds = new ArrayList<>();
        List<Poll> polls = user.getPollList();
        for(Poll p : polls){
            pollIds.add(p.getId().intValue());
        }
        stop(em);
        return pollIds;
    }

}
