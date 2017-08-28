package com.frank.discover;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zuowenbin on 17/8/27.
 */
public class ServiceManager {

    private Map<String, Map<String, Connection>> services = new ConcurrentHashMap<String, Map<String, Connection>>();
    private static ServiceManager instance;
    private ServiceManager(){}

    public static ServiceManager getInstance(){
        if(instance == null){
            instance = new ServiceManager();
        }
        return instance;
    }

    public void addService(String node, String child, Connection connection){
        Map<String, Connection> connects = services.get(node);
        if(connects == null){
            connects = new ConcurrentHashMap<String, Connection>();
            services.put(node, connects);
        }
        connects.put(child, connection);
    }

}
