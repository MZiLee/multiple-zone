package com.multiple.zone.config.shiro;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
* @Description
* @Author lichao
* @Date 2017年10月11日14:31:41
*/
public class RedisSessionRepository extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionRepository.class);
    /**
     * shiro-redis的session对象前缀
     */
    @Autowired
    private RedisTemplate<?, ?> redisTemplate;

    /**
     * The Redis key prefix for the sessions
     */
    private String keyPrefix = "performance_session:";

    private int expire = 1800;

    @Override
    public void update(Session session) throws UnknownSessionException {
        logger.debug("更新session",session);
        this.saveSession(session);
    }

    /**
     * save session
     *
     * @param session
     * @throws org.apache.shiro.session.UnknownSessionException
     */
    private void saveSession(final Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = getByteKey(session.getId());
                byte[] value = SerializationUtils.serialize(session);
                connection.set(key, value);
                if (expire > 0) {
                    connection.expire(key, expire);
                }
                return 1L;
            }
        });
    }

    @Override
    public void delete(final Session session) {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.del(getByteKey(session.getId()));
            }
        });
    }

    //用来统计当前活动的session
    @Override
    public Collection<Session> getActiveSessions() {
        return redisTemplate.execute(new RedisCallback<Set<Session>>() {
            public Set<Session> doInRedis(RedisConnection connection) {
                Set<byte[]> keys = connection.keys(SerializationUtils.serialize(getKeyPrefix() + "*"));
                Set<Session> sessions = new HashSet<Session>();
                if (CollectionUtils.isEmpty(keys)) {
                    return Collections.emptySet();
                } else {
                    Set<Session> newKeys = new HashSet<Session>();
                    for (byte[] key : keys) {
                        Session s = (Session) SerializationUtils.deserialize(connection.get(key));
                        sessions.add(s);
                    }
                    return newKeys;
                }
            }
        }, true);

    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(final Serializable sessionId) {
        if (sessionId == null) {
            logger.error("session id is null");
            return null;
        }
        Session session=redisTemplate.execute(new RedisCallback<Session>() {
            public Session doInRedis(RedisConnection connection) throws DataAccessException {
                return (Session) SerializationUtils.deserialize(connection.get(getByteKey(sessionId)));
            }
        });

        if(null!=session){
        logger.debug("read session from redis:{}",session.getId());
        }else{
        logger.debug("cannot read session from redis,sessionId:",sessionId);
        }
        return session;
    }

    /**
     * 获得byte[]型的key
     * @param sessionId
     * @return
     */
    private byte[] getByteKey(Serializable sessionId) {
        String preKey = this.keyPrefix + sessionId;
        return preKey.getBytes();
    }

    /**
     * Returns the Redis session keys
     * prefix.
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


    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
