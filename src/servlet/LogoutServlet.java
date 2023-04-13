package servlet;

import acc.Acc;
import auth.Auth;
import auth.Authenticator;
import exc.AccountDoesNotExist;
import exc.AccountIsLocked;
import exc.AuthenticationError;
import exc.EncryptionDontWork;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="Logout", urlPatterns={"/Logout"})
public class LogoutServlet extends HttpServlet {

    private Auth auth;

    @Override
    public void init() {
        auth =  new Authenticator();
        // Not sure if it is done in that way or like this:
        // (Auth) getServletContext().getAttribute("authenticator");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);
            auth.logout(authUser);
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        } catch (AuthenticationError e) {
            throw new RuntimeException(e);
        }
        catch (AccountIsLocked e) {
            throw new RuntimeException(e);
        }
        catch (EncryptionDontWork e) {
            throw new RuntimeException(e);
        }
        catch (AccountDoesNotExist e) {
            throw new RuntimeException(e);
        }

    }

}
