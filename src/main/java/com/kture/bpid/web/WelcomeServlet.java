package com.kture.bpid.web;

import com.kture.bpid.RSAChiper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mariia_Lukianets on 18.03.14.
 */
public class WelcomeServlet extends HttpServlet {
    private static String INDEX_JSP = "/index.jsp";
    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                                    IOException {
        ServletContext context = getServletContext();
        RSAChiper chiper = (RSAChiper) context.getAttribute("RSAChiper");

        String publicKey = chiper.getPublicKey().toString();
        request.setAttribute("publicKey", publicKey);
        context.getRequestDispatcher(INDEX_JSP).forward(request, response);
    }

    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();
        RSAChiper chiper = (RSAChiper) context.getAttribute("RSAChiper");
        String encodedText = request.getParameter("text");

        String decodedText = chiper.decodeText(encodedText);
        response.getWriter().write("Decoded text : " + decodedText);
    }
}
