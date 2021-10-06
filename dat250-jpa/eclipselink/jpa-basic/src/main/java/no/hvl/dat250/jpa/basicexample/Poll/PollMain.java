package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;
import lombok.Lombok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import static org.junit.Assert.assertTrue;
import static spark.Spark.*;
import static spark.Spark.delete;

public class PollMain {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory;
    static HashMap<Long, Poll> pollMap = new HashMap<>();
    static HashMap<Long, PollUser> userMap = new HashMap<>();
    static  HashMap<Long, Vote> voteMap = new HashMap<>();

    public static void main(String[] args) {

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();



        // read the existing entries
        Query q = em.createQuery("select u from PollUser u");
        // Persons should be empty

        // do we have entries?
        boolean createNewEntries = (q.getResultList().size() == 0);

        PollUser pollUser = new PollUser("Max Musterman", "max.musterman@gmail.com", "Passord123");
        PollUser pollUser2 = new PollUser("Maxine Musterwoman", "maxint.musterwoman@gmail.com", "Passord123");

        Poll poll = new Poll("My first poll","Can you attend my birthday party?", pollUser);
        Poll poll2 = new Poll("My second poll","Can you host my birthday party?", pollUser2);

        Vote vote = new Vote(Answer.NO, poll);
        Vote vote2 = new Vote(Answer.YES, poll2);

        //poll.addVote(vote);
        //poll2.addVote(vote2);
        pollUser.addPoll(poll);


        em.getTransaction().begin();/*

        em.persist(pollUser);
        em.persist(poll);
        em.persist(pollUser2);
        em.persist(poll2);
        em.persist(vote);
        em.persist(vote2);
        System.out.println("test2");*/

        List<Vote> newVotes = new ArrayList<>();
        for(int i=0; i<20;i++){
            Vote newVote;
            if(i%2==1){
                newVote = new Vote(Answer.YES,poll);
            } else {
                newVote = new Vote(Answer.NO,poll);
            }
            em.persist(newVote);
            newVotes.add(newVote);
            voteMap.put(newVote.getId(),newVote);
        }
        poll.setVotes(newVotes);


        //em.getTransaction().commit();
        em.close();

        Long pollId = poll.getId();
        Long pollId2 = poll2.getId();

        Long userId = pollUser.getId();
        Long userId2 = pollUser2.getId();

        Long voteId = vote.getId();
        Long voteId2 = vote2.getId();

        System.out.println(pollId + " + " + userId);


        // No, so lets create new entries

        /**
        if (createNewEntries) {
            assertTrue(q.getResultList().size() == 0);

            PollDAO pollDAO = new PollDAO();
            pollDAO.setEmf(factory);
            PollUserDAO pollUserDAO = new PollUserDAO();
            pollUserDAO.setEmf(factory);


            pollUserDAO.persistPollUser(pollUser);

            List<Poll> pollList = new ArrayList<>();
            //Poll poll2 = new Poll("Poll name", "Poll question #1", pollUser);
            //Poll poll2 = new Poll("Second poll", "Do you like polls?", pollUser);
            pollList.add(poll);
            pollUser.setPollList(pollList);




            List<Vote> newVotes = new ArrayList<>();
            for(int i=0; i<20;i++){
                Vote newVote;
                if(i%2==1){
                    newVote = new Vote(Answer.YES,poll);
                } else {
                    newVote = new Vote(Answer.NO,poll);
                }
                newVotes.add(newVote);
            }
            poll.setVotes(newVotes);

            pollDAO.persistPoll(poll);
        }
         **/


        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        pollMap.put(pollId, poll);
        userMap.put(userId, pollUser);
        pollMap.put(pollId2, poll2);
        userMap.put(userId2, pollUser2);
        voteMap.put(voteId,vote);
        voteMap.put(voteId2,vote2);

        System.out.println(pollMap);
        System.out.println(userMap);

        after((req, res) -> {
            res.type("application/json");
        });

        EntityManager ema = factory.createEntityManager();;

        //Get

        get("/polls", (req, res) -> {
            Gson gson = new Gson();
            StringBuilder string = new StringBuilder();
            for(Poll p : pollMap.values()){
                string.append(p.toJson());
                string.append(',');
            }
            string.deleteCharAt(string.length()-1);
            return gson.toJson(string);
        });

        get("/users", (req, res) -> {
            Gson gson = new Gson();
            StringBuilder string = new StringBuilder();
            for(PollUser u : userMap.values()){
                string.append(u.toJson());
                string.append(',');
            }
            string.deleteCharAt(string.length()-1);
            return gson.toJson(string);
        });

        get("/votes", (req, res) -> {
            Gson gson = new Gson();
            StringBuilder string = new StringBuilder();
            for(Vote v : voteMap.values()){
                string.append(v.toJson());
                string.append(',');
            }
            string.deleteCharAt(string.length()-1);
            return gson.toJson(string);
            //return voteMap.get(voteId).toJson();
        });

        get("/users/:id", (req, res) -> {
            Gson gson = new Gson();
            Long id = Long.parseLong(req.params("id"));
            //return gson.toJson(userMap.get(id));
            return userMap.get(id).simpleToJson();
        });

        get("/polls/:id", (req, res) -> {
            Gson gson = new Gson();
            Long id = Long.parseLong(req.params("id"));
            System.out.println(pollMap.get(id));
            return pollMap.get(id).toJson();
        });

        get("/votes/:id", (req, res) -> {
            Gson gson = new Gson();
            Long id = Long.parseLong(req.params("id"));
            //return gson.toJson(userMap.get(id));
            return voteMap.get(id).toJson();
        });
        //Put

        put("/polls/:id", (req, res) -> {

            Gson gson = new Gson();

            Poll tempPoll = gson.fromJson(req.body(), Poll.class);

            Long id = Long.parseLong(req.params("id"));

            pollMap.put(id, tempPoll);

            ema.getTransaction().begin();
            ema.persist(tempPoll);
            ema.merge(tempPoll.getPollUser());
            ema.getTransaction().commit();

            return pollMap.get(id).toJson();

        });

        put("/users/:id", (req, res) -> {
            Gson gson = new Gson();

            PollUser user = gson.fromJson(req.body(), PollUser.class);

            Long id = Long.parseLong(req.params("id"));

            user.setId(id);

            //userMap.remove(id);

            userMap.put(id,user);

            ema.getTransaction().begin();
            ema.merge(user);
            ema.getTransaction().commit();


            return user.toJson();

        });


        //Post

        post("/polls", (req, res) -> {
            Gson gson = new Gson();

            Poll tempPoll = gson.fromJson(req.body(), Poll.class);

            ema.getTransaction().begin();
            ema.persist(tempPoll);
            ema.merge(tempPoll.getPollUser());
            ema.getTransaction().commit();
            System.out.println(tempPoll.toJson());
            Long id = tempPoll.getId();

            pollMap.put(id, tempPoll);

            return tempPoll.toJson();

        });

        post("/users", (req, res) -> {
            Gson gson = new Gson();

            PollUser user = gson.fromJson(req.body(), PollUser.class);

            ema.getTransaction().begin();

            ema.persist(user);
            ema.getTransaction().commit();

            Long id = user.getId();

            userMap.put(id, user);


            return user.toJson();
        });

        //Delete

        delete("/polls/:id", (req, res) -> {
            Gson gson = new Gson();

            Long id = Long.parseLong(req.params("id"));

            ema.getTransaction().begin();
            ema.remove(pollMap.get(id));
            ema.getTransaction().commit();
            pollMap.remove(id);
            StringBuilder string = new StringBuilder();
            for(Poll p : pollMap.values()){
                string.append(p.toJson());
                string.append(',');
            }
            string.deleteCharAt(string.length()-1);
            return gson.toJson(string);

        });

        delete("/polls", (req, res) -> {
            Gson gson = new Gson();

            pollMap = new HashMap<>();
            return gson.toJson(pollMap);

        });

        delete("/users/:id", (req, res) -> {
            Gson gson = new Gson();

            Long id = Long.parseLong(req.params("id"));
            ema.getTransaction().begin();
            ema.remove(userMap.get(id));
            ema.getTransaction().commit();
            userMap.remove(id);

            return gson.toJson(userMap);

        });

        delete("/users", (req, res) -> {
            Gson gson = new Gson();
            userMap = new HashMap<>();
            return gson.toJson(pollMap);

        });


        System.out.println("Ran main");

    }

}


/**
 * Gammel Main
 * if (createNewEntries) {
 *             assertTrue(q.getResultList().size() == 0);
 *
 *             //Create user
 *             PollUser pollUser = new PollUser();
 *             PollUser pollUser2 = new PollUser();
 *             pollUser.setName("Max Musterman");
 *             pollUser2.setName("Ola Normann");
 *             pollUser2.setEmail("ola.normann@gmail.com");
 *             pollUser.setEmail("m.musterman@gmail.com");
 *             pollUser.setPassword("Password123");
 *             pollUser2.setPassword("Spania2009");
 *
 *             //Create poll & votes
 *
 *             Poll poll = new Poll();
 *             Poll poll2 = new Poll();
 *             List<Poll> pollList = new ArrayList<>();
 *             poll.setName("Simple poll");
 *             poll.setQuestion("Is this a question?");
 *             poll.setUser(pollUser);
 *             poll2.setName("Second poll");
 *             poll2.setQuestion("Do you like taco?");
 *             poll2.setUser(pollUser);
 *             pollList.add(poll);
 *             pollList.add(poll2);
 *             pollUser.setPollList(pollList);
 *
 *
 *
 *             List<Vote> votes = new ArrayList<>();
 *             Vote vote = new Vote();
 *             vote.setAnswer(1);
 *             Vote vote2 = new Vote();
 *             vote2.setAnswer(0);
 *             vote.setPoll(poll);
 *             vote2.setPoll(poll);
 *             votes.add(vote);
 *             votes.add(vote2);
 *             poll.setVotes(votes);
 *
 *             em.persist(pollUser);
 *             em.persist(poll);
 *             em.persist(poll2);
 *             em.persist(vote);
 *             em.persist(vote2);
 *             em.persist(pollUser2);
 *
 */

