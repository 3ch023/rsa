package com.kture.bpid.web;

import com.kture.bpid.DHExchangeUtil;

import javax.crypto.spec.DHParameterSpec;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Mariia_Lukianets on 01.04.14.
 */
public class LoginServlet extends HttpServlet {
    private static String LOGIN_JSP = "/rsa/login.jsp";
    private static String WAIT_JSP = "/rsa/login.jsp";
    private static String ERROR_JSP = "/error.jsp";
    private static String MESSAGE_JSP = "/message.jsp";

    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        ServletContext context = getServletContext();
        Integer count = (Integer) context.getAttribute("count");
        switch (count) {
            case 2:
                response.getWriter().write("Пётр 3 по жизни ((");
                break;
            default:
                response.sendRedirect(LOGIN_JSP);
        }

    }

    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();

        DHExchangeUtil dhUtil = (DHExchangeUtil) context.getAttribute("DHUtil");
        Integer count = (Integer) context.getAttribute("count");
        DHParameterSpec dhParameterSpec = (DHParameterSpec) context.getAttribute("dhParameterSpec");
        byte[] publicKey;

        try {

        switch (count) {
            case 0:
                context.setAttribute("count", ++count);
                KeyPair aliceKpair = dhUtil.getAliceKpair(dhParameterSpec);
                publicKey = aliceKpair.getPublic().getEncoded();
                request.setAttribute("publicKey", new String(publicKey, "UTF-8"));
                request.setAttribute("user", "Alice");
                context.getRequestDispatcher(MESSAGE_JSP).forward(request, response);
                break;
            case 1:
                context.setAttribute("count", ++count);
                KeyPair bobKpair = dhUtil.getBobKpair(dhParameterSpec);
                publicKey = bobKpair.getPublic().getEncoded();
                dhUtil.genSecret();
                request.setAttribute("publicKey", new String(publicKey, "UTF-8"));
                request.setAttribute("user", "Bob");
                context.getRequestDispatcher(MESSAGE_JSP).forward(request, response);
                break;
            default:
                response.sendRedirect(ERROR_JSP);
        }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
