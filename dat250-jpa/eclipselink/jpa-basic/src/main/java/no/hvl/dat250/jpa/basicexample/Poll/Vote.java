package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Answer answer;
    private Long pollId;

    public Vote() {
    }

    public Vote(Answer answer, Long pollId) {
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

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }


    @ManyToOne
    private Poll poll;

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }


    String oldtoJson() {
        Gson gson = new Gson();

        String jsonInString = gson.toJson(("{ VoteID: " + id + ", Answer: " + answer + ", PollID: " + pollId + "}"));
        return jsonInString;
    }


    String toJson() {
        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(strategy)
                .create();

        String jsonInString = gson.toJson(this);

        return jsonInString;
    }

    static ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            if (field.getDeclaringClass() == Vote.class && field.getName().equals("poll")) {
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
