package com.example.demo.mq.rabbitmq.spring;

/**
 * @author zzs
 * @date 2019/7/10 20:51
 */
public class PushMQListener {

    public void onDirectMessage1(String message) {
        System.out.println("[DirectMessage1] Received -> " + message);
    }

    public void onDirectMessage2(String message) {
        System.out.println("[DirectMessage2] Received -> " + message);
    }

    public void onTopicMessage1(String message) {
        System.out.println("[TopicMessage1] Received -> " + message);
    }

    public void onTopicMessage2(String message) {
        System.out.println("[TopicMessage2] Received -> " + message);
    }

    public void onTopicMessage3(String message) {
        System.out.println("[TopicMessage3] Received -> " + message);
    }

}
