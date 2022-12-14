# How to deploy CampManager-backend
* [Windows](#windows)

* [Ubuntu](#ubuntu)

## Windows
1. In order to deploy the back-end in docker once we have cloned the repository inside the folder we must execute the command:
```
docker-compose up
```
2. Once all the necessary dependencies for our docker have been installed (it can take up to 5 minutes the first time the command is executed), a docker-compose made up of 3 containers with the following contents will be installed on our PC:

    - Database (PostgreSQL).
    - pgAdmin 4.
    - Spring Boot.

3. In order to check that the springboot and the database is working properly we have to check the following URL in our browser to see that are working:

    3.1 - pgAdmin 4
    ```
    http://localhost:5050/
    ```
    3.2 - Spring Boot
    ```
    http://localhost:8080/
    ```


4. Now, as the last step, it is necessary to register our server in pgAdmin 4. To do this in our browser at the URL **localhost:5050/** we will follow the following steps:

    4.1 - Right click on:
    > **Servers** -> **Register** -> **Servers...**
    
    4.3 - Write a name for the server (is mandatory) and then go the tab called:
    > **Connection**
    
    4.4 - In the Connection tab write this parameter:
    > **Host name/address**: **pg_container**
    
    > **Port**: **5432**
    
    > **Username**: **admin**
    
    > **Password**: **admin**
    
<br/>
Congratulation you have deployed successfully the back-end of our project, and you can start to work on it :smiley:
<br/>
<br/>

## Ubuntu
1. Install docker engine on Ubuntu. Please follow the instructions that are provided in the official website:
```
https://docs.docker.com/engine/install/ubuntu/
```
2. Install docker on Ubuntu. Please follow the instructions that are provided in the official website:
```
https://docs.docker.com/desktop/install/ubuntu/
```
3. Once docker desktop and docker engine have been installed in Ubuntu, you have to give docker user permission so that it can correctly build the containers with:
```
sudo usermod -a -G docker $USER
```
4. Then you have to give permissions to the entire repository folder with:
```
sudo chmod -R 777 .
```
5. Before executing the *docker compose up* command, in order to generate the docker image, it is necessary to ask an administrator for the *serviceAccountKey* file in order to have the necessary credentials to connect to Firebase. Add the following file to the back-end repository folder:
```
serviceAccountKey.json
```
6. In order to deploy the back-end in docker once we have cloned the repository inside the folder we must execute the command:
```
docker compose up
```
7. Once all the necessary dependencies for our docker have been installed (it can take up to 5 minutes the first time the command is executed), a docker-compose made up of 3 containers with the following contents will be installed on our PC:

    - Database (PostgreSQL).
    - pgAdmin 4.
    - Spring Boot.

8. In order to check that the springboot and the database is working properly we have to check the following URL in our browser to see that are working:

    8.1 - pgAdmin 4
    ```
    http://localhost:5050/
    ```
    8.2 - Spring Boot
    ```
    http://localhost:8080/
    ```


9. Now, as the last step, it is necessary to register our server in pgAdmin 4. To do this in our browser at the URL **localhost:5050/** we will follow the following steps:

    9.1 - Right click on:
    > **Servers** -> **Register** -> **Servers...**
    
    9.3 - Write a name for the server (is mandatory) and then go the tab called:
    > **Connection**
    
    9.4 - In the Connection tab write this parameter:
    > **Host name/address**: **pg_container**
    
    > **Port**: **5432**
    
    > **Username**: **admin**
    
    > **Password**: **admin**
    
<br/>
Congratulation you have deployed successfully the back-end of our project, and you can start to work on it :smiley:
 
