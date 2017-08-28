package com.frank.discover;

import com.frank.net.Client;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * Created by zuowenbin on 17/8/27.
 */
public class ZkDiscoverBinder {
    private ZkClient zkClient;

    public ZkDiscoverBinder(String zkServers, int timeOut){
        zkClient = new ZkClient(zkServers, timeOut);
    }

    public void connect(String... nodes){
        for(String node : nodes){
            if(node == null){
                continue;
            }
            if(!zkClient.exists(node)){
                zkClient.createPersistent(node, true);
            }
            zkClient.subscribeChildChanges(node, new ZkDiscoverListener());
            List<String> childs = zkClient.getChildren(node);
            for(String child : childs){
                if(child == null){
                    continue;
                }
                String[] params = child.split(":");
                if(params.length != 2){
                    continue;
                }
                String ip = params[0];
                int port = Integer.parseInt(params[1]);
                Connection connection = new ClientConnectImpl(ip, port);
                ServiceManager.getInstance().addService(node, child, connection);
            }
        }
    }
}
