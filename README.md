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

Example of transactions:
```
{"from": "0x0", "to":"0x1", "value":10, "blockNumber":0, "timestamp":1438269973}
{"from": "0x0", "to":"0x2", "value":20, "blockNumber":0, "timestamp":1438269973}
{"from": "0x0", "to":"0x3", "value":30, "blockNumber":0, "timestamp":1438269973}
{"from": "0x0", "to":"0x3", "value":30, "blockNumber":1, "timestamp":1438269988}
{"from": "0x3", "to":"0x1", "value":20, "blockNumber":2, "timestamp":1438270017}
{"from": "0x1", "to":"0x2", "value":20, "blockNumber":3, "timestamp":1438270048}
```
Result:
```
{"address":"0x0","balance":-60,"blockNumber":0}
{"address":"0x1","balance":10,"blockNumber":0}
{"address":"0x2","balance":20,"blockNumber":0}
{"address":"0x3","balance":30,"blockNumber":0}

{"address":"0x0","balance":-90,"blockNumber":1}
{"address":"0x3","balance":60,"blockNumber":1}

{"address":"0x1","balance":30,"blockNumber":2}
{"address":"0x3","balance":40,"blockNumber":2}

{"address":"0x1","balance":10,"blockNumber":3}
{"address":"0x2","balance":40,"blockNumber":3}
```