#!/bin/bash

BUILD_JAR=$(ls /home/ec2-user/build/build/libs/ascSb-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build file name: $JAR_NAME" >> /home/ec2-user/deploy.log

echo "> Checl the currently running application PID" >> /home/ec2-user/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> No applications are currently running" >> /home/ec2-user/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> build file copy" >> /home/ec2-user/deploy.log
DEPLOY_PATH=/home/ec2-user/
cp $BUILD_JAR $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
PROPERTIES_LIST=file:/home/ec2-user/resources/application.properties,file:/home/ec2-user/resources/application-prod.properties
echo "> DEPLOY_JAR deploy" >> /home/ec2-user/deploy.log
echo "> properties: $PROPERTIES_LIST" >> /home/ec2-user/deploy.log
nohup java -jar -Dspring.config.location=$PROPERTIES_LIST $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &