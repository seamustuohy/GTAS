# GTAS Installation

## Environment

* Java 8 update 45
* Apache Tomcat 7.0.62
* MariaDB 10.0.19 Stable
* Maven 3.3
* NodeJs 4 (for npm)

## Download

Grab the latest code from BitBucket

```
git clone https://[username]@bitbucket.org/sanandreas/sanandreas.git
```

## Build

First update the following values in gtas-parent/gtas-commons/src/main/resourceshibernate.properties to work with your installation of MariaDB:

```
hibernate.connection.url=jdbc:mariadb://localhost:3306/gtas
hibernate.connection.username=root
hibernate.connection.password=admin
```

GTAS uses bower to manage front-end dependencies.  Use npm to download bower first and then use bower to download javascript libraries:

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

Build with integration tests (and unit tests).  Requires setting up the database fully.  See below.

```
mvn clean install -Dskip.integration.tests=false
```

## Deploy

Create the database 

```
mysql -u root -p
$ create database gtas
```

Create the schema using Maven:

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

## Backend Processes

GTAS currently relies on the following batch processes:

* GTAS Loader: Parses and loads APIS and PNR messages.
* GTAS Rule Runner: Applies user-defined rules against messages.

On production environments, both processes should be set up as cron jobs.

### GTAS Loader

After compiling, the GTAS loader is located in gtas-loader/target/gtas-loader.jar.  It's an executable jar that takes one or more input filenames on the command line.  For example, the following command would execute the loader on two input files:

```
java -jar gtas-loader/target/gtas-loader.jar 101.txt 102.txt
```

Alternatively, you can provide input and output directories.  This is useful for setting up the loader as a batch process, and in cases where the shell may not be able to handle a large number of input files:

```
java -jar gtas-loader/target/gtas-loader.jar inputdir outputdir
```

### GTAS Rule Runner

The rule runner takes no arguments

```
java -jar gtas-rulesvc/target/gtas-rulesvc-jar-with-dependencies.jar 
```
