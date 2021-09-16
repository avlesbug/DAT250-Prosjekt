package no.hvl.dat250.jpa.basicexample.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private String description;

    @OneToMany(mappedBy = "family")
    private final List<Person2> members = new ArrayList<Person2>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Person2> getMembers() {
        return members;
    }

}