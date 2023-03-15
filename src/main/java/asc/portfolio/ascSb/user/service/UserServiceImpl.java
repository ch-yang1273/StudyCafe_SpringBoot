package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.dto.UserProfileDto;
import asc.portfolio.ascSb.user.dto.UserQrAndNameResponseDto;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserQrAndNameResponseDto userQrAndName(Long id) {
        User findUser = userRepository.findById(id).orElseThrow(() -> new UnknownUserException());
        return new UserQrAndNameResponseDto(findUser.getName(), findUser.getQrCode());
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDto getUserInfo(String userLoginId) {
        User findUser = userRepository.findByLoginId(userLoginId).orElseThrow(() -> new UnknownUserException());
        return new UserProfileDto(findUser);
    }
}
