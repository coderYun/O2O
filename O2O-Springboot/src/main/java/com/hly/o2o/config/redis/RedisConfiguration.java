package com.hly.o2o.config.redis;

import com.hly.o2o.cache.JedisPoolWriper;
import com.hly.o2o.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
    private String hostname;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.pool.maxActive}")
    private int maxTotal;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.pool.maxWait}")
    private long maxWaitMillions;
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;
    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWriper jedisWritePool;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 创建redis连接池的设置
     *
     */
    @Bean(name="jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        //设置一个Pool可以分配多少个redis
        jedisPoolConfig.setMaxTotal(maxTotal);
        //设置最多可以有多少个空闲
        jedisPoolConfig.setMaxIdle(maxIdle);
        //设置最大等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillions);
        //设置在获取连接时检查其有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;

    }


    /**
     * 创建redis连接池
     *
     */
    @Bean(name="jedisWritePool")
    public JedisPoolWriper createJedisPoolWriper(){
      /*  JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        //设置一个Pool可以分配多少个redis
        jedisPoolConfig.setMaxTotal(maxTotal);
        //设置最多可以有多少个空闲
        jedisPoolConfig.setMaxIdle(maxIdle);
        //设置最大等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillions);
        //设置在获取连接时检查其有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);*/

        JedisPoolWriper jedisPoolWriper=new JedisPoolWriper(jedisPoolConfig,hostname,port);
        return jedisPoolWriper;

    }

    /**
     * 创建jedeisUtil工具类
     * @return
     */
    @Bean(name="jedisUtil")
    public  JedisUtil createJedisUtil(){
        JedisUtil jedisUtil=new JedisUtil();
        jedisUtil.setJedisPool(jedisWritePool);
        return jedisUtil;


    }

    /**
     * Redis的key操作
     * @return
     */
    @Bean(name="jedisKeys")
    public JedisUtil.Keys createKeys(){
        JedisUtil.Keys jedisKeys=jedisUtil.new Keys();
        return jedisKeys;

    }

    /**
     * Redis的Strings操作
     * @return
     */
    @Bean(name="jedisStrings")
    public JedisUtil.Strings createStrings(){
        JedisUtil.Strings jedisStrings=jedisUtil.new Strings();
        return jedisStrings;

    }
}
