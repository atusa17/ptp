## PTP Persistence API

The PTP Persistence API is the web API for accessing the theorems database.

<http://localhost:8090/>

## Running from IntelliJ

* Create a new run configuration in IntelliJ.

  ```
  Name: "PTP Persistence API Tomcat"
  Application Server: Tomcat (8.5.12)
  HTTP Port: 8090
  JMX Port: 1090
  Deployment tabe: Deploy persistence-api.war (exploded)
  ```
## Running the Tests

This project is unit tested with JUnit and Mockito. You can run the unit tests with IntelliJ or Gradle. To run them with IntelliJ, browse to any `*Test.java` file and use IntelliJ's built-in test runner to run or debug the test. To run all the unit tests with Gradle:

* On a Linux or Macintosh machine:

    ```$ ./gradlew test```
    
* On a Windows machine:

    ```$ gradlew.bat test```

You can also test modules individually:

* On a Linux or Macintosh machine:

    ```$ ./gradlew persistence:test```
  
* On a Windows machine:

    ```$ gradlew.bat persistence:test```

## Integration Tests

To run the integration tests with IntelliJ, browse to any `*Test.java` file residing in any module name `integrationTest` and use IntelliJ's built-in test runner to run or debug the test. To run all the integration tests with Gradle:

* On a Linux or Macintosh machine:

    ```$ ./gradlew integrationTest```
  
* On a Windows machine

    ```$ gradlew.bat integrationTest```
  
## Built with

* [Spring Boot](https://projects.spring.io/spring-boot/) - Web framework
* [Spring Web Flow](https://projects.spring.io/spring-webflow/) - MVC framework
* [Spring Data](https://spring.io/projects/spring-data/) - Persistence framework
* [Gradle](https://gradle.org/) - Dependency management
* [JUnit](http://junit.org/junit4/) - Unit tests
* [Mockito](http://site.mockito.org/) - Mock objects library
* [Lombok](https://projectlombok.org/) - Boilerplate Code Generator
* [Hibernate ORM](http://hibernate.org/orm/) - Object/Relational Mapping
