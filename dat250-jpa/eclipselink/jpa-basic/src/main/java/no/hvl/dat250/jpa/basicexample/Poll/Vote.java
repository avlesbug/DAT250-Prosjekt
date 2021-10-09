package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Answer answer;
    private Long pollId;

    public Vote(){
        answer = null;
    }

    public Vote(Answer answer, Long pollId){
        this.answer = answer;
        this.pollId = pollId;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Long getPollId(){ return pollId; }

    public void setPollId(Long pollId){ this.pollId=pollId; }


    @ManyToOne
    private Poll poll;
    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }


    String toJson() {
        Gson gson = new Gson();

        String jsonInString = gson.toJson(("{ VoteID: " + id + ", Answer: " + answer + ", PollID: " + pollId + "}"));
        return jsonInString;
    }


    String simpleToJson() {
        Gson gson = new Gson();

        String jsonInString = gson.toJson(this);

        return jsonInString;
    }
}
