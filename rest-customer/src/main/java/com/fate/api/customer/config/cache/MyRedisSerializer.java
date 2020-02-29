package com.fate.api.customer.config.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
 /**
 * @program: parent
 * @description: redis支持JAVA8时间类型序列化器
 * @author: chenyixin
 * @create: 2019-05-25 18:52
 **/
 @Component
public class MyRedisSerializer implements RedisSerializer<Object> {
  @Autowired
  private ObjectMapper mapper;


  @Override
  public byte[] serialize(@Nullable Object source) throws SerializationException {

   if (source == null) {
    return new byte[0];
   }

   try {
    return mapper.writeValueAsBytes(source);
   } catch (JsonProcessingException e) {
    throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
   }
  }


  @Override
  public Object deserialize(@Nullable byte[] source) throws SerializationException {
   return deserialize(source, Object.class);
  }

  /**
   * @param source can be {@literal null}.
   * @param type must not be {@literal null}.
   * @return {@literal null} for empty source.
   * @throws SerializationException
   */
  @Nullable
  public <T> T deserialize(@Nullable byte[] source, Class<T> type) throws SerializationException {

   Assert.notNull(type, "Deserialization type must not be null! Pleaes provide Object.class to make use of Jackson2 default typing.");

   if (isEmpty(source)) {
    return null;
   }

   try {
    return mapper.readValue(source, type);
   } catch (Exception ex) {
    throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
   }
  }

  static boolean isEmpty(@Nullable byte[] data) {
   return (data == null || data.length == 0);
  }
}
