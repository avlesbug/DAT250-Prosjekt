package no.hvl.dat250.jpa.basicexample.Banking;

import no.hvl.dat250.jpa.basicexample.model.Family;

import javax.persistence.*;
import java.util.List;

@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer number;
    private Integer limit;
    private Integer balance;

    private Bank bank;


    public void setId(Long id) { this.id = id; }

    public Long getId() { return id; }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    @ManyToOne
    private Pincode pincode;

    public Pincode getPincode() {
        return pincode;
    }

    public void setPincode(Pincode pincode){
        this.pincode = pincode;
    }

    @ManyToOne
    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @ManyToOne
    public Bank getBank() {
        return bank;
    }
    public void setBank(Bank bank){ this.bank = bank; }

}
