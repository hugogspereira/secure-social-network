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

@WebServlet(name="Logout", urlPatterns={"/Logout"})
public class LogoutServlet extends HttpServlet {

    private Auth auth;

    @Override
    public void init() {
        auth =  new Authenticator();
        // Not sure if it is done in that way or like this:
        // (Auth) getServletContext().getAttribute("authenticator");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/logout.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);
            auth.logout(authUser);

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Redirect to login page after successful logout
            response.sendRedirect(request.getContextPath() + "/AuthenticateUser");
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
    }

}
