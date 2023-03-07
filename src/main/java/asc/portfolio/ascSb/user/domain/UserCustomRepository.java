package asc.portfolio.ascSb.user.domain;

import asc.portfolio.ascSb.user.dto.UserQrAndNameResponseDto;

public interface UserCustomRepository {

    UserQrAndNameResponseDto findQrAndUserNameById(Long id);
}
