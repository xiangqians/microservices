package org.microservices.monitor.support;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.notify.filter.web.NotificationFilterController;
import de.codecentric.boot.admin.server.ui.config.AdminServerUiAutoConfiguration;
import de.codecentric.boot.admin.server.ui.config.AdminServerUiProperties;
import de.codecentric.boot.admin.server.ui.extensions.UiExtensions;
import de.codecentric.boot.admin.server.ui.extensions.UiRoutesScanner;
import de.codecentric.boot.admin.server.ui.web.UiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通过代码自动识别public-url
 * {@link AdminServerUiProperties#getPublicUrl()}
 * {@link UiController#getBaseUrl(org.springframework.web.util.UriComponentsBuilder)}
 * {@link UiController#UiController(java.lang.String, de.codecentric.boot.admin.server.ui.extensions.UiExtensions, de.codecentric.boot.admin.server.ui.web.UiController.Settings)}
 * {@link AdminServerUiAutoConfiguration#homeUiController(de.codecentric.boot.admin.server.ui.extensions.UiExtensions)}
 * <p>
 * see {@link UiController}
 *
 * @author xiangqian
 * @date 16:45 2022/04/06
 */
@Configuration
public class MonitorUiControllerSupport {

    private static final List<String> DEFAULT_UI_ROUTES = Arrays.asList("/about/**",
            "/applications/**",
            "/instances/**",
            "/journal/**",
            "/wallboard/**",
            "/external/**");

    @Value("${spring.application.name}")
    private String name;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public UiController homeUiController(ApplicationContext applicationContext,
                                         UiExtensions uiExtensions,
                                         AdminServerUiProperties adminUi,
                                         AdminServerProperties adminServer) throws IOException {
        List<String> extensionRoutes = (new UiRoutesScanner(applicationContext)).scan(adminUi.getExtensionResourceLocations());
        List<String> routes = Stream.concat(DEFAULT_UI_ROUTES.stream(), extensionRoutes.stream()).collect(Collectors.toList());
        UiController.Settings uiSettings = UiController.Settings.builder().brand(adminUi.getBrand()).title(adminUi.getTitle()).loginIcon(adminUi.getLoginIcon()).favicon(adminUi.getFavicon()).faviconDanger(adminUi.getFaviconDanger()).notificationFilterEnabled(!applicationContext.getBeansOfType(NotificationFilterController.class).isEmpty()).routes(routes).rememberMeEnabled(adminUi.isRememberMeEnabled()).availableLanguages(adminUi.getAvailableLanguages()).externalViews(adminUi.getExternalViews()).pollTimer(adminUi.getPollTimer()).viewSettings(adminUi.getViewSettings()).build();
        String publicUrl = adminUi.getPublicUrl() != null ? adminUi.getPublicUrl() : adminServer.getContextPath();
        return new MonitorUiController(publicUrl, uiExtensions, uiSettings);
    }

}
