package ru.tsystems.javaschool.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import ru.tsystems.javaschool.utils.CityIdToCityConverter;
import ru.tsystems.javaschool.utils.TruckConverter;

@Configuration
@EnableWebMvc // Config support web-mvc
@ComponentScan("ru.tsystems.javaschool") // Path for controllers and services
public class WebAppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    CityIdToCityConverter cityIdToCityConverter;

    @Autowired
    TruckConverter truckConverter;

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }


    /*
     * Configure ResourceHandlers to serve static resources like CSS/ Javascript etc...
     *
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    /*
     * Configure Converter to be used.
     */
    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        registry.addConverter(truckConverter);
        registry.addConverter(cityIdToCityConverter);
    }
}
