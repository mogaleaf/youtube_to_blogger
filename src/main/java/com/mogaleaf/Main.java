package com.mogaleaf;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.mogaleaf.oauth.RedisDatastoreFactory;
import com.mogaleaf.server.Configuration;
import com.mogaleaf.server.OAuthHttpServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.File;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {


    public static void main(String args[]) {
        Logger.getLogger("APPBLOG").info("STARTING");
        try {
           // Jedis jedis = new Jedis(Configuration.REDIS_HOST,Integer.valueOf(Configuration.REDIS_PORT));
            URI redisURI = new URI(System.getenv("REDISTOGO_URL"));
            JedisPool pool = new JedisPool(new JedisPoolConfig(),
                    redisURI.getHost(),
                    redisURI.getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    redisURI.getUserInfo().split(":",2)[1]);
            RedisDatastoreFactory redisDatastoreFactory = new RedisDatastoreFactory(pool.getResource());
            Configuration.DATA_STORE_FACTORY = redisDatastoreFactory;
            new OAuthHttpServer().start();

        } catch (Exception e) {
            Logger.getLogger("APPBLOG").log(Level.SEVERE,"problem",e);
            e.printStackTrace();
        }
    }


}
