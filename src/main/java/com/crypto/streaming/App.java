package com.crypto.streaming;

import java.util.Properties;

import com.crypto.streaming.model.Transfer;
import com.crypto.streaming.utils.*;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {

	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	private static final int timeSeconds = 5;
	private static final String inputTopic = "input-topic";
	private static final String outputTopic = "output-topic";


	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("auto.offset.reset", "earliest");
		properties.setProperty("group.id", "crypto-consumer-group");
		LOG.info("Properties set {}", properties);

		FlinkKafkaConsumer<Transfer> kafkaSource = new FlinkKafkaConsumer<>(
				inputTopic,
				new CryptoDeserializationSchema(),
				properties);

		DataStream<Transfer> inputStream = env.addSource(kafkaSource);

		LOG.info("Stream created, {}", inputStream);

		DataStream<String> aggregateStream = inputStream
				.assignTimestampsAndWatermarks(new TimestampExtractor())
				.keyBy(Transfer::getBlockNumber)
				.timeWindow(Time.seconds(timeSeconds))
				.aggregate(new CryptoAggregator());

		FlinkKafkaProducer<String> kafkaProducer = new FlinkKafkaProducer<>(
				outputTopic,
				new CryptoSerializationSchema(outputTopic),
				properties,
				FlinkKafkaProducer.Semantic.EXACTLY_ONCE);

		aggregateStream.addSink(kafkaProducer);

		env.execute("CryptoTransactionApp");

	}

}
