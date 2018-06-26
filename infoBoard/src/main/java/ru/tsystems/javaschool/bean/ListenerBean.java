package ru.tsystems.javaschool.bean;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;


@Startup
@Singleton
public class ListenerBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerBean.class);

    @Inject
    private UpdateDataBean updateDataBean;

    @Inject
    private BeanManager beanManager;

    private final static String QUEUE_NAME = "infoBoardQueue";
    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void updateAfterDeploy(){
        LOGGER.info("From ListenerBean method init: Calling updateDataBean after deploy");
        updateDataBean.observeUpdateActivity("update");
        init();
    }

    public void init() {
        try {
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            LOGGER.info("ListenerBean is waiting for messages");


            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {

                    try {
                        String message = new String(body, "UTF-8");
                        LOGGER.info("ListenerBean got message from TruckingWebApp: {}", message);
                        Thread.sleep(2000);
                        beanManager.fireEvent(message);

                    } catch (Exception e) {
                        LOGGER.error("From ListenerBean method handleDelivery: something went wrong\n",e);
                    }
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            LOGGER.error("From ListenerBean method init: something went wrong\n",e);
        } catch (TimeoutException e) {
            LOGGER.error("From ListenerBean method init: something went wrong\n",e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            LOGGER.info("From ListenerBean Invoking method: preDestroy()");
            connection.close();
            channel.close();
        } catch (IOException e) {
            LOGGER.error("From ListenerBean method preDestroy: something went wrong\n",e);
        } catch (TimeoutException e) {
            LOGGER.error("From ListenerBean method preDestroy: something went wrong\n",e);
        }
    }
}
