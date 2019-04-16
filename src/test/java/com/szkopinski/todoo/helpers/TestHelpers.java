package com.szkopinski.todoo.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestHelpers {

  private static final ObjectMapper mapper = getObjectMapper();

  public static String convertToJson(Object object) throws JsonProcessingException {
    return mapper.writeValueAsString(object);
  }

  public static String convertToJson(Iterable<Object> objects) throws JsonProcessingException {
    return mapper.writeValueAsString(objects);
  }

  private static ObjectMapper getObjectMapper() {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return mapper;
  }
}
