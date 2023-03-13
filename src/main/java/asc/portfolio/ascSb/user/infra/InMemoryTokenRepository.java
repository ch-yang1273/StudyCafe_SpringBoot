package asc.portfolio.ascSb.user.infra;

import asc.portfolio.ascSb.common.repository.InMemoryRepository;
import asc.portfolio.ascSb.user.domain.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InMemoryTokenRepository implements TokenRepository {

    private final InMemoryRepository repository;

    @Override
    public void saveToken(String key, String value, Long timeOut) {
        repository.saveValue(key, value, timeOut);
    }

    @Override
    public String getToken(String key) {
        return repository.getValue(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return repository.hasKey(key);
    }

    @Override
    public void deleteToken(String key) {
        repository.deleteValue(key);
    }
}
