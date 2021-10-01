package no.hvl.dat250.jpa.basicexample.Poll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

public class VoteDAO {
    private static final String PERSISTENCE_UNIT_NAME = "people";
    private static EntityManagerFactory factory;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory emf) {
        this.factory = emf;
    }


}
