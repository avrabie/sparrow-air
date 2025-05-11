# Prompt helper

1. Give the AircraftType and Airport entities, their routes and handlers and services, I would like to have the same for
2. also add sample data in the DataDemoProfileConfig, and also write the unit tests similar to what we have for AirpotTest
3. In the Flight class we have  private LocalDateTime scheduledDeparture; I would also like to have a LocalDateTime scheduledArrival; Please also add this to the Flight class and the database which is managed with liquibase, add the new file in changes and then update the db.changelog-master.xml