package util;

import com.alibaba.fastjson.JSON;
import com.mianbao.dao.DynamicEvaluateMapper;
import com.mianbao.domain.DynamicEvaluate;
import com.mianbao.domain.DynamicEvaluateExample;
import com.mianbao.service.RedisService;
import com.mianbao.util.DbConnectionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
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

}
