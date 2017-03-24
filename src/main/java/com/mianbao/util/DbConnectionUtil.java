package com.mianbao.util;



import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
@Service
public class DbConnectionUtil {

    /**
     * 初始化连接池大小
     */
    @Value("${jdbc.initialPoolSize}")
    private int initialSize = 10;

    /**
     * 最小活跃连接数
     */
    @Value("${jdbc.minPoolSize}")
    private int minIdle = 10;

    /**
     * 最大活跃连接数
     */
    @Value("${jdbc.maxPoolSize}")
    private int maxActive = 50;

    @Value("${jdbc.url}")
    private String url = null;

    @Value("${jdbc.username}")
    private String username = null ;

    @Value("${jdbc.password}")
    private String password = null;

    @Value("${jdbc.driver}")
    private String driver = null;

    private AtomicInteger poolSize =  new AtomicInteger();

    private Set<Connection> connectionPool = Sets.newConcurrentHashSet();

    private ExecutorService producerService = new ThreadPoolExecutor(5,10,10, TimeUnit.SECONDS ,new LinkedBlockingQueue<>(100));;

    public DbConnectionUtil(){

    }

    private Connection producer() throws ClassNotFoundException, SQLException {

        if(StringUtils.isEmpty(driver)
                || StringUtils.isEmpty(url) || StringUtils.isEmpty(username)){
            throw new IllegalArgumentException("driver or url or userName is empty");
        }
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }

    private void producerWithCapacity(int capacity){
        if(capacity <= 0){
            throw new IllegalArgumentException("capacity exception");
        }
        //check 防止多线程进入一个线程初始化的时候 另外一个线程生产 导致溢出
        while(capacity != 0 && getPoolSize() < initialSize){
            try {
                connectionPool.add(producer());
                poolSize.incrementAndGet();
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
        // 第一次获取连接的时候连接池初始化
        if(poolSize.get() == 0){
            producerWithCapacity(initialSize);
        }

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
                    if(getPoolSize() <= minIdle){
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
                if(getPoolSize() < maxActive){
                    final int currentSize = getPoolSize();
                    if(currentSize < maxActive){
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
                if(getPoolSize() < maxActive){
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

    public void setPoolSize(AtomicInteger poolSize) {
        this.poolSize = poolSize;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Set<Connection> getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(Set<Connection> connectionPool) {
        this.connectionPool = connectionPool;
    }

    public ExecutorService getProducerService() {
        return producerService;
    }

    public void setProducerService(ExecutorService producerService) {
        this.producerService = producerService;
    }
}
