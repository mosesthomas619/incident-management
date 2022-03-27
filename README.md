# Finleap case study - please refer Question pdf included in the repo.



How to Run the code: 

- Start the mongodb locally and create a database called "regDB"

- Execute the Application from an IDE or run using gradle command in the 
  terminal(gradlew bootRun).

- Use postman(recommended) for hitting apis and can easily specify different creds 
  each time if needed. Swagger is also available(http://localhost:8080/swagger-ui/index.html/).
  
- users collection is initialised with 5 users when the application starts if users collection
  doesnot contain any data at the moment. (refer UserFactory class for details of users).
  
- The application uses spring security(basic auth) for incident CRUD operations and the users api 
  does not require authentication for CRUD operation. The incident apis are required to provide creds 
  to perform all operations. Use the creds of users created in user collection for authentication.

- Incident POST/PUT request format:
  {
  "title": "string",
  "assignee": "any username in users collection",    //non required field
  "status": "any value from enum"     //non required field - only for PUT
  }
  
- User POST/PUT request format:
  {
  "username": "string",
  "password": "string",      //will be bcrypted while saving
  "assigned": true          // non required field
  }
  
- run tests using the command : gradlew test








