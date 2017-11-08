package com.sven.model;

import java.util.Locale;

import javax.annotation.Generated;

public class Region
{

    private Locale locale;

    @Generated("SparkTools")
    private Region(final Builder builder)
    {
        this.locale = builder.locale;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(final Locale locale)
    {
        this.locale = locale;
    }

    /**
     * Creates builder to build {@link Region}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link Region}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private Locale locale;

        private Builder()
        {
        }

        public Builder withLocale(final Locale locale)
        {
            this.locale = locale;
            return this;
        }

        public Region build()
        {
            return new Region(this);
        }
    }
    
}
