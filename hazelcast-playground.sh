#!/usr/bin/env bash

# build server
mvn clean install -f `pwd`/hazelcast-server/pom.xml

# start servers
java -jar `pwd`/hazelcast-server/target/*.jar --server.port=5000 --spring.hazelcast.config=file://`pwd`/hazelcast-server/hazelcast-server.xml &
java -jar `pwd`/hazelcast-server/target/*.jar --server.port=5001 --spring.hazelcast.config=file://`pwd`/hazelcast-server/hazelcast-server.xml &
java -jar `pwd`/hazelcast-server/target/*.jar --server.port=5002 --spring.hazelcast.config=file://`pwd`/hazelcast-server/hazelcast-server.xml &

sleep 20s

# build client
mvn clean install -f `pwd`/hazelcast-client/pom.xml

# start client
java -jar `pwd`/hazelcast-client/target/*.jar --server.port=8080 --spring.hazelcast.config=`pwd`/hazelcast-client/hazelcast-client.xml &

sleep 7s

# schedules before submission
curl -XGET http://localhost:8080/schedules | python -m json.tool

MESSAGE="{\"id\":\"`uuidgen`\",\"at\":\"`date -u +"%Y-%m-%dT%H:%M:%S.%3N"Z`\",\"destination\":\"foo.queue\",\"message\":\"{\\\"foo\\\":\\\"baz\\\"}\",\"submitted\":false}"
for i in 1 0 2; do curl -i -H 'Content-Type:application/json' -XPOST "http://localhost:500$i/schedule" -d ${MESSAGE}; done;

# wait for schedule to be submitted
sleep 30s
curl -XGET http://localhost:8080/schedules | python -m json.tool
