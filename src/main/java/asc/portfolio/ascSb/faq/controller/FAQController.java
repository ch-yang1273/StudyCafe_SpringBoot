package asc.portfolio.ascSb.faq.controller;


import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.common.auth.jwt.LoginUser;
import asc.portfolio.ascSb.faq.service.FAQService;
import asc.portfolio.ascSb.faq.dto.FAQRequestDto;
import asc.portfolio.ascSb.faq.dto.FAQResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/faq")
public class FAQController {

    private final FAQService fAQService;
    @GetMapping("/list")
    public ResponseEntity<?> showFAQList(@LoginUser User user) {

        if (user == null) {
            return new ResponseEntity<>("잘못된 접근입니다.", HttpStatus.BAD_REQUEST);
        }

        List<FAQResponseDto> fAQList = fAQService.cafeFAQList(user);

        if (fAQList == null) {
            return new ResponseEntity<>("FAQ가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(fAQList, HttpStatus.OK);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> putFAQ(@LoginUser User user, @RequestBody FAQRequestDto fAQRequestDto) {
        if (user.getRole() != UserRoleType.ADMIN) {
            return new ResponseEntity<>("어드민 계정이 아닙니다.", HttpStatus.BAD_REQUEST);
        } else if (fAQRequestDto == null) {
            return new ResponseEntity<>("Invalid body request.", HttpStatus.BAD_REQUEST);
        }

        Long fAQId = fAQService.saveFAQ(user, fAQRequestDto);
        if (fAQId != null) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("save failed", HttpStatus.BAD_REQUEST);
    }
}
