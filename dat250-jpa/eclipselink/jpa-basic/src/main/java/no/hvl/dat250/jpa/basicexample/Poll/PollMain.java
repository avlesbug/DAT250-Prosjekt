package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;

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
    static HashMap<Integer, PollUser> userMap = new HashMap<>();

    public static void main(String[] args) {

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        // Begin a new local transaction so that we can persist a new entity
        em.getTransaction().begin();


        // read the existing entries
        Query q = em.createQuery("select u from PollUser u");
        // Persons should be empty

        // do we have entries?
        boolean createNewEntries = (q.getResultList().size() == 0);

        PollUser pollUser = new PollUser("Max Musterman", "max.musterman@gmail.com", "Passord123");
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
            Poll poll = new Poll("Poll name", "Poll question #1", pollUser);
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

        // Commit the transaction, which will cause the entity to
        // be stored in the database
        em.getTransaction().commit();

        // It is always good practice to close the EntityManager so that
        // resources are conserved.
        // read the existing entries and write to console

        em.close();


        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        userMap.put(0, pollUser);

        after((req, res) -> {
            res.type("application/json");
        });

        get("/hello", (req, res) -> "Hello World!");

        get("/users", (req, res) -> {
            Gson gson = new Gson();
            return gson.toJson(userMap);
        });

        get("/users/:id", (req, res) -> {
            Gson gson = new Gson();
            int id = Integer.parseInt(req.params("id"));

            return userMap.get(id).toJson();
        }
        );

        put("/users/:id", (req, res) -> {

            Gson gson = new Gson();

            int id = Integer.parseInt(req.params("id"));

            userMap.put(id, gson.fromJson(req.body(), PollUser.class));

            return userMap.get(id).toJson();

        });


        post("/users", (req, res) -> {
            Gson gson = new Gson();

            int id = userMap.size();

            userMap.put(id, gson.fromJson(req.body(), PollUser.class));

            return userMap.get(id).toJson();

        });

        delete("/users/:id", (req, res) -> {
            Gson gson = new Gson();

            int id = Integer.parseInt(req.params("id"));

            userMap.remove(id);

            return gson.toJson(userMap);

        });

        delete("/users", (req, res) -> {
            Gson gson = new Gson();
            userMap = new HashMap<>();
            return gson.toJson(userMap);

        });

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

