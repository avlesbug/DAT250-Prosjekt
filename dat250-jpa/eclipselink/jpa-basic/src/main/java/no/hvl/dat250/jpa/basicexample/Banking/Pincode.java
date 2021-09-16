package no.hvl.dat250.jpa.basicexample.Banking;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pincode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String pincode;
    private Integer count;



    public void setId(Long id) { this.id = id;}

    public void setPincode(String pincode){
        this.pincode = pincode;
    }

    public String getPincode(){
        return pincode;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount(){
        return count;
    }

    @Id
    public Long getId() { return id; }

    @OneToMany
    @JoinTable(name = "jnd_crd_pin",
            joinColumns = @JoinColumn(name = "pincode_fk"),
            inverseJoinColumns = @JoinColumn(name = "creditcard_fk"))
    private List<CreditCard> cardList = new ArrayList<CreditCard>();
    public List<CreditCard> getCardList() {
        return this.cardList;
    }
    public void setCardList(List<CreditCard> nickName) {
        this.cardList = nickName;
    }

}
