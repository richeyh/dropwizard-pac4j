package org.pac4j.dropwizard;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.pac4j.core.config.Config;
import org.pac4j.dropwizard.Pac4jFactory.ServletCallbackFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletLogoutFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletSecurityFilterConfiguration;
import org.pac4j.j2e.filter.AbstractConfigFilter;
import org.pac4j.j2e.filter.ApplicationLogoutFilter;
import org.pac4j.j2e.filter.CallbackFilter;
import org.pac4j.j2e.filter.SecurityFilter;

import io.dropwizard.setup.Environment;

/**
 * 
 * @author Evan Meagher
 * @author Victor Noel - Linagora
 * @since 1.1.0
 *
 */
public final class J2EHelper {

    private J2EHelper() {
        // utility class
    }

    public static void registerSecurityFilter(Environment environment,
            Config config, ServletSecurityFilterConfiguration fConf) {

        final SecurityFilter filter = new SecurityFilter();

        filter.setClients(fConf.getClients());
        filter.setAuthorizers(fConf.getAuthorizers());
        filter.setMatchers(fConf.getMatchers());
        filter.setMultiProfile(fConf.getMultiProfile());

        registerFilter(environment, config, filter, fConf.getMapping());
    }

    public static void registerCallbackFilter(Environment environment,
            Config config, ServletCallbackFilterConfiguration fConf) {

        final CallbackFilter filter = new CallbackFilter();

        filter.setDefaultUrl(fConf.getDefaultUrl());
        filter.setMultiProfile(fConf.getMultiProfile());
        filter.setRenewSession(fConf.getRenewSession());

        registerFilter(environment, config, filter, fConf.getMapping());
    }

    public static void registerLogoutFilter(Environment environment,
            Config config, ServletLogoutFilterConfiguration fConf) {

        final ApplicationLogoutFilter filter = new ApplicationLogoutFilter();

        filter.setDefaultUrl(fConf.getDefaultUrl());
        filter.setLogoutUrlPattern(fConf.getLogoutUrlPattern());

        registerFilter(environment, config, filter, fConf.getMapping());
    }

    private static void registerFilter(Environment environment, Config config,
            AbstractConfigFilter filter, String mapping) {

        // TODO replace that with something that does not use ConfigSingleton!
        filter.setConfig(config);

        final FilterRegistration.Dynamic filterRegistration = environment
                .servlets().addFilter(filter.getClass().getName(), filter);

        filterRegistration.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST), true, mapping);
    }
}
