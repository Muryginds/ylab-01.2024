## Meter monitoring service

#### Monitoring Service Console Application
Is a simple Java-based program designed to manage and monitor various aspects related to meter readings and user submissions. The application offers different user interfaces for both regular and administrative users, allowing them to perform tasks such as submitting readings, accessing submission history, and managing meter types.

#### Key Features
User Authentication: Users can register, log in, and log out, ensuring secure access to the
system. Submission Management: Users can submit meter readings, view their submission history,
and retrieve specific submissions by date. Administrative Actions: Administrators have additional features,
including adding new meter types, accessing audition event history, and retrieving submission information for
specific users.

#### Sprints:
- <a href ="https://github.com/Muryginds/ylab-01.2024/pull/3"> sprint 1</a> Entities, in-memory collections, console interface, javadoc, service tests
- <a href ="https://github.com/Muryginds/ylab-01.2024/pull/4"> sprint 2</a> Docker + postgres, liquibase migrations, repository tests
- <a href ="https://github.com/Muryginds/ylab-01.2024/pull/5"> sprint 3</a> Servlets + tomcat, jackson, validations, aspects, logging

#### API
```
POST: /api/v1/auth/login - authorization with {UserAuthorizationRequestDTO}
POST: /api/v1/auth/logout - logout

POST: /api/v1/accounts/registration - registrate new user with {UserRegistrationRequestDTO}

GET:  /api/v1/users/me - currentUser

GET:  /api/v1/submissions/all - get all submissions with {AllSubmissionsRequestDTO}
GET:  /api/v1/submissions - get submission with {SubmissionRequestDTO}
POST: /api/v1/submissions - add new submission with {NewReadingsSubmissionRequestDTO}

GET:  /api/v1/events/all - get all audition events with {AuditionEventsRequestDTO}

POST: /api/v1/meter-types - add new meter type with {NewMeterTypeRequestDTO}
```

#### Testing
    By default, added user 'admin' with password 'admin' and administrative role

    to run tests, add -Denv=test

### Run the Project:

- Clone the repository
- Open a terminal or command prompt.
- Navigate to your project directory.
- Run the following command to execute your application:

#### On Unix-like systems:

```bash
./gradlew run -q --console=plain
```
#### On Windows:

```bash
gradlew.bat run -q --console=plain
```
The Gradle wrapper will download the specified Gradle distribution, compile and run your Java application.
