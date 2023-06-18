package servlet;

import acc.Acc;
import accCtrl.AccessController;
import accCtrl.AccessControllerClass;
import auth.Auth;
import auth.Authenticator;
import exc.AccessControlError;
import exc.AlreadyRequestedFollow;
import exc.AuthenticationError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import socialNetwork.FState;
import socialNetwork.SN;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "Follow", urlPatterns = {"/Follow"})
public class FollowServlet extends HttpServlet {

    private Auth auth;
    private Logger logger;
    private AccessController accessController;
    @Override
    public void init() {
        auth = Authenticator.getInstance();
        accessController = AccessControllerClass.getInstance();
        logger = Logger.getLogger(CreateAccServlet.class.getName());
        logger.setLevel(Level.FINE);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Acc authUser = auth.checkAuthenticatedRequest(request, response);

            String pageId = request.getParameter("pageId");
            String visitedPageId = request.getParameter("visitedPageId");

            if(pageId != null && visitedPageId != null) {
                SN sn = SN.getInstance();
                FState state = sn.getfollowState(Integer.parseInt(pageId), Integer.parseInt(visitedPageId));

                if(state == null) { // if it is not following
                    sn.follows(Integer.parseInt(pageId), Integer.parseInt(visitedPageId), FState.PENDING);
                }
                else if(state.equals(FState.NONE)) { // if the following state is none
                    sn.updatefollowsstatus(Integer.parseInt(pageId), Integer.parseInt(visitedPageId), FState.PENDING);
                }
                else { // if the following state is pending or accepted
                    throw new AlreadyRequestedFollow();
                }
                response.sendRedirect(request.getContextPath() + "/SocialNetwork?pageId=" + pageId);
                logger.log(Level.INFO, authUser.getAccountName() + " requested a follow in  the social network.");
            }
            else {
                logger.log(Level.WARNING, authUser.getAccountName() + " did not provide a pageId or pageRequestId");
                request.setAttribute("errorMessage", "No a pageId or pageRequestId was provided.");
                request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
            }
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
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
        catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation - follow request");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        }
        catch (AlreadyRequestedFollow e) {
            logger.log(Level.WARNING, "Problems regarding the social network. The user has already requested a follow.");
            request.setAttribute("errorMessage", "The user has already requested a follow.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }

}