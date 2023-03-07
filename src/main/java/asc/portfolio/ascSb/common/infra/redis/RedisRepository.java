package asc.portfolio.ascSb.common.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveValue(String key, String value, Long timeOut) {
        redisTemplate
                .opsForValue()
                .set(key, value, Duration.ofMillis(timeOut));
    }

    public String getValue(String key) {
        return redisTemplate
                .opsForValue()
                .get(key);
    }

    public Boolean hasKey(String string) {
        return redisTemplate
                .hasKey(string);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}
