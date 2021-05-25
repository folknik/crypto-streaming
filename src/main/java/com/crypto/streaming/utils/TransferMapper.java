package com.crypto.streaming.utils;

import com.crypto.streaming.model.Transfer;
import com.crypto.streaming.model.Transfer2;

import java.math.BigInteger;

public class TransferMapper {

    public static Transfer mapRecord(Transfer2 record) {
        Transfer obj = new Transfer();
        obj.setFrom(record.getFrom());
        obj.setTo(record.getTo());
        obj.setValue(new BigInteger(record.getValue()));
        obj.setValueExactBase36(record.getValueExactBase36());
        obj.setBlockNumber(record.getBlockNumber());
        obj.setTimestamp(record.getTimestamp());
        obj.setTransactionHash(record.getTransactionHash());
        obj.setType(record.getType());
        obj.setPrimaryKey(record.getPrimaryKey());
        obj.setTransactionPosition(record.getTransactionPosition());
        return obj;
    }
}
