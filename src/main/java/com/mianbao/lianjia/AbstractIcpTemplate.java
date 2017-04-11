package com.mianbao.lianjia;


import org.apache.commons.lang3.StringUtils;
/**
 * Created by zoujiajian on 2017-4-11.
 */
public abstract class AbstractIcpTemplate implements RemoteGetICP{

    public String getICPbyURL() throws Exception{
        String body = getRemoteBody();
        if(StringUtils.isNotEmpty(body)){
            if(body.contains(Common.ICP_PREFIX)){
                int index = body.indexOf(Common.ICP_PREFIX);
                char[] chars = new char[body.length() - index];
                body.getChars(index,index + 20,chars,0);
                int endIndex = 0;
                for(int i = 0; i< chars.length;i++){
                    if(getEndIndex(chars[i])){
                        endIndex = i;
                        break;
                    }
                }
                return new String(chars,0,endIndex);
            }
        }
        return null;
    }

    protected abstract boolean getEndIndex(char source);

    protected abstract String getRemoteBody();

}
