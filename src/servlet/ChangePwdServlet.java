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

@WebServlet(name = "ChangePwd", urlPatterns = {"/ChangePassword"})
public class ChangePwdServlet extends HttpServlet {

    private Auth auth;
    private Acc authUser;

    @Override
    public void init() {
        auth = new Authenticator();
        authUser = null;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            authUser = auth.checkAuthenticatedRequest(request, response);
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
        catch (AuthenticationError e) {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountIsLocked e) {
            request.setAttribute("errorMessage", "Your account is locked");
            request.getRequestDispatcher("/WEB-INF/manageUsers.jsp").forward(request, response);
        }
        catch (EncryptionDontWork e) {
            request.setAttribute("errorMessage", "Problems with encryption");
            request.getRequestDispatcher("/WEB-INF/manageUsers.jsp").forward(request, response);
        }
        catch (AccountDoesNotExist e) {
            request.setAttribute("errorMessage", "The Root account does not exist");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String password1 = request.getParameter("newPassword1");
            String password2 = request.getParameter("newPassword2");

            auth.changePwd(authUser.getAccountName(), password1, password2);

            // Redirect to home page after successful password change
            response.sendRedirect(request.getContextPath() + "/manageUsers.jsp");
        }
        catch (EncryptionDontWork e) {
            request.setAttribute("errorMessage", "Problems with encryption");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (PasswordsDontMatch e) {
            request.setAttribute("errorMessage", "New passwords do not match");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
        catch (NullParameterException e) {
            request.setAttribute("errorMessage", "Please fill out all fields");
            request.getRequestDispatcher("/WEB-INF/changePwd.jsp").forward(request, response);
        }
        catch (AccountDoesNotExist e) {
            request.setAttribute("errorMessage", "The Root account does not exist");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
    }
}