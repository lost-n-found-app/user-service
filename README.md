# User Service
This code contains the User Service for the Lost and Found application, which is based microservices architecture.The User Service handles all operations related to user management.

## Setup Yugabyte and Config with pgAdmin
### Create the YugabyteDB Cluster on the Server: 
    Create a cluster named 'LostAndFoundApplication,' connect to it, and provide the root.crt file and credentials 
    for its creation.
    Download the root.crt and credentials file in local repo.
### Download pgAdmin and Configure with YugaByte:
    Now to Connect YugabyteDB to pgAdmin:-
    1.Open pgAdmin and right-click on Servers, then select Create > Server
    2.In the Create - Server window, fill in the following details:
     -> General Tab: Name: Enter a name for your server (e.g., YugabyteDB).
     -> Connection Tab:-
                       * Host: Enter the IP address of the server where YugabyteDB is running.
                       * Port: Use the default YugabyteDB PostgreSQL port 5433.
                       * Username: Use the PostgreSQL username
                       * Password: Enter the password (u will get from credentials file)
    3.Click Save to create and connect.
## Configure the Application Properties:
   1.create the database with name 'user_db'
   2.Add the following database configuration to your application's application.properties file:
                 spring.datasource.url=jdbc:postgresql://database_hostname:5432/dbName
                 spring.datasource.username=your_username
                 spring.datasource.password=your_password
                 spring.datasource.hikari.data-source-properties.sslrootcert=path/to/root.file




