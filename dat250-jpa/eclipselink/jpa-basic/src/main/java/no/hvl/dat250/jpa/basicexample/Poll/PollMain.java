package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;
import spark.Filter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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




    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        FirestoreHandler fs = new FirestoreHandler();
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();



        // read the existing entries
        Query q = em.createQuery("select u from PollUser u");
        // Persons should be empty

        // do we have entries?
        boolean createNewEntries = (q.getResultList().size() == 0);



        if (createNewEntries) {
            PollUser pollUser = new PollUser("Max Musterman", "max.musterman@gmail.com", SCryptUtil.scrypt("Passord123",16384,8,1));
            PollUser pollUser2 = new PollUser("Maxine Musterwoman", "maxint.musterwoman@gmail.com", SCryptUtil.scrypt("Passord123",16384,8,1));

            em.getTransaction().begin();

            em.persist(pollUser);
            em.persist(pollUser2);


            Long userId = pollUser.getId();
            Long userId2 = pollUser2.getId();

            String endDate = LocalDate.of(2021,12,16).toString();

            Poll poll = new Poll("My first poll", "Can you attend my birthday party?", true, userId, Answer.YES, Answer.NO,endDate);
            Poll poll2 = new Poll("My second poll", "Can you host my birthday party?", false, userId2,Answer.YES, Answer.NO,endDate);

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
                return gson.toJson("Could not find poll... Make sure the ID is correct");
            }
        });

        get("/pollsforuser/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                List<Integer> pollIds = pollUserDAO.getPollIds(id);
                return gson.toJson(pollIds);

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Could not find polls... Make sure the User ID is correct");
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
                return gson.toJson("Could not find user... Make sure the ID is correct");
            }
        });
        get("/userByMail/:email", (req, res) -> {
            Gson gson = new Gson();
            try{
                String email = req.params("email");
                System.out.println(email);
                return pollUserDAO.findByMail(email).toJson();
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Could not find user... Make sure the ID is correct");
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
                return gson.toJson("Could not find vote... Make sure the ID is correct");
            }
        });

        get("/votesforpoll/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                return gson.toJson(pollDAO.getVotesforPoll(id));
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Could not find votes... Make sure the user ID is correct");
            }
        });

        get("/results/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                fs.addResult(pollDAO.getResults(id));
                return gson.toJson(pollDAO.getResults(id));
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Could not find votes... Make sure the user ID is correct");
            }
        });
        get("/results", (req, res) -> {
            Gson gson = new Gson();
            List<Result> results = new ArrayList<>();
            try {
                for(Poll p : pollDAO.getPolls()){
                    Result result = pollDAO.getResults(p);
                    fs.addResult(result);
                    results.add(result);
                }
                return gson.toJson(results);
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Could not find votes... Make sure the user ID is correct");
            }
        });
        //Put
        put("/polls/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Long id = Long.parseLong(req.params("id"));
                Poll newPoll = gson.fromJson(req.body(), Poll.class);
                pollDAO.updatePoll(newPoll,id);
                fs.update();
                return newPoll.toJson();

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong... Make sure the ID and format is correct");
            }

        });

        put("/users/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                PollUser user = gson.fromJson(req.body(), PollUser.class);
                Long id = Long.parseLong(req.params("id"));
                pollUserDAO.updatePollUser(user, id);
                return user.toJson();
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong... Make sure the ID and format is correct");
            }

        });

        put("/votes/:id", (req, res) -> {
            Gson gson = new Gson();
            try {
                Vote tempVote = gson.fromJson(req.body(), Vote.class);
                Long id = Long.parseLong(req.params("id"));

                if(tempVote.getAnswer()!= null) {
                    voteDAO.updateVote(tempVote,id);
                    fs.update();
                } else {
                    return gson.toJson("Invalid answer");
                }

                return voteDAO.findById(id).toJson();


            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong... Make sure the ID and format is correct");
            }
        });


        //Post
        post("/polls", (req, res) -> {
            Gson gson = new Gson();
            try {
                Poll tempPoll = gson.fromJson(req.body(), Poll.class);
                pollDAO.persistPoll(tempPoll);
                fs.update();

                return tempPoll.toJson();
            }catch (Exception e) {
                return gson.toJson("Something went wrong... Make sure the format is correct");
            }


        });

        post("/users", (req, res) -> {
            Gson gson = new Gson();
            try {
                PollUser user = gson.fromJson(req.body(), PollUser.class);
                if(pollUserDAO.findByMail(user.getEmail())== null){
                    String password = user.getPassword();
                    user.setPassword(SCryptUtil.scrypt(password,16384,8,1));
                    pollUserDAO.persistPollUser(user);
                    return user.toJson();
                }else {
                    return gson.toJson("There is already an account associated with this email..");
                }
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong... Make sure the format is correct");
            }
        });
        post("/login", (req, res) -> {
            Gson gson = new Gson();
            try {
                LoginForm login = gson.fromJson(req.body(), LoginForm.class);
                return gson.toJson(pollUserDAO.login(login));
            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong... Make sure the format is correct");
            }
        });
        post("/votes", (req, res) -> {
            Gson gson = new Gson();
            try{
                Vote tempVote = gson.fromJson(req.body(), Vote.class);
                if(voteDAO.persistVote(tempVote)) {
                    fs.update();
                    return tempVote.toJson();
                }else {
                    return gson.toJson("Poll has expired...");
                }

            }catch (Exception e) {
                System.out.println(e.getStackTrace());
                return gson.toJson("Something went wrong... Make sure the poll has not ended and the format is correct");
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
                return gson.toJson("Something went wrong... Make sure the ID is correct");
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
                return gson.toJson("Something went wrong... Make sure the ID is correct");
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
                return gson.toJson("Something went wrong... Make sure the ID is correct");
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

