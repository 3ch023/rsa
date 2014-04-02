package com.kture.bpid.web;

import com.kture.bpid.DHExchangeUtil;

import javax.crypto.spec.DHParameterSpec;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mariia_Lukianets on 01.04.14.
 */
public class ChatServlet extends HttpServlet {
    private static String MESSAGE_JSP = "/message.jsp";

    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        ServletContext context = getServletContext();
        String log = (String) context.getAttribute("log");
        Map<String, ArrayList<String>> messages = (Map<String, ArrayList<String>>) context.getAttribute("messages");
        Integer count = (Integer) context.getAttribute("count");
        String user = request.getParameter("user");
        DHExchangeUtil dhUtil = (DHExchangeUtil) context.getAttribute("DHUtil");

        byte[] publicKey;

        try{
        switch (count) {
            case 0:
                response.getWriter().write("Log in!");
                break;
            case 1:
                response.getWriter().write("Wait for opponent, please!");
                break;
            default:
                for(Map.Entry<String, ArrayList<String>> entry : messages.entrySet()) {
                    String author = entry.getKey();
                    ArrayList<String> mTexts = entry.getValue();
                    if((mTexts!=null) && (!mTexts.isEmpty())) {
                        for(String message : entry.getValue()) {
                            String s;
                            byte[] ciphertext = message.getBytes();
                            if(user.equalsIgnoreCase("Alice")) {
                                KeyPair aliceKpair = dhUtil.getAliceKpair();
                                publicKey = aliceKpair.getPublic().getEncoded();
                                request.setAttribute("publicKey", new String(publicKey, "UTF-8"));

                                s = dhUtil.aliceChiperDecrypt(ciphertext);
                            }
                            else if(user.equalsIgnoreCase("Bob")) {
                                KeyPair bobKpair = dhUtil.getBobKpair();
                                publicKey = bobKpair.getPublic().getEncoded();
                                request.setAttribute("publicKey", new String(publicKey, "UTF-8"));

                                s = dhUtil.bobChiperDecrypt(ciphertext);
                            }
                            else {
                                throw new Exception("No user");
                            }
                            log += author + ": " + s + "</br>";
                        }
                    }
                }
                context.setAttribute("log", log);
                request.setAttribute("log", log);
                messages = new HashMap<String, ArrayList<String>>();
                context.setAttribute("messages", messages);
                context.getRequestDispatcher(MESSAGE_JSP).forward(request, response);
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();
        Integer count = (Integer) context.getAttribute("count");
        DHExchangeUtil dhUtil = (DHExchangeUtil) context.getAttribute("DHUtil");
        Map<String, ArrayList<String>> messages = (Map<String, ArrayList<String>>) context.getAttribute("messages");


        String text = request.getParameter("text");
        String user = request.getParameter("user");
        String publicKey = request.getParameter("publicKey");

        System.out.println(user.toUpperCase() + ": sends message " + text);
        System.out.println("PublicKey " + publicKey);
        if(count == 2) {
            try {
                //System.out.println("Clear text: " + text);
                byte[] ciphertext;

                if(user.equalsIgnoreCase("Alice")) {
                    ciphertext = dhUtil.bobChiperEncrypt(text);
                }
                else if(user.equalsIgnoreCase("Bob")) {
                    ciphertext = dhUtil.aliceChiperEncrypt(text);
                }
                else {
                    throw new Exception("No user");
                }

                String s = new String(ciphertext);

                ArrayList<String> list = messages.get(user);
                if(list == null) {
                    list = new ArrayList<String>();
                }

                //list.add(text);
                list.add(new String(ciphertext));
                messages.put(user, list);
                request.setAttribute("user", user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        doGet(request, response);
        //context.getRequestDispatcher(MESSAGE_JSP).forward(request, response);
    }

}
