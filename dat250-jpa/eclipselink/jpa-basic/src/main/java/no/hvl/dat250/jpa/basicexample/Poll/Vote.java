package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    private Long id;
    @Expose
    private Answer answer;

    public Vote(){
        answer = null;
    }

    public Vote(Answer answer, Poll poll){
        this.answer = answer;
        this.poll = poll;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }


    @ManyToOne
    private Poll poll;
    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }


    String toJson() {
        Gson gson = new Gson();

        String jsonInString = gson.toJson(("{ VoteID: " + id + ", Answer: " + answer + ", PollID: " + poll.getId() + "}"));
        return jsonInString;
    }


    String simpleToJson() {
        Gson gson = new Gson();

        String jsonInString = gson.toJson(this);

        return jsonInString;
    }
}
