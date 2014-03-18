package com.kture.bpid.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mariia_Lukianets on 18.03.14.
 */
public class WelcomeServlet extends HttpServlet {
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String text = request.getParameter("text");
        response.getWriter().write("GET/POST response : " + text);
    }
}
