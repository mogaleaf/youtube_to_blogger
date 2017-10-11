package com.mogaleaf.oauth;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisDataStore implements DataStore<StoredCredential> {

	private DataStoreFactory dataStoreFactory;
	private String id;
	private Jedis jedis;

	public RedisDataStore(DataStoreFactory dataStoreFactory, String id, Jedis jedis) {
		this.dataStoreFactory = dataStoreFactory;
		this.id = id;
		this.jedis = jedis;
	}

	@Override
	public DataStoreFactory getDataStoreFactory() {
		return dataStoreFactory;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int size() throws IOException {
		return jedis.hlen(id).intValue();
	}

	@Override
	public boolean isEmpty() throws IOException {
		return jedis.hlen(id) > 0;
	}

	@Override
	public boolean containsKey(String s) throws IOException {
		return jedis.hexists(id, s);
	}

	@Override
	public boolean containsValue(StoredCredential store) throws IOException {
		return jedis.hvals(id).contains(serializeStore(store));
	}


	@Override
	public Set<String> keySet() throws IOException {
		return jedis.hkeys(id);
	}

	@Override
	public Collection<StoredCredential> values() throws IOException {
		List<String> hvals = jedis.hvals(id);
		return hvals.stream().map(this::getStoredCredential).collect(Collectors.toList());
	}

	@Override
	public StoredCredential get(String s) throws IOException {
		String hget = jedis.hget(id, s);
		if(hget == null) {
			return null;
		}
		StoredCredential storedCredential = getStoredCredential(hget);

		return storedCredential;
	}

	private StoredCredential getStoredCredential(String hget) {
		String[] split = hget.split(",");
		StoredCredential storedCredential = new StoredCredential();
		storedCredential.setAccessToken(split[0]);
		storedCredential.setRefreshToken(split[1]);
		storedCredential.setExpirationTimeMilliseconds(Long.valueOf(split[2]));
		return storedCredential;
	}

	@Override
	public DataStore set(String s, StoredCredential store) throws IOException {
		jedis.hset(id, s, serializeStore(store));
		return this;
	}

	private String serializeStore(StoredCredential store) {
		return store.getAccessToken() + "," + store.getRefreshToken() + "," + store.getExpirationTimeMilliseconds();
	}

	@Override
	public DataStore<StoredCredential> clear() throws IOException {
		jedis.del(id);
		return this;
	}

	@Override
	public DataStore<StoredCredential> delete(String s) throws IOException {
		jedis.hdel(id, s);
		return this;
	}
}
