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

@WebServlet(name = "ChangePwd", urlPatterns = {"/ChangePassword"})
public class ChangePwdServlet extends HttpServlet {

    private Auth auth;
    private Acc authUser;
    private Logger logger;

    @Override
    public void init() {
        auth = Authenticator.getInstance();
        authUser = null;
        logger = Logger.getLogger(ChangePwdServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            authUser = auth.checkAuthenticatedRequest(request, response);
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
            logger.log(Level.INFO, "User " + authUser.getAccountName() + " is trying to change password");
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
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
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            auth.checkAuthenticatedRequest(request, response);

            String password1 = request.getParameter("newPassword1");
            String password2 = request.getParameter("newPassword2");

            auth.changePwd(authUser.getAccountName(), password1, password2);

            // Redirect to home page after successful password change
            response.sendRedirect(request.getContextPath() + "/ManageUsers");
        }
        catch (EncryptionDontWork e) {
            request.setAttribute("errorMessage", "Problems with encryption");
            logger.log(Level.SEVERE, "Something went wrong, please try again");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
        catch (PasswordsDontMatch e) {
            request.setAttribute("errorMessage", "New passwords do not match");
            logger.log(Level.WARNING, "New passwords do not match");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
        catch (NullParameterException e) {
            request.setAttribute("errorMessage", "Please fill out all fields");
            logger.log(Level.WARNING, "Please fill out all fields");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
        catch (AccountDoesNotExist e) {
            request.setAttribute("errorMessage", "The Root account does not exist");
            logger.log(Level.WARNING, "Something went wrong, please try again");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
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
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
    }
}