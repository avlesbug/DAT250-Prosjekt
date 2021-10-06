package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "polluser")
public class PollUser {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        private String email;
        private String password;

        public PollUser(){
            name = "Poll User";
            email = "poll@user.com";
            password = "Password";
        }

        public PollUser(String name, String email, String password){
            this.name = name;
            this.email = email;
            this.password = password;
        }

    public void setId(Long Id) { this.id = Id; }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @OneToMany
    @JoinTable(name = "jnd_poll_pllusr",
            joinColumns = @JoinColumn(name = "polluser_fk"),
            inverseJoinColumns = @JoinColumn(name = "poll_fk"))
    private List<Poll> pollList = new ArrayList<Poll>();
    public List<Poll> getPollList() { return pollList; }
    public void setPollList(List<Poll> pollList) { this.pollList = pollList; }

    public void addPoll(Poll poll){
        pollList.add(poll);
    }

    public void removePoll(Poll poll){
        pollList.remove(poll);
    }

    public String simpleToJson() {
        Gson gson = new Gson();

        String jsonInString = gson.toJson(this);

        return jsonInString;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(strategy)
                .create();
        String jsonInString = gson.toJson(this);

        return jsonInString;
    }

    static ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            if (field.getDeclaringClass() == PollUser.class && field.getName().equals("pollList")) {
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


