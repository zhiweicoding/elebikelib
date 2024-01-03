package xyz.zhiweicoding.bike.constants;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
public class ConfigureConstant {
    public static final String appID = "wxc76acda6f97a2b46";
    public static final String appSecret = "cd2fab87bba9057babb4995a01216286";
    public static final String PREPARE_APP_ID = "appid";
    public static final String SIGN_APP_ID = "appId";
    public static final String PREPARE_MCH_ID = "mch_id";
    public static final String PREPARE_NONCE_STR = "nonce_str";
    public static final String PREPARE_BODY = "body";
    public static final String PREPARE_OUT_TRADE_NO = "out_trade_no";
    public static final String PREPARE_TOTAL_FEE = "total_fee";
    public static final String PREPARE_SPBILL_CREATE_IP = "spbill_create_ip";
    public static final String PREPARE_NOTIFY_URL = "notify_url";
    public static final String PREPARE_TRADE_TYPE = "trade_type";
    public static final String PREPARE_OPEN_ID = "openid";
    public static final String PREPARE_SIGN_TYPE = "sign_type";
    public static final String PREPARE_SIGN = "sign";
    public static final String TENPAY_RESULT_CODE = "result_code";
    public static final String TENPAY_RETURN_CODE = "return_code";
    public static final String ipStr = "123.206.43.125";

    public ConfigureConstant() {
    }

    public static class TencentInterface {
        public static final String getOpenIdUrl = "https://api.weixin.qq.com/sns/jscode2session";
        public static final String preOrder = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        public TencentInterface() {
        }
    }

    public static class QinNiuYunInterface {
        public static final String ACCESS_KEY = "7il3YawvaF6AqOR6jc88kKhcEs1F3WfgWP82VIPb";
        public static final String SECRET_KEY = "WhtICo_5k0lPp42xSGpJZQmxoubJffjZVyGBwGip";
        public static final String BUCKET = "youchong";
        public static final String BUCKET_URL = "http://photo.youchong.net.cn/";

        public QinNiuYunInterface() {
        }
    }

    public static enum SignTypeEnum {
        MD5;

        private SignTypeEnum() {
        }
    }

    public static enum TradeTypeEnum {
        JSAPI;

        private TradeTypeEnum() {
        }
    }
}
