package com.mmall.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 全局自定义变量
 *
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/10/30 9:32
 */
public class Const {
    public final static String CURRENT_USER = "CURRENT_USER"; // 当前用户
    public final static String USER_NAME = "username"; // 用户名属性
    public final static String EMAIL = "email"; // 邮件名属性


    // redis过期时间
    public interface RedisCacheExtime {
        int REDIS_SESSION_EXTIME = 60 * 30; // 30分钟
    }

    public interface Cart {
        int CHECKED = 1;//即购物车选中状态
        int UN_CHECKED = 0;//购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface Roles {
        int ROLE_CUSTOMER = 0; // 普通用户
        int ROLE_ADMIN = 1; // 管理员
    }

    public interface ProductListOrderBy {
        Set<String> set = new HashSet<>(Arrays.asList(new String[]{"price_asc", "price_desc"}));

    }

    // 商品状态
    public enum ProductStatusEnum {
        ON_SALE(1, "在售");
        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }


    // 订单状态枚举
    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");


        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }
    }

    // 支付方式
    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付");

        private int code;
        private String value;

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static PaymentTypeEnum codeOf(int code) {

            for (PaymentTypeEnum value : values()) {
                if (value.getCode() == code) {
                    return value;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }
    }

    // 支付宝回调的状态
    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY"; // 交易创建，等待买家付款
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS"; // 交易支付成功

        String RESPONSE_SUCCESS = "success"; // 当商户收到服务器异步通知并打印出success时，服务器异步通知参数notify_id才会失效
        String RESPONSE_FAILED = "failed";
    }

    // 支付类型
    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

}
