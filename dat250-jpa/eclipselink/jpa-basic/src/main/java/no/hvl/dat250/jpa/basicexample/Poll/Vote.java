package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer answer; // 0="No", 1="Yes"

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }


    @ManyToOne
    private Poll poll;
    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }
}
