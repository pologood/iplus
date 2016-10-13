package commons.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonHelper {

  private static ObjectMapper getMapper() {
    return new Jackson2ObjectMapperBuilder().featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .serializerByType(LocalDate.class, new LocalDateJsonSerializer())
        .serializerByType(LocalDateTime.class, new LocalDateTimeJsonSerializer()).build()
        .setSerializationInclusion(Include.NON_NULL).setSerializationInclusion(Include.NON_EMPTY);
  }

  public static <T> T readValue(String src, Class<T> valueType) {
    try {
      return getMapper().readValue(src, valueType);
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  public static <T> T readValue(String src, TypeReference<T> valueTypeRef) {
    try {
      return getMapper().readValue(src, valueTypeRef);
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  public static byte[] writeValueAsBytes(Object value) {
    try {
      return getMapper().writeValueAsBytes(value);
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  public static String writeValueAsString(Object value) {
    try {
      return getMapper().writeValueAsString(value);
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }
}
