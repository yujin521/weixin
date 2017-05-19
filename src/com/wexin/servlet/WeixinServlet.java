package com.wexin.servlet;


import com.wexin.until.CheckUtil;
import com.wexin.until.MessageUtil;
import org.dom4j.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by yujin on 2017/5/16.
 */
public class WeixinServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String signature =req.getParameter("signature");
        String timestamp =req.getParameter("timestamp");
        String nonce =req.getParameter("nonce");
        String  echostr  =req.getParameter("echostr");
        PrintWriter out=resp.getWriter();
        if (CheckUtil.checksignature(signature,timestamp,nonce)){
            out.print(echostr);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out=resp.getWriter();
        Map<String,String> map = null;
        try {
            map = MessageUtil.xmlToMap(req);
            String FromUserName =map.get("FromUserName");
            String ToUserName =map.get("ToUserName");
            String CreateTime =map.get("CreateTime");
            String MsgType =map.get("MsgType");
            String content =map.get("Content");
            String message=null;
            System.out.print(map);
            if(MessageUtil.MESSAGE_TEXT.equals(MsgType)){
                if("1".equals(content)){
                   message= MessageUtil.initText(MessageUtil.FirstMenu(),ToUserName,FromUserName);
                }
                else if("2".equals(content)){
                    message= MessageUtil.initText(MessageUtil.secondMenu(),ToUserName,FromUserName);
                }
                else if ("?".equals(content)||"？".equals(content)){
                    message=  MessageUtil.initText(MessageUtil.menuText(),ToUserName,FromUserName);

                }else if("3".equals(content)){
                    message= MessageUtil.initNews(ToUserName,FromUserName);
                }else if("4".equals(content)){
                    message=MessageUtil.initImage(ToUserName,FromUserName);
                }else if ("5".equals(content)){
                    message=MessageUtil.initMusic(ToUserName,FromUserName);
                }
                else{
                    message=  MessageUtil.initText(MessageUtil.returnmenuText(),ToUserName,FromUserName);

                }

            }else if(MessageUtil.MESSAGE_EVENT.equals(MsgType)){
                String eventType=map.get("Event");
                if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                    message=  MessageUtil.initText(MessageUtil.menuText(),ToUserName,FromUserName);

                }else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
                    message=  MessageUtil.initText(MessageUtil.menuText(),ToUserName,FromUserName);
                }else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
                    String url=map.get("eventType");
                    message=  MessageUtil.initText(url,ToUserName,FromUserName);
                }else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
                    String key=map.get("eventType");
                    message=  MessageUtil.initText(key,ToUserName,FromUserName);
                }
            }else if (MessageUtil.MESSAGE_LOCATION.equals(MsgType)){
                    String Label=map.get("Label");
                    message=  MessageUtil.initText(Label,ToUserName,FromUserName);

            }

            out.print(message);
            System.out.print(message);

        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }




    }
}
