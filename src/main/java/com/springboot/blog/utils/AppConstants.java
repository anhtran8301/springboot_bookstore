package com.springboot.blog.utils;

public class AppConstants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$";


//    public static final String LANGUAGE_VI = "vi";
//    public static final String LANGUAGE_EN = "en";

    public static final String REGEX_PASSWORD_MESSAGE = "Password must be 8-16 characters with no space, " +
            "including at least 1 uppercase letter, " +
            "1 lowercase letter, " +
            "1 number (0-9), " +
            "1 non-alpha numeric number";

    // ORDER TYPE
    public static final String WAIT_FOR_PAYMENT = "Chờ thanh toán";
    public static final String IS_PAYMENT = "Đã thanh toán";
    public static final String PROCESSING = "Đang xử lý";
    public static final String SHIPPING = "Đang giao hàng";
    public static final String DELIVERED = "Giao hàng thành công";
    public static final String DELIVERY_FAILED = "Giao hàng thất bại";
    public static final String CANCELED = "Đã hủy";
    public static final String RETURN = "Đổi/trả";
    public static final String REFUND = "Hoàn tiền";
    public static final String CANCELED_REQUEST = "Yêu cầu hủy";

    // PAYMENT TYPE
    public static String MOMO_PAYMENT = "captureWallet";
    public static String MOMO_PAYMENT_CC = "payWithCC";
    public static String MOMO_PAYMENT_ATM = "payWithATM";
    public static String COD_PAYMENT = "cash";

    // SALE MASSAGE
    public static String FLASH_SALE_NOT_EXIST = "Flash sale không tồn tại";
    public static String FLASH_SALE_SAME_TIME = "Flash sale đã tồn tại trong khoảng thời gian này";
    public static String FLASH_SALE_BOOK_NOT_ENOUGH = "Số lượng sách trong đợt giảm giá đã hết";
    public static String FLASH_SALE_INVALID_END_TIME = "Thời gian kết thúc Flash sale không được nhỏ hơn thời gian hiện tại";
    public static String FLASH_SALE_NOT_TODAY = "Không có Flash sale nào trong ngày";

    //PRODUCT
    public static String BOOK_NOT_ENOUGH = "Số lượng sách không đủ";
    public static String BOOK_SLUG_EXIST = "Slug sách đã tồn tại";

    public static int BOOK_LOW_STOCK_QUANTITY = 15;
    public static int BOOK_OUT_OF_STOCK_QUANTITY = 0;
    public static int BOOK_IN_STOCK_QUANTITY = 15;
    public static int BOOK_STOP_SALE_QUANTITY = 0;
}
