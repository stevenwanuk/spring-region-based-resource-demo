package com.sven.config;

import java.util.Collection;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.sven.component.RegionMessageResolver;

@Configuration
public class ResourceMessageConfig
{
    @Bean
    public SpringTemplateEngine templateEngine(
            final Collection<ITemplateResolver> templateResolvers,
            final ObjectProvider<Collection<IDialect>> dialectsProvider,
            final MessageSource messageSource, final RegionMessageResolver messageResolver)
    {
        SpringTemplateEngine engine = new SpringTemplateEngine()
        {

            @Override
            public void afterPropertiesSet() throws Exception
            {

                /**
                 * inject our message resolver instead of SpringMessageResolver.
                 */
                messageResolver.setMessageSource(messageSource);
                super.setMessageResolver(messageResolver);
            }

        };
        templateResolvers.forEach(engine::addTemplateResolver);
        dialectsProvider.getIfAvailable().forEach(engine::addDialect);

        return engine;
    }
}
