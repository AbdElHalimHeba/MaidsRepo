package cc.maids.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
		
		RedisTemplate template = new RedisTemplate();
		
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        serializer.configure((mapper) -> {
        	mapper.registerModule(new JavaTimeModule()); 
        	mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        });
        
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(serializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(serializer);     
        template.setConnectionFactory(connectionFactory);

		return template;
	}

}
