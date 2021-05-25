package com.crypto.streaming.utils;

import com.crypto.streaming.model.Transfer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataCollector {

    public static List<Tuple2<Integer, Transfer>> collectTransferData(String pathToRead) throws IOException {
        List<Tuple2<Integer, Transfer>> data = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToRead));
        String line = "";
        while (line != null) {
            line = bufferedReader.readLine();
            if (line != null) {
                Transfer transfer = new ObjectMapper().readValue(line, Transfer.class);
                data.add(new Tuple2<>(transfer.getBlockNumber(), transfer));
            }
        }
        bufferedReader.close();
        return data;
    }
}
