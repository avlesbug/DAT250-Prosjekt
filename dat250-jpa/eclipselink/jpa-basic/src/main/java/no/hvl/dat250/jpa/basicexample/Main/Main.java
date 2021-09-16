package no.hvl.dat250.jpa.basicexample.Main;

import no.hvl.dat250.jpa.basicexample.model.Family;
import no.hvl.dat250.jpa.basicexample.model.Person2;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import static org.junit.Assert.assertTrue;

public class Main {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        // Begin a new local transaction so that we can persist a new entity
        em.getTransaction().begin();

        // read the existing entries
        Query q = em.createQuery("select m from Person2 m");
        // Persons should be empty

        // do we have entries?
        boolean createNewEntries = (q.getResultList().size() == 0);

        // No, so lets create new entries
        if (createNewEntries) {
            assertTrue(q.getResultList().size() == 0);
            Family family = new Family();
            family.setDescription("Family for the Knopfs");
            em.persist(family);
            for (int i = 0; i < 40; i++) {
                Person2 person = new Person2();
                person.setFirstName("Jim_" + i);
                person.setLastName("Knopf_" + i);
                em.persist(person);
                // now persists the family person relationship
                family.getMembers().add(person);
                em.persist(person);
                em.persist(family);
            }
        }

        // Commit the transaction, which will cause the entity to
        // be stored in the database
        em.getTransaction().commit();

        // It is always good practice to close the EntityManager so that
        // resources are conserved.
        // read the existing entries and write to console

        Query q2 = em.createQuery("select p from Person2 p");
        List<Person2> todoList = q.getResultList();
        for (Person2 todo : todoList) {
            System.out.println(todo.getFirstName() + " " + todo.getLastName() + ", Job(s): " + todo.getJobList() + ", Family: " + todo.getFamily());
        }

        Query q3 = em.createQuery("select f from Family f");
        List<Family> familyList = q.getResultList();
        for (Family fam : familyList) {
            System.out.println("Family: " + fam.getMembers());
        }

        // create new todo
        /**
        em.getTransaction().begin();
        Person2 todo = new Person2();
        todo.setFirstName("Kristian");
        todo.setLastName("Avlesbug");
        em.persist(todo);
        em.getTransaction().commit();
         **/
        em.close();

    }
}
