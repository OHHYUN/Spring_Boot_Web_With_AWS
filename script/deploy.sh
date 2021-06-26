#! /bin/bash

REPOSITORY=/home/ec2-user/app/step1
PROJECT_NAME=Spring_Boot_Web_With_AWS

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"

git pull

echo "> 프로젝트 Build  시작"

./gradlew build

echo "> Move Step1 dir"

cd $REPOSITORY

echo "> Build 파일 복사"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo " 현재 구동 중인 애플리케이션 pid  확인 "

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
	echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다"
else
	echo "> kill -15 $CURRENT_PID"
	kill -15 $CURRENT_PID
	sleep 5
fi

echo "새 애플리케이션 배포"
echo $REPOSITORY
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR Name : $JAR_NAME "

nohup java -jar \
	-Dspring.config.location=classpath:/application.yaml,/home/ec2-user/app/application-oauth.yaml,/home/ec2-user/app/application-real-db.yaml,classpath:/application-real.yaml -Dspring.profiles.active=real $REPOSITORY/$JAR_NAME 2>&1 &
