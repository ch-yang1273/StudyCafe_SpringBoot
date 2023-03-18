package asc.portfolio.ascSb.bootpay.dto.model.response.data;

import kr.co.bootpay.model.response.data.BillingKeyCardData;

public class BillingKeyData {
    public String billing_key;
    public String pg_name;

    public String method_name;
    public String method;

    public String e_at;
    public String c_at;

    public BillingKeyCardData data;
}