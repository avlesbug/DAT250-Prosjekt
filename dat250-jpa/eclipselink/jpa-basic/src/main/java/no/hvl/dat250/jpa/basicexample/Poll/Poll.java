package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "poll")
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String question;
    private Long pollUserId;
    private boolean isPrivate;
    private Answer opt1;
    private Answer opt2;
    private LocalDate endDate;

    public Poll(){
        name = "Unnamed poll";
        question = "undefined question";
        isPrivate = false;
        opt1 = Answer.YES;
        opt2 = Answer.NO;
    }

    public Poll(String name, String question,boolean isPrivate, Long pollUserId, Answer opt1, Answer opt2, String endDate){

        this.name = name;
        this.question = question;
        this.isPrivate = isPrivate;
        this.pollUserId = pollUserId;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.endDate = LocalDate.parse(endDate);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Answer getOpt1(){ return opt1; }

    public void setOpt1(Answer opt1) { this.opt1 = opt1; }

    public Answer getOpt2() { return opt2; }

    public void setOpt2(Answer opt2) { this.opt2 = opt2; }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getPollUserId(){
        return pollUserId;
    }

    public void setPollUserId(Long pollUserId) {
        this.pollUserId = pollUserId;
    }

    public boolean getPrivacy() {
        return isPrivate;
    }

    public void setPrivacy(boolean isPrivate){
        this.isPrivate = isPrivate;
    }


    public boolean isActive() {
        LocalDate today = LocalDate.now();
        LocalDate end = LocalDate.from(endDate);
        if(today.isAfter(end)){
            return false;
        } else{
            return true;
        }
    }

    public LocalDate getEndDate() { return endDate; }

    @OneToOne
    private PollUser pollUser;
    public PollUser getPollUser() { return pollUser; }
    public void setPollUser(PollUser pollUser) { this.pollUser = pollUser; }

    @OneToMany(mappedBy = "poll")
    @JoinTable(name = "jnd_vote_poll",
            joinColumns = @JoinColumn(name = "poll_fk"),
            inverseJoinColumns = @JoinColumn(name = "vote_fk"))
    private List<Vote> votes = new ArrayList<>();

    public List<Vote> getVotes() { return votes; }
    public void setVotes(List<Vote> votes) { this.votes = votes; }

    public void addVote(Vote vote){
        votes.add(vote);
    }

    public int getOpt1Votes(){
        int opt1 = 0;
        for(Vote v : getVotes()){
            if(v.getAnswer() == Answer.YES){
                opt1++;
            }
        }

        return opt1;
    }

    public int getOpt2Votes(){
        int opt2 = 0;
        for(Vote v : getVotes()){
            if(v.getAnswer() == Answer.NO){
                opt2++;
            }
        }

        return opt2;
    }

    String simpleToJson() {
        Gson gson = new Gson();

        //String jsonInString = gson.toJson("{ id: " + id + ", name: " + name + ", question: " + question + ", isPrivate: " + isPrivate + ", pollUserId: " + pollUserId + ", Yes votes: " + getYesVotes() + ", No Votes: " + getNoVotes());
        String jsonInString = gson.toJson(this);

        System.out.println(pollUser);
        return jsonInString;
    }

    String toJson() {
        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(strategy)
                .create();

        String jsonInString = gson.toJson(this);// + gson.toJson(" Yes votes: " + getYesVotes() + ", No votes: " + getNoVotes() + "}");

        return jsonInString;
    }

    static ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            if (field.getDeclaringClass() == Poll.class && field.getName().equals("pollUser") ||  field.getName().equals("votes")) {
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

}
