package org.xiangqian.microservices.common.register;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.xiangqian.microservices.common.util.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author xiangqian
 * @date 21:53 2023/09/04
 */
public class ConfigServiceImpl implements ConfigService {

    private ConsulProperties consulProperties;
    private ConsulConfigProperties consulConfigProperties;

    private String appName;
    private String profile;

    private List<String> keyPrefixes;

    public ConfigServiceImpl() {
        InputStream configBootstrapYamlInputStream = null;
        InputStream bootstrapYamlInputStream = null;
        try {
            configBootstrapYamlInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/bootstrap.yml");
            Yaml configBootstrapYaml = new Yaml(configBootstrapYamlInputStream);
            consulProperties = new ConsulProperties();
            String host = configBootstrapYaml.getString("spring.cloud.consul.host");
            consulProperties.setHost(host);
            int port = configBootstrapYaml.getInt("spring.cloud.consul.port").intValue();
            consulProperties.setPort(port);
            consulConfigProperties = new ConsulConfigProperties();
            String prefix = configBootstrapYaml.getString("spring.cloud.consul.config.prefix");
            consulConfigProperties.setPrefix(prefix);
            String dataKey = configBootstrapYaml.getString("spring.cloud.consul.config.data-key");
            consulConfigProperties.setDataKey(dataKey);
            String defaultContext = configBootstrapYaml.getString("spring.cloud.consul.config.default-context");
            consulConfigProperties.setDefaultContext(defaultContext);

            bootstrapYamlInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bootstrap.yml");
            Yaml bootstrapYaml = new Yaml(bootstrapYamlInputStream);
            appName = bootstrapYaml.getString("spring.application.name");
            profile = bootstrapYaml.getString("spring.profiles.active");

            keyPrefixes = List.of(String.format("%s/%s,%s/", prefix, appName, profile),
                    String.format("%s/%s/", prefix, appName),
                    String.format("%s/%s,%s/", prefix, defaultContext, profile),
                    String.format("%s/%s/", prefix, defaultContext, profile));
        } finally {
            IOUtils.closeQuietly(configBootstrapYamlInputStream, bootstrapYamlInputStream);
        }
    }

    @Override
    public List<Config> gets() {
        Supplier<ConsulRawClient.Builder> consulRawClientBuilderSupplier = org.springframework.cloud.consul.ConsulAutoConfiguration.createConsulRawClientBuilder();
        ConsulClient consulClient = ConsulAutoConfiguration.createConsulClient(consulProperties, consulRawClientBuilderSupplier);
        String token = consulConfigProperties.getAclToken();
        String dataKey = consulConfigProperties.getDataKey();
        QueryParams queryParams = QueryParams.DEFAULT;
        List<Config> list = new ArrayList<>(keyPrefixes.size());
        for (String keyPrefix : keyPrefixes) {
            Response<List<GetValue>> response = consulClient.getKVValues(keyPrefix, token, queryParams);
            List<GetValue> values = response.getValue();
            if (CollectionUtils.isNotEmpty(values)) {
                for (GetValue value : values) {
                    String key = value.getKey();
                    if (key.endsWith(dataKey)) {
                        list.add(new Config(key, StringUtils.trim(value.getDecodedValue())));
                    }
                }
            }
        }
        return list;
    }

}
