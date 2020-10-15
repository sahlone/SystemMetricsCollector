# System Metrics Collector

System Metrics Collector is a small app written to produce the system metrics like CPU , Memory and Disk metrics and publish it to the kafka cluster. The metrics are then consumed from kafka and then injected into database as time series data for later inspection.
### Verifying the APP
If you want to verify the app for full package like Unit test, functional tests.
Then the app comes with an in build docker setup which will run all the  dependencies in an isolated docker containers. Once the containers are started, the App will already start publishing metrics and consuming and saving it in database. For analysis you can connect to database on port 5432 localhost to verify the data.
```
$ ./gradlew clean build
```
Also the system contains functional tests that can be verified if needed. This requires to have a docker daemon running as docker containers are used for verifying the build

```
$ ./gradlew clean functionalTest
```
### Create Docker Image
To create the docker image that can be used to run the system
```
$ ./gradlew clean build dockerPushtag -Pregistry=registrytopushto
```
### Setup
For local setup we have two options:
##### External kafka and Postgres dependencies
For application to work with external dependencies, various system variables need to be setup before the app can start. The variables are injected using the environment variables. The variables are as follows:
```
JDBC_URL
DB_USER
DB_PW
KAFKA_BOOTSTRAP_SERVERS
SYSTEM_METRICS_TOPIC
```
The above variables are a minimum required for a basic startup without tuning any application parameters
If you want to fine tune the application, please have a look at the `application.conf` for the extensive list of variables that can be used to modify the behaviour.
##### In build docker dependencies
If you dont have a Kafka and postgres instance running , then you can use the auto built in capabilities to test the app with docker.
To start the app and the dependencies below command can be used.
```
$./gradlew composeUp
```
This will start the docker container of postgres and kafka and zookeeper for dependencies. It will also start the application
in the docker container.

#### Running only as producer or Consumer
If you just want to just run the producer or a consumer and not the full app. You can inject the config into the environment with following variables to change the behaviour.  Also the behaviour of metrics publishing can be changed as well.
```
METRICS_PUBLISH_DELAY
ENABLE_CONSUMER
ENABLE_PRODUCER
```
### Contributions needed
1.  The unit tests need to be improved with coverage
2.  The functional  test coverage also needs improvement
3.  Integration of sending metrics to systems like [Prometheus](https://prometheus.io/) or any other monitoring tools
4.  The Main thread uses sleep for delay which can be fixed by using some executors and wrapping into futures to schedule the jobs
5.  The application is built with java beans (mutability) because of libraries used like type safe. Immutability should be the preferred way.
6.  Usage of dependency injection
7.  The shutdown procedure is not written for the app, so no controlled shutdown available.
8. Integrate github Actions/CI-CD to check every merge to master and every Pull request integrity
