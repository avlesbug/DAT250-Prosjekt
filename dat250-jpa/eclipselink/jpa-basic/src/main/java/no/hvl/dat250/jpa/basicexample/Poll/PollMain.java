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
        pollUser.addPoll(poll2);
        System.out.println(pollUser.getPollList());


        em.getTransaction().begin();

        em.persist(pollUser);
        em.persist(poll);
        em.persist(pollUser2);
        em.persist(poll2);
        em.persist(vote);
        em.persist(vote2);

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


        em.getTransaction().commit();
        em.close();

        Long pollId = poll.getId();
        Long pollId2 = poll2.getId();

        Long userId = pollUser.getId();
        Long userId2 = pollUser2.getId();

        Long voteId = vote.getId();
        Long voteId2 = vote2.getId();


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

            return pollMap.get(id).toJson();
        });

        get("/votes/:id", (req, res) -> {
            Gson gson = new Gson();
            Long id = Long.parseLong(req.params("id"));
            //return gson.toJson(userMap.get(id));
            return voteMap.get(id).toJson();
        });
        //Put
        put("/votes/:id", (req, res) -> {

            Gson gson = new Gson();
            em.getTransaction().begin();

            Vote vote = gson.fromJson(req.body(), Vote.class);

            Long id = Long.parseLong(req.params("id"));

            voteMap.put(id, vote);

            em.persist(vote);
            em.merge(vote);
            em.getTransaction().commit();

            return voteMap.get(id).toJson();
        });

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

        post("/votes", (req, res) -> {
            Gson gson = new Gson();
            em.getTrnsaction().begin();

            Vote vote = gson.fromJson(req.body(), Vote.class);

            em.persist(vote);
            em.getTransaction().commit();

            Long id = vote.getId();

            voteMap.put(id, vote);

            return vote.toJson();

        });

        post("/polls", (req, res) -> {
            Gson gson = new Gson();

            Poll tempPoll = gson.fromJson(req.body(), Poll.class);

            ema.getTransaction().begin();
            ema.persist(tempPoll);
            ema.merge(tempPoll.getPollUser());
            System.out.println(tempPoll.getPollUser().getPollList());
            tempPoll.getPollUser().addPoll(tempPoll);
            System.out.println(tempPoll.getPollUser().getPollList());
            ema.getTransaction().commit();

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

        delete("/votes/:id", (req, res) -> {
            Gson gson = new Gson();

            Long id = Long parseLong(req.params("id"));

            voteMap.remove(id);

            return gson.toJson(voteMap);
        });

        delete("/votes", (req, res) -> {
            Gson gson = new Gson();
            voteMap = new HashMap<>();
            return gson.toJson(voteMap);
        });

        delete("/polls/:id", (req, res) -> {
            Gson gson = new Gson();

            Long id = Long.parseLong(req.params("id"));

            pollMap.remove(id);

            return gson.toJson(pollMap);

        });

        delete("/polls", (req, res) -> {
            Gson gson = new Gson();
            pollMap = new HashMap<>();
            return gson.toJson(pollMap);

        });

        delete("/users/:id", (req, res) -> {
            Gson gson = new Gson();

            Long id = Long.parseLong(req.params("id"));

            userMap.remove(id);

            return gson.toJson(pollMap);

        });

        delete("/users", (req, res) -> {
            Gson gson = new Gson();
            userMap = new HashMap<>();
            return gson.toJson(pollMap);

        });

    }

}
