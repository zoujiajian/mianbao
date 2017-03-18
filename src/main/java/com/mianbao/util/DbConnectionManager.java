package com.mianbao.util;



import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zoujiajian on 2017-3-14.
 */
public class DbConnectionManager extends  DbDriverConfig{

    private static final int DEFAULT_INIT_SIZE = 16;

    /**
     * 连接数
     */
    private AtomicInteger poolSize;

    private Set<Connection> connectionPool;

    private ExecutorService producerService;

    private DbConnectionManager(){
        int init = getInitialSize();
        if(init <= 0){
            init = DEFAULT_INIT_SIZE;
        }
        poolSize = new AtomicInteger(init);
        producerService = new ThreadPoolExecutor(5,10,10, TimeUnit.SECONDS ,new LinkedBlockingQueue<>(100));
        connectionPool = Sets.newConcurrentHashSet();
        producerWithCapacity(init);
    }

    //使用静态内部类实现单列 从而实现连接池的单实例 多线程
    private static class DbConnectionFactory{

        private static DbConnectionManager dbConnectionManager = new DbConnectionManager();

    }

    public static DbConnectionManager createDbConnectionManager(){
        return DbConnectionFactory.dbConnectionManager;
    }

    private Connection producer() throws ClassNotFoundException, SQLException {
        if(StringUtils.isEmpty(getDriver())
                || StringUtils.isEmpty(getUrl()) || StringUtils.isEmpty(getUsername())){
            throw new IllegalArgumentException("driver or url or userName is empty");
        }
        Class.forName(getDriver());
        return DriverManager.getConnection(getUrl(),getUsername(),getPassword());
    }

    private void producerWithCapacity(int capacity){
        if(capacity <= 0){
            throw new IllegalArgumentException("capacity exception");
        }
        while(capacity != 0 && getPoolSize() < getMaxActive()){
            try {
                connectionPool.add(producer());
                capacity -- ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Connection tryConnection(){
        return getConnection(1L,TimeUnit.MILLISECONDS);
    }

    public Connection getConnection(){
        for(;;){
            Connection connection = getConnection(10L,TimeUnit.MILLISECONDS);
            if(connection != null){
                return connection;
            }
        }
    }

    public Connection getConnection(Long timeOut, TimeUnit timeUnit){
        long count = timeUnit.toMillis(timeOut);
        //cas 获取连接
        while(count > 0){
            //防止消费过快的情况
            if(getPoolSize() > 0){
                Iterator<Connection> iterator = connectionPool.iterator();
                while(iterator.hasNext()){
                    Connection connection =  iterator.next();
                    connectionPool.remove(connection);
                    // 连接池中连接数是否小于最小活跃数
                    if(getPoolSize() <= getMinIdle()){
                        producerService.submit(new Procuder(10));
                    }
                    poolSize.decrementAndGet() ;
                    return connection;
                }
            }else{
                producerService.submit(new Procuder(10));
            }
            count--;
        }
        return null;
    }

    public void returnPool(Connection connection){
        if(connection != null){
            try{
                if(getPoolSize() < getMaxActive()){
                    final int currentSize = getPoolSize();
                    if(currentSize < getMaxActive()){
                        connectionPool.add(connection);
                    }
                    poolSize.incrementAndGet();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class Procuder implements Runnable{

        private int procuderNum;

        Procuder(int procuderNum){
            this.procuderNum = procuderNum;
        }

        @Override
        public void run() {
            while(procuderNum > 0 ){
                if(getPoolSize() < getMaxActive()){
                    try {
                        poolSize.incrementAndGet();
                        connectionPool.add(producer());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    procuderNum -- ;
                }else{
                    return;
                }
            }
        }
    }

    private int getPoolSize(){
        return poolSize.get();
    }
}
