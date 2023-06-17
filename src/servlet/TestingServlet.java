package servlet;

import acc.Acc;
import auth.Auth;
import auth.Authenticator;
import exc.AuthenticationError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestingServlet extends HttpServlet {

    private Auth auth;
    private Logger logger;
    private static int counter = 0;

    @Override
    public void init() {
        auth = Authenticator.getInstance();
        logger = Logger.getLogger(ManageUsersServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);
            logger.log(Level.INFO, "User '" + authUser.getAccountName() + "' is trying to manage users");

            request.setAttribute("counter", counter);
            request.getRequestDispatcher("/WEB-INF/test.jsp").forward(request, response);
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username and/or password");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
        }
        catch (ExpiredJwtException e){
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid.");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (SignatureException e){
            logger.log(Level.WARNING, "JWT has been tampered with or is invalid");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid.");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            auth.checkAuthenticatedRequest(request, response);
            counter++;
            doGet(request, response);
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
    }
}

