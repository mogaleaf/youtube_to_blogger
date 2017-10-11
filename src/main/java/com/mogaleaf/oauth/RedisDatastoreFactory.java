package com.mogaleaf.oauth;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class RedisDatastoreFactory extends AbstractDataStoreFactory
{
	private Jedis jedis;
	public RedisDatastoreFactory(Jedis jedis){
		this.jedis = jedis;
	}


	@Override
	protected DataStore<StoredCredential> createDataStore(String s) throws IOException {
			DataStore<StoredCredential> redisDataStore = new RedisDataStore(this,s,jedis);
			return redisDataStore;
	}
}
