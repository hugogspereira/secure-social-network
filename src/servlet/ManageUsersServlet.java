package servlet;

import auth.Auth;
import auth.Authenticator;
import exc.AccountDoesNotExist;
import exc.AccountIsLocked;
import exc.AuthenticationError;
import exc.EncryptionDontWork;
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
        auth =  new Authenticator();
        logger = Logger.getLogger(ManageUsersServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            auth.checkAuthenticatedRequest(request, response);
            logger.log(Level.INFO, "User is trying to manage users");
            request.getRequestDispatcher("/WEB-INF/manageUsers.jsp").forward(request, response);
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
        }
        catch (AccountIsLocked e) {
            logger.log(Level.WARNING, "Account is locked");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
        }
        catch (EncryptionDontWork e) {
            logger.log(Level.SEVERE, "Problems with encryption");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
        }
        catch (AccountDoesNotExist e) {
            logger.log(Level.WARNING, "The account does not exist");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
        }
    }
}