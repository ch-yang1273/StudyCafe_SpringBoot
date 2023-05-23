package asc.portfolio.ascSb.bootpay.infra;

import asc.portfolio.ascSb.bootpay.dto.BootPayReceipt;
import asc.portfolio.ascSb.bootpay.exception.BootPayErrorData;
import asc.portfolio.ascSb.bootpay.exception.BootPayException;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class BootPayApiService {

    private final Bootpay api;

    public BootPayApiService(
            @Value("bootpay.api-application-id") String rest_application_id,
            @Value("bootpay.private-key") String private_key) {
        api = new Bootpay(
                rest_application_id,
                private_key
        );
    }

    public Bootpay getBootPayApi() {
        return api;
    }

    private void refreshAccessToken() {
        HashMap<String, Object> res;
        try {
            res = api.getAccessToken();
        } catch (Exception e) {
            throw new BootPayException(BootPayErrorData.TOKEN_REFRESH_ERROR);
        }

        if(res.get("error_code") == null) { //success
            log.info("goGetToken success: {}", res);
        } else {
            log.error("goGetToken false: {}", res);
            throw new BootPayException(BootPayErrorData.TOKEN_REFRESH_ERROR);
        }
    }

    public BootPayReceipt getReceipt(String receiptId) {
        // 매번 AccessToken을 갱신해야 한다.
        refreshAccessToken();

        BootPayReceipt dto;
        try {
            HashMap<String, Object> receipt = api.getReceipt(receiptId);
            dto = BootPayReceipt.of(receipt);
        } catch (Exception e) {
            throw new BootPayException(BootPayErrorData.RECEIPT_LOOKUP_ERROR);
        }

        return dto;
    }

    /**
     * 교차 검증 대상 : status, price
     * 결제 완료에 대한 교차검증을 하려면 결제 단건 조회를 통해 status 값이 1인지 대조합니다.
     * 실제 요청했던 결제금액과 결제 단건 조회를 통해 리턴된 price 값이 일치하는지 대조합니다.
     */
    public boolean crossValidation(BootPayReceipt dto, int status, int price) {
        return (dto.getStatus() == status && dto.getPrice() == price);
    }

    public BootPayReceipt confirm(String receiptId) {
        try {
            HashMap<String, Object> resp = api.confirm(receiptId);
            return BootPayReceipt.of(resp);
        } catch (Exception e) {
            throw new BootPayException(BootPayErrorData.RECEIPT_CONFIRM_ERROR);
        }
    }

    public BootPayReceipt cancelReceipt(String receiptId) {
        Cancel cancel = new Cancel();
        cancel.receiptId = receiptId;
        cancel.cancelUsername = "관리자";
        cancel.cancelMessage = "서버 검증 오류";

        try {
            return BootPayReceipt.of(api.receiptCancel(cancel));
        } catch (Exception e) {
            throw new BootPayException(BootPayErrorData.RECEIPT_CANCEL_ERROR);
        }
    }
}
