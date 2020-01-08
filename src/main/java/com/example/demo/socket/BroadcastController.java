package com.example.demo.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zzs
 * @date 2020/1/2 15:27
 */
@Slf4j
@Controller
public class BroadcastController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 该地址 /receive 接收前端发送过来的消息
     * 该地址 /topic/getResponse 是后端要将消息发送到的websocket客户端地址
     *
     * 参数的接收和传递按照springMVC的格式来就行了
     *
     * @param name
     * @return
     */
    @MessageMapping("/receive")
    @SendTo("/topic/getResponse")
    public String broadcast(@RequestParam String name) {
        log.info("receive a message = {}", name);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return "success: " + sdf.format(now);
    }

    @RequestMapping("/sendToPage")
    @ResponseBody
    public void send() {
        // 后台主动发送消息
        this.simpMessagingTemplate.convertAndSend("/topic/getResponse", "时间：" + new Date());
    }

}
