package com.ef.util;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.MapPropertySource;
import io.micronaut.context.env.PropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ContextUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextUtil.class);

    public static ApplicationContext getCtx(String dotenvFileName, String env) throws IOException {
        File dotenvFile = dotenvFileName != null ? new File(dotenvFileName) : null;
        Properties properties = new Properties();
        if (dotenvFile != null) {
            if (dotenvFile.exists()) {
                try (FileReader reader = new FileReader(dotenvFile)) {
                    properties.load(reader);
                }
            } else {
                LOGGER.warn("Unable to find {}. You might want to copy .env-sample into your {} to configure the " +
                                "database properties",
                        dotenvFile.getAbsolutePath(), dotenvFile.getAbsolutePath());
            }
        }
        PropertySource propertySource = new MapPropertySource(env, properties);
        return ApplicationContext.run(propertySource, env);
    }
}
