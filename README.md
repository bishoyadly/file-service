# File Service

## Table of Contents

- [About](#About)
- [Technologies Used](#technologies-used)
- [Build And Run Instructions](#build-and-run-instructions)

## About

File Service:

- SaveFile endpoint with payload of file (saving on local disk) and it's retention period
- Delete the expired files regarding to it's saved retention period (automated not from request)
- Use leader election pattern to execute delete.
- Add and use Service config for saving path
- Add and use Service config for delete cycle time (run every 10 minutes for example)

## Technologies Used

- Java 17
- Maven
- Spring Boot to save time writing boilerplate configurations (DB configurations,ORM vendor configurations, Servlets
  configs in web.xml file, etc...)
- Spring MVC to implement multipart endpoint that receive file attachment along with it's retention period
- Spring Data JPA (which uses Hibernate ORM) to save time instead of writing native SQL
- PostgreSQL Database to save the file state store that hold file path, retention period
- Liquibase DB migration tool to initialize schemas
- Apache Zookeeper that is used for highly reliable distributed coordination that implements ZooKeeper Atomic Broadcast
  Protocol (ZAB protocol)
- Apache Curator is a Java/JVM client library for Apache ZooKeeper
- Apache Zookeeper, Apache Curator used for leader election

## Build And Run Instructions

**Install Required Tools:**

1. Install Java OpenJDK 17 using [Software Development Kit Manager](https://sdkman.io/) or from preferred source

```
curl -s "https://get.sdkman.io" | bash
sdk install java 11.0.2-open
```

2. Install maven using APT or manually from [Apache Maven Website](https://maven.apache.org/install.html) or just run using maven wrapper shell script installed within project.

```
sudo apt update
sudo apt install maven
```

3. Install PostgreSQL Database

```
https://www.postgresql.org/download/
```

4. Install and run Apache Zookeeper

```
https://www.apache.org/dyn/closer.lua/zookeeper/zookeeper-3.8.4/apache-zookeeper-3.8.4-bin.tar.gz
https://zookeeper.apache.org/doc/current/zookeeperStarted.html

./zkServer.sh start
```

5. Run multiple instances of the spring boot application using server shell scripts
```
./server1.sh
./server2.sh
./server3.sh
```