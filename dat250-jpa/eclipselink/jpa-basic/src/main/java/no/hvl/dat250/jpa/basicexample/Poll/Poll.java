package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;

import javax.persistence.*;
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
    private boolean isPrivate;

    public Poll(){
        name = "Unnamed poll";
        question = "undefined question";
        isPrivate = false;
    }

    public Poll(String name, String question,boolean isPrivate, PollUser pollUser){
        this.name = name;
        this.question = question;
        this.pollUser = pollUser;
        this.isPrivate = isPrivate;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public boolean getPrivacy() {
        return isPrivate;
    }

    public void setPrivacy(boolean isPrivate){
        this.isPrivate = isPrivate;
    }


    @OneToOne
    private PollUser pollUser;
    public PollUser getPollUser() { return pollUser; }
    public void setUser(PollUser pollUser) { this.pollUser = pollUser; }

    @OneToMany(mappedBy = "poll")
   // @JoinTable(name = "jnd_vote_poll",
     //       joinColumns = @JoinColumn(name = "poll_fk"),
       //     inverseJoinColumns = @JoinColumn(name = "vote_fk"))
    private List<Vote> votes = new ArrayList<>();
    public List<Vote> getVotes() { return votes; }

    public void setVotes(List<Vote> votes) { this.votes = votes; }

    public void addVote(Vote vote){
        votes.add(vote);
    }

    public int getYesVotes(){
        int yes = 0;
        for(Vote v : getVotes()){
            if(v.getAnswer() == Answer.YES){
                yes++;
            }
        }

        return yes;
    }

    public int getNoVotes(){
        int no = 0;
        for(Vote v : getVotes()){
            if(v.getAnswer() == Answer.NO){
                no++;
            }
        }

        return no;
    }

    String toJson() {
        Gson gson = new Gson();

        StringBuilder votes = new StringBuilder();

        String jsonInString = gson.toJson("{ id: " + id + ", name: " + name + ", question: " + question + ", isPrivate: " + isPrivate + ", pollUserId: " + pollUser.getId() + ", Yes votes: " + getYesVotes() + ", No Votes: " + getNoVotes());

        return jsonInString;
    }

    String simpleToJson() {
        Gson gson = new Gson();

        StringBuilder votes = new StringBuilder();

        String jsonInString = gson.toJson(this);

        return jsonInString;
    }

}
