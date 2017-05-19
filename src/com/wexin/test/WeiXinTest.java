package com.wexin.test;

import com.wexin.po.AcessToken;
import com.wexin.until.WeixinUtil;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by yujin on 2017/5/18.
 */
public class WeiXinTest {
    public static  void  main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        AcessToken acessToken= WeixinUtil.getAcessToken();

//        System.out.print("凭证："+acessToken.getAccess_token());
//        System.out.print("有效时间"+acessToken.getExpires_in());
//        String path="d:/1.jpg";
//        String MediaId=WeixinUtil.upload(path,acessToken.getAccess_token(),"thumb");
//        System.out.print(MediaId);
        String menu=JSONObject.fromObject(WeixinUtil.initMenu()).toString();
        int reslut =WeixinUtil.createMenu(acessToken.getAccess_token(),menu);
        if(reslut==0){
            System.out.print("菜单创建成功");
        }else{
            System.out.print("菜单创建失败"+reslut);
        }





    }
}
