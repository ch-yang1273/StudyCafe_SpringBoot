package asc.portfolio.ascSb.bootpay.infra;

import kr.co.bootpay.Bootpay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BootPayTokenService {

    private final Bootpay bootPayApi;

    public BootPayTokenService(
            @Value("bootpay.api-application-id") String rest_application_id,
            @Value("bootpay.private-key") String private_key) {
        bootPayApi = new Bootpay(
                rest_application_id,
                private_key
        );
    }

    public Bootpay getBootPayApi() {
        return bootPayApi;
    }
}
