package org.springframework.cloud.consul.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.cloud.consul.config.ConsulConfigProperties.Format;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author xiangqianx (修改者) 打印配置中心文件内容
 * @date 21:02 2023/08/25
 */
@Slf4j
public class ConsulPropertySource extends EnumerablePropertySource<ConsulClient> {
    private final Map<String, Object> properties = new LinkedHashMap();
    private String context;
    private ConsulConfigProperties configProperties;
    private Long initialIndex;

    public ConsulPropertySource(String context, ConsulClient source, ConsulConfigProperties configProperties) {
        super(context, source);
        this.context = context;
        this.configProperties = configProperties;
    }

    public void init() {
        if (!context.endsWith("/")) {
            context = context + "/";
        }

        if (context.startsWith("/")) {
            context = context.substring(1);
        }

        Response<List<GetValue>> response = source.getKVValues(context, configProperties.getAclToken(), QueryParams.DEFAULT);
        initialIndex = response.getConsulIndex();
        List<GetValue> values = response.getValue();

        if (CollectionUtils.isNotEmpty(values)) {
            String dataKey = configProperties.getDataKey();
            for (GetValue value : values) {
                if (value.getKey().endsWith(dataKey)) {
                    log.info("{}\n{}", value.getKey(), value.getDecodedValue());
                }
            }
        }

        Format format = configProperties.getFormat();
        switch (format) {
            case KEY_VALUE:
                parsePropertiesInKeyValueFormat(values);
                break;
            case PROPERTIES:
            case YAML:
                parsePropertiesWithNonKeyValueFormat(values, format);
        }

    }

    public Long getInitialIndex() {
        return initialIndex;
    }

    protected void parsePropertiesInKeyValueFormat(List<GetValue> values) {
        if (values != null) {
            Iterator var2 = values.iterator();

            while (var2.hasNext()) {
                GetValue getValue = (GetValue) var2.next();
                String key = getValue.getKey();
                if (!StringUtils.endsWithIgnoreCase(key, "/")) {
                    key = key.replace(context, "").replace('/', '.');
                    String value = getValue.getDecodedValue();
                    properties.put(key, value);
                }
            }

        }
    }

    protected void parsePropertiesWithNonKeyValueFormat(List<GetValue> values, Format format) {
        if (values != null) {
            Iterator var3 = values.iterator();

            while (var3.hasNext()) {
                GetValue getValue = (GetValue) var3.next();
                String key = getValue.getKey().replace(context, "");
                if (configProperties.getDataKey().equals(key)) {
                    parseValue(getValue, format);
                }
            }

        }
    }

    protected void parseValue(GetValue getValue, Format format) {
        String value = getValue.getDecodedValue();
        if (value != null) {
            Properties props = generateProperties(value, format);
            Iterator var5 = props.entrySet().iterator();

            while (var5.hasNext()) {
                Entry entry = (Entry) var5.next();
                properties.put(entry.getKey().toString(), entry.getValue());
            }

        }
    }

    protected Properties generateProperties(String value, Format format) {
        Properties props = new Properties();
        if (format == Format.PROPERTIES) {
            try {
                props.load(new ByteArrayInputStream(value.getBytes("ISO-8859-1")));
                return props;
            } catch (IOException var5) {
                throw new IllegalArgumentException(value + " can't be encoded using ISO-8859-1");
            }
        } else if (format == Format.YAML) {
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new Resource[]{new ByteArrayResource(value.getBytes(Charset.forName("UTF-8")))});
            return yaml.getObject();
        } else {
            return props;
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getDecoded(String value) {
        return value == null ? null : new String(Base64Utils.decodeFromString(value));
    }

    protected Map<String, Object> getProperties() {
        return properties;
    }

    protected ConsulConfigProperties getConfigProperties() {
        return configProperties;
    }

    protected String getContext() {
        return context;
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public String[] getPropertyNames() {
        Set<String> strings = properties.keySet();
        return strings.toArray(new String[strings.size()]);
    }
}