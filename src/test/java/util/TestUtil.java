package util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.mianbao.common.Result;
import com.mianbao.dao.DynamicEvaluateMapper;
import com.mianbao.domain.DynamicEvaluate;
import com.mianbao.domain.DynamicEvaluateExample;
import com.mianbao.service.DynamicService;
import com.mianbao.service.PictureService;
import com.mianbao.service.RedisService;
import com.mianbao.service.ScenicSpotService;
import com.mianbao.util.DbConnectionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Resource
    private DynamicService dynamicService;

    @Resource
    private ScenicSpotService scenicSpotService;

    @Resource
    private DbConnectionUtil dbConnectionUtil;

    @Test
    public void testRedis() throws SQLException {
        redisService.addByKey("name","zoujiajian");
        System.out.println(  redisService.getByKey("name"));
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

    @Test
    public void testDynamicAll(){
        System.out.println(JSON.toJSONString(dynamicService.getUserAllDynamic(1,2,3)));
    }

    @Test
    public void testScenicInfo(){
        Map<String,Object> params = Maps.newHashMap();
        params.put("pageNo",1);
        params.put("pageSize",10);

        System.out.println(JSON.toJSONString(scenicSpotService.selectAllScenicInfo(params)));
    }

    @Test
    public void testScenic(){
        System.out.println(JSON.toJSONString(scenicSpotService.getScenicSpotInfoWithDynamic(4,1,6)));
    }
}
