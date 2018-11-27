package com.zm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置
 *
 * 已废弃，采用spring.data.redis的配置
 */
@Configuration
@ConditionalOnMissingClass("org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration")
@Deprecated
public class RedisConfig {

    private RedisConnectionFactory factory;
    @Autowired
    public void setFactory(RedisConnectionFactory factory) {
        this.factory = factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new RedisObjectSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    /**
     * 对象序列化
     */
    public static class RedisObjectSerializer implements RedisSerializer<Object> {
        private Converter<Object, byte[]> serializer = new SerializingConverter();
        private Converter<byte[], Object> deserializer = new DeserializingConverter();

        static final byte[] EMPTY_ARRAY = new byte[0];
        @Override
        public Object deserialize(byte[] bytes) {
            if (isEmpty(bytes)) {
                return null;
            }
            try {
                return deserializer.convert(bytes);
            } catch (Exception ex) {
                throw new SerializationException("Cannot deserialize", ex);
            }
        }
        @Override
        public byte[] serialize(Object object) {
            if (object == null) {
                return EMPTY_ARRAY;
            }
            try {
                return serializer.convert(object);
            } catch (Exception ex) {
                return EMPTY_ARRAY;
            }
        }
        private boolean isEmpty(byte[] data) {
            return (data == null || data.length == 0);
        }
    }
}
