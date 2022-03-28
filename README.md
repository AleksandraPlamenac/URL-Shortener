# URL Shortener Service
This project contains a simple URL Shortener service which exposes its functionality via a Web Service API.

## Assignment
The implementation of this service is not quite complete. Implement the following features:

- A user can add a website URL for which a short URL is created and stored in the database. The short URL must have a unique ID
  consisting of alphanumeric characters.
- The service exposes a Web Service API for providing appropriate CRUD operations to consumers.
- *Optional*: The Web Service API accepts and provides data in multiple formats, e.g. JSON, XML, etc.
- When opening a short URL in a Web Browser the website of the original URL shows up.
- Whenever someone opens a short URL a set of statistics is stored (number of calls, date and time, user agent, referrer, etc.).
- These statistics can be retrieved via the Web Service API.

These features are sorted by priority and therefore should be implemented in that order. It's not necessary to implement
all of these features. However, you should spend at least 3 - 4 hours on them. Please submit the assignment 3 days after
it has been send to you by pushing the code to GitHub.

Thank you for taking your time.

**Happy coding!**

## Work with the Project

### Build
This project uses [Maven](https://maven.apache.org) as its build tool. In order to build, test and package the application
run the following command:

```bash
mvn clean package
```

## Run
Once the application has been build and packaged successfully, you'll find a JAR file in the `target` folder. The file
is called `url-shortener.jar`. Use the following command to run the application:

```bash
java -jar target/url-shortener.jar
```

After a few seconds the application should have started successfully. You can access the Web Service endpoints via
[http://localhost:8080](http://localhost:8080) now.