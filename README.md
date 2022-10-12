# How to deploy CampManager-backend

1. In order to deploy the back-end in docker once we have cloned the repository inside the folder we must execute the command:
```
docker-compose up
```
2. Once all the necessary dependencies for our docker have been installed (it can take up to 5 minutes the first time the command is executed), a docker-compose made up of 3 containers with the following contents will be installed on our PC:
    - Database (PostgreSQL).
    - pgAdmin 4.
    - Spring Boot.

3. In order to check that the springboot and the database is working properly we have to check the following URL in our browser to see that are working:
    - pgAdmin 4
    ```
    http://localhost:5050/
    ```
    - Spring Boot
    ```
    http://localhost:8080/
    ```
    
