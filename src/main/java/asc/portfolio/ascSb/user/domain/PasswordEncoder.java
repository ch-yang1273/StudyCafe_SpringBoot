package asc.portfolio.ascSb.user.domain;

@FunctionalInterface
public interface PasswordEncoder {
    String encryptPassword(String id, String password);
}
