package org.xiangqian.microservices.common.code.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xiangqian.microservices.common.model.Entity;
import org.xiangqian.microservices.common.register.Config;
import org.xiangqian.microservices.common.register.ConfigService;
import org.xiangqian.microservices.common.register.ConfigServiceFactory;
import org.xiangqian.microservices.common.util.Assert;
import org.xiangqian.microservices.common.util.Yaml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MyBatis代码生成器
 * <p>
 * 基于mybatis v3.5.3.1
 *
 * @author xiangqian
 * @date 19:52 2023/05/19
 */
public class MyBatisCodeGenerator {
    // 基础包
    private final String basePkg;

    // 模块名
    private final String moduleName;

    // 作者
    private final String author;

    // 数据表名
    private final String[] tables;

    // 输出目录
    private final String outputDir;

    // 自动生成器
    private AutoGenerator generator;

    // 模板引擎
    private AbstractTemplateEngine templateEngine;

    /**
     * 构造器
     *
     * @param basePkg    基础包
     * @param moduleName 模块名
     * @param author     作者
     * @param tables     数据表名
     * @param outputDir  输出目录
     */
    private MyBatisCodeGenerator(String basePkg, String moduleName, String author, String[] tables, String outputDir) {
        this.basePkg = basePkg;
        this.moduleName = moduleName;
        this.author = author;
        this.tables = tables;
        this.outputDir = outputDir;
    }

    @SneakyThrows
    public final void execute() {
        // 初始化自动生成器
        initGenerator();

        // 初始化模板引擎
        initTemplateEngine();

        // 执行
        generator.execute(templateEngine);

        // 打印输出目录
        System.out.format("输出目录 %s", outputDir).println();
    }

    /**
     * 初始化自动生成器
     */
    private void initGenerator() {
        // 获取数据源配置
        DataSourceConfig dataSourceConfig = getDataSourceConfig();

        // 自动生成器
        generator = new AutoGenerator(dataSourceConfig);

        // 全局配置
        generator.global(new GlobalConfig.Builder()
                .author(author)
                .enableSwagger()
                .disableOpenDir()
                .outputDir(Paths.get(outputDir, "java").toFile().getAbsolutePath())
                .dateType(DateType.TIME_PACK)
                .commentDate("HH:mm yyyy/MM/dd")
                .build());

        // 包配置
        generator.packageInfo(new PackageConfig.Builder()
                .parent(basePkg)
                .moduleName(moduleName)
                .entity("entity")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .controller("controller")
                .pathInfo(Map.of(OutputFile.xml, Paths.get(outputDir, "resources", "mybatis", "mapper").toFile().getAbsolutePath()))
                .build());

        // 策略配置
        generator.strategy(new StrategyConfig.Builder()
                .addInclude(tables)

                // entity构建
                .entityBuilder()
                .superClass(Entity.class)
                .enableLombok()
                .logicDeleteColumnName("del")
                .logicDeletePropertyName("del")
                .formatFileName("%sEntity")
                .enableFileOverride()

                // mapper构建
                .mapperBuilder()
                .enableMapperAnnotation()
                .enableBaseResultMap()
                .enableBaseColumnList()
                .enableFileOverride()

                // service构建
                .serviceBuilder()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")
                .enableFileOverride()

                // controller构建
                .controllerBuilder()
                .enableRestStyle()
                .enableHyphenStyle()
                .enableFileOverride()
                .build());

        generator.injection(new InjectionConfig.Builder().build());
        generator.template(new TemplateConfig.Builder().build());
    }

    /**
     * 初始化模板引擎
     */
    private void initTemplateEngine() {
        templateEngine = new VelocityTemplateEngine() {
            @Override
            public String templateFilePath(String filePath) {
                return "/mybatis" + super.templateFilePath(filePath);
            }
        };
    }

    /**
     * 获取数据源配置
     *
     * @return
     */
    @SneakyThrows
    private DataSourceConfig getDataSourceConfig() {
        ConfigService configService = ConfigServiceFactory.get();
        List<Config> configs = configService.gets();
        String driverClassName = null;
        String url = null;
        String username = null;
        String password = null;
        for (Config config : configs) {
            String content = config.getContent();
            if (StringUtils.isEmpty(content)) {
                continue;
            }

            Yaml yaml = new Yaml(content);
            driverClassName = yaml.getString("spring.datasource.driver-class-name");
            if (StringUtils.isNotEmpty(driverClassName)) {
                url = yaml.getString("spring.datasource.url");
                username = yaml.getString("spring.datasource.username");
                password = yaml.getString("spring.datasource.password");
                break;
            }
        }

        System.out.format("数据源\n\tdriverClassName: %s" +
                        "\n\turl:\t\t\t %s" +
                        "\n\tusername:\t\t %s" +
                        "\n\tpassword:\t\t %s",
                driverClassName, url, username, "******").println();

        return new DataSourceConfig.Builder(url, username, password)
                .dbQuery(new MySqlQuery())
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String author;
        private String[] tables;

        private Builder() {
        }

        /**
         * 设置作者
         *
         * @param author
         * @return
         */
        public Builder author(String author) {
            this.author = StringUtils.trim(author);
            return this;
        }

        /**
         * 设置数据表名
         *
         * @param tables
         * @return
         */
        public Builder tables(String... tables) {
            this.tables = Arrays.stream(tables)
                    .map(StringUtils::trim)
                    .filter(StringUtils::isNotEmpty)
                    .toArray(String[]::new);
            return this;
        }

        public MyBatisCodeGenerator build() {
            Assert.notNull(author, "author cannot be empty");
            Assert.isTrue(ArrayUtils.isNotEmpty(tables), "tables cannot be empty");

            // 基础包
            String basePkg = "org.xiangqian.microservices";

            // 模块名
            String moduleName = null;
            InputStream inputStream = null;
            try {
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bootstrap.yml");
                Yaml yaml = new Yaml(inputStream);
                moduleName = yaml.getString("spring.application.name").replace("-", ".");
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

            // 输出目录
            URL url = Thread.currentThread().getContextClassLoader().getResource("");
            String path = new File(url.getPath().substring(1)).getParentFile().getPath();
            File file = Paths.get(path, "generated-sources").toFile();
            if (!file.exists()) {
                file.mkdirs();
            }
            String outputDir = file.getPath();

            return new MyBatisCodeGenerator(basePkg, moduleName, author, tables, outputDir);
        }
    }

}
