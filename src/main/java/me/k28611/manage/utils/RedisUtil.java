package me.k28611.manage.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author K28611
 * @date 2020/5/24 23:07
 */
@Slf4j
@Component
public final class RedisUtil {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public boolean expire(String key,long time){
        try{
            if(time>0){
                redisTemplate.expire(key,time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    //查询过期时间

    public long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    //判断是否存在Key

    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    @SuppressWarnings("unchecked")
    public void del(String... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }

    }
    public Object get(String key){
        return key ==null?null:redisTemplate.opsForValue().get(key);
    }

    public boolean set(String key,Object value){
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    //客制化过期时间
    public boolean set(String key,Object value,long time){
        try {
            if (time>0){
                redisTemplate.opsForValue().set(key, value,time,TimeUnit.SECONDS);
            }
            else{
                redisTemplate.opsForValue().set(key,value);
            }
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }


}
