package utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import org.apache.http.entity.StringEntity;



public class Httputil {


    public static  void httpPost(String url, String json, RequestCallBack callBacks){

        HttpUtils http=new HttpUtils();
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(json.toString(), "UTF-8"));
            params.setContentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        http.send(HttpRequest.HttpMethod.POST, url, params, callBacks );
    }

   public  static  void httpGet(String url,RequestCallBack callbacks){
       HttpUtils http=new HttpUtils();
       http.send(HttpRequest.HttpMethod.GET,url,callbacks);
   }


}
