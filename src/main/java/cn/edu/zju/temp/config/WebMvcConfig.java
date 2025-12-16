package cn.edu.zju.temp.config;

import cn.edu.zju.temp.config.formatters.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 功能说明：处理跨域配置的过滤器.
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }


    /**
     * 通用拦截器排除swagger设置，所有拦截器都会自动加swagger相关的资源排除信息
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // swagger拦截器
        SwaggerConfiguration.excludeSwaggerPath(registry);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.getDefault());
        registry.addFormatterForFieldType(LocalDate.class, new LocalDateFormatter(localDateFormatter));
    }
}
