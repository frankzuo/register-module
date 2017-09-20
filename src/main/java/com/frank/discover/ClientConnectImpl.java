package com.frank.discover;

import com.frank.msg.MessageDecoder;
import com.frank.net.Client;
import com.frank.net.MessageHandler;

/**
 * Created by zuowenbin on 17/8/27.
 */
public class ClientConnectImpl implements Connection {

    private Client client;

    private String ip;
    private int port;
    private boolean recommend;
    private int version;

    private MessageHandler messageHandler;

    private Class<? extends MessageDecoder> messageDecoder;

    public ClientConnectImpl(String ip, int port, int version){
        this.ip = ip;
        this.port = port;
        this.recommend = false;
        this.version = version;
        client = new Client(ip, port, messageHandler, messageDecoder);
    }

    public Client getClient() {
        return client;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void connect() {
        client.reConnect();
    }

    public boolean isConnect() {
        return client.isConnect();
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend){
        this.recommend = recommend;
    }

    public int getVersion() {
        return version;
    }
}
