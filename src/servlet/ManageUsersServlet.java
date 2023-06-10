package servlet;

import acc.Acc;
import auth.Auth;
import auth.Authenticator;
import exc.AuthenticationError;
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

@WebServlet(name="ManageUsers", urlPatterns={"/ManageUsers"})
public class ManageUsersServlet extends HttpServlet {

    private Auth auth;
    private Logger logger;

    @Override
    public void init() {
        auth = Authenticator.getInstance();
        logger = Logger.getLogger(ManageUsersServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);
            logger.log(Level.INFO, "User '" + authUser.getAccountName() + "' is trying to manage users");

            if (authUser.getAccountName().equals("root")) {
                request.getRequestDispatcher("/WEB-INF/manageUsersRoot.jsp").forward(request, response);
            }
            else {
                request.getRequestDispatcher("/WEB-INF/manageUsers.jsp").forward(request, response);
            }
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username and/or password");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
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
}