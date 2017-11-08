package com.sven.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.sven.model.Region;



@Controller
public class RegionResourceController
{

    @Value("${region.resource.location.base}")
    private String regionResourceLocationBase;

    @Autowired
    private Region region;

    @Autowired
    private ResourceLoader resourceLoader;

    private Resource location;
    private PathResourceResolver resourceResolver;

    @PostConstruct
    private void init()
    {
        location = resourceLoader.getResource(regionResourceLocationBase);
        resourceResolver = new PathResourceResolver();
    }

    @GetMapping("/region-images/{requestPath:.+}")
    public @ResponseBody Resource getRegionResource(@PathVariable final String requestPath,
            final HttpServletRequest request)
    {

        List<Resource> locations = new ArrayList<>();
        
        String language = region.getLocale() == null ? "" :region.getLocale().toString();
        
        locations.add(
                resourceLoader.getResource(regionResourceLocationBase + language + "/"));
        locations.add(location);
        return resourceResolver.resolveResource(request, requestPath, locations, null);
    }
}
