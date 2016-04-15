# GTAS AWS Notes

## Overview

Access our AWS deployment on govcloud: [https://taspd01govcloud.signin.amazonaws-us-gov.com]()

We have two environments -- dev and staging -- deployed on separate tomcat instances on the same ec2 server.  There are separate RDS instances for dev and staging.

## Deployment

1. Build gtas.war locally.  Make sure your hibernate.properties is set for the correct RDS instance.
2. Copy gtas.war, gtas-loader.jar, and gtas-rulesvc-jar-with-dependencies.jar to the app server ec2 instance.
3. Login to the ec2 instance.  Copy the files to a folder under /tmp.
4. Deploy the files in the appropriate tomcat folder: /data/atsg/tomcat for dev and /data/atsg_staging/tomcat for staging.

If you need to shutdown/startup tomcat use the following command:
```
$ sudo -u tomcat sh ./bin/catalina.sh stop
```

## Cron jobs

To start/stop the cron jobs, edit the crontab as the 'tomcat' user:

```
$ sudo -u tomcat crontab -e
```

The file should look something like the following:

```
# dev
*/1 * * * * /data/atsg/tomcat/gtas-loader.sh >> /data/atsg/tomcat/logs/loader.log 2>&1
*/2 * * * * /data/atsg/tomcat/gtas-rulesvc.sh > /data/atsg/tomcat/logs/rulesvc.log 2>&1

# staging
*/1 * * * * /data/atsg_staging/tomcat/gtas-loader.sh >> /data/atsg_staging/tomcat/logs/loader.log 2>&1
*/2 * * * * /data/atsg_staging/tomcat/gtas-rulesvc.sh > /data/atsg_staging/tomcat/logs/rulesvc.log 2>&1
```

Both dev and staging specify how to execute the loader and rulesvc jars.  To disable a service, comment out the line and save the file.  Cron will automatically read the updated crontab.