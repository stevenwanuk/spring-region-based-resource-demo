package com.sven.interceptor;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sven.model.Region;
import com.sven.service.RegionService;

public class MockRegionInterceptor extends HandlerInterceptorAdapter
{
    @Autowired
    private Region region;

    @Autowired
    RegionService regionService;

    private Locale defaultLocale;

    @PostConstruct
    public void init()
    {
        defaultLocale = new Locale("en", "GB");
    }

    private static final Logger LOG = LoggerFactory.getLogger(MockRegionInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request,
            final HttpServletResponse response, final Object handler)
    {

        String testCountryCode = request.getParameter("testCountryCode");

        if (!StringUtils.isEmpty(testCountryCode))
        {
            LOG.info("Setting region using testCountryCode " + testCountryCode);

            Locale locale = regionService.getLocale(testCountryCode).orElseThrow(
                    () -> new RuntimeException(
                            String.format("%s not found", testCountryCode)));

            region.setLocale(locale);
        }
        else
        {

            region.setLocale(defaultLocale);
        }

        return true;
    }
}
