package com.sven.component;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import com.sven.model.Region;

/**
 * Region template resolver looks for resource based on user's region.
 * <P>
 * with this template structure: template |- en-gb | |- a.html # if requests a.html from
 * <i>en-gb</i> region |- a.html # if requests a.html from non <i>en-gb</i> region |-
 * b.html # if requests b.html from any region
 * 
 * @author KWan
 *
 */
@Component
public class RegionTemplateResolver extends SpringResourceTemplateResolver
{

    @Autowired
    private Region region;

    @Autowired
    private ThymeleafProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init()
    {

        this.setApplicationContext(applicationContext);
        this.setPrefix(properties.getPrefix());
        this.setSuffix(properties.getSuffix());
        this.setTemplateMode(properties.getMode());
        if (properties.getEncoding() != null)
        {
            this.setCharacterEncoding(properties.getEncoding().name());
        }
        this.setCacheable(properties.isCache());
        this.setCheckExistence(true);
    }

    @Override
    protected ITemplateResource computeTemplateResource(
            final IEngineConfiguration configuration,
            final String ownerTemplate, final String template, final String resourceName,
            final String characterEncoding,
            final Map<String, Object> templateResolutionAttributes)
    {

        String language = region.getLocale().toString();

        String parent = resourceName.substring(0, resourceName.lastIndexOf("/"));
        String fileName = resourceName.substring(resourceName.lastIndexOf("/"));

        String regionResourceName =
                parent + "/" + language + "/" + fileName;

        return super.computeTemplateResource(configuration, ownerTemplate, template,
                regionResourceName,
                characterEncoding, templateResolutionAttributes);
    }
}
