package cn.letterme.tools.patcher.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

/**
 * JSON工具类
 */
public final class JsonUtil
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(JsonUtil.class);

    /**
     * 转换器
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil()
    {
    }

    /**
     * JSON字符串转为Bean对象
     * @param json JSON字符串
     * @param clazz 对象Class
     * @param <T> 泛型
     * @return Bean
     */
    public static <T> T jsonToBean(String json, Class<T> clazz)
    {
        if (Objects.isNull(json) || json.trim().isEmpty())
        {
            LOG.error("JSON is null or empty.");
            return null;
        }

        try
        {
            return MAPPER.readValue(json, clazz);
        }
        catch (IOException e)
        {
            LOG.error("JSON to bean failed.", e);
        }

        return null;
    }

    /**
     * Bean转换为JSON字符串
     * @param object Bean
     * @return JSON字符串
     */
    public static String beanToJson(Object object)
    {
        if (Objects.isNull(object))
        {
            LOG.error("Object is null.");
            return null;
        }

        try
        {
            return MAPPER.writeValueAsString(object);
        }
        catch (IOException e)
        {
            LOG.error("Bean to JSON failed.", e);
        }
        return null;
    }
}
