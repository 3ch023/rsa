package com.kture.bpid.web;

import com.kture.bpid.DHExchangeUtil;
import com.kture.bpid.RSAChiper;

import javax.crypto.spec.DHParameterSpec;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mariia_Lukianets on 18.03.14.
 */
public class MyContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        RSAChiper chiper = new RSAChiper();
        DHExchangeUtil dhUtil = new DHExchangeUtil();
        DHParameterSpec dhParameterSpec = null;
        try {
            dhParameterSpec = dhUtil.initDHParams();
            context.setAttribute("dhParameterSpec", dhParameterSpec);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        Map<String, ArrayList<String>> messages = new HashMap<String, ArrayList<String>>();
        context.setAttribute("RSAChiper", chiper);
        context.setAttribute("DHUtil", dhUtil);
        context.setAttribute("count", 0);
        context.setAttribute("messages", messages);
        context.setAttribute("log", "");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
