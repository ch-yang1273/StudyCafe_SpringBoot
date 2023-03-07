package asc.portfolio.ascSb.order.domain;

public enum OrderStateType {
    ORDER, // 결제중
    DONE, // 결제완료
    CANCEL, // 결제취소
    ERROR,// 결제오류
    ISSUE
}
