package ru.runov.rabbitmq.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ExchangeSenderApp {
    private static final String EXCHANGE_NAME = "directExchanger";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            while (true) {
                System.out.println("please enter  message {TAG + MSG }.  or „exit„ to cancel");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String tagMsg = br.readLine();
                if ("exit".equals(tagMsg))
                    break;


                System.out.println("tag :" + tagMsg.substring(0, 3));
                String message = "[" + tagMsg.substring(3) + "]";
                System.out.println("Mess=" + message);

//            channel.basicPublish(EXCHANGE_NAME, "programming.best-practices.java", null, message.getBytes("UTF-8"));
                channel.basicPublish(EXCHANGE_NAME, tagMsg.substring(0, 3), null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent tag=" + tagMsg.substring(0, 3) + " MSG body . '" + message + "'");
            }
        }
    }
}