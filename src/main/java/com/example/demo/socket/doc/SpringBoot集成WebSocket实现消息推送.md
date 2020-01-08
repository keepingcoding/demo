> 简介

​	目前对于服务端向客户端推送数据，常用技术方案有轮询、websocket等。

轮询的方式比较简单，而且获取的数据不是完全实时的，这里就不介绍了。

websocket特点：

- WebSocket 是一种双向通信协议，WebSocket 服务器和 Browser/Client Agent 都能主动的向对方发送或接收数据；
- WebSocket 需要类似 TCP 的客户端和服务器端通过握手连接，连接成功后才能相互通信。



对于一些概念性的东西，这里就不多讲了，网上资料一大堆。简单粗暴直接上代码，springboot集成了websocket，使用起来非常方便。



### 一、引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

我这里用的是`2.1.6.RELEASE`版本

### 二、编写websocket配置类

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author zzs
 * @date 2020/1/2 15:31
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        /**
         * 注册 Stomp的端点
         * addEndpoint：添加STOMP协议的端点。这个HTTP URL是供WebSocket或SockJS客户端访问的地址
         * withSockJS：指定端点使用SockJS协议
         */
        stompEndpointRegistry.addEndpoint("/websocket-simple")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /**
         * 配置消息代理
         * 启动简单Broker，消息的发送的地址符合配置的前缀来的消息才发送到这个broker
         */
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}
```

`@EnableWebSocketMessageBroker`：此注解表示使用STOMP协议来传输基于消息代理的消息，此时可以在@Controller类中使用@MessageMapping

很简单，里面配置的路径如果看不懂注释没有关系，待会在前端代码中对应上就明白了。



### 三、编写前后端程序

后端Controller层：

```java
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

    public void send() {
        // 后台主动发送消息
        this.simpMessagingTemplate.convertAndSend("/topic/getResponse", "时间：" + new Date());
    }

}
```

`SimpMessagingTemplate`：看到Template后缀就知道，这是spring为我们封装好的模板工具了，当需要后台主动向前端发送消息时，将其注入进来使用即可。

`@MessageMapping`注解和我们之前使用的@RequestMapping类似。`@SendTo`注解表示当服务器有消息需要推送的时候， 会对订阅了@SendTo中路径的浏览器发送消息。



l浏览器客户端代码：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript" src="/js/public/jquery-3.4.1.min.js"></script>
    <!-- 这里需要引入这两个websocket相关的js -->
    <script type="text/javascript" src="/js/public/stomp-websocket/stomp.min.js"></script>
    <script type="text/javascript" src="/js/public/sockjs-client/sockjs.min.js"></script>
</head>

<body onload="connect();">
<div>
    <div id="conversationDiv">
        <label>Please input : </label><input type="text" id="name"/>

        <button id="sendName" onclick="sendName();">发送</button>
        <p id="response"></p>
    </div>
</div>

<script type="text/javascript">
    window.onbeforeunload = function () {
        disconnect();
    }

    var stompClient = null;

    function connect() {
        // websocket的连接地址，即WebSocketMessageBrokerConfigurer中registry.addEndpoint("/websocket-simple")配置的地址
        var socket = new SockJS('/websocket-simple');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            // 客户端订阅消息的地址：即服务端使用@SendTo("/topic/getResponse")注解的里配置的值
            stompClient.subscribe('/topic/getResponse', function (response) {
                showResponse(response.body);
            });
        });
    }

    function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }

    function sendName() {
        var name = $('#name').val();
        // 客户端消息发送的地址：即服务端使用@MessageMapping("/receive")注解的里配置的值
        // 前缀/app是WebSocketMessageBrokerConfigurer中config.setApplicationDestinationPrefixes("/app")配置的值
        stompClient.send("/app/receive", {}, JSON.stringify({'name': name}));
    }

    function showResponse(message) {
        var response = $("#response");
        response.html(message + "<br>" + response.html());
    }
</script>
</body>
</html>
```

**Stomp** websocket使用socket实现双工异步通信能力。但是如果直接使用websocket协议开发程序比较繁琐，我们可以使用它的子协议Stomp

**SockJS** sockjs是websocket协议的实现，增加了对浏览器不支持websocket的时候的兼容支持。 SockJS的支持的传输的协议有3类: WebSocket, HTTP Streaming, and HTTP Long Polling。默认使用websocket，如果浏览器不支持websocket，则使用后两种的方式。 SockJS使用"Get /info"从服务端获取基本信息。然后客户端会决定使用哪种传输方式。如果浏览器使用websocket，则使用websocket。如果不能，则使用Http Streaming，如果还不行，则最后使用 HTTP Long Polling



客户端的逻辑很简单，就是在页面加载完成的时候，建立websocket连接，连接的地址即是websocket配置类中的地址，然后订阅指定的地址（上边都有注释），订阅到的消息会显示在页面上；同时页面上又有一个可以主动发送消息到后端的按钮，最后页面销毁时关闭连接。



至此，程序都已编写完成

### 四、测试

![](/img/01.png)

![](/img/02.png)

可以看到，chrome中发送的消息，后台收到了，而且还推送给了都订阅该地址的客户端，即两个浏览器都收到了推送。



至此，SpringBoot集成Websocket的简单实用已经完成，更深入的研究有待完善。