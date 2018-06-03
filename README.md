# Mutant detector application

Detect if a DNA sequence is mutant based on at least 2 matches of 4 equals characters.

### Prerequisites
In order to run this project you need:

- Java 8
- Maven (at least 3.3.9)
- MongoDB 

As an alternative, you can avoid doing the installation of Mongo and Redis server by using Docker containers.

### Configuration

Before running the application for the first time, please update the `application.properties` file with your right servers configuration:

    #Mongo server configuration
    spring.data.mongodb.database=xmen
    spring.data.mongodb.host=192.168.99.100
    spring.data.mongodb.port=27017



### Running the application from your IDE

Clone this repository via git clone command:

```
git clone https://github.com/corbatron/MLxmen.git
```

Import as Existing maven project.

Run the project as java application, if you are requested for the main class, input `MLxmenApplication` class.

Once you see a `Started MLxmenApplication` message like the below one, it means that Spring Boot has finished the deployment and it's ready to use.
```
2018-06-02 19:11:09.512  INFO 5624 --- [           main] a.c.m.xmen.MLxmen.MLxmenApplication      : Started MLxmenApplication in 6.987 seconds (JVM running for 7.583)
```

You can check this if you navigate to `localhost:8080`

### Application server deployment

Execute maven with the package goal

    mvn package

This will create a .war file in the `target` workspace folder.

    /MLxmen/target/MLxmen-0.0.1-SNAPSHOT.war

You can deploy this file to your prefered application server (tomcat, jboss, etc)
	
### Exposed API service
The application only expose 2 methods (refer to class  `ar.com.mercadolibre.xmen.MLxmen.rest`), one to check if a given DNA is mutant and another one to get the stats on how many were checked, how many were possitive and the ratio. 

##### POST Method
###### /mutant/
Usage:

    curl -H "Content-Type: application/json" --request POST --data '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}' http://localhost:8080/mutant/

API will return either a HTTP 200 (OK) if DNA is mutant or HTTP 403 (forbidden) if it's not. Additionally, the response body will show a "DNA is mutant" or "DNA is NOT mutant".

##### GET Method
###### /stats
Usage:

    curl http://localhost:8080/stats/

Result:

    {"count_mutant_dna":24,"count_human_dna":43,"ratio":0.56} 

This method shows how many DNA chains were checked and how many were detected as mutants as well as the ratio.

### Tech considerations
 - There is certainly no limits over the DNA size as long as it's NxN
 - In order to simplify the schema, all services were installed on the same server, but it would be better to have the DB running in a different one as suggested below
 
 ![Suggested Architecture](https://github.com/corbatron/MLxmen/blob/master/Suggested%20Architecture.jpg)

- Below screenshots show the server added into an autoscaling group:

Instance running the application:
 ![autoscale1](https://github.com/corbatron/MLxmen/blob/master/autoscale1.jpg)
 
Autoscale group containing the above instance:
 ![autoscale2](https://github.com/corbatron/MLxmen/blob/master/autoscale2.jpg)
 
Scale policy (might need adjusted)
![autoscale3](https://github.com/corbatron/MLxmen/blob/master/autoscale3.jpg)

