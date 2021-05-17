package com.crypto.streaming.utils;

import com.crypto.streaming.model.Transfer;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

public class TimestampExtractor implements AssignerWithPunctuatedWatermarks<Transfer> {

    private static final long serialVersionUID = 1L;

    @Override
    public long extractTimestamp(Transfer element, long previousElementTimestamp) {
        return element.getTimestamp();
    }

    @Override
    public Watermark checkAndGetNextWatermark(Transfer lastElement, long extractedTimestamp) {
        return new Watermark(extractedTimestamp);
    }

}