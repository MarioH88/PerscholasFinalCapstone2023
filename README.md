# Alumni Event Management System


The Alumni Event Management System is a web-based application designed to simplify the organization and management of events for alumni associations. This platform allows administrators to create and manage events, while providing an easy way for alumni to stay connected, register for events, and engage with their fellow graduates.

1. Features
Event creation and management
User registration and authentication
Event registration and attendance tracking

2. Alumni Event Management System
The Alumni Event Management System is a web-based application designed to simplify the organization and management of events for alumni associations. This platform allows administrators to create and manage events, while providing an easy way for alumni to stay connected, register for events, and engage with their fellow graduates.

3. Features
Event creation and management
User registration and authentication
Event registration and attendance tracking

4. Getting Started
These instructions will help you set up the project on your local machine for development and testing purposes.

5. Prerequisites
Java 11 or later
Maven
MySQL or any other relational database

6. Installation
i. Clone the repository:
git clone https://github.com/yourusername/alumni-event-management-system.git

ii. Navigate to the project directory:
cd alumni-event-management-system

iii. Open the application.properties file in the src/main/resources directory and update the following fields with your database credentials:
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password

iv. Build the project using Maven:
mvn clean install

v. Run the application:

mvn spring-boot:run

vi. Open your web browser and navigate to http://localhost:8082. You should now see the Alumni Event Management System home page.

License
This project is licensed under the MIT License - see the LICENSE.md file for details.


Acknowledgments
Special thanks to Igor and Erica for teaching this wonderful cohort and all th alumni who have contributed their valuable feedback and suggestions to make this platform even better.
Don't forget to update the repository URL and other placeholders in the README file with the appropriate values for your GitHub account and project.
