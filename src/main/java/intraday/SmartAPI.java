package intraday;

import okhttp3.*;
import org.json.JSONObject;
import sun.security.krb5.internal.HostAddress;

import java.io.IOException;
import java.net.InetAddress;


public class SmartAPI {
    public static void main(String[] args) throws IOException {
        login();
    }
    static void login() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
/*        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientcode","");
        jsonObject.put("clientcode","");*/

      //  RequestBody body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        RequestBody body = RequestBody.create(mediaType,
                "{\n\"clientcode\":\"A404075\",\n\"password\":\"Tanka@123\"\n }"
            );

    Request request = new Request.Builder()
            .url("https://apiconnect.angelbroking.com/rest/auth/angelbroking/user/v1/loginByPassword")

                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("X-UserType", "USER")
                            .addHeader("X-SourceID", "WEB")
                            .addHeader("X-ClientLocalIP", InetAddress.getLocalHost().toString())
                            .addHeader("X-ClientPublicIP", InetAddress.getLocalHost().toString())
                            .addHeader("X-MACAddress", "MAC_ADDRESS")
                            .addHeader("X-PrivateKey", "e11e9798-a2e7-43ea-964f-1e3508282c8e")
                            .build();
    Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
    void getProfile() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://apiconnect.angelbroking.com/rest/secure/angelbroking/user/v1/getProfile")

                .method("GET", null)
                .addHeader("Authorization", "Bearer AUTHORIZATION_TOKEN")
                .addHeader("Accept", "application/json")
                .addHeader("X-UserType", "USER")
                .addHeader("X-SourceID", "WEB")
                .addHeader("X-ClientLocalIP", "CLIENT_LOCAL_IP")
                .addHeader("X-ClientPublicIP", "CLIENT_PUBLIC_IP")
                .addHeader("X-MACAddress", "MAC_ADDRESS")
                .addHeader("X-PrivateKey", "j68rnmC7")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body());
    }
}
