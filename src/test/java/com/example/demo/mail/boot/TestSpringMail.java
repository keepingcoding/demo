package com.example.demo.mail.boot;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author zzs
 * @date 2019/6/1 15:39
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSpringMail {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 一、发送简单文本邮件
     */
    @Test
    public void testSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("xxx@163.com");
        message.setTo("xxx@sina.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");

        this.mailSender.send(message);
    }

    /**
     * 二、发送html格式的邮件
     */
    @Test
    public void testHtmlMail() throws MessagingException {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封Html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        MimeMessage message = mailSender.createMimeMessage();

        //true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("xxx@163.com");
        helper.setTo("xxx@sina.com");
        helper.setSubject("html mail");
        helper.setText(content, true);

        mailSender.send(message);

    }

    /**
     * 三、发送附件邮件
     */
    @Test
    public void testAttachmentMail() throws MessagingException {
        String filePath = "E:\\tmp\\img\\111.jpg";
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("xxx@163.com");
        helper.setTo("xxx@sina.com");
        helper.setSubject("附件邮件");
        helper.setText("这是一封带附件的邮件", true);

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        helper.addAttachment(fileName, file);

        mailSender.send(message);
        log.info("带附件的邮件已经发送。");

    }

    /**
     * 四、发送带图片的邮件
     */
    @Test
    public void testImgMail() throws MessagingException {
        String Id = "img123";
        String content = "这是有图片的邮件：<img src=\'cid:" + Id + "\' >";
        String imgPath = "E:\\tmp\\img\\111.jpg";
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("xxx@163.com");
        helper.setTo("xxx@sina.com");
        helper.setSubject("这是有图片的邮件");
        helper.setText(content, true);

        FileSystemResource res = new FileSystemResource(new File(imgPath));
        helper.addInline(Id, res);

        mailSender.send(message);
        log.info("嵌入静态资源的邮件已经发送。");
    }

    /**
     * 五、发送带图片以及附件的复杂邮件
     */
    @Test
    public void testImgAndAttachmentMail() throws MessagingException {
        String Id = "img123";
        String content = "这是有图片的邮件：<img src=\'cid:" + Id + "\' >";
        String imgPath = "E:\\tmp\\img\\111.jpg";
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("xxx@163.com");
        helper.setTo("xxx@sina.com");
        helper.setSubject("这是有图片以及附件的邮件");
        helper.setText(content, true);

        FileSystemResource res = new FileSystemResource(new File(imgPath));
        helper.addInline(Id, res);

        String fileName = imgPath.substring(imgPath.lastIndexOf(File.separator));
        helper.addAttachment(fileName, res);
        helper.addAttachment(fileName, res);
        helper.addAttachment(fileName, res);

        mailSender.send(message);
        log.info("嵌入静态资源并携带附件的邮件已经发送。");
    }
}
