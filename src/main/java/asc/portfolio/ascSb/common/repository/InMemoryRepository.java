package asc.portfolio.ascSb.common.repository;

public interface InMemoryRepository {
    void saveValue(String key, String value, Long timeOut);

    String getValue(String key);

    Boolean hasKey(String key);

    void deleteValue(String key);
}
