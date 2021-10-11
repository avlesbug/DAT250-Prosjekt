package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class VoteDAO {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public void persistVote(Vote vote){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(vote);
        Poll tempPoll = em.find(Poll.class, vote.getPollId());
        vote.setPoll(tempPoll);
        tempPoll.addVote(vote);
        em.getTransaction().commit();
        em.close();
    }

    public void updateVote(Vote vote){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.merge(vote);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteVote(Vote vote){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        if(vote!=null) {
            em.remove(em.merge(vote));
            em.getTransaction().commit();
        }
        em.close();

    }

    public List<Vote> getVotes(){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("select v from Vote v");
        List<Vote> allVotes = q.getResultList();
        em.getTransaction().commit();
        em.close();
        return allVotes;
    }

    public Vote findById(Long id){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Vote vote = em.find(Vote.class,id);
        em.getTransaction().commit();
        em.close();
        return vote;
    }


}
