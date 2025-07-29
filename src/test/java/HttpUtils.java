import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/3
 */
public class HttpUtils {

    public static void doPost(String className, String methodName, String param, String token) {
        try {
            String url = "http://10.30.80.83:8923/jcmadm/RMIService";
            //String url = "http://10.30.80.83:8924/jcmsche/testJob";
            //url = "http://10.30.80.83:8924/jcmsche/calendar/getCalendarTree";
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);

            // 发送表单
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("className", className));
            params.add(new BasicNameValuePair("methodName", methodName));
            params.add(new BasicNameValuePair("params", param));
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            // token
            post.setHeader("token", token);
            // token
            post.setHeader("tenant-code", "987");

            // 执行请求
            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());

            System.out.println("状态码: " + statusCode);
            System.out.println("响应体: ");
            System.out.println(responseBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
