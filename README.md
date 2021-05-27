First of all, you need to compile the Java code 

```
$ mvn clean package
``` 

and you will receive JAR file which will be at path

```
$ target/stream-crypto-transactions-1.0.0.jar
```

Below steps to run JAR file
```
$ JOB_CLASS_NAME="com.crypto.streaming.App"
$ JM_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})

$ docker cp target/stream-crypto-transactions-1.0.0.jar "${JM_CONTAINER}":/job.jar
$ docker cp eth_transfers_input.txt "${JM_CONTAINER}":/eth_transfers_input.txt 
$ docker exec -t -i "${JM_CONTAINER}" flink run -d -c ${JOB_CLASS_NAME} /job.jar
```
