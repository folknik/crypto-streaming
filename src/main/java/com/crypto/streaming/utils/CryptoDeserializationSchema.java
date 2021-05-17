package com.crypto.streaming.utils;

import com.crypto.streaming.model.Transfer;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CryptoDeserializationSchema implements DeserializationSchema<Transfer> {

    private static final long serialVersionUID = 1L;

    @Override
    public Transfer deserialize(byte[] bytes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(bytes, Transfer.class);
    }

    @Override
    public boolean isEndOfStream(Transfer s) {
        return false;
    }

    @Override
    public TypeInformation<Transfer> getProducedType() {
        return TypeInformation.of(Transfer.class);
    }
}
