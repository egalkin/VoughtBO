#!/usr/bin/env bash

mvn clean

mvn package -e -DskipTests

scp -P 2222 -i ~/.ssh/id_rsa \
    target/Vought-1.0.0-SNAPSHOT.jar \
    s223378@se.ifmo.ru:/home/s223378/

ssh -P 2222 -i ~/.ssh/id_rsa s223378@se.ifmo.ru << EOF
pgrep -u s223378 java | xargs kill -9
nohup java -jar Vought-1.0.0-SNAPSHOT.jar > log.txt &
EOF
