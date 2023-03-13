package asc.portfolio.ascSb.common.infra.redis;

import asc.portfolio.ascSb.common.repository.InMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisRepository implements InMemoryRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveValue(String key, String value, Long timeOut) {
        redisTemplate
                .opsForValue()
                .set(key, value, Duration.ofMillis(timeOut));
    }

    @Override
    public String getValue(String key) {
        return redisTemplate
                .opsForValue()
                .get(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate
                .hasKey(key);
    }

    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}
