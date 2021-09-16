package no.hvl.dat250.jpa.basicexample.Banking;

import no.hvl.dat250.jpa.basicexample.model.Family;
import no.hvl.dat250.jpa.basicexample.model.Job;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;


    public Long getId() {
        return id;
    }

    public void setId(Long Id) { this.id = Id; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Leave the standard column name of the table
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ManyToMany
    @JoinTable(name = "jnd_per_add",
            joinColumns = @JoinColumn(name = "person_fk"),
            inverseJoinColumns = @JoinColumn(name = "address_fk"))
    private List<Address>personOnAddress;
    public List<Address> getAddress() { return personOnAddress; }
    public void setAddress(List<Address> personOnAddress) {
        this.personOnAddress = personOnAddress;
    }


    @OneToMany
    @JoinTable(name = "jnd_crd_per",
            joinColumns = @JoinColumn(name = "person_fk"),
            inverseJoinColumns = @JoinColumn(name = "creditcard_fk"))
    private List<CreditCard> cardList = new ArrayList<CreditCard>();
    public List<CreditCard> getCardList() {
        return this.cardList;
    }

    public void setCardList(List<CreditCard> nickName) {
        this.cardList = nickName;
    }

}