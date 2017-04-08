package com.mianbao.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScenicSpotExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ScenicSpotExample() {
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

        public Criteria andScenicSpotNameIsNull() {
            addCriterion("scenic_spot_name is null");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameIsNotNull() {
            addCriterion("scenic_spot_name is not null");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameEqualTo(String value) {
            addCriterion("scenic_spot_name =", value, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameNotEqualTo(String value) {
            addCriterion("scenic_spot_name <>", value, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameGreaterThan(String value) {
            addCriterion("scenic_spot_name >", value, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameGreaterThanOrEqualTo(String value) {
            addCriterion("scenic_spot_name >=", value, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameLessThan(String value) {
            addCriterion("scenic_spot_name <", value, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameLessThanOrEqualTo(String value) {
            addCriterion("scenic_spot_name <=", value, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameLike(String value) {
            addCriterion("scenic_spot_name like", value, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameNotLike(String value) {
            addCriterion("scenic_spot_name not like", value, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameIn(List<String> values) {
            addCriterion("scenic_spot_name in", values, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameNotIn(List<String> values) {
            addCriterion("scenic_spot_name not in", values, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameBetween(String value1, String value2) {
            addCriterion("scenic_spot_name between", value1, value2, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotNameNotBetween(String value1, String value2) {
            addCriterion("scenic_spot_name not between", value1, value2, "scenicSpotName");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoIsNull() {
            addCriterion("scenic_spot_info is null");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoIsNotNull() {
            addCriterion("scenic_spot_info is not null");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoEqualTo(String value) {
            addCriterion("scenic_spot_info =", value, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoNotEqualTo(String value) {
            addCriterion("scenic_spot_info <>", value, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoGreaterThan(String value) {
            addCriterion("scenic_spot_info >", value, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoGreaterThanOrEqualTo(String value) {
            addCriterion("scenic_spot_info >=", value, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoLessThan(String value) {
            addCriterion("scenic_spot_info <", value, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoLessThanOrEqualTo(String value) {
            addCriterion("scenic_spot_info <=", value, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoLike(String value) {
            addCriterion("scenic_spot_info like", value, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoNotLike(String value) {
            addCriterion("scenic_spot_info not like", value, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoIn(List<String> values) {
            addCriterion("scenic_spot_info in", values, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoNotIn(List<String> values) {
            addCriterion("scenic_spot_info not in", values, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoBetween(String value1, String value2) {
            addCriterion("scenic_spot_info between", value1, value2, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotInfoNotBetween(String value1, String value2) {
            addCriterion("scenic_spot_info not between", value1, value2, "scenicSpotInfo");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreIsNull() {
            addCriterion("scenic_spot_picutre is null");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreIsNotNull() {
            addCriterion("scenic_spot_picutre is not null");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreEqualTo(String value) {
            addCriterion("scenic_spot_picutre =", value, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreNotEqualTo(String value) {
            addCriterion("scenic_spot_picutre <>", value, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreGreaterThan(String value) {
            addCriterion("scenic_spot_picutre >", value, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreGreaterThanOrEqualTo(String value) {
            addCriterion("scenic_spot_picutre >=", value, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreLessThan(String value) {
            addCriterion("scenic_spot_picutre <", value, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreLessThanOrEqualTo(String value) {
            addCriterion("scenic_spot_picutre <=", value, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreLike(String value) {
            addCriterion("scenic_spot_picutre like", value, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreNotLike(String value) {
            addCriterion("scenic_spot_picutre not like", value, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreIn(List<String> values) {
            addCriterion("scenic_spot_picutre in", values, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreNotIn(List<String> values) {
            addCriterion("scenic_spot_picutre not in", values, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreBetween(String value1, String value2) {
            addCriterion("scenic_spot_picutre between", value1, value2, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicSpotPicutreNotBetween(String value1, String value2) {
            addCriterion("scenic_spot_picutre not between", value1, value2, "scenicSpotPicutre");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeIsNull() {
            addCriterion("scenic_createTime is null");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeIsNotNull() {
            addCriterion("scenic_createTime is not null");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeEqualTo(Date value) {
            addCriterion("scenic_createTime =", value, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeNotEqualTo(Date value) {
            addCriterion("scenic_createTime <>", value, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeGreaterThan(Date value) {
            addCriterion("scenic_createTime >", value, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("scenic_createTime >=", value, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeLessThan(Date value) {
            addCriterion("scenic_createTime <", value, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("scenic_createTime <=", value, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeIn(List<Date> values) {
            addCriterion("scenic_createTime in", values, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeNotIn(List<Date> values) {
            addCriterion("scenic_createTime not in", values, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeBetween(Date value1, Date value2) {
            addCriterion("scenic_createTime between", value1, value2, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andScenicCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("scenic_createTime not between", value1, value2, "scenicCreatetime");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(Integer value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(Integer value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(Integer value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(Integer value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(Integer value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<Integer> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<Integer> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(Integer value1, Integer value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(Integer value1, Integer value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
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