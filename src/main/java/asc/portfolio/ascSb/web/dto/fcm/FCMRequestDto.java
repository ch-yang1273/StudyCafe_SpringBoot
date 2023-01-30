package asc.portfolio.ascSb.web.dto.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FCMRequestDto {
    private String user_name;
    private String title;
    private String body;
}
