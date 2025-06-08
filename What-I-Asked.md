# Questions I Asked


1. I woulod like to refactor the airport2 handler and put the login inside the services package, in the handler i would like you to handle errors that are coming from streams of Fluxes and Monos. Use webflux best practices.
2. at the moment for testing springboot is using h2 database to test the repo layer, could you please suggest moving from h2 to testcontainers using postgres, as I would like to make it more aligned with the code itself.
3. in liquibase migration in this project, all the changes are written in xml, could you please rewrite them in sql instead?
4. Add MTOW INT field to aircraft_types in Liquibase and POJO.
5. please make sure that you are using liquibase and you need to update the DB schema and you are dealing with changesets, you cannot simply add a new field in the table
6. Create "datademo" profile for sample airport2 data.
7. Modeling AircraftType with Handlers, Repositories, and Services.
8. Create unit tests for AircraftTypeRoutes similar to AirportRoutesTest.
9. Refactor DataDemoProfileConfig for Flux Integration.
10. 