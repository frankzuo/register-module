package com.frank.discover;

import com.frank.net.Client;

/**
 * Created by zuowenbin on 17/8/27.
 */
public interface Connection {

    public Client getClient();

    public String getIp();

    public int getPort();

    public void connect();

    public boolean isConnect();

    public boolean isRecommend();

    public void setRecommend(boolean recommend);

    public int getVersion();
}
