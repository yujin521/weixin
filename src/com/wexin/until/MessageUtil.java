package com.wexin.until;

import com.thoughtworks.xstream.XStream;
import com.wexin.po.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**XMLtoMAP
 * Created by yujin on 2017/5/16.
 */
public class MessageUtil {
    //文本消息的三种类型 text image voice
    public static  final String MESSAGE_TEXT="text";
    public static  final String MESSAGE_NEWS="news";
    public static  final String MESSAGE_IMAGE="image";
    public static  final String MESSAGE_VOICE="voice";
    public static  final  String MESSAGE_MUSIC="music";
    public static  final String MESSAGE_LINK="link";
    public static  final String MESSAGE_LOCATION="location";
    public static  final String MESSAGE_EVENT="event";
    public static  final String MESSAGE_SUBSCRIBE="subscribe";
    public static  final String MESSAGE_CLICK="CLICK";
    public static  final String MESSAGE_VIEW="VIEW";
    public static  final String MESSAGE_SCANCODE="scancode_push";




    /**
     *
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String,String > xmlToMap(HttpServletRequest request) throws IOException,DocumentException {
        Map<String,String> map=new HashMap<String,String>();
        SAXReader reader=new SAXReader();

        InputStream ins= request.getInputStream();
        Document doc=reader.read(ins);

        Element root=doc.getRootElement();
        List<Element> list =root.elements();

        for(Element e :list){
            map.put(e.getName(),e.getText());
        }
        ins.close();

        return map;



    }

    /**
     * 将文本对象转成XML
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xStream =new XStream();
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);

    }
    public static String initText(String content,String ToUserName,String FromUserName){
        TextMessage text= new TextMessage();
        text.setFromUserName(ToUserName);
        text.setToUserName(FromUserName);
        text.setMsgType(MessageUtil.MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent(content);
        return MessageUtil.textMessageToXml(text);
    }

    /**
     * 将图文消息转成文本对象
     * @param
     * @return
     */
    public  static String newsMessageToXml(NewsMessage newsMessage){
        XStream xStream =new XStream();
        xStream.alias("xml",newsMessage.getClass());
        xStream.alias("item",new News().getClass());
        return xStream.toXML(newsMessage);
    }

    /**
     *
     * @param ToUserName
     * @param FromUserName
     * @return
     */
    public  static  String initNews(String ToUserName,String FromUserName){
        String message=null;
        List<News> newses=new ArrayList<News>();


        NewsMessage newsMessage=new NewsMessage();
        News news=new News();
        news.setTitle("健的图片");
        news.setDescription("健是儿子，对就是这样。\n我是他爸爸");
        news.setPicUrl("http://group.store.qq.com/qun/PR7ssbejHSmTVLJQKQWoCA!!/V3tiObgDHflaVeU9wAD/800?w5=398&h5=349&rf=viewer_421");
        news.setUrl("http://group.store.qq.com/qun/PR7ssbejHSmTVLJQKQWoCA!!/V3tiObgDHflaVeU9wAD/800?w5=398&h5=349&rf=viewer_421");
        newses.add(news);
//        News news1=new News();
//        news1.setTitle("杨建的图片");
//        news1.setDescription("杨建是我的儿子，对就是这样。\n我是他爸爸");
//        news1.setPicUrl("http://group.store.qq.com/qun/PR7ssbejHSmTVLJQKQWoCA!!/V3tiObgDHflaVeU9wAD/800?w5=398&h5=349&rf=viewer_421");
//        news1.setUrl("http://group.store.qq.com/qun/PR7ssbejHSmTVLJQKQWoCA!!/V3tiObgDHflaVeU9wAD/800?w5=398&h5=349&rf=viewer_421");
//        newses.add(news1);

        newsMessage.setFromUserName(ToUserName);
        newsMessage.setToUserName(FromUserName);
        newsMessage.setArticles(newses);
        newsMessage.setArticleCount(newses.size());
        newsMessage.setMsgType(MESSAGE_NEWS);
        message=newsMessageToXml(newsMessage);
        return message;

    }

    /**
     * 图文消息自动回复
     * Image--XML
     * @param imageMessage
     * @return
     */
    public static String imageToXml(ImageMessage imageMessage){
        XStream xStream =new XStream();
        xStream.alias("xml",imageMessage.getClass());
        xStream.alias("MediaId",new Image().getClass());
        return xStream.toXML(imageMessage);

    }
    public static  String initImage(String ToUserName,String FromUserName){
        Image image =new Image();
        ImageMessage imageMessage =new ImageMessage();
        image.setMediaId("IH20wiGWhRGO3MypQFrMpGwsGG7hc_1sNlyxw8hcfO6YADizr9uOivrLdQwmHGtX");
        imageMessage.setFromUserName(ToUserName);
        imageMessage.setToUserName(FromUserName);
        imageMessage.setMsgType(MESSAGE_IMAGE);
        imageMessage.setCreateTime(new Date().getTime());
        imageMessage.setImage(image);
        String message=imageToXml(imageMessage);
        return message;

    }
    /**
     * 音乐消息自动回复
     * @param musicMessage
     * @return
     */
    public static String musicToXml(MusicMessage musicMessage){
        XStream xStream =new XStream();
        xStream.alias("xml",musicMessage.getClass());
        xStream.alias("Music",new Image().getClass());
        return xStream.toXML(musicMessage);

    }
    public static  String initMusic(String ToUserName,String FromUserName){

        Music music =new Music();
        MusicMessage musicMessage=new MusicMessage();
        music.setThumbMediaId("KDXMfDCffEsyUpX1oY_OkDHIkUiMnuLQMO_Mv5dBrtTm6eSr4L2VhTi7yDFxyrnM");
        music.setTitle("暧昧");
        music.setDescription("演唱：薛之谦");
        music.setMusicUrl("http://237239970yu.ngrok.ccwexin/web/WEB-INF/resource/薛之谦 - 暧昧.mp3");
        music.setHQMusicUrl("http://237239970yu.ngrok.ccwexin/web/WEB-INF/resource/薛之谦 - 暧昧.mp3");
        musicMessage.setMusic(music);
        musicMessage.setFromUserName(ToUserName);
        musicMessage.setToUserName(FromUserName);
        musicMessage.setMsgType(MESSAGE_MUSIC);
        musicMessage.setCreateTime(new Date().getTime());
        String message=musicToXml(musicMessage);
        return message;

    }


    /*
    主菜单
     */
    public static String menuText(){
        StringBuffer sb=new StringBuffer();
        sb.append("欢迎您的关注，请按菜单提示进行操作：\n\n");
        sb.append("1.杨建\n");
        sb.append("2.李世间\n");
        sb.append("3.图文消息\n");
        sb.append("输入?调出此菜单");
        return sb.toString();
    }
    public static String FirstMenu(){
        StringBuffer sb=new StringBuffer();
        sb.append("1.杨建是大儿子");
        return sb.toString();
    }
    public  static  String secondMenu(){
        StringBuffer sb =new StringBuffer();
        sb.append("2.李世健是二儿子");
        return  sb.toString();
    }
    public  static  String returnmenuText(){
        StringBuffer sb =new StringBuffer();
        sb.append("您的操作指令不识别，请按菜单提示进行操作：\n\n");
        sb.append("1.杨建\n");
        sb.append("2.李世间\n");
        sb.append("3.图文消息\n");
        sb.append("输入?调出此菜单");
        return  sb.toString();
    }


}
