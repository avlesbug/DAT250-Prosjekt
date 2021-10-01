package no.hvl.dat250.jpa.basicexample.Poll;

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

}


