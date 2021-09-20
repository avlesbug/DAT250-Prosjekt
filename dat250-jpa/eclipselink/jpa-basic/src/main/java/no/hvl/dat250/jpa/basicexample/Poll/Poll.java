package no.hvl.dat250.jpa.basicexample.Poll;

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
}
