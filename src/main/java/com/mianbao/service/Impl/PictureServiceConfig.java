package com.mianbao.service.Impl;

import com.google.common.collect.Sets;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zoujiajian on 2017-4-12.
 */
public abstract class PictureServiceConfig {

    private static final Logger logger = LoggerFactory.getLogger(PictureServiceConfig.class);

    //连接池默认客户端数量
    private static final int DEFAULT_POOL_SIZE = 32;

    //连接池默认最大客户端数量
    private static final int DEFAULT_POOL_MAX_SIZE = 100;

    //连接池默认最小客户端数量
    private static final int DEFAULT_POOL_MIN_SIZE = 16;

    //每次步进的客户端数量
    private static final int STEP_NUMBER = 4;

    //连接池
    private static final Set<StorageClient1> clientPool = Sets.newConcurrentHashSet();

    //连接池中连接的大小
    private static AtomicInteger poolSize = new AtomicInteger();

    private static TrackerServer trackerServer = null;

    private static StorageServer storageServer = null;

    private static ExecutorService executorService;

    static {
        try{
            logger.info("init clientPool");
            String configPath = PictureServiceImpl.class.getClassLoader().getResource("config/fdfs_client.conf").getFile();
            ClientGlobal.init(configPath);
            executorService =  new ThreadPoolExecutor(5,10,10, TimeUnit.SECONDS ,new LinkedBlockingQueue<>(100));

            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);

            while(poolSize.get() < DEFAULT_POOL_SIZE){
                clientPool.add(new StorageClient1(trackerServer, storageServer));
                poolSize.incrementAndGet();
            }
        }catch (Exception e){
            logger.error("init clientPool error", e);
        }
    }

    private StorageClient1 producer(){
        StorageClient1 client = null;
        if(poolSize.get() < DEFAULT_POOL_MAX_SIZE){
            client = new StorageClient1(trackerServer, storageServer);
        }
        return client;
    }

    public StorageClient1 getClinet(){
        for(;;){
            StorageClient1 client = getClient(10L,TimeUnit.MILLISECONDS);
            if(client != null){
                return client;
            }
        }
    }

    public StorageClient1 getClient(Long timeOut, TimeUnit timeUnit){

        long count = timeUnit.toMillis(timeOut);
        //cas 获取连接
        while(count > 0){
            if(poolSize.get() > 0){
                Iterator<StorageClient1> iterator = clientPool.iterator();
                while(iterator.hasNext()){
                    StorageClient1 client = iterator.next();

                    clientPool.remove(client);
                    // 连接池中连接数是否小于最小活跃数
                    if(poolSize.get() <= DEFAULT_POOL_MIN_SIZE){
                        executorService.submit(new Procuder(STEP_NUMBER));
                    }
                    poolSize.decrementAndGet() ;
                    return client;
                }
            }else{
                executorService.submit(new Procuder(STEP_NUMBER));
            }
            count--;
        }
        return null;
    }

    public void returnPool(StorageClient1 client){
        if(client != null){
            try{
                if(poolSize.get() < DEFAULT_POOL_MAX_SIZE){
                    final int currentSize = poolSize.get();
                    if(currentSize < DEFAULT_POOL_MAX_SIZE){
                        clientPool.add(client);
                    }
                    poolSize.incrementAndGet();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class Procuder implements Runnable{

        private int countNum;

        Procuder(int countNum){
            this.countNum = countNum;
        }

        @Override
        public void run() {
            while(countNum > 0){
                if(poolSize.get() < DEFAULT_POOL_MAX_SIZE){
                    try{
                       clientPool.add(producer());
                       poolSize.incrementAndGet();

                    }catch (Exception e){
                        logger.error("procuder error",e);
                    }
                    countNum -- ;
                }else{
                    return;
                }
            }
        }
    }
}
