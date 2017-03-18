package util;

import com.alibaba.fastjson.JSON;
import com.mianbao.util.DbConnectionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by zoujiajian on 2017-3-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext.xml")
public class TestUtil {

    @Test
    public void testConnection() throws SQLException {
        Connection connection = DbConnectionManager.createDbConnectionManager().getConnection();
        System.out.println(JSON.toJSONString(connection));
        connection.close();
    }
}
