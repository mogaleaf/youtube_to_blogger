package com.mogaleaf;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.mogaleaf.oauth.RedisDatastoreFactory;
import com.mogaleaf.server.Configuration;
import com.mogaleaf.server.OAuthHttpServer;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {


    public static void main(String args[]) {
        Logger.getLogger("APPBLOG").info("STARTING");
        try {
            Jedis jedis = new Jedis(Configuration.REDIS_HOST,Integer.valueOf(Configuration.REDIS_PORT));
            RedisDatastoreFactory redisDatastoreFactory = new RedisDatastoreFactory(jedis);
            Configuration.DATA_STORE_FACTORY = redisDatastoreFactory;
            new OAuthHttpServer().start();

        } catch (Exception e) {
            Logger.getLogger("APPBLOG").log(Level.SEVERE,"problem",e);
            e.printStackTrace();
        }
    }


}
