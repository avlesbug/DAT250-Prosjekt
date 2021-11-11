package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;
import java.awt.*;
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

    public void updatePollUser(PollUser newUser, Long id){
        EntityManager em = start();
        PollUser oldUser = findById(id);
        newUser.setPollList(oldUser.getPollList());
        newUser.setId(oldUser.getId());
        em.merge(newUser);
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

    public PollUser findByMail(String email){
        EntityManager em = start();
        for(PollUser p : getUsers()){
            if(p.getEmail().equals(email)){
                return p;
            }
        }
        stop(em);
        return null;
    }

    public Boolean login(LoginForm login){
        PollUser user = findByMail(login.getEmail());
        String passwordIn = login.getPassword();
        return SCryptUtil.check(passwordIn,user.getPassword());
    }
}
