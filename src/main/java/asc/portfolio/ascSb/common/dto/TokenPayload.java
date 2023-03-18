package asc.portfolio.ascSb.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenPayload {

    private Long userId;

    public Map<String, Object> toMap() {
        return Map.of("id", userId);
    }
}
