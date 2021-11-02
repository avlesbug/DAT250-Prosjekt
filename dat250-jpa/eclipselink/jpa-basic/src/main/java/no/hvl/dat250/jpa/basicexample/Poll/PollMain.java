package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;
import spark.Filter;

import java.util.ArrayList;
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

            Poll poll = new Poll("My first poll", "Can you attend my birthday party?", true, userId, Answer.YES, Answer.NO);
            Poll poll2 = new Poll("My second poll", "Can you host my birthday party?", false, userId2,Answer.YES, Answer.NO);

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
            }
            poll.setVotes(newVotes);

            em.getTransaction().commit();
            em.close();
        }


        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        after((req, res) -> {
            res.type("application/json");
        });

        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
        });

        PollDAO pollDAO = new PollDAO();
        PollUserDAO pollUserDAO = new PollUserDAO();
        VoteDAO voteDAO = new VoteDAO();

        //Get

        get("/polls", (req, res) -> {
            Gson gson = new Gson();
            try {
                StringBuilder string = new StringBuilder();
                for (Poll p : pollDAO.getPolls()) {
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
                return pollDAO.findById(id).toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/pollsforuser/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                PollUser user = pollUserDAO.findById(id);
                List<Integer> pollIds = new ArrayList<>();
                List<Poll> polls = user.getPollList();
                for(Poll p : polls){
                    pollIds.add(p.getId().intValue());
                }
                return gson.toJson(pollIds);

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/users", (req, res) -> {
            Gson gson = new Gson();
            try {
                StringBuilder string = new StringBuilder();
                for (PollUser u : pollUserDAO.getUsers()) {
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
            return pollUserDAO.findById(id).toJson();
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/votes", (req, res) -> {
            Gson gson = new Gson();
            StringBuilder string = new StringBuilder();
            try {
                for (Vote v : voteDAO.getVotes()) {
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
                return voteDAO.findById(id).toJson();
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        get("/votesforpoll/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                Poll poll = pollDAO.findById(id);
                int opt1Votes = poll.getOpt1Votes();
                int opt2Votes = poll.getOpt2Votes();
                return gson.toJson(poll.getOpt1().toString() + " votes: " + opt1Votes + ", " + poll.getOpt2().toString() +" votes " + opt2Votes);
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
                Poll oldPoll = pollDAO.findById(id);
                Poll newPoll = gson.fromJson(req.body(), Poll.class);

                newPoll.setPollUser(oldPoll.getPollUser());
                newPoll.setId(oldPoll.getId());
                newPoll.setVotes(oldPoll.getVotes());

                pollDAO.updatePoll(newPoll);

                return newPoll.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }

        });

        put("/users/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                PollUser user = gson.fromJson(req.body(), PollUser.class);
                Long id = Long.parseLong(req.params("id"));
                user.setId(id);
                PollUser oldUser = pollUserDAO.findById(id);
                user.setPollList(oldUser.getPollList());
                pollUserDAO.updatePollUser(user);

                return user.toJson();
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }

        });

        put("/votes/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Vote tempVote = gson.fromJson(req.body(), Vote.class);

                Long id = Long.parseLong(req.params("id"));

                Vote oldVote = voteDAO.findById(id);
                tempVote.setId(oldVote.getId());
                tempVote.setPoll(oldVote.getPoll());

                voteDAO.updateVote(tempVote);

                return voteDAO.findById(id).toJson();


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

                return user.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        post("/votes", (req, res) -> {
            Gson gson = new Gson();
            try{
                Vote tempVote = gson.fromJson(req.body(), Vote.class);
                voteDAO.persistVote(tempVote);

                Long id = tempVote.getId();

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
                Poll deletedPoll = pollDAO.findById(id);
                pollDAO.deletePoll(deletedPoll);
                return gson.toJson("Deleted poll: " + deletedPoll.toJson());

            }catch (Exception e) {
                return gson.toJson("Something went wrong...");
            }
        });

        delete("/polls", (req, res) -> {
            Gson gson = new Gson();
            try {
                for(Poll poll : pollDAO.getPolls()){
                    pollDAO.deletePoll(poll);
                }
                return gson.toJson(pollDAO.getPolls());
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        delete("/users/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                PollUser tempUser = pollUserDAO.findById(id);
                List<Poll> polls = tempUser.getPollList();
                if (polls != null) {
                    while (polls.size() > 0) {
                        Poll poll = polls.get(polls.size() - 1);
                        pollDAO.deletePoll(poll);
                    }
                }
                pollUserDAO.deleteUser(tempUser);
                return gson.toJson("Deleted: ") + tempUser.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }

        });

        delete("/users", (req, res) -> {
            Gson gson = new Gson();
            try {
                for(PollUser u : pollUserDAO.getUsers()) {
                    if (u != null) {
                        List<Poll> polls = u.getPollList();
                        while (polls.size()>1) {
                            Poll deletedPoll = polls.get(polls.size()-1);
                            deletedPoll.setPollUser(null);
                            if(deletedPoll!=null) {
                                pollDAO.deletePoll(deletedPoll);
                            }
                        }
                        u.setPollList(null);
                        pollUserDAO.deleteUser(u);
                    }
                }
                return gson.toJson("Deleted all users");

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }


        });

        delete("/votes/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                Vote deletedVote = voteDAO.findById(id);
                voteDAO.deleteVote(deletedVote);
                return gson.toJson("Deleted: ") + deletedVote.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

        delete("/votes", (req, res) -> {
            Gson gson = new Gson();
            try {
                for(Vote vote : voteDAO.getVotes()){
                    voteDAO.deleteVote(vote);
                }
                return gson.toJson("Deleted all votes");

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong...");
            }
        });

    }

}

