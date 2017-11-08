package com.sven.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class RegionService
{

    private List<Locale> mockLocales = new ArrayList<>();

    @PostConstruct
    public void init()
    {

        mockLocales.add(new Locale("en", "GB"));
        mockLocales.add(new Locale("az", "az"));
        mockLocales.add(new Locale("pt", "BR"));

    }

    public Optional<Locale> getLocale(final String countryCode)
    {

        return mockLocales.stream().filter(
                s -> s.getCountry().equalsIgnoreCase(countryCode)).findAny();

    }
}
