package cn.hamm.airpower.util.redis;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.root.RootEntity;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Redis封装类</h1>
 *
 * @author Hamm
 */
@Component
public class RedisUtil<E extends RootEntity<E>> {
    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * <h1>从缓存中获取实体</h1>
     *
     * @param entity 实体
     * @return 实体
     */
    public E getEntity(E entity) {
        Object object = get(getCacheKey(entity));
        if (Objects.isNull(object)) {
            return null;
        }
        String json = object.toString();
        if (Objects.isNull(json)) {
            return null;
        }
        return (E) JSON.parseObject(json, entity.getClass());
    }

    /**
     * <h1>删除指定的实体缓存</h1>
     *
     * @param entity 实体
     */
    public void deleteEntity(E entity) {
        del(getCacheKey(entity));
    }

    /**
     * <h1>获取缓存Entity的cacheKey</h1>
     *
     * @param entity 实体
     * @return key
     */
    private String getCacheKey(E entity) {
        return entity.getClass().getSimpleName() + "_" + entity.getId().toString();
    }

    /**
     * <h1>缓存实体</h1>
     *
     * @param entity 实体
     */
    public void saveEntityCacheData(E entity) {
        if (GlobalConfig.cacheExpTime > 0) {
            set(getCacheKey(entity), JSON.toJSONString(entity), GlobalConfig.cacheExpTime);
        } else {
            set(getCacheKey(entity), JSON.toJSONString(entity));
        }
    }

    /**
     * <h1>指定缓存失效时间</h1>
     *
     * @param key  键
     * @param time 时间(秒)
     * @noinspection unused
     */
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * <h1>删除所有满足条件的数据</h1>
     *
     * @param pattern 正则
     */
    public void clearAll(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (Objects.nonNull(keys)) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * <h1>删除所有数据</h1>
     */
    public void clearAll() {
        clearAll("*");
    }

    /**
     * <h1>根据key 获取过期时间</h1>
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     * @noinspection unused
     */
    public long getExpire(String key) {
        try {
            //noinspection ConstantConditions
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * <h1>判断key是否存在</h1>
     *
     * @param key 键
     * @return <code>true</code> 存在; <code>false</code> 不存在
     */
    public boolean hasKey(String key) {
        try {
            //noinspection ConstantConditions
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <h1>删除缓存</h1>
     *
     * @param key 键
     */
    public void del(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * <h1>普通缓存获取</h1>
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            return Objects.isNull(key) ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * <h1>普通缓存放入</h1>
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * <h1>普通缓存放入并设置时间</h1>
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @apiNote time要大于0 如果time小于等于0 将设置无限期
     */
    public void set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

}