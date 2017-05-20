package com.mianbao.enums;

import lombok.ToString;

/**
 * Created by zoujiajian on 2017-3-21.
 */

@ToString
public enum Response {

    REGION_FAIL(100001,"注册失败"),
    REGION_FAIL_USER_NAME(100002,"注册失败 该用户名已经被注册"),
    LOGIN_FAIL(1000023,"登录失败，用户名或者密码错误"),
    UPDATE_USER_INFO_FAIL(100004,"更新用户信息失败"),
    UPDATE_PICTURE(100005,"更新用户照片失败"),
    GET_USER_INFO(100006,"获取用户信息失败"),
    RELEASE_DYNAMIC_FAIL(100007,"发布动态消息失败"),
    USER_NOT_CONTAINS(100008,"用户不存在"),
    DYNAMIC_NOT_CONTAINS(100009,"动态不存在"),
    LIKE_DYNAMIC_FAIL(100010,"点赞动态失败"),
    UPLOAD_PICTURE_FAIL(100011,"图片上传失败"),
    UPLOAD_PICTURE_SUCCESS(100012,"图片上传成功"),
    USER_LOGIN_TIMEOUT(100013,"用户过期"),
    ADD_SCENIC_SPOT_FAIL(100014,"新增景点失败"),
    USER_NOT_LOGIN(100015,"用户尚未登录"),
    SCENIC_SPOT_NOT_CONTAINS(100016,"景点不存在"),
    SCENIC_SPOT_COLLECTION(100017,"景点收藏失败"),
    SCENIC_SPOT_CONTAINS_COLLECTION(100018,"景点已经收藏"),
    SELECT_SCENIC_SPOT_LIST_FAIL(100019,"查询收藏列表失败"),
    SEARCH_SCENIC_SPOT_FAIL(100020,"搜索景点失败"),
    REVOKE_COLLECTION_FAIL(100021,"取消收藏失败"),
    SELECT_SCENIC_SPOT_INFO_FAIL(100022,"查询景点信息失败"),
    COMMENT_DYNAMIC_FAIL(100023,"动态评论失败"),
    SCORE_FAIL(100024,"景点评价失败!"),
    SCORE_SUCCESS(100025,"景点评价成功!"),
    SELECT_SCORE_LIST_FAIL(100026,"获取景点评价列表失败"),
    SELECT_SCORE_LIST_SUCCESS(100027,"获取景点评价列表成功"),
    BAD_WORD(100028,"内容包含敏感词");

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
