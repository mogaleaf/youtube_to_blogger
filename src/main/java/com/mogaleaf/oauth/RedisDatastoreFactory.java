package com.mogaleaf.oauth;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

public class RedisDatastoreFactory extends AbstractDataStoreFactory
{
	private JedisPool jedisPool;
	public RedisDatastoreFactory(JedisPool jedis){
		this.jedisPool = jedis;
	}


	@Override
	protected DataStore<StoredCredential> createDataStore(String s) throws IOException {
			DataStore<StoredCredential> redisDataStore = new RedisDataStore(this,s,jedisPool.getResource());
			return redisDataStore;
	}
}
