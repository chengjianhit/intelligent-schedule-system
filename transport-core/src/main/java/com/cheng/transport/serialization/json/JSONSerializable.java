package com.cheng.transport.serialization.json;

import com.cheng.logger.BusinessLoggerFactory;
import com.cheng.transport.serialization.MessageCodec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;


public class JSONSerializable  implements MessageCodec {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = BusinessLoggerFactory.getBusinessLogger("SCHEDULE",JSONSerializable.class);


    @Override
    public <T> byte[] serializable(T obj) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(obj);
            return bytes;
        } catch (JsonProcessingException e) {
            logger.error("JSONSerializable serializable error {}", e);
        }
        return null;
    }

    @Override
    public <T> T deSerializable(byte[] bytes, Class<T> obj) {
        try {
            T msg = objectMapper.readValue(bytes, obj);
            return msg;
        } catch (IOException e) {
            logger.error("JSONSerializable deSerializable error {}", e);
        }

        return null;
    }

    @Override
    public String getCoderName() {
        return null;
    }
}
