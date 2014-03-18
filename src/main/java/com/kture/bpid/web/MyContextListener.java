package com.kture.bpid.web;

import com.kture.bpid.RSAChiper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Mariia_Lukianets on 18.03.14.
 */
public class MyContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        RSAChiper chiper = new RSAChiper();
        //TODO
        String publicKey = chiper.getPublicKey().toString();
        String privateKey = chiper.getPrivateKey().toString();

        context.setAttribute("publicKey", publicKey);
        context.setAttribute("privateKey", privateKey);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
