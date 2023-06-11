package servlet;

import acc.Acc;
import auth.Auth;
import auth.Authenticator;
import exc.AuthenticationError;
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

@WebServlet(name = "FollowersRequests", urlPatterns = {"/FollowersRequests"})
public class FollowersRequestsServlet extends HttpServlet {

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

            String pageId = request.getParameter("pageId");

            /*
             * TODO
             *  - Check Permissions/Capabilities
             */

            if(pageId != null) {
                request.getRequestDispatcher("/WEB-INF/followrequests.jsp").forward(request, response);
                logger.log(Level.INFO, authUser.getAccountName() + " is viewing the follow requests in the social network.");
            }
            else {
                // TODO: REDIRECT
            }
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
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/createPage.jsp").forward(request, response);
        }
    }

}