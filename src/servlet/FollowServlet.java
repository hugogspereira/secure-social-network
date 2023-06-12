package servlet;

import acc.Acc;
import accCtrl.Capability;
import accCtrl.operations.OperationClass;
import accCtrl.operations.OperationValues;
import accCtrl.resources.ResourceClass;
import auth.Auth;
import auth.Authenticator;
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
            String visiterPageId = request.getParameter("visiterPageId");

            /*
             * TODO
             *  - No need to check permissions/capabilities because users can access all pages in the social network
             *  - Right??
             *
             *  ! We might need to check if they are already following the page OR if it is pending !
             */

            if(pageId != null && visiterPageId != null) {
                SN sn = SN.getInstance();
                FState state = sn.getfollowState(Integer.parseInt(visiterPageId), Integer.parseInt(pageId));

                if(state == null) { // if it is not following
                    SN.getInstance().follows(Integer.parseInt(visiterPageId), Integer.parseInt(pageId), FState.PENDING);
                }
                else if(state.equals(FState.NONE)) { // if the following state is none
                    SN.getInstance().updatefollowsstatus(Integer.parseInt(visiterPageId), Integer.parseInt(pageId), FState.PENDING);
                }
                else { // if the following state is pending or accepted
                    throw new AlreadyRequestedFollow();
                }
                response.sendRedirect(request.getContextPath() + "/SocialNetwork?pageId=" + visiterPageId);
                logger.log(Level.INFO, authUser.getAccountName() + " requested a follow in  the social network.");
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