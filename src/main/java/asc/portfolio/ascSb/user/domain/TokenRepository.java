package asc.portfolio.ascSb.user.domain;

public interface TokenRepository {
    void saveToken(String key, String value, Long timeOut);

    String getToken(String key);

    Boolean hasKey(String key);

    void deleteToken(String key);
}
