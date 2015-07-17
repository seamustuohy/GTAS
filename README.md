# GTAS

### Environment

* Java 8 
* Maven 3, npm, bower
* Apache Tomcat (7.0.62)
* MariaDB (10.0.19 Stable)
    * https://downloads.mariadb.org/
* Drools (6.2)
    * http://www.drools.org/download/download.html
    * http://download.jboss.org/drools/release/6.2.0.Final/org.drools.updatesite/

### Build and Deployment

1. Standard build with unit tests:
> mvn clean install
2. Build without unit tests
> mvn clean install -Dskip.unit.tests=true
3. Build with integration tests (and unit tests)
> mvn clean install -Dskip.integration.tests=false
4. Deploy to tomcat
> cp gtas-webapp/gtas.war [tomcat home]/webapps
5. Access site at http://localhost:8080/gtas

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

1. The lookup data is located in gtas-commons/src/main/resources/sql.  Load these files from the mysql command line as you may experience problems with special characters from Heidisql:
> mysql -u root -p
source [sql filename]
2. Sample users and roles are in config/db/gtas_data.sql 
3. To load sample APIS data (flights, passengers), download keith_msgs.zip from the google drive folder/APIS.
4. unzip the folder into a temp directory
5. Use the generated jar under the apis-loader module to load one or more files.
6. For example, to load one file:
> java -jar apis-loader/target/apis-loader-0.1-SNAPSHOT-jar-with-dependencies.jar 10.txt
7. To load all of the sample files, you need to provide two directories -- for the source files and one where the loader will place the processed files.  e.g.,
> java -jar apis-loader/target/apis-loader-0.1-SNAPSHOT-jar-with-dependencies.jar /tmp/keith_msgs/ /tmp/out/