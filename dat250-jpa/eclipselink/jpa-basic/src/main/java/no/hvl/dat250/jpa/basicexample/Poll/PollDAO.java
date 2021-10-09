
package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import java.util.List;

public class PollDAO {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);


    public void persistPoll(Poll poll) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        PollUser pollUser = em.find(PollUser.class,poll.getPollUserId());
        em.persist(poll);
        poll.setPollUser(pollUser);
        pollUser.addPoll(poll);
        em.getTransaction().commit();
        em.close();
    }

    public void updatePoll(Poll poll){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.merge(poll);
        em.getTransaction().commit();
        em.close();
    }



    public void deletePoll(Poll poll){
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
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
        System.out.println("Deleted poll");
        em.close();

    }
}