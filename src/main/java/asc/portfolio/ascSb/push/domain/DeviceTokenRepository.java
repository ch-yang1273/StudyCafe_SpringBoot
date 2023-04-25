package asc.portfolio.ascSb.push.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long>, DeviceTokenCustomRepository {

    Optional<DeviceToken> findByToken(String token);
}
