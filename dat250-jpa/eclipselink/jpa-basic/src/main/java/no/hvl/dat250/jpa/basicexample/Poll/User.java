package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long Id) { this.id = Id; }

    public String getFirstName() {
        return name;
    }

    public void setFirstName(String firstName) {
        this.name = name;
    }

}
