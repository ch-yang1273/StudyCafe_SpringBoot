package asc.portfolio.ascSb.order.domain;

public enum OrderStatus {
    PROCESSING, // 결제 중
    DONE, // 결제완료
    CANCEL, // 결제취소
    ERROR,// 결제오류
    ISSUE
}
