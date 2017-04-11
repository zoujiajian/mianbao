package util;

import com.alibaba.fastjson.JSON;
import com.mianbao.dao.DynamicEvaluateMapper;
import com.mianbao.domain.DynamicEvaluate;
import com.mianbao.domain.DynamicEvaluateExample;
import com.mianbao.service.PictureService;
import com.mianbao.service.RedisService;
import com.mianbao.util.DbConnectionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zoujiajian on 2017-3-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext.xml")
public class TestUtil {

    @Resource
    private RedisService redisService;

    @Resource
    private DynamicEvaluateMapper dynamicEvaluateMapper;

    @Resource
    private PictureService pictureService;

    @Test
    public void testRedis() throws SQLException {
        redisService.addByKey("name","zoujiajian");
        System.out.println(  redisService.getByKey("name"));
    }

    @Test
    public void testDb(){
        DynamicEvaluateExample dynamicEvaluateExample = new DynamicEvaluateExample();
                                dynamicEvaluateExample.createCriteria().andDynamicIdEqualTo(1);
        List<DynamicEvaluate> list = dynamicEvaluateMapper.selectByExample(dynamicEvaluateExample);
        System.out.println("value =" + JSON.toJSONString(list));
    }

    @Resource
    private DbConnectionUtil dbConnectionUtil;

    @Test
    public void testConnection(){
        Connection connection = dbConnectionUtil.getConnection();
        System.out.println(connection);
        dbConnectionUtil.returnPool(connection);
    }

    @Test
    public void test(){
        final int a = 0;
        int b = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(a);
                System.out.println(b);
            }
        }).start();
    }

    @Test
    public void testUploadFile(){
        try {
          String url =   pictureService.uploadFile("C:"+ File.separator + "Users"+File.separator + "" +
                  "Administrator"+File.separator + "CnRgzljqaHGAdukXAAAYKMIXiWc886.jpg","jpg");
          System.out.println("url = "+ url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
