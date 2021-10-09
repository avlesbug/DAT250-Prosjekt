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
    static HashMap<Long, Vote> voteMap = new HashMap<>();



    public static void main(String[] args) {

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();



        // read the existing entries
        Query q = em.createQuery("select u from PollUser u");
        // Persons should be empty

        // do we have entries?
        boolean createNewEntries = (q.getResultList().size() == 0);

        if (createNewEntries) {
            PollUser pollUser = new PollUser("Max Musterman", "max.musterman@gmail.com", "Passord123");
            PollUser pollUser2 = new PollUser("Maxine Musterwoman", "maxint.musterwoman@gmail.com", "Passord123");

            em.getTransaction().begin();

            em.persist(pollUser);
            em.persist(pollUser2);


            Long userId = pollUser.getId();
            Long userId2 = pollUser2.getId();

            Poll poll = new Poll("My first poll", "Can you attend my birthday party?", true, userId);
            Poll poll2 = new Poll("My second poll", "Can you host my birthday party?", false, userId2);

            em.persist(poll);
            em.persist(poll2);

            Long pollId = poll.getId();
            Long pollId2 = poll2.getId();

            poll.setPollUser(em.find(PollUser.class, userId));
            poll2.setPollUser(em.find(PollUser.class, userId2));

            pollUser.addPoll(poll);
            pollUser2.addPoll(poll2);


            List<Vote> newVotes = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Vote newVote;
                if (i % 2 == 1) {
                    newVote = new Vote(Answer.YES, pollId);
                } else {
                    newVote = new Vote(Answer.NO, pollId);
                }
                newVote.setPoll(poll);
                em.persist(newVote);
                newVotes.add(newVote);
                voteMap.put(newVote.getId(), newVote);
            }
            poll.setVotes(newVotes);

            em.getTransaction().commit();
            em.close();



            pollMap.put(pollId, poll);
            userMap.put(userId, pollUser);
            pollMap.put(pollId2, poll2);
            userMap.put(userId2, pollUser2);

        } else{
            //Add entities in jpa db to hashmaps

            //Users
            List<PollUser> users = q.getResultList();
            for (PollUser u : users){
                userMap.put(u.getId(),u);
            }

            Query qp = em.createQuery("select p from Poll p");

            //Polls
            List<Poll> polls = qp.getResultList();
            for (Poll p : polls){
                pollMap.put(p.getId(),p);
            }

            Query qv = em.createQuery("select v from Vote v");

            //Polls
            List<Vote> votes = qv.getResultList();
            for (Vote v : votes){
                voteMap.put(v.getId(),v);
            }

        }


        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        after((req, res) -> {
            res.type("application/json");
        });

        EntityManager ema = factory.createEntityManager();
        PollDAO pollDAO = new PollDAO();
        PollUserDAO pollUserDAO = new PollUserDAO();
        VoteDAO voteDAO = new VoteDAO();

        //Get

        get("/polls", (req, res) -> {
            Gson gson = new Gson();
            try {
                StringBuilder string = new StringBuilder();
                for (Poll p : pollMap.values()) {
                    string.append(p.toJson());
                    string.append("<---->");
                }
                if (string.length() > 1) {
                    string.deleteCharAt(string.length() - 1);
                    string.delete(string.length() - 6,string.length() - 1);
                }
                return gson.toJson(string);

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/polls/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                return pollMap.get(id).simpleToJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/users", (req, res) -> {
            Gson gson = new Gson();
            try {
                StringBuilder string = new StringBuilder();
                for (PollUser u : userMap.values()) {
                    string.append(u.toJson());
                    string.append(',');
                }
                if (string.length() > 1) {
                    string.deleteCharAt(string.length() - 1);
                }
                return gson.toJson(string);

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/users/:id", (req, res) -> {
            Gson gson = new Gson();
            try{
            Long id = Long.parseLong(req.params("id"));
            return userMap.get(id).toJson();
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/votes", (req, res) -> {
            Gson gson = new Gson();
            StringBuilder string = new StringBuilder();
            try {
                for (Vote v : voteMap.values()) {
                    string.append(v.toJson());
                    string.append(',');
                }
                if (string.length() > 1) {
                    string.deleteCharAt(string.length() - 1);
                }
                return gson.toJson(string);

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/votes/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                return voteMap.get(id).toJson();
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });
        //Put
        put("/polls/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                Poll oldPoll = pollMap.get(id);
                Poll newPoll = gson.fromJson(req.body(), Poll.class);

                newPoll.setPollUser(oldPoll.getPollUser());
                newPoll.setId(oldPoll.getId());
                newPoll.setVotes(oldPoll.getVotes());

                pollDAO.updatePoll(newPoll);

                pollMap.put(id, newPoll);

                return newPoll.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }

        });

        put("/users/:id", (req, res) -> {
            Gson gson = new Gson();
            ema.getTransaction().begin();
            try {
                PollUser user = gson.fromJson(req.body(), PollUser.class);
                Long id = Long.parseLong(req.params("id"));
                user.setId(id);
                userMap.put(id, user);
                ema.merge(user);
                ema.getTransaction().commit();

                return user.toJson();
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                ema.getTransaction().commit();
                return gson.toJson("Something went wrong...");
            }

        });

        put("/votes/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Vote tempVote = gson.fromJson(req.body(), Vote.class);

                Long id = Long.parseLong(req.params("id"));

                Vote oldVote = voteMap.get(id);
                tempVote.setId(oldVote.getId());
                tempVote.setPoll(oldVote.getPoll());

                voteMap.put(id, tempVote);

                voteDAO.updateVote(tempVote);

                return voteMap.get(id).toJson();


            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });


        //Post
        post("/polls", (req, res) -> {
            Gson gson = new Gson();
            try {
                Poll tempPoll = gson.fromJson(req.body(), Poll.class);
                pollDAO.persistPoll(tempPoll);
                Long id = tempPoll.getId();
                pollMap.put(id, tempPoll);

                return tempPoll.toJson();
            }catch (Exception e) {
                return gson.toJson("Something went wrong...");
            }


        });

        post("/users", (req, res) -> {
            Gson gson = new Gson();
            try {
                PollUser user = gson.fromJson(req.body(), PollUser.class);
                pollUserDAO.persistPollUser(user);

                Long id = user.getId();

                userMap.put(id, user);

                return user.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                ema.getTransaction().commit();
                return gson.toJson("Something went wrong...");
            }
        });

        post("/votes", (req, res) -> {
            Gson gson = new Gson();
            try{
                Vote tempVote = gson.fromJson(req.body(), Vote.class);
                voteDAO.persistVote(tempVote);

                Long id = tempVote.getId();

                voteMap.put(id, tempVote);

                return tempVote.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        //Delete

        delete("/polls/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                Poll deletedPoll = pollMap.get(id);
                pollDAO.deletePoll(pollMap.get(id));
                pollMap.remove(id);
                return gson.toJson("Deleted poll: " + deletedPoll.toJson());

            }catch (Exception e) {
                return gson.toJson("Something went wrong...");
            }
        });

        delete("/polls", (req, res) -> {
            Gson gson = new Gson();
            try {
                for(Long pollId : pollMap.keySet()){
                    pollDAO.deletePoll(em.find(Poll.class, pollId));
                }
                pollMap = new HashMap<>();
                return gson.toJson(pollMap);
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        delete("/users/:id", (req, res) -> {
            Gson gson = new Gson();
            Long id = Long.parseLong(req.params("id"));
            PollUser tempUser = userMap.get(id);
            List<Poll> polls = tempUser.getPollList();
            if(polls!=null) {
                while (polls.size()>1) {
                    Poll poll = polls.get(polls.size()-1);
                    pollDAO.deletePoll(poll);
                }
            }
            pollUserDAO.deleteUser(tempUser);
            userMap.remove(id);
            return gson.toJson("Deleted: ") + tempUser.toJson();

        });

        delete("/users", (req, res) -> {
            Gson gson = new Gson();
            //try {
                for(PollUser u : userMap.values()) {
                    if (u != null) {
                        List<Poll> polls = u.getPollList();
                        while (polls.size()>1) {
                            Poll deletedPoll = polls.get(polls.size()-1);
                            deletedPoll.setPollUser(null);
                            if(deletedPoll!=null) {
                                pollDAO.deletePoll(deletedPoll);
                                //pollMap.remove(deletedPoll.getId());
                            }
                        }
                        u.setPollList(null);
                        pollUserDAO.deleteUser(u);
                    }
                }
                userMap = new HashMap<>();
                return gson.toJson("Deleted all users");
/**
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
**/

        });

        delete("/votes/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                Vote deletedVote = voteMap.get(id);
                voteDAO.deleteVote(deletedVote);
                voteMap.remove(id);
                return gson.toJson("Deleted: ") + deletedVote.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        delete("/votes", (req, res) -> {
            Gson gson = new Gson();
            try {
                for(Long voteId : voteMap.keySet()){
                    voteDAO.deleteVote(em.find(Vote.class, voteId));
                }
                voteMap = new HashMap<>();
                return gson.toJson("Deleted all votes");

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

    }

}

