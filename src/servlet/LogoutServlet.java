package servlet;

import acc.Acc;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name="Logout", urlPatterns={"/Logout"})
public class LogoutServlet extends HttpServlet {

    private Auth auth;
    private Logger logger;

    @Override
    public void init() {
        auth = Authenticator.getInstance();
        logger = Logger.getLogger(LogoutServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);
            logger.log(Level.INFO, "User '" + authUser.getAccountName() + "' is trying to logout");
            auth.logout(authUser);
            logger.log(Level.INFO, "User '" + authUser.getAccountName() + "' has logged out");
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            request.getRequestDispatcher("/WEB-INF/logout.jsp").forward(request, response);
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username or password");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
        }
        catch (AccountIsLocked e) {
            logger.log(Level.WARNING, "Account is locked");
            request.setAttribute("errorMessage", "Your account is locked");
            response.sendRedirect(request.getContextPath() + "/ManageUsers");
        }
        catch (EncryptionDontWork e) {
            logger.log(Level.SEVERE, "Problems with encryption");
            request.setAttribute("errorMessage", "Problems with encryption");
            response.sendRedirect(request.getContextPath() + "/ManageUsers");
        }
        catch (AccountDoesNotExist e) {
            logger.log(Level.WARNING, "The account does not exist");
            request.setAttribute("errorMessage", "The account does not exist");
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
        }
    }
}
