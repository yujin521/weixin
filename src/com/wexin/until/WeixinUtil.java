package com.wexin.until;

import com.wexin.menu.Button;
import com.wexin.menu.Menu;
import com.wexin.menu.clickButton;
import com.wexin.menu.viewButton;
import com.wexin.po.AcessToken;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**微信工具类
 * Created by yujin on 2017/5/18.
 */
public class WeixinUtil {
    private static final  String AppID="wxffb4cca86aeef9fa";
    private static final  String AppSecret="dccc9a2cdd2451c5cfc811801e9328b2";
    private static final  String ACESSTOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    /**
     * GET
     * @param url
     * @return
     */
    public  static JSONObject doGetStr(String url){
        DefaultHttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(url);
        JSONObject jsonObject=null;
        try {
            HttpResponse httpResponse=httpClient.execute(httpGet);
            HttpEntity httpEntity= httpResponse.getEntity();
            if(httpEntity!=null){
                String result= EntityUtils.toString(httpEntity,"UTF-8");
                jsonObject =JSONObject.fromObject(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
                return jsonObject;
    }

    /**
     * post
     * @param url
     * @param outStr
     * @return
     */
    public  static JSONObject doPostStr(String url,String outStr){
        DefaultHttpClient httpClient=new DefaultHttpClient();
        HttpPost httpPost=new HttpPost(url);
        JSONObject jsonObject=null;
        try {
            httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
            HttpResponse httpResponse=httpClient.execute(httpPost);
            String result= EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            jsonObject =JSONObject.fromObject(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    /**
     * 获取AcessToken
     * @return
     */
    public static AcessToken getAcessToken(){
        AcessToken acessToken =new AcessToken();
        String url =ACESSTOKEN_URL.replace("APPID",AppID).replace("APPSECRET",AppSecret);
        JSONObject jsonObject=doGetStr(url);
        acessToken.setAccess_token(jsonObject.getString("access_token"));
        acessToken.setExpires_in(jsonObject.getInt("expires_in"));
        return  acessToken;
    }
    /**
     * 文件上传
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    /**
     *
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);

        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        //设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");

        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);
        String typeName = "media_id";
        if(!"image".equals(type)){
            typeName = type + "_media_id";
        }
        String mediaId = jsonObj.getString(typeName);
        return mediaId;
    }

    /**
     * 组装菜单
     * @return
     */
    public  static Menu initMenu(){
        Menu menu =new Menu();
        clickButton button11=new clickButton();
        button11.setName("click菜单");
        button11.setType("click");
        button11.setKey("11");

        viewButton button21=new viewButton();
        button21.setName("view菜单");
        button21.setType("view");
        button21.setUrl("http://237239970yu.ngrok.cc");


        clickButton button31=new clickButton();
        button31.setName("地理位置");
        button31.setType("location_select");
        button31.setKey("31");

        clickButton button32=new clickButton();
        button32.setName("扫码事件");
        button32.setType("scancode_push");
        button32.setKey("32");

        Button button =new Button();
        button.setName("菜单");
        button.setSub_button(new Button[]{button31,button32});
        menu.setButton(new Button[]{button11,button21,button});
        return  menu;


    }
    public static int createMenu(String token,String meun){
        int result=0;
        String url=CREATE_MENU_URL.replace("ACCESS_TOKEN",token) ;
        JSONObject jsonObject=doPostStr(url,meun);
        if(jsonObject!=null){
            result=jsonObject.getInt("errcode");
        }
        return result;

    }
}
