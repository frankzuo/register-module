package com.frank.register;

/**
 * Created by zuowenbin on 17/8/27.
 */
public class ZKRegister {

    private static ZKRegister instance;
    private RegistryManager registryManager;
    private ZKRegister(){
    }

    private void init(String zkServers, int timeOut){
        registryManager = new RegistryManager(zkServers, timeOut);
    }

    public static ZKRegister getInstance(){
        if(instance == null){
            instance = new ZKRegister();
        }
        return instance;
    }

    public void register(String node, String ip, int port, int version){
        String child = ip + ":" + port + "|" + version;
        registryManager.register(node, child);
    }

}
