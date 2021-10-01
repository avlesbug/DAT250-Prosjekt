package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
}
