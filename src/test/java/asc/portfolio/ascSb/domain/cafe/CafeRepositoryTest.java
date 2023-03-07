package asc.portfolio.ascSb.domain.cafe;


import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CafeRepositoryTest {

    @Autowired
    CafeRepository cafeRepository;

    @Test
    public void Cafe_카페생성기() {

        Cafe cafe = Cafe.builder()
                .businessHour(24)
                .cafeArea("서울")
                .cafeName("알라딘스터디카페")
                .cafeState("Y")
                .build();

        cafeRepository.save(cafe);
    }

    @Test
    public void findAllCafeDto() {

        List<CafeResponseDto> allCafeDto = cafeRepository.findAllCafeNameAndArea();

//        for (int i = 0; i < allCafeDto.size(); i++) {
//
//            System.out.println("allCafeDto = " + allCafeDto.get(i).getCafeNames() + ", " + allCafeDto.get(i).getCafeArea());
//
//        }
    }
}
