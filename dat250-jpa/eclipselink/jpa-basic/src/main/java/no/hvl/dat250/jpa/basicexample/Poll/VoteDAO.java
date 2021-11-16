package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class VoteDAO {
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

    public boolean persistVote(Vote vote){
        EntityManager em = start();
        em.persist(vote);
        Poll tempPoll = em.find(Poll.class, vote.getPollId());
        if(tempPoll.isActive()) {
            vote.setPoll(tempPoll);
            tempPoll.addVote(vote);
            return true;
        }
        stop(em);
        return false;
    }

    public void updateVote(Vote newVote,Long id){
        EntityManager em = start();
        Vote oldVote = findById(id);
        newVote.setId(oldVote.getId());
        newVote.setPoll(oldVote.getPoll());
        em.merge(newVote);
        stop(em);
    }

    public void deleteVote(Vote vote){
        EntityManager em = start();
        if(vote!=null) {
            em.remove(em.merge(vote));
            em.getTransaction().commit();
        }
        em.close();

    }

    public List<Vote> getVotes(){
        EntityManager em = start();
        Query q = em.createQuery("select v from Vote v");
        List<Vote> allVotes = q.getResultList();
        stop(em);
        return allVotes;
    }

    public Vote findById(Long id){
        EntityManager em = start();
        Vote vote = em.find(Vote.class,id);
        stop(em);
        return vote;
    }


}
