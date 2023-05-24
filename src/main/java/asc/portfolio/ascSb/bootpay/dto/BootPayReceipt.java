package asc.portfolio.ascSb.bootpay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true) // 변환하지 못하는 값은 무시
public class BootPayReceipt {
    private String receipt_id;
    private String order_id;
    private int price;
    private int tax_free;
    private int cancelled_price;
    private int cancelled_tax_free;
    private String order_name;
    private String company_name;
    private String gateway_url;
    private Map<String, Object> metadata;
    private boolean sandbox;
    private String pg;
    private String method;
    private String method_symbol;
    private String method_origin;
    private String method_origin_symbol;
    private String currency;
    private String receipt_url;
    private LocalDateTime purchased_at;
    private LocalDateTime cancelled_at;
    private LocalDateTime requested_at;
    private int status;

    public static BootPayReceipt of(HashMap<String, Object> receipt) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(receipt, BootPayReceipt.class);
    }
}
