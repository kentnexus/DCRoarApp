package com.paszlelab.dcroarapp.constants;

import java.util.HashMap;

public class Constants {

    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders(){
        if(remoteMsgHeaders ==null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAUJRx7xI:APA91bHnRWQkrXG1_xD8GWyVvqcq9fB_ianceaft-xQ0pvesN4qXxONIwDcXudn1TJipHfQSqBvE6Wrx4dbBbs2KiASBKs7FDDm4tqM8OLjNQgQYK7dCXFuIO2D_h7k0CwhsoSFtinE7"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }
}
