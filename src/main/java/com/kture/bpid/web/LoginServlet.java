package com.kture.bpid.web;

import com.kture.bpid.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mariia_Lukianets on 01.04.14.
 */
public class LoginServlet extends HttpServlet {
    private static String INDEX_JSP = "/login.jsp";
    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.sendRedirect(INDEX_JSP);
    }

    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();
        String name = request.getParameter("name");


        //context.setAttribute(name, user);

        //String decodedText = chiper.decodeText(encodedText);
        //response.getWriter().write("Decoded text : " + decodedText);
    }
}
