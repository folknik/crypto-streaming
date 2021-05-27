package com.crypto.streaming.utils;

import com.crypto.streaming.model.Transfer;
import com.crypto.streaming.model.Transfer2;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DataCollector {

    public static List<Tuple2<Integer, Transfer>> collectTransferData(String pathToRead) throws IOException {
        List<Tuple2<Integer, Transfer>> data = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToRead));
        String line = "";
        int maxBlock = 0;
        while (line != null) {
            line = bufferedReader.readLine();
            if (line != null) {
                Transfer transfer = null;
                try {
                    transfer = new ObjectMapper().readValue(line, Transfer.class);
                } catch(Exception e) {
                    Transfer2 transfer2 = new ObjectMapper().readValue(line, Transfer2.class);
                    transfer = TransferMapper.mapRecord(transfer2);
                } finally {
                    assert transfer != null;
                    data.add(new Tuple2<>(1, transfer));
                    if (transfer.getBlockNumber() > maxBlock) maxBlock = transfer.getBlockNumber();
                }
            }
        }

        Transfer finalElement = new Transfer();
        finalElement.setFrom("final");
        finalElement.setTo("final");
        finalElement.setValue(BigInteger.valueOf(0));
        finalElement.setBlockNumber(maxBlock + 1);
        data.add(new Tuple2<>(1, finalElement));

        bufferedReader.close();
        return data;
    }
}
