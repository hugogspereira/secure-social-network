package servlet;

import acc.Acc;
import auth.Auth;
import auth.Authenticator;
import exc.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DeleteAccount", urlPatterns = {"/DeleteAccount"})
public class DeleteAccServlet extends HttpServlet {

    private Auth auth;
    private Logger logger;

    @Override
    public void init() {
        auth = Authenticator.getInstance();
        logger = Logger.getLogger(DeleteAccServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);

            // Only allow root user to delete accounts
            if (!authUser.getAccountName().equals("root")) {
                logger.log(Level.WARNING, "User '" + authUser.getAccountName() + "'does not have permission to delete accounts");
                request.setAttribute("errorMessage", "Do not have permission to delete accounts");
                response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
                return;
            }
            request.getRequestDispatcher("/WEB-INF/deleteAcc.jsp").forward(request, response);
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
            request.getRequestDispatcher("/WEB-INF/deleteAcc.jsp").forward(request, response);
        }
        catch (ExpiredJwtException e){
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (SignatureException e){
            logger.log(Level.WARNING, "JWT has been tampered with or is invalid");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            auth.checkAuthenticatedRequest(request, response);
            String username = request.getParameter("username");

            auth.deleteAccount(username);
            logger.log(Level.INFO, "Account deleted: '" + username + "'");

            // Redirect to home page after successful account deletion
            response.sendRedirect(request.getContextPath() + "/ManageUsers");

        }
        catch (AccountIsNotLocked e) {
            logger.log(Level.WARNING, "The account is not locked");
            request.setAttribute("errorMessage", "The account is not locked");
            request.getRequestDispatcher("/WEB-INF/deleteAcc.jsp").forward(request, response);
        }
        catch (AccountIsLoggedIn e) {
            logger.log(Level.WARNING, "The account is logged in");
            request.setAttribute("errorMessage", "The account is logged in");
            request.getRequestDispatcher("/WEB-INF/deleteAcc.jsp").forward(request, response);
        }
        catch (AccountDoesNotExist e) {
            logger.log(Level.WARNING, "The account does not exist");
            request.setAttribute("errorMessage", "The account does not exist");
            request.getRequestDispatcher("/WEB-INF/deleteAcc.jsp").forward(request, response);
        }
        catch (ExpiredJwtException e){
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (SignatureException e){
            logger.log(Level.WARNING, "JWT has been tampered with or is invalid");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
    }
}