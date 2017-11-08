package com.sven.interceptor;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sven.model.Region;

public class MockRegionInterceptor extends HandlerInterceptorAdapter
{
    @Autowired
    private Region region;

    private static final Logger LOG = LoggerFactory.getLogger(MockRegionInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request,
            final HttpServletResponse response, final Object handler)
    {

        String testCountryCode = request.getParameter("testCountryCode");

        if (!StringUtils.isEmpty(testCountryCode))
        {
            LOG.info("Setting region using testCountryCode " + testCountryCode);
            if (testCountryCode.equalsIgnoreCase("br")) {
            
                region.setLocale(new Locale("pt", "BR"));
            } else {
                region.setLocale(new Locale("en", "GB"));
            }
        }

        return true;
    }
}
