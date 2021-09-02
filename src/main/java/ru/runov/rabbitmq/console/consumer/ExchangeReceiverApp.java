package ru.runov.rabbitmq.console.consumer;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExchangeReceiverApp {
    private static final String EXCHANGE_NAME = "directExchanger";

    public static void main(String[] argv) throws Exception {
       

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();
        System.out.println("queue name: " + queueName);

        System.out.println("please enter tag to subscribe");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String tag = br.readLine();
        channel.queueBind(queueName, EXCHANGE_NAME, tag);

        System.out.println(" [*] Waiting for messages to TAG " + tag);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(Thread.currentThread().getName());
            System.out.println(" [x] Received '" + message + "'");
        };

        System.out.println(Thread.getAllStackTraces().keySet());
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
//        Thread.sleep(3000);
//        System.out.println(Thread.getAllStackTraces().keySet());

        // channel.queueBind(queueName, EXCHANGE_NAME, "c++");
    }
}
