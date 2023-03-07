package asc.portfolio.ascSb.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

  Optional<User> findByLoginId(String loginId);

  User findByNameContains(String name);

}
