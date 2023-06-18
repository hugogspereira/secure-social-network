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

@WebServlet(name = "CreateAccount", urlPatterns = {"/CreateAccount"})
public class CreateAccServlet extends HttpServlet {

    private Auth auth;
    private Logger logger;

    @Override
    public void init() {
        auth = Authenticator.getInstance();
        logger = Logger.getLogger(CreateAccServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);

            // Only allow root user to create accounts
            if (!authUser.getAccountName().equals("root")) {
                request.setAttribute("errorMessage", "Do not have permission to create accounts");
                logger.log(Level.WARNING, "User '" + authUser.getAccountName() + "' does not have permission to delete accounts");
                response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
                return;
            }
            logger.log(Level.INFO, "Root is trying to create an account");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
        catch (ExpiredJwtException e){
            logger.log(Level.WARNING, "Session has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (SignatureException e){
            logger.log(Level.WARNING, "JWT has been tampered with or is invalid");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding authentication.");
            request.setAttribute("errorMessage", "Problems regarding authentication. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            auth.checkAuthenticatedRequest(request, response);

            String username = request.getParameter("username");
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");

            auth.createAccount(username, password1, password2);

            logger.log(Level.INFO, "Account created");

            // Redirect to home page after successful account creation
            response.sendRedirect(request.getContextPath() + "/ManageUsers");
        }
        catch (EncryptionDontWork e) {
            logger.log(Level.SEVERE, "Problems with encryption");
            request.setAttribute("errorMessage", "Something went wrong, please try again");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
        catch (AccountAlreadyExists e) {
            logger.log(Level.WARNING, "Username already exists");
            request.setAttribute("errorMessage", "Username already exists");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
        catch (PasswordsDontMatch e) {
            logger.log(Level.WARNING, "Passwords do not match");
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
        catch (NullParameterException e) {
            logger.log(Level.WARNING, "Please fill out all fields");
            request.setAttribute("errorMessage", "Please fill out all fields");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
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
        catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding authentication.");
            request.setAttribute("errorMessage", "Problems regarding authentication. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }
}