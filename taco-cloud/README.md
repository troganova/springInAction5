------------------------------------------------
cassandra ch4
------------------------------------------------
$ docker network create cassandra-net
$ docker run --name cassandra \
--network cassandra-net \
-p 9042:9042 \
-d cassandra:latest


запустить оболочку касандры
$ docker run -it --network cassandra-net --rm cassandra cqlsh cassandra

cqlsh> CREATE KEYSPACE tacocloud
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}
AND durable_writes = true;

cqlsh> USE tacocloud;
cqlsh:tacocloud> select * from orders;

------------------------------------------------
mongo ch4
------------------------------------------------
$ docker run --name mongo -p 27017:27017 -d mongo:latest
или
<dependency>
<groupId>de.flapdoodle.embed</groupId>
<artifactId>de.flapdoodle.embed.mongo</artifactId>
<!-- <scope>test</scope> -->
</dependency>

------------------------------------------------
jms artemis ch9
------------------------------------------------
docker run --detach \
--name artemis \
-p 61616:61616 \
-p 8161:8161 \
-e ARTEMIS_USER=tacoweb \
-e ARTEMIS_PASSWORD=letm31n \
-e ARTEMIS_QUEUES=tacocloud.order.queue \
--rm \
apache/activemq-artemis:latest-alpine


http://localhost:8161/console

------------------------------------------------
rabbitMQ ch9
------------------------------------------------

# latest RabbitMQ 4.x
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management

http://localhost:15672 guest/guest

------------------------------------------------
kafka ch9
------------------------------------------------
docker run -d  --name kafka  -p 9092:9092  \
-e KAFKA_ENABLE_KRAFT=yes  \
-e KAFKA_CFG_PROCESS_ROLES=broker,controller \
-e KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE=true \
-e KAFKA_CFG_NODE_ID=1  \
-e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
-e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093  \
-e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092  \
-e KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
-e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER  \
bitnami/kafka:3.7

docker exec -it kafka /opt/bitnami/kafka/bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic tacocloud.orders.topic \
--from-beginning


------------------------------------------------
docker ch18
------------------------------------------------
docker build . -t tacocloud-web
docker run -p 8080:8080 -d tacocloud-web  