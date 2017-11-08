package com.sven.component;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;
import org.thymeleaf.util.Validate;

import com.sven.model.Region;
import com.sven.utils.LangCodeLocaleUtils;

public class RegionMessageResolver extends AbstractMessageResolver
        implements MessageSourceAware
{
    private static final Logger logger =
            LoggerFactory.getLogger(RegionMessageResolver.class);

    private StandardMessageResolver standardMessageResolver;
    private MessageSource messageSource;

    @Autowired
    private Region region;

    @PostConstruct
    public void init()
    {
        this.standardMessageResolver = new StandardMessageResolver();
    }

    /**
     * resolve locale
     * 
     * @param context
     * @param key
     * @param isRegionMessage
     * @return
     */
    protected Locale resolveLocale(final ITemplateContext context, final String key,
            final boolean isRegionMessage)
    {

        Locale locale = context.getLocale();

        if (context instanceof WebEngineContext)
        {

            // use locale from region by default
            locale = region.getLocale();
            if (!isRegionMessage)
            {
                WebEngineContext weContext = (WebEngineContext) context;
                // handle model attribute 'langcode'
                String langCode = (String) weContext.getVariable(
                        "langcode");
                if (!StringUtils.isEmpty(langCode))
                {
                    try
                    {
                        locale = LangCodeLocaleUtils.toLocale(langCode);
                    }
                    catch (IllegalArgumentException e)
                    {
                        // do nothing here, use default locale
                    }
                }
            }
        }
        return locale;
    }

    @Override
    public final String resolveMessage(
            final ITemplateContext context, final Class<?> origin, String key,
            final Object[] messageParameters)
    {
        boolean isRegionMessage = true;
        if (key.startsWith("C:"))
        {
            key = key.substring("C:".length());
            isRegionMessage = false;
        }

        Locale locale = this.resolveLocale(context, key, isRegionMessage);

        /**
         * 
         * ************ The codes behind this line come from Thymeleaf
         * SpringMessageResolver
         * 
         * ************
         */

        Validate.notNull(locale, "Locale in context cannot be null");
        Validate.notNull(key, "Message key cannot be null");

        /*
         * FIRST STEP: Look for the message using template-based resolution
         */
        if (context != null)
        {

            checkMessageSourceInitialized();

            if (logger.isTraceEnabled())
            {
                logger.trace(
                        "[THYMELEAF][{}] Resolving message with key \"{}\" for template \"{}\" and locale \"{}\". "
                                +
                                "Messages will be retrieved from Spring's MessageSource infrastructure.",
                        new Object[] { TemplateEngine.threadIndex(), key,
                                context.getTemplateData().getTemplate(),
                                locale });
            }

            try
            {

                String message = this.messageSource.getMessage(key, messageParameters,
                        locale);

                return message;
            }
            catch (NoSuchMessageException e)
            {
                // Try other methods
            }

        }

        /*
         * SECOND STEP: Look for the message using origin-based resolution, delegated to
         * the StandardMessageResolver
         */
        if (origin != null)
        {
            // We will be disabling template-based resolution when delegating in order to
            // use only origin-based
            final String message =
                    this.standardMessageResolver.resolveMessage(context, origin, key,
                            messageParameters, false, true, true);
            if (message != null)
            {
                return message;
            }
        }

        /*
         * NOT FOUND, return null
         */
        return null;

    }

    /*
     * Check the message source has been set.
     */
    private void checkMessageSourceInitialized()
    {
        if (this.messageSource == null)
        {
            throw new ConfigurationException(
                    "Cannot initialize " + SpringMessageResolver.class.getSimpleName() +
                            ": MessageSource has not been set. Either define this object as "
                            +
                            "a Spring bean (which will automatically set the MessageSource) or, "
                            +
                            "if you instance it directly, set the MessageSource manually using its "
                            +
                            "corresponding setter method.");
        }
    }

    /**
     * <p>
     * Returns the message source ({@link MessageSource}) to be used for message
     * resolution.
     * </p>
     *
     * @return the message source
     */
    public final MessageSource getMessageSource()
    {
        return this.messageSource;
    }

    @Override
    public String createAbsentMessageRepresentation(
            final ITemplateContext context, final Class<?> origin, final String key,
            final Object[] messageParameters)
    {
        return this.standardMessageResolver.createAbsentMessageRepresentation(context,
                origin, key, messageParameters);
    }

    @Override
    public void setMessageSource(final MessageSource messageSource)
    {
        this.messageSource = messageSource;
        
    }

}
