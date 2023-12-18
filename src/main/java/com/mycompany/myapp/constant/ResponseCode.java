package com.mycompany.myapp.constant;

public class ResponseCode {

    public class MSG {

        public static final String TRANSACTION_SUCCESSFUL = "Thành công";
        public static final String INVALID_JSON_FORMAT_DATA = "Format của request body chưa đúng chuẩn JSON";
        public static final String EXISTED = "Đã tồn tại";
        public static final String NOT_EXISTED = "Không tồn tại";
        public static final String UNABLE_TO_CHANGE_STATUS = "Không đủ điều kiện để thay đổi trạng thái";
        public static final String ERROR_IN_BACKEND = "Hệ thống xử lý lỗi, vui lòng thử lại";
        public static final String INVALID_INPUT_DATA = "Dữ liệu truyền vào không hợp lệ";
        public static final String LACK_OF_INPUT_DATA = "Thiếu dữ liệu truyền vào";
        public static final String UNABLE_TO_DELETE = "Không đủ điều kiện để xóa";
        public static final String SOA_TIMEOUT_BACKEND_MSG = "SOA timeout backend";
        public static final String ERROR_WHEN_CALL_TO_BACKEND_MSG = "Không gọi được đến backend";
        public static final String UNABLE_TO_UPDATE_USER_USB_TOKEN = "Không được đổi UserName hoặc email tài khoản đăng nhập bằng USBToken";
        public static final String NOT_ACTIVE = "Không active hoặc đã xóa";
        public static final String SESSION_TIMEOUT = "Hết hiệu lực";
        public static final String AUTHENTICATE_FAILED = "Xác thực thất bại";
        public static final String EXPIRED = "Hết thời gian hiệu lực";
        public static final String SEND_EMAIL_FAILED = "Gửi email thất bại";
        public static final String TOKEN_EXPIRED = "Token hết hạn";
        public static final String TOKEN_INVALID = "Token không hợp lệ";
        public static final String UN_EXPIRED = "Chưa hết thời gian hiệu lực";
        public static final String UNABLE_TO_LOGIN_BY_USB_TOKEN = "Không đủ điều kiện để đăng nhập bằng UsbToken";
        public static final String ERROR_EMPTY_DATA = "Không có dữ liệu";
        public static final String UNABLE_TO_RUN_JOB = "Không đủ điều kiện để chạy tiến trình";
    }

    public class CODE {

        public static final int TRANSACTION_SUCCESSFUL = 1;
        public static final int INVALID_JSON_FORMAT_DATA = 2;
        public static final int EXISTED = 3;
        public static final int NOT_EXISTED = 4;
        public static final int UNABLE_TO_CHANGE_STATUS = 5;
        public static final int ERROR_IN_BACKEND = 6;
        public static final int INVALID_INPUT_DATA = 7;
        public static final int UNABLE_TO_DELETE = 8;
        public static final int SOA_TIMEOUT_BACKEND = 9;
        public static final int ERROR_WHEN_CALL_TO_BACKEND = 10;
        public static final int UNABLE_TO_UPDATE_USER_USB_TOKEN = 11;
        public static final int NOT_ACTIVE = 12;
        public static final int SESSION_TIMEOUT = 13;
        public static final int LACK_OF_INPUT_DATA = 14;
        public static final int AUTHENTICATE_FAILED = 15;
        public static final int EXPIRED = 16;
        public static final int SEND_EMAIL_FAILED = 17;
        public static final int TOKEN_EXPIRED = 18;
        public static final int TOKEN_INVALID = 19;
        public static final int UN_EXPIRED = 20;
        public static final int UNABLE_TO_LOGIN_BY_USB_TOKEN = 21;
        public static final int ERROR_EMPTY_DATA = 22;
        public static final int UNABLE_TO_RUN_JOB = 23;
    }
}
