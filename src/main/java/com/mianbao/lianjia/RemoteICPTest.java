package com.mianbao.lianjia;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zoujiajian on 2017-4-11.
 */
public class RemoteICPTest {

//    public static void main(String[] args) throws Exception {
//        CountDownLatch countDownLatch = new CountDownLatch(2);
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//
//        LianJiaICP lianJiaICP = ICPFactory.getLianjiaIcp();
//        BaiDuICP baiDuICP = ICPFactory.getBaiduIcp();
//        executorService.submit(new Worker(baiDuICP,countDownLatch));
//        executorService.submit(new Worker(lianJiaICP,countDownLatch));
//
//        countDownLatch.await();
//    }


    private static class Worker implements Runnable{

        private RemoteGetICP remoteGetICP;

        private CountDownLatch countDownLatch;

        Worker(RemoteGetICP remoteGetICP, CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
            this.remoteGetICP = remoteGetICP;
        }

        @Override
        public void run() {
            try{
                String icp = remoteGetICP.getICPbyURL();
                System.out.println("icp = "+ icp);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        }
    }
}
