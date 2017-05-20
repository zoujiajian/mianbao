package com.mianbao.util;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by zoujiajian on 2017-5-20.
 * 敏感词检测
 */
public class BadWordsUtil {

    private static final int WORDS_MAX_LENGTH = 10;
    private static final String BAD_WORDS_LIB_FILE_NAME = "badWords.txt";

    //敏感词列表
    private static Map[] badWordsList = null;

    //敏感词索引
    private static Map<String, Integer> wordIndex = Maps.newHashMap();

    /*
    * 初始化敏感词库
    */
    private static void initbadWordsList() throws IOException {
        if (badWordsList == null) {
            badWordsList = new Map[WORDS_MAX_LENGTH];

            for (int i = 0; i < badWordsList.length; i++) {
                badWordsList[i] = new HashMap<String, String>();
            }
        }

        //敏感词词库所在目录，这里为txt文本，一个敏感词一行
        String path = BadWordsUtil.class.getClassLoader()
                .getResource(BAD_WORDS_LIB_FILE_NAME)
                .getPath();

        List<String> words = FileUtils.readLines(new File(path),"UTF-8");
        for (String w : words) {
            if (StringUtils.isNotBlank(w)) {
                //将敏感词按长度存入map
                badWordsList[w.length()].put(w.toLowerCase(), "");

                Integer index = wordIndex.get(w.substring(0, 1));

                //生成敏感词索引，存入map
                if (index == null) {
                    index = 0;
                }

                int x = (int) Math.pow(2, w.length());
                index = (index | x);
                wordIndex.put(w.substring(0, 1), index);
            }
        }
    }
    /**
     * 检索敏感词
     * @param content
     * @return
     */
    public static boolean searchBanWords(String content) {
        boolean flag = false;
        if (badWordsList == null) {
            try {
                initbadWordsList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < content.length(); i++) {
            if(wordIndex.containsKey(content.substring(i, i + 1))){
                flag = true;
                break;
            }
        }

        return flag;
    }

    public static void main(String[] args) throws IOException {
        String content = "含有敏感词的测试SB";
        BadWordsUtil.initbadWordsList();
        System.out.println(BadWordsUtil.searchBanWords(content));
    }
}
