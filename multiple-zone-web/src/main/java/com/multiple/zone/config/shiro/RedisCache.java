package com.multiple.zone.config.shiro;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.SerializationUtils;

import java.util.*;

/**
* @Description
* @Author lichao
* @Date 2017年10月11日14:30:55
*/
public class RedisCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisTemplate<K, V> cache;

    /**
     * The Redis key prefix for the sessions
     */
    private String keyPrefix = "shiro_redis_cache:";

    /**
     * Returns the Redis session keys
     * prefix.
     *
     * @return The prefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * Sets the Redis sessions key
     * prefix.
     *
     * @param keyPrefix The prefix
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    /**
     * 通过一个JedisCluster实例构造RedisCache
     */
    public RedisCache(RedisTemplate cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
    }

    /**
     * Constructs a cache instance with the specified
     * Redis manager and using a custom key prefix.
     *
     * @param cache  The cache manager instance
     * @param prefix The Redis key prefix
     */
    public RedisCache(RedisTemplate cache, String prefix) {
        this(cache);
        // set the prefix
        this.keyPrefix = prefix;
    }

    /**
     * 获得byte[]型的key
     *
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.keyPrefix + key;
            return preKey.getBytes();
        } else {
            return SerializationUtils.serialize(key);
        }
    }

    @Override
    public V get(final K key) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            } else {
                return cache.execute(new RedisCallback<V>() {
                    @Override
                    public V doInRedis(RedisConnection connection) throws DataAccessException {
                        byte[] keybytes = getByteKey(key);
                        if (connection.exists(keybytes)) {
                            byte[] valuebytes = connection.get(keybytes);
                            @SuppressWarnings("unchecked")
                            V value = (V) SerializationUtils.deserialize(valuebytes);
                            return value;
                        }
                        return null;
                    }
                });
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V put(final K key, final V value) throws CacheException {
        logger.debug("根据key从存储 key [" + key + "]");
        try {
            final byte[] vbytes = SerializationUtils.serialize(value);
            cache.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.set(getByteKey(key), vbytes);
                    return null;
                }
            });
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(final K key) throws CacheException {
        logger.debug("从redis中删除 key [" + key + "]");
        try {
            V previous = get(key);
            cache.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.del(getByteKey(key));
                    return null;
                }
            });
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        logger.debug("从redis中删除所有元素");
        try {
            cache.execute(new RedisCallback<Void>() {
                public Void doInRedis(RedisConnection connection) {
                    Set<byte[]> keys = connection.keys(SerializationUtils.serialize(getKeyPrefix() + "*"));
                    connection.del((byte[][]) keys.toArray());
                    return null;
                }
            }, true);
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            return (Integer) cache.execute(new RedisCallback() {
                public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.dbSize().intValue();
                }
            });
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        try {
            return cache.execute(new RedisCallback<Set<K>>() {
                public Set<K> doInRedis(RedisConnection connection) {
                    Set<byte[]> keys = connection.keys(SerializationUtils.serialize(getKeyPrefix() + "*"));
                    if (CollectionUtils.isEmpty(keys)) {
                        return Collections.emptySet();
                    } else {
                        Set<K> newKeys = new HashSet<K>();
                        for (byte[] key : keys) {
                            newKeys.add((K) key);
                        }
                        return newKeys;
                    }
                }
            }, true);

        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            return cache.execute(new RedisCallback<Collection<V>>() {
                public Collection<V> doInRedis(RedisConnection connection) {
                    Set<byte[]> keys = connection.keys(SerializationUtils.serialize(getKeyPrefix() + "*"));
                    if (!CollectionUtils.isEmpty(keys)) {
                        List<V> values = new ArrayList<V>(keys.size());
                        for (byte[] key : keys) {
                            @SuppressWarnings("unchecked")
                            V value = get((K) key);
                            if (value != null) {
                                values.add(value);
                            }
                        }
                        return Collections.unmodifiableList(values);
                    } else {
                        return Collections.emptyList();
                    }
                }
            }, true);
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
}
