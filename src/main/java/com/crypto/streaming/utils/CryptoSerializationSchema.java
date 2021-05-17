package com.crypto.streaming.utils;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.Nullable;

public class CryptoSerializationSchema implements KafkaSerializationSchema<String> {

    private final String topic;
    private ObjectMapper mapper;

    public CryptoSerializationSchema(String topic) {
        super();
        this.topic = topic;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(String s, @Nullable Long aLong) {
        byte[] b = null;
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        try {
            b= mapper.writeValueAsBytes(s);
        } catch (JsonProcessingException e) {
            // TODO
        }
        return new ProducerRecord<byte[], byte[]>(topic, b);
    }
}
