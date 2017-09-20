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
        zkClient.waitUntilConnected();
    }

    public void connect(String... nodes){
        List<String> childNodes;
        for(String node : nodes){
            if(node == null){
                continue;
            }
            if(!zkClient.exists(node)){
                zkClient.createPersistent(node, true);
            }
            zkClient.subscribeChildChanges(node, ZkDiscoverListener.getInstance());
            childNodes = zkClient.getChildren(node);
            int maxVersion = 0;
            for(String child : childNodes){
                if(child == null){
                    continue;
                }
                String[] infos = child.split("\\|");
                if(infos.length !=2 ) {
                    continue;
                }
                String[] params = infos[0].split(":");
                if(params.length != 2){
                    continue;
                }
                int tempVersion = Integer.parseInt(infos[1]);
                if (tempVersion > maxVersion){
                    maxVersion = tempVersion;
                }
                String ip = params[0];
                int port = Integer.parseInt(params[1]);
                Connection connection = new ClientConnectImpl(ip, port, tempVersion);
                ServiceManager.getInstance().addService(node, child, connection);
            }
            ServiceManager.getInstance().checkRecommendNode(node, maxVersion);
        }
    }
}
