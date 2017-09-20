package com.frank.discover;

import java.util.*;
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

    public void checkConnects(){
        if(services.isEmpty()){
            return;
        }
        Collection<Map<String, Connection>> map = services.values();
        if(map == null || map.isEmpty()){
            return;
        }
        for(Map<String, Connection> m : map){
            if(m == null || m.isEmpty()){
                continue;
            }
            Collection<Connection> cons = m.values();
            if(cons == null || cons.isEmpty()){
                continue;
            }
            for(Connection con : cons){
                if(!con.isConnect()){
                    con.connect();
                }
            }
        }
    }

    /**
     * 添加节点新服务
     * @param node
     * @param child
     * @param connection
     */
    public void addService(String node, String child, Connection connection){
        Map<String, Connection> connects = services.get(node);
        if(connects == null){
            connects = new ConcurrentHashMap<String, Connection>();
            services.put(node, connects);
        }
        connects.put(child, connection);
    }

    /**
     * 设置推荐节点服务链接
     * @param node
     * @param version
     */
    public void checkRecommendNode(String node, int version){
        Map<String, Connection> connects = services.get(node);
        if(connects == null){
            return;
        }
        for(Connection con : connects.values()){
            if(con.getVersion() == version){
                con.setRecommend(true);
            }
        }
    }

    /**
     * 获取一个节点下的所有子节点信息
     * @param parentNode
     * @return
     */
    public Set<String> getNodeInfo(String parentNode){
        Set<String> nodes = new HashSet<String>();
        Map<String, Connection> connects = services.get(parentNode);
        if(connects == null){
            return nodes;
        }
        Set<String> keys = connects.keySet();
        for(String k:keys){
            nodes.add(k);
        }
        return nodes;
    }

    public boolean contains(String parentNode, String childNode){
        Map<String, Connection> connects = services.get(parentNode);
        if(connects == null){
            return false;
        }
        return connects.containsKey(childNode);
    }

    public void remove(String parentNode, String node) {
        Map<String, Connection> connects = services.get(parentNode);
        if(connects == null){
            return;
        }
        connects.remove(node);
    }

}
