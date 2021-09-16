package no.hvl.dat250.jpa.basicexample.Banking;

import javax.persistence.*;
import java.util.List;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street;
    private Integer number;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet(){
        return street;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber(){
        return number;
    }

    @ManyToMany (mappedBy = "personOnAddress")
    private List<Person> personOnAddress;

}
