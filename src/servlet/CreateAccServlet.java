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

@WebServlet(name = "CreateAccount", urlPatterns = {"/CreateAccount"})
public class CreateAccServlet extends HttpServlet {

    private Auth auth;

    @Override
    public void init() {
        auth = new Authenticator();
        // Not sure if it is done in that way or like this:
        // (Auth) getServletContext().getAttribute("authenticator");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // TODO: "(should only work for “root”)" - does it means root needs to be authenticated?
            Acc authUser = auth.checkAuthenticatedRequest(request, response);

            // Only allow root user to create accounts
            if (!authUser.getAccountName().equals("root")) {
                request.setAttribute("errorMessage", "Do not have permission to delete accounts");
                request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
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
            String username = request.getParameter("username");
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");

            auth.createAccount(username, password1, password2);

            // Redirect to home page after successful account creation
            response.sendRedirect(request.getContextPath() + "/manageUsers.jsp");
        }
        catch (EncryptionDontWork e) {
            request.setAttribute("errorMessage", "Problems with encryption");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountAlreadyExists e) {
            request.setAttribute("errorMessage", "Username already exists");
            request.getRequestDispatcher("/WEB-INF/createUser.jsp").forward(request, response);
        }
        catch (PasswordsDontMatch e) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/createUser.jsp").forward(request, response);
        }
        catch (NullParameterException e) {
            request.setAttribute("errorMessage", "Please fill out all fields");
            request.getRequestDispatcher("/WEB-INF/createUser.jsp").forward(request, response);
        }
    }
}