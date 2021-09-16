package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        private String email;
        private String password;


    public Long getId() {
        return id;
    }

    public void setId(Long Id) { this.id = Id; }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
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
    @JoinTable(name = "jnd_poll_user",
            joinColumns = @JoinColumn(name = "user_fk"),
            inverseJoinColumns = @JoinColumn(name = "poll_fk"))
    private List<Poll> pollList = new ArrayList<>();
    public List<Poll> getPollList() { return pollList; }
    public void setPollList(List<Poll> pollList) { this.pollList = pollList; }
}
