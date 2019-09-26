# Turbo Consent #
Turbo Consent is a web application developed for We The Curious, a charity based science centre based in Bristol. The application aims to enable We The Curious to efficiently handle visitors' consent on data being generated through their participation in public experiments.

## Getting Started ##
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites ###
- At least Java version 8 or newer installed on your machine. Download: https://www.java.com/en/download/
- MySQL installed. Download: https://dev.mysql.com/downloads/
- An IDE which supports Java.

### Installation ###
- Pull the repository to your local machine and import the pom.xml file as a project with your IDE. Your IDE should automatically download all the dependencies required.
- Log in to your local MySQL server with the root user.
- Create the database needed for the web app with `CREATE DATABASE consentDB;`.
- Create a user and grant previlages on the database with `CREATE USER 'tcuser'@'localhost' IDENTIFIED BY 'tcpass';` then `GRANT ALL PRIVILEGES ON consentDB.* TO 'tcuser'@'localhost';`
- Run the `main` function of the application, make sure your local MySQL server is running on your machine.
- On successful build, navigate to localhost:8080/ to visit the web application.
- Log in with the email: admin@turboconsent.com and password: tcadmin123.
- Navigate to localhost:8080/admin to create and remove accounts and test out other functionalities.

## Built With ##
- Spring Boot [Web Framework] - https://spring.io/projects/spring-boot
- Thymeleaf [Templating Engine] - https://www.thymeleaf.org/
- Maven [Dependency Management] - https://maven.apache.org/
- MySQL [Server] - https://www.mysql.com/

## License ##
Turbo Consent is licensed under MIT License.
