package no.hvl.dat250.jpa.basicexample.Banking;

import no.hvl.dat250.jpa.basicexample.model.Family;
import no.hvl.dat250.jpa.basicexample.model.Person2;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import static org.junit.Assert.assertTrue;

public class BankMain {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        // Begin a new local transaction so that we can persist a new entity
        em.getTransaction().begin();

        // read the existing entries
        Query q = em.createQuery("select p from Person p");
        // Persons should be empty

        // do we have entries?
        boolean createNewEntries = (q.getResultList().size() == 0);

        // No, so lets create new entries
        if (createNewEntries) {
            assertTrue(q.getResultList().size() == 0);
            Family family = new Family();
            family.setDescription("Family for the Knopfs");
            em.persist(family);
            /**
            for (int i = 0; i < 10; i++) {
                Person person = new Person();

                // Create address
                Address address = new Address();
                address.setStreet("Allegate");
                address.setNumber(i);
                List<Address> addresses = new ArrayList<>();
                addresses.add(address);


                // Create bank
                Bank dnb = new Bank();
                dnb.setName("DNB" + i);

                //Create pincode
                Pincode pin = new Pincode();

                //Create card
                List<CreditCard> cards = new ArrayList<>();
                CreditCard card = new CreditCard();
                card.setPincode(pin);
                card.setBank(dnb);
                cards.add(card);

                //Create person
                person.setFirstName("Jim" + i);
                person.setLastName("Knopf" + i);
                person.setCardList(cards);
                person.setAddress(addresses);

                em.persist(dnb);
                em.persist(address);
                em.persist(pin);
                em.persist(card);
                em.persist(person);

            }
             **/
            Person person = new Person();

            // Create address
            Address address = new Address();
            address.setStreet("Inndalsveien");
            address.setNumber(28);
            List<Address> addresses = new ArrayList<>();
            addresses.add(address);


            // Create bank
            Bank bank = new Bank();
            bank.setName("Pengebank");

            //Create pincode
            Pincode pin = new Pincode();
            pin.setPincode("123");
            pin.setCount(1);


            //Create card
            List<CreditCard> cards = new ArrayList<>();
            CreditCard card = new CreditCard();
            card.setPincode(pin);
            card.setBank(bank);
            card.setNumber(12345);
            card.setBalance(-5000);
            card.setLimit(-10000);
            card.setPerson(person);
            CreditCard card2 = new CreditCard();
            card2.setPincode(pin);
            card2.setBank(bank);
            card2.setNumber(123);
            card2.setBalance(1);
            card2.setLimit(2000);
            card2.setPerson(person);

            cards.add(card);
            cards.add(card2);
            pin.setCardList(cards);

            //Create person
            person.setFirstName("Max");
            person.setLastName("Mustermann");
            person.setCardList(cards);
            person.setAddress(addresses);
            System.out.println(cards.size());

            em.persist(bank);
            em.persist(address);
            em.persist(pin);
            em.persist(card);
            em.persist(card2);
            em.persist(person);

        }

        // Commit the transaction, which will cause the entity to
        // be stored in the database
        em.getTransaction().commit();

        // It is always good practice to close the EntityManager so that
        // resources are conserved.
        // read the existing entries and write to console

        Query q2 = em.createQuery("select p from Person p");
        List<Person> todoList = q.getResultList();
        for (Person person : todoList) {
            System.out.print(person.getFirstName() + " " + person.getLastName() + ", Card(s): ");
            for (CreditCard c : person.getCardList()){
                System.out.print(" Card Number: " + c.getNumber() + " in "+ c.getBank().getName()+", ");
            }
            System.out.print(", Address: ");
            for(Address a : person.getAddress()){
                System.out.println(a.getStreet() + " "+a.getNumber());
            }
        }
        //  + ", Address: " + person.getAddress()
        em.close();

    }
}