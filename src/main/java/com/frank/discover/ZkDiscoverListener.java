package com.frank.discover;

import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;
import java.util.Set;

/**
 * Created by zuowenbin on 17/8/27.
 */
public class ZkDiscoverListener implements IZkChildListener {

    private ZkDiscoverListener(){
    }

    public static ZkDiscoverListener instance = new ZkDiscoverListener();

    public static ZkDiscoverListener getInstance(){
        return instance;
    }

    public void handleChildChange(String parentNode, List<String> currentNode) throws Exception {
        Set<String> nodes = ServiceManager.getInstance().getNodeInfo(parentNode);
        int maxVersion = 0;
        for(String node : currentNode){
            if(!ServiceManager.getInstance().contains(parentNode, node)){
                String[] infos = node.split("\\|");
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
                ServiceManager.getInstance().addService(parentNode, node, connection);
            }
            nodes.remove(node);
        }

        if(!nodes.isEmpty()){
            for (String node : nodes){
                ServiceManager.getInstance().remove(parentNode, node);
            }
        }
        ServiceManager.getInstance().checkRecommendNode(parentNode, maxVersion);
    }
}
