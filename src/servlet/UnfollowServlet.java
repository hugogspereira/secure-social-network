package servlet;

import acc.Acc;
import accCtrl.AccessController;
import accCtrl.AccessControllerClass;
import accCtrl.DBcheck;
import accCtrl.operations.OperationClass;
import accCtrl.operations.OperationValues;
import accCtrl.resources.ResourceClass;
import auth.Auth;
import auth.Authenticator;
import exc.AuthenticationError;
import exc.NotFollowing;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jwt.JWTAccount;
import socialNetwork.FState;
import socialNetwork.SN;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "Unfollow", urlPatterns = {"/Unfollow"})
public class UnfollowServlet extends HttpServlet {

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

                if(state != null) { // if it is following
                    SN.getInstance().updatefollowsstatus(Integer.parseInt(pageId), Integer.parseInt(visitedPageId), FState.NONE);
                    HttpSession session = request.getSession();
                    List<String> capabilities = JWTAccount.getInstance().getCapabilities(authUser.getAccountName(), (String) session.getAttribute("Capability"));
                    String cap = Util.getHash(Util.serializeToBytes(new String[]{visitedPageId, OperationValues.ACCESS_POST.getOperation()}));
                    capabilities.remove(cap);
                    session.setAttribute("Capability", JWTAccount.getInstance().createJWTCapability(authUser.getAccountName(), capabilities));
                }
                else { // if the following state is not following
                    throw new NotFollowing();
                }
                response.sendRedirect(request.getContextPath() + "/SocialNetwork?pageId=" + pageId);
                logger.log(Level.INFO, authUser.getAccountName() + " unfollowed "+ pageId + " in  the social network.");
            }
            else {
                logger.log(Level.WARNING, authUser.getAccountName() + " did not provide a pageId or visiterPageId.");
                response.sendRedirect(request.getHeader("referer"));
                request.setAttribute("errorMessage", "No a pageId or or visiterPageId. was provided.");
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
        catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }

}