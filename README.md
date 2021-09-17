# DAT250-Prosjekt

Husk å legge til 
       " <class>no.hvl.dat250.jpa.basicexample.Poll.PollUser</class>
        <class>no.hvl.dat250.jpa.basicexample.Poll.Poll</class>
        <class>no.hvl.dat250.jpa.basicexample.Poll.Vote</class>"
        i persistence.xml.

Dette er feilkoden som kommer når jeg kjører koden:
"Call: INSERT INTO poll (ID, NAME, QUESTION, POLLUSER_ID) VALUES (?, ?, ?, ?)
	bind => [4 parameters bound]
Query: InsertObjectQuery(no.hvl.dat250.jpa.basicexample.Poll.Poll@3703bf3c)"
