package servlet;

import acc.Acc;
import auth.Auth;
import auth.Authenticator;
import exc.*;
import storage.DbAccount;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name="AuthenticateUser", urlPatterns={"/AuthenticateUser"})
public class AuthenticateUserServlet extends HttpServlet {

    private Auth auth;
    private Logger logger;

    @Override
    public void init() {
        auth =  new Authenticator();
        logger = Logger.getLogger(AuthenticateUserServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        logger.log(Level.INFO, "User is trying to authenticate");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Acc authUser = auth.authenticateUser(username, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("JWT", authUser.getJWT());
            request.getRequestDispatcher("/WEB-INF/manageUsers.jsp").forward(request, response);
            logger.log(Level.INFO, "User authenticated");
        }
        catch (AuthenticationError e) {
            request.setAttribute("errorMessage", "Invalid username or password");
            logger.log(Level.WARNING, "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountIsLocked e) {
            request.setAttribute("errorMessage", "Account is locked");
            logger.log(Level.WARNING, "Account is locked");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (EncryptionDontWork e) {
            request.setAttribute("errorMessage", "Problems with encryption");
            logger.log(Level.SEVERE, "Problems with encryption");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountDoesNotExist e) {
            request.setAttribute("errorMessage", "Account does not exist");
            logger.log(Level.WARNING, "Account does not exist");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
    }

}
