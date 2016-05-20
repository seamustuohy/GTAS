# GTAS AWS Notes

## Overview

Note: *this information is intended for GTAS developers only.*

Access our AWS deployment on govcloud: [https://taspd01govcloud.signin.amazonaws-us-gov.com]()

We have two environments -- dev and staging -- deployed on separate Tomcat instances on the same ec2 server.  There are separate RDS instances for dev and staging.

## Deployment

1. Build GTAS locally.  Make sure hibernate.properties is set for the correct RDS instance.  Also update jobScheduler.properties with the correct directories.
2. Transfer gtas.war and gtas-job-scheduler.war to the app server ec2 instance.
3. Login to the ec2 instance.  Copy the files to a folder under /tmp. 
4. Deploy the files in the appropriate Tomcat folder: /data/atsg/tomcat for dev and /data/atsg_staging/tomcat for staging:
```$ sudo -u tomcat cp /tmp/* /data/atsg/tomcat/webapps```

If you need to shutdown/startup tomcat use the following command:
```
$ sudo -u tomcat sh ./bin/catalina.sh stop
```

## Cron jobs

The scheduler war will execute both the loader and the rule runner.  If you wish to configure traditional cron jobs, you can use the following instructions:

Edit the crontab as the 'tomcat' user:

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