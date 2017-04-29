package com.mianbao.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDynamicExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserDynamicExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleIsNull() {
            addCriterion("dynamic_title is null");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleIsNotNull() {
            addCriterion("dynamic_title is not null");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleEqualTo(String value) {
            addCriterion("dynamic_title =", value, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleNotEqualTo(String value) {
            addCriterion("dynamic_title <>", value, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleGreaterThan(String value) {
            addCriterion("dynamic_title >", value, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleGreaterThanOrEqualTo(String value) {
            addCriterion("dynamic_title >=", value, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleLessThan(String value) {
            addCriterion("dynamic_title <", value, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleLessThanOrEqualTo(String value) {
            addCriterion("dynamic_title <=", value, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleLike(String value) {
            addCriterion("dynamic_title like", value, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleNotLike(String value) {
            addCriterion("dynamic_title not like", value, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleIn(List<String> values) {
            addCriterion("dynamic_title in", values, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleNotIn(List<String> values) {
            addCriterion("dynamic_title not in", values, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleBetween(String value1, String value2) {
            addCriterion("dynamic_title between", value1, value2, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicTitleNotBetween(String value1, String value2) {
            addCriterion("dynamic_title not between", value1, value2, "dynamicTitle");
            return (Criteria) this;
        }

        public Criteria andDynamicContentIsNull() {
            addCriterion("dynamic_content is null");
            return (Criteria) this;
        }

        public Criteria andDynamicContentIsNotNull() {
            addCriterion("dynamic_content is not null");
            return (Criteria) this;
        }

        public Criteria andDynamicContentEqualTo(String value) {
            addCriterion("dynamic_content =", value, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentNotEqualTo(String value) {
            addCriterion("dynamic_content <>", value, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentGreaterThan(String value) {
            addCriterion("dynamic_content >", value, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentGreaterThanOrEqualTo(String value) {
            addCriterion("dynamic_content >=", value, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentLessThan(String value) {
            addCriterion("dynamic_content <", value, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentLessThanOrEqualTo(String value) {
            addCriterion("dynamic_content <=", value, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentLike(String value) {
            addCriterion("dynamic_content like", value, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentNotLike(String value) {
            addCriterion("dynamic_content not like", value, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentIn(List<String> values) {
            addCriterion("dynamic_content in", values, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentNotIn(List<String> values) {
            addCriterion("dynamic_content not in", values, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentBetween(String value1, String value2) {
            addCriterion("dynamic_content between", value1, value2, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicContentNotBetween(String value1, String value2) {
            addCriterion("dynamic_content not between", value1, value2, "dynamicContent");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureIsNull() {
            addCriterion("dynamic_picture is null");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureIsNotNull() {
            addCriterion("dynamic_picture is not null");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureEqualTo(String value) {
            addCriterion("dynamic_picture =", value, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureNotEqualTo(String value) {
            addCriterion("dynamic_picture <>", value, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureGreaterThan(String value) {
            addCriterion("dynamic_picture >", value, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureGreaterThanOrEqualTo(String value) {
            addCriterion("dynamic_picture >=", value, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureLessThan(String value) {
            addCriterion("dynamic_picture <", value, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureLessThanOrEqualTo(String value) {
            addCriterion("dynamic_picture <=", value, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureLike(String value) {
            addCriterion("dynamic_picture like", value, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureNotLike(String value) {
            addCriterion("dynamic_picture not like", value, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureIn(List<String> values) {
            addCriterion("dynamic_picture in", values, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureNotIn(List<String> values) {
            addCriterion("dynamic_picture not in", values, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureBetween(String value1, String value2) {
            addCriterion("dynamic_picture between", value1, value2, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andDynamicPictureNotBetween(String value1, String value2) {
            addCriterion("dynamic_picture not between", value1, value2, "dynamicPicture");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsIsNull() {
            addCriterion("scenic_spot_ids is null");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsIsNotNull() {
            addCriterion("scenic_spot_ids is not null");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsEqualTo(String value) {
            addCriterion("scenic_spot_ids =", value, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsNotEqualTo(String value) {
            addCriterion("scenic_spot_ids <>", value, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsGreaterThan(String value) {
            addCriterion("scenic_spot_ids >", value, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsGreaterThanOrEqualTo(String value) {
            addCriterion("scenic_spot_ids >=", value, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsLessThan(String value) {
            addCriterion("scenic_spot_ids <", value, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsLessThanOrEqualTo(String value) {
            addCriterion("scenic_spot_ids <=", value, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsLike(String value) {
            addCriterion("scenic_spot_ids like", value, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsNotLike(String value) {
            addCriterion("scenic_spot_ids not like", value, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsIn(List<String> values) {
            addCriterion("scenic_spot_ids in", values, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsNotIn(List<String> values) {
            addCriterion("scenic_spot_ids not in", values, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsBetween(String value1, String value2) {
            addCriterion("scenic_spot_ids between", value1, value2, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andScenicSpotIdsNotBetween(String value1, String value2) {
            addCriterion("scenic_spot_ids not between", value1, value2, "scenicSpotIds");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("createTime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("createTime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("createTime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createTime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("createTime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("createTime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("createTime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("createTime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("createTime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("createTime not between", value1, value2, "createtime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}