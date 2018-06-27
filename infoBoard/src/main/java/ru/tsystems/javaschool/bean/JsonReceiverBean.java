package ru.tsystems.javaschool.bean;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import java.io.Serializable;

@Singleton
public class JsonReceiverBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonReceiverBean.class);

    ResteasyClient client = new ResteasyClientBuilder().build();

    public JSONObject getInfoForUpdate(){
        try {
            ResteasyWebTarget target = client.target("http://127.0.0.1:8080/trucking/emit");
            LOGGER.info("From ObjectReceiverBean method getInfoForUpdate receiving request " +
                    "from http://127.0.0.1:8080/trucking/emit");
            return target.request()
                    .get(JSONObject.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
