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

@WebServlet(name = "DeleteAccount", urlPatterns = {"/DeleteAccount"})
public class DeleteAccServlet extends HttpServlet {
    private Auth auth;

    @Override
    public void init() {
        auth = new Authenticator();
        // Not sure if it is done in that way or like this:
        // (Auth) getServletContext().getAttribute("authenticator");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/deleteAcc.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);

            // Only allow root user to delete accounts
            if (!authUser.getAccountName().equals("root")) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String username = request.getParameter("username");

            auth.deleteAccount(username);

            // Redirect to home page after successful account deletion
            response.sendRedirect(request.getContextPath() + "/manageUsers.jsp");

        }
        catch (AuthenticationError e) {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountIsLocked e) {
            request.setAttribute("errorMessage", "Your account is locked");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (EncryptionDontWork e) {
            request.setAttribute("errorMessage", "Problems with encryption");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountDoesNotExist e) {
            request.setAttribute("errorMessage", "The Root account does not exist");
            request.getRequestDispatcher("/WEB-INF/authenticateUser.jsp").forward(request, response);
        }
        catch (AccountIsNotLocked e) {
            request.setAttribute("errorMessage", "The account is not locked");
            request.getRequestDispatcher("/WEB-INF/managerUsers.jsp").forward(request, response);
        }
        catch (AccountIsLoggedIn e) {
            request.setAttribute("errorMessage", "The account is logged in");
            request.getRequestDispatcher("/WEB-INF/managerUsers.jsp").forward(request, response);
        }
    }
}