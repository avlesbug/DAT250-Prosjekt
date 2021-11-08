
package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class PollDAO {
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

    public void persistPoll(Poll poll) {
        EntityManager em = start();
        PollUser pollUser = em.find(PollUser.class,poll.getPollUserId());
        em.persist(poll);
        poll.setPollUser(pollUser);
        pollUser.addPoll(poll);
        stop(em);
    }

    public void updatePoll(Poll newPoll, Long id){
        EntityManager em = start();
        Poll oldPoll = findById(id);
        newPoll.setPollUser(oldPoll.getPollUser());
        newPoll.setId(oldPoll.getId());
        newPoll.setVotes(oldPoll.getVotes());
        em.merge(newPoll);
        stop(em);
    }



    public void deletePoll(Poll poll){
        EntityManager em = start();
        PollUser user = poll.getPollUser();
        if(user!=null) {
            user.removePoll(poll);
            poll.setPollUser(null);
            List<Vote> votes = poll.getVotes();
            for (Vote v : votes) {
                if(v!=null) {
                    v.setPoll(null);
                    em.merge(v);
                }
            }
            em.merge(user);
            em.remove(em.merge(poll));
            for (Vote v : votes) {
                if(v!=null) {
                    em.remove(em.merge(v));
                }
            }
            em.getTransaction().commit();
        }
        em.close();

    }

    public List<Poll> getPolls(){
        EntityManager em = start();
        Query q = em.createQuery("select p from Poll p");
        List<Poll> allPolls = q.getResultList();
        stop(em);
        return allPolls;
    }

    public Poll findById(Long id){
        EntityManager em = start();
        Poll poll = em.find(Poll.class,id);
        stop(em);
        return poll;
    }

    public String getVotesforPoll(Long id){
        EntityManager em = start();
        Poll poll = em.find(Poll.class,id);
        int opt1Votes = poll.getOpt1Votes();
        int opt2Votes = poll.getOpt2Votes();
        stop(em);
        return poll.getOpt1().toString() + " votes: " + opt1Votes + ", " + poll.getOpt2().toString() +" votes " + opt2Votes;
    }

    public Result getResults(Long id) {
        Poll poll = findById(id);
        Result result = new Result(poll.getQuestion(),poll.getOpt1(),poll.getOpt2(),poll.getId(),poll.getPollUserId(),poll.getOpt1Votes(),poll.getOpt2Votes());

        return result;
    }
}