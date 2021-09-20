# DAT250-Prosjekt

Husk å legge til de nye klassene i persistence.xml.

Dette er feilkoden som kommer når jeg kjører koden:

'Internal Exception: java.sql.SQLSyntaxErrorException: 'POLLUSER_ID' is not a column in table or VTI 'TEST.POLL'.
Error Code: 20000
Call: INSERT INTO poll (ID, NAME, QUESTION, POLLUSER_ID) VALUES (?, ?, ?, ?)
	bind => [4 parameters bound]
Query: InsertObjectQuery(no.hvl.dat250.jpa.basicexample.Poll.Poll@3703bf3c)'
