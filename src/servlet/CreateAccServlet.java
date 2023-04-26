package servlet;

import acc.Acc;
import auth.Auth;
import auth.Authenticator;
import exc.*;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CreateAccount", urlPatterns = {"/CreateAccount"})
public class CreateAccServlet extends HttpServlet {

    private Auth auth;
    private Logger logger;

    @Override
    public void init() {
        auth = new Authenticator();
        logger = Logger.getLogger(CreateAccServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // TODO: "(should only work for “root”)" - does it means root needs to be authenticated?
            Acc authUser = auth.checkAuthenticatedRequest(request, response);

            // Only allow root user to create accounts
            if (!authUser.getAccountName().equals("root")) {
                request.setAttribute("errorMessage", "Do not have permission to create accounts");
                logger.log(Level.WARNING, "User does not have permission to delete accounts");
                request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
            logger.log(Level.INFO, "Root is trying to create an account");
        }
        catch (AuthenticationError e) {
            request.setAttribute("errorMessage", "Invalid username or password");
            logger.log(Level.WARNING, "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountIsLocked e) {
            request.setAttribute("errorMessage", "Your account is locked");
            logger.log(Level.WARNING, "Account is locked");
            request.getRequestDispatcher("/WEB-INF/manageUsers.jsp").forward(request, response);
        }
        catch (EncryptionDontWork e) {
            request.setAttribute("errorMessage", "Problems with encryption");
            logger.log(Level.SEVERE, "Problems with encryption");
            request.getRequestDispatcher("/WEB-INF/manageUsers.jsp").forward(request, response);
        }
        catch (AccountDoesNotExist e) {
            request.setAttribute("errorMessage", "The Root account does not exist");
            logger.log(Level.WARNING, "The account does not exist");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");

            auth.createAccount(username, password1, password2);

            logger.log(Level.INFO, "Account created");

            // Redirect to home page after successful account creation
            request.getRequestDispatcher("/WEB-INF/manageUsers.jsp").forward(request, response);
        }
        catch (EncryptionDontWork e) {
            request.setAttribute("errorMessage", "Problems with encryption");
            logger.log(Level.SEVERE, "Problems with encryption");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountAlreadyExists e) {
            request.setAttribute("errorMessage", "Username already exists");
            logger.log(Level.WARNING, "Username already exists");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
        catch (PasswordsDontMatch e) {
            request.setAttribute("errorMessage", "Passwords do not match");
            logger.log(Level.WARNING, "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
        catch (NullParameterException e) {
            request.setAttribute("errorMessage", "Please fill out all fields");
            logger.log(Level.WARNING, "Please fill out all fields");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
    }
}