package com.mianbao.util;

import java.security.MessageDigest;

/**
 * Created by zoujiajian on 2017-3-24.
 */
public class Md5Util {

    private Md5Util(){

    }

    public static String getMD5Key(String key) {
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(key.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
