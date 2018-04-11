package demo;



import com.yzone.translate.TransApi;

import java.io.UnsupportedEncodingException;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20180119000117102";
    private static final String SECURITY_KEY = "PrUuzyAWuXxi8Cpm4ubM";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "fuck";
        try {
			System.out.println(api.getTransResult(query, "auto", "zh"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
