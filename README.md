# GTAS

### Environment

* Java 8 
* Build: Maven 3, npm, bower
* Apache Tomcat (7.0.62)
* MariaDB (10.0.19 Stable)
* Drools (6.2)
    * http://www.drools.org/download/download.html
    * http://download.jboss.org/drools/release/6.2.0.Final/org.drools.updatesite/

### Build and Deployment

0. Get the latest code from BitBucket
> git clone https://[username]@bitbucket.org/sanandreas/sanandreas.git
1. Download Bower components if you're going to deploy the web app.
> cd gtas-parent/gtas-webapp    
bower install 
2. Standard build with unit tests.  
> cd gtas-parent  
mvn clean install
3. Build without unit tests
> mvn clean install -Dskip.unit.tests=true
4. Build with integration tests (and unit tests)
> mvn clean install -Dskip.integration.tests=false
5. Create the database (make sure the settings in hibernate.properties are correct).
> mysql -u root -p  
create database gtas
6. Create the schema
> cd gtas-commons  
mvn hibernate4:export
7. Deploy to tomcat
> cp gtas-webapp/gtas.war [tomcat home]/webapps
8. The lookup data (countries, airports, carriers) is located in gtas-commons/src/main/resources/sql.  Make sure you load countries.sql before airports.sql.  Load these files from the mysql command line as you may experience problems with special characters from Heidisql:
> mysql -u root -p  
source [sql filename]
9. Sample users and roles are in gtas_data.sql 
10. Start tomcat and access site at http://localhost:8080/gtas

### Deployment to AWS

1. download hibernate.aws.properties from our google drive folder under the 'config' directory.
2. replace your local hibernate.properties with this one.
3. do a full build
4. move the war over to your local aws account, e.g., if you're using putty on windows
> pscp gtas.war mcopenhafer@[app server IP]:.
5. login to your aws appserver account and move the war to /tmp
> mv gtas.war /tmp
6. deploy to tomcat
> cd /data/atsg/tomcat/
sudo -u tomcat sh bin/catalina.sh stop  
mv /tmp/gtas.war webapps  
sudo -u tomcat sh bin/catalina.sh start  
7. check the logs for errors  
> tail -f logs/catalina.out

### Importing Test Data

1. To load sample APIS data (flights, passengers), download keith_msgs.zip from the google drive folder/APIS.
2. unzip the folder into a temp directory
3. Use the generated jar under the gtas-loader module to load one or more files.
4. For example, to load one file:
> java -jar gtas-loader/target/gtas-loader.jar 101.txt
5. To load all of the sample files, you need to provide two directories -- one for the source files and one where the loader will place the processed files.  Note that some of the files are invalid and will not import.  e.g.,
> java -jar gtas-loader/target/gtas-loader.jar /tmp/keith_msgs/ /tmp/out/