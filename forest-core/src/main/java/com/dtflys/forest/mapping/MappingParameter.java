package com.dtflys.forest.mapping;

import com.dtflys.forest.filter.Filter;
import com.dtflys.forest.filter.FilterChain;

import java.util.Iterator;

/**
 * 字符串模板解析类 方法参数
 *
 * @author gongjun[dt_flys@hotmail.com]
 * @since 1.0.0
 */
public class MappingParameter {

    public final static int TARGET_UNKNOWN = 0;
    public final static int TARGET_QUERY = 1;
    public final static int TARGET_BODY = 2;
    public final static int TARGET_HEADER = 3;

    /**
     * 参数类型
     */
    protected final Class type;

    /**
     * 参数下标
     */
    protected Integer index;

    /**
     * 参数名称
     */
    protected String name;

    /**
     * 映射操作
     */
    protected String map;

    /**
     * 参数绑定的目标位置
     */
    protected int target = TARGET_UNKNOWN;

    /**
     * 是否为对象属性
     */
    private boolean objectProperties = false;

    /**
     * 是否为JSON参数
     */
    private boolean isJsonParam = false;

    /**
     * 子项Content-Type
     */
    private String partContentType;

    /**
     * 默认值
     */
    private String defaultValue;

    private String jsonParamName;

    private FilterChain filterChain = new FilterChain();

    public MappingParameter(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public boolean isUnknownTarget() {
        return target == TARGET_UNKNOWN;
    }

    public boolean isQuery() {
        return target == TARGET_QUERY;
    }

    public boolean isBody() {
        return target == TARGET_BODY;
    }

    public boolean isHeader() {
        return target == TARGET_HEADER;
    }

    public static boolean isUnknownTarget(int target) {
        return target == TARGET_UNKNOWN;
    }

    public static boolean isHeader(int target) {
        return target == TARGET_HEADER;
    }

    public static boolean isBody(int target) {
        return target == TARGET_BODY;
    }

    public static boolean isQuery(int target) {
        return target == TARGET_QUERY;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public boolean isObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(boolean objectProperties) {
        this.objectProperties = objectProperties;
    }

    public boolean isJsonParam() {
        return isJsonParam;
    }

    public void setJsonParam(boolean jsonParam) {
        isJsonParam = jsonParam;
    }

    public String getPartContentType() {
        return partContentType;
    }

    public void setPartContentType(String partContentType) {
        this.partContentType = partContentType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getJsonParamName() {
        return jsonParamName;
    }

    public void setJsonParamName(String jsonParamName) {
        this.jsonParamName = jsonParamName;
    }

    public FilterChain getFilterChain() {
        return filterChain;
    }

    public void addFilter(Filter filter) {
        filterChain.addFilter(filter);
    }
}
