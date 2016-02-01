# GTAS Installation

## Environment
* Java 8 update 45
* Apache Tomcat 7.0.62
* MariaDB 10.0.19 Stable
* Maven 3.3
* NodeJs 4 (for npm)

## Download

Get the latest code from BitBucket

```
git clone https://[username]@bitbucket.org/sanandreas/sanandreas.git
```

## Build

Navigate to gtas-parent/gtas-commons/src/main/resources. Update the values in the **hibernate.properties** file with the values of your database.

Download front-end dependencies:

```
cd gtas-webapp
npm install  
bower install
```

Standard build with unit tests. Note: if maven shows a java socket error (invalid argument 'connect'), you may need to add -Djava.net.preferIPv4Stack=true to the MAVEN_OPTS environment variable.

```
cd gtas-parent
mvn clean install
```

Build without unit tests

```
mvn clean install -Dskip.unit.tests=true
```

Build with integration tests (and unit tests)

```
mvn clean install -Dskip.integration.tests=false
```

## Deploy

Create the database 

```
mysql -u root -p
$ create database gtas
```

Create the schema

```
cd gtas-commons
mvn hibernate4:export
```

Load the application configuration and lookup data (Note that countries must be loaded before airports):


```
mysql –u root –p
$ source gtas-parent/gtas-commons/src/main/resources/sql/gtas_data.sql
$ source gtas-parent/gtas-commons/src/main/resources/sql/countries.sql
$ source gtas-parent/gtas-commons/src/main/resources/sql/carriers.sql
$ source gtas-parent/gtas-commons/src/main/resources/sql/airports.sql
```

Deploy to tomcat and start the server.

```
cp gtas-webapp/gtas.war [tomcat home]/webapps
```

Access site at http://localhost:8080/gtas

## Set up Backend Processes

