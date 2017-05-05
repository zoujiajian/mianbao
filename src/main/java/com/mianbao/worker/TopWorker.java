package com.mianbao.worker;

import com.mianbao.common.CacheKey;
import com.mianbao.exception.BusinessException;
import com.mianbao.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by zoujiajian on 2017-4-7.
 * 动态浏览排行榜
 */
@Component
public class TopWorker {

    private static final Logger logger = LoggerFactory.getLogger("cache");

    @Resource
    private RedisService redisService;

    @Value(value = "${top.capacity}")
    private int topCapacity;

    private ExecutorService executors;

    @PostConstruct
    public void init(){
        executors = new ThreadPoolExecutor(5,20,5, TimeUnit.MINUTES, new LinkedBlockingQueue<>(100));
    }

    @PreDestroy
    public void destory(){
        executors.shutdown();
    }

    //每小时定时清除排行榜capacity之后的数据
//    @Scheduled(cron = "0 0 * * * ? ")
    public void work(){
        executors.submit(new Runnable() {
            @Override
            public void run() {
                //删除排行榜中命中次数最低 到倒数第top capacity+1高的数据
                long number = redisService.delOrderSetBetweenXAndY(CacheKey.TOP_DYNAMIC_INFO,0,-(topCapacity + 1));
                logger.info("淘汰排行榜中的数据条件 : {}",number);
            }
        });
    }

    /**
     * 调整排行榜顺序
     * @param member
     */
    public void recordAccess(String member){
        if(StringUtils.isEmpty(member)){
            throw new IllegalArgumentException("dynamicId less zero");
        }
        boolean exists = redisService.exists(CacheKey.TOP_DYNAMIC_INFO);
        if(!exists){
            redisService.addToOrderSet(CacheKey.TOP_DYNAMIC_INFO,1,member);
        }else{
            redisService.orderSetScoreIncrByMember(CacheKey.TOP_DYNAMIC_INFO,member);
        }
    }

    /**
     * 获取前 top 元素
     * @return
     */
    public Set<String> getTopInfo(){
        boolean exists = redisService.exists(CacheKey.TOP_DYNAMIC_INFO);
        if(!exists){
            return Collections.emptySet();
        }
        return redisService.getTopByOrderSet(CacheKey.TOP_DYNAMIC_INFO,topCapacity);
    }
}
