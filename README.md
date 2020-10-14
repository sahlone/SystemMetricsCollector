# System Metrics Collector

[![](https://jitpack.io/v/sahlone/SystemMetricsCollector.svg?label=Release)](https://jitpack.io/#sahlone/SystemMetricsCollector)

System Metrics Collector is a small app written to produce the system metrics like CPU , Memory and Disk metrics and publish it to the kafka cluster. The metrics are then consumed from kafka and then injected into database as time series data for later inspection.
### Verifying the APP
Ifg you want to verify the app for full package like Unit test, functional tests.
Then the app comes with an inbuild docker setup which will run all the  dependencies in an isolated docker containers. Once the containers are started, the App will already start publishing metrics and consuming and saving it in database. For analysis you can connect to database on port 5432 localhost to verifiy the data.
```
$./gradlew clean build
```
### Setup
For local setup we have two options:
##### External kafka and Postgress dependencies
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
##### Inbuild docker dependencies
If you dont have a Kafka and postgress instance running , then you can use the auto built in capabilities to test the app with docker.
To start the app and the dependencies below command can be used.
```
$./gradlew composeUp
```
This will start the docker container of postgress and kafka and xookeeper for dependencies. It will also start the application
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
2.  The functiona  test converage also needs improvement
3.  Integration of sending metrics to systems like Promethius or any other monitoring tools
4.  The Main thread uses sleep for delay which can be fixed by using some executors and wrapping into futures to schedule the jobs
5.  The application is built with java beans (mutability) because of libraries used like type safe. Immutability should be the preferred way.
6.  Usage of dependency injection
7.  The shutdown procedure is not written for the app, so no controlled shutdown available.
