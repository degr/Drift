package org.forweb.drift;


import org.forweb.database.HibernateSupport;
import org.forweb.drift.filter.CacheFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

public class AppInitializer extends AbstractSecurityWebApplicationInitializer {


    static final String BASE_PACKAGE = "org.forweb.drift";
    static final String WORD_PACKAGE = "org.forweb.word";
    public static String ROOT;
    public static Boolean DEV = true;


    @Override
    public void afterSpringSecurityFilterChain(ServletContext servletContext) {
        super.afterSpringSecurityFilterChain(servletContext);

        if (DEV) {
            HibernateSupport.init("127.0.0.1:3306", "root", "admin", "commandos", new String[]{BASE_PACKAGE + ".entity", WORD_PACKAGE});
        } else {
            HibernateSupport.init("localhost", "root", "aU2nYwYV", "commandos", new String[]{BASE_PACKAGE + ".entity", WORD_PACKAGE});
        }
        HibernateSupport.setDebug(DEV);

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(SpringConfiguration.class);
        ROOT = servletContext.getRealPath("").trim();
        addServlet(new DispatcherServlet(rootContext), "dispatcher", "/server/*", servletContext);
        servletContext.addListener(new ContextLoaderListener(rootContext));
        servletContext.addListener(new RequestContextListener());
        if(DEV) {
            FilterRegistration.Dynamic cacheFilter = servletContext.addFilter("cache", CacheFilter.class);
            cacheFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        }

        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        //addFilter("UrlRewriteFilter", new UrlRewriteFilter(), servletContext);
    }


    //
    private void addServlet(Servlet servlet, String servletName, String mapping, ServletContext container) {
        ServletRegistration.Dynamic dynamic = container.addServlet(servletName, servlet);
        dynamic.setLoadOnStartup(1);
        dynamic.addMapping(mapping);
    }

}