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

@WebServlet(name="AuthenticateUser", urlPatterns={"/AuthenticateUser"})
public class AuthenticateUserServlet extends HttpServlet {

    private Auth auth;

    @Override
    public void init() {
        auth =  new Authenticator();
        // Not sure if it is done in that way or like this:
        // (Auth) getServletContext().getAttribute("authenticator");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("username");
            String pwd = request.getParameter("password");
            Acc authuser = auth.authenticateUser(name, pwd);

            HttpSession session = request.getSession(true);
            session.setAttribute("JWT", authuser.getJWT());
        }
        catch (AccountIsLocked e) {
            e.printStackTrace();
        }
        catch (EncryptionDontWork e) {
            e.printStackTrace();
        }
        catch (AuthenticationError e) {
            e.printStackTrace();
        }
        catch (AccountDoesNotExist e) {
            e.printStackTrace();
        }
    }

}
