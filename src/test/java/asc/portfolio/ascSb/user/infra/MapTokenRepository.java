package asc.portfolio.ascSb.user.infra;

import asc.portfolio.ascSb.user.domain.TokenRepository;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

// Fake double
@Primary
public class MapTokenRepository implements TokenRepository {

    Map<String, String> map = new HashMap<>();

    @Override
    public void saveToken(String key, String value, Long timeOut) {
        System.out.println("MapTokenRepository.saveToken");
        map.put(key, value);
    }

    @Override
    public String getToken(String key) {
        System.out.println("MapTokenRepository.getToken");
        return map.get(key);
    }

    @Override
    public Boolean hasKey(String key) {
        System.out.println("MapTokenRepository.hasKey");
        return map.containsKey(key);
    }

    @Override
    public void deleteToken(String key) {
        System.out.println("MapTokenRepository.deleteToken");
        map.remove(key);
    }
}
