package com.crypto.streaming;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.crypto.streaming.model.Balance;
import com.crypto.streaming.model.Transfer;
import com.crypto.streaming.utils.*;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import static com.crypto.streaming.utils.DataCollector.collectTransferData;

public class App {

	private static final String path = "/input.txt";

	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		List<Tuple2<Integer, Transfer>> transferList = collectTransferData(path);

		env.fromCollection(transferList)
				.keyBy(0)
				.flatMap(new CryptoWindowAggregator())
				.map(values -> {
					List<String> jsonBalances = new ArrayList<>();
					for(Balance value : values){
						String strValue = new ObjectMapper().writeValueAsString(value);
						jsonBalances.add(strValue);
					}
					return jsonBalances.stream().collect(Collectors.joining("\n", "", "\n"));
				})
				.print();
		env.execute("CryptoTransactionApp");

	}

}
