package com.frank.register;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by zuowenbin on 17/8/27.
 */
public class RegistryManager {

    private ZkClient zkClient;

    public RegistryManager(String zkServers, int timeOut){
        zkClient = new ZkClient(zkServers, timeOut);
    }

    public void register(String node, String child){
        if(!zkClient.exists(node)){
            zkClient.createPersistent(node, true);
        }
        String nodeName = getNodeName(node, child);
        zkClient.createEphemeral(nodeName);
    }

    private String getNodeName(String... params){
        StringBuilder builder = new StringBuilder();
        for(String param : params){
            if(param == null){
                continue;
            }
            if(param.charAt(0) != '/'){
                builder.append('/');
            }
            builder.append(param);
        }
        return builder.toString();
    }
}
