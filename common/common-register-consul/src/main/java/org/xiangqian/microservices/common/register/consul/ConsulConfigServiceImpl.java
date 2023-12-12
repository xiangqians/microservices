package org.xiangqian.microservices.common.register.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.xiangqian.microservices.common.register.Config;
import org.xiangqian.microservices.common.register.ConfigService;
import org.xiangqian.microservices.common.util.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author xiangqian
 * @date 21:53 2023/09/04
 */
public class ConsulConfigServiceImpl implements ConfigService {

    private String appName;
    private String profile;

    private ConsulProperties consulProperties;
    private ConsulConfigProperties consulConfigProperties;

    private List<String> keyPrefixes;

    public ConsulConfigServiceImpl() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Yaml configBootstrapYaml = new Yaml(classLoader.getResourceAsStream("config/bootstrap.yml"), true);
        Yaml bootstrapYaml = new Yaml(classLoader.getResourceAsStream("bootstrap.yml"), true);

        appName = getValue(bootstrapYaml, "spring.application.name");
        profile = getValue(bootstrapYaml, "spring.profiles.active");

        consulProperties = new ConsulProperties();
        consulProperties.setHost(getValue(configBootstrapYaml, "spring.cloud.consul.host"));
        consulProperties.setPort(Integer.parseInt(getValue(configBootstrapYaml, "spring.cloud.consul.port")));

        consulConfigProperties = new ConsulConfigProperties();
        String prefix = getValue(configBootstrapYaml, "spring.cloud.consul.config.prefix");
        consulConfigProperties.setPrefix(prefix);
        String dataKey = getValue(configBootstrapYaml, "spring.cloud.consul.config.data-key");
        consulConfigProperties.setDataKey(dataKey);
        String defaultContext = getValue(configBootstrapYaml, "spring.cloud.consul.config.default-context");
        consulConfigProperties.setDefaultContext(defaultContext);

        keyPrefixes = List.of(String.format("%s/%s,%s/", prefix, appName, profile),
                String.format("%s/%s/", prefix, appName),
                String.format("%s/%s,%s/", prefix, defaultContext, profile),
                String.format("%s/%s/", prefix, defaultContext, profile));
    }

    private String getValue(Yaml yaml, String key) {
        String value = yaml.getString(key);

        // eg: ${REGISTER_HOST:register}
        if (value.startsWith("${") && value.contains(":") && value.endsWith("}")) {
            int index = value.indexOf(":");
            String name = value.substring("${".length(), index);
            return Optional.ofNullable(System.getenv(name)).orElse(value.substring(index + 1, value.length() - "}".length()));
        }

        return value;
    }

    @Override
    public List<Config> gets() {
        Supplier<ConsulRawClient.Builder> consulRawClientBuilderSupplier = org.springframework.cloud.consul.ConsulAutoConfiguration.createConsulRawClientBuilder();
        ConsulClient consulClient = ConsulAutoConfiguration.createConsulClient(consulProperties, consulRawClientBuilderSupplier);
        String token = consulConfigProperties.getAclToken();
        QueryParams queryParams = QueryParams.DEFAULT;
        List<Config> list = new ArrayList<>(keyPrefixes.size());
        for (String keyPrefix : keyPrefixes) {
            Response<List<GetValue>> response = consulClient.getKVValues(keyPrefix, token, queryParams);
            List<GetValue> values = response.getValue();
            if (CollectionUtils.isNotEmpty(values)) {
                String dataKey = consulConfigProperties.getDataKey();
                for (GetValue value : values) {
                    if (value.getKey().endsWith(dataKey)) {
                        list.add(new Config(value.getKey(), Optional.ofNullable(value.getDecodedValue()).map(StringUtils::trim).orElse(null)));
                    }
                }
            }
        }
        return list;
    }

}
