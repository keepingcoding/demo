package com.example.demo.mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 发送邮件
 */
public class TestMail {
    public static void main(String[] args) {
        snedMail();
    }


    private static void snedMail() {
        try {
            //1、连接邮件服务器的参数配置
            Properties props = new Properties();
            //设置用户的认证方式
            props.setProperty("mail.smtp.auth", "true");
            //设置传输协议
            props.setProperty("mail.transport.protocol", "smtp");
            //设置发件人的SMTP服务器地址
            props.setProperty("mail.smtp.host", "smtp.163.com");
            //2、创建定义整个应用程序所需的环境信息的 Session 对象
            Session session = Session.getInstance(props);
            //设置调试信息在控制台打印出来
            session.setDebug(true);

            //3、创建邮件的实例对象
            Message msg = getMimeMessageWithImgAndAttach(session);

            //4、根据session对象获取邮件传输对象Transport
            Transport transport = session.getTransport();
            //设置发件人的账户名和密码
            transport.connect("xxx@163.com", "xxx");
            //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(msg, msg.getAllRecipients());

            //如果只想发送给指定的人，可以如下写法
            //transport.sendMessage(msg, new Address[]{new InternetAddress("xxx@qq.com")});

            //5、关闭邮件连接
            transport.close();

            System.out.println("Sent message successfully....");

        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    /**
     * 一、发送简单的文本邮件
     */
    private static Message getMimeMessage(Session session) throws MessagingException {
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress("xxx@163.com"));
        /*
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("xxx@sina.com"));
        //设置邮件主题
        msg.setSubject("邮件主题", "UTF-8");
        //设置邮件正文
        msg.setContent("简单的纯文本邮件！", "text/html;charset=UTF-8");
        //msg.setText("This is actual message");
        //设置邮件的发送时间,默认立即发送
        //msg.setSentDate(new Date());

        return msg;
    }

    /**
     * 二、发送带图片的邮件
     */
    private static Message getMimeMessageWithImg(Session session) throws MessagingException, IOException {

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("xxx@163.com"));
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("xxx@sina.com"));
        msg.setSubject("邮件主题", "UTF-8");
        msg.setContent("简单的纯文本邮件！", "text/html;charset=UTF-8");
        //msg.setText("This is actual message");
        //设置邮件的发送时间,默认立即发送
        //msg.setSentDate(new Date());

        // 准备邮件数据
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("这是一封邮件正文带图片<img src='cid:xxx.jpg'>的邮件", "text/html;charset=UTF-8");
        // 准备图片数据
        MimeBodyPart image = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("E:\\tmp\\img\\111.jpg"));
        image.setDataHandler(dh);
        image.setContentID("xxx.jpg");
        // 描述数据关系
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.addBodyPart(image);
        mm.setSubType("related");

        msg.setContent(mm);
        //msg.saveChanges();
        //将创建好的邮件写入到E盘以文件的形式进行保存
        //msg.writeTo(new FileOutputStream("E:\\ImageMail.eml"));

        return msg;
    }

    /**
     * 三、发送带附件的邮件
     */
    private static Message getMimeMessageWithAttach(Session session) throws MessagingException, IOException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("xxx@163.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("xxx@sina.com"));
        message.setSubject("JavaMail邮件发送测试");

        //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("使用JavaMail创建的带附件的邮件", "text/html;charset=UTF-8");

        //创建邮件附件
        MimeBodyPart attach = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("E:\\tmp\\img\\111.jpg"));
        attach.setDataHandler(dh);
        attach.setFileName(MimeUtility.encodeText(dh.getName()));//文件名编码

        //创建容器描述数据关系
        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(text);
        mp.addBodyPart(attach);
        mp.setSubType("mixed");

        message.setContent(mp);

        return message;
    }

    /**
     * 四、发送带图片以及附件的邮件
     */
    private static Message getMimeMessageWithImgAndAttach(Session session) throws MessagingException, IOException {
        //创建邮件
        MimeMessage message = new MimeMessage(session);

        //设置邮件的基本信息
        message.setFrom(new InternetAddress("xxx@163.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("xxx@sina.com"));
        message.setSubject("带附件和带图片的的邮件");

        //正文
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("xxx这是图片<br/><img src='cid:aaa.jpg'>", "text/html;charset=UTF-8");

        //图片
        MimeBodyPart image = new MimeBodyPart();
        image.setDataHandler(new DataHandler(new FileDataSource("E:\\tmp\\img\\111.jpg")));
        image.setContentID("aaa.jpg");

        //描述关系
        MimeMultipart mp1 = new MimeMultipart();
        mp1.addBodyPart(text);
        mp1.addBodyPart(image);
        mp1.setSubType("related");

        //代表正文的bodypart
        MimeBodyPart content = new MimeBodyPart();
        content.setContent(mp1);

        //附件1
        MimeBodyPart attach = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("E:\\tmp\\img\\111.jpg"));
        attach.setDataHandler(dh);
        attach.setFileName(dh.getName());

        //附件2
        MimeBodyPart attach2 = new MimeBodyPart();
        DataHandler dh2 = new DataHandler(new FileDataSource("E:\\tmp\\img\\111.jpg"));
        attach2.setDataHandler(dh2);
        attach2.setFileName(MimeUtility.encodeText(dh2.getName()));

        //描述关系:正文和附件
        MimeMultipart mp2 = new MimeMultipart();
        mp2.addBodyPart(attach);
        mp2.addBodyPart(attach2);
        mp2.addBodyPart(content);
        mp2.setSubType("mixed");

        message.setContent(mp2);

        return message;
    }
}
