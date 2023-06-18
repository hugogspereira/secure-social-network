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
import exc.AccessControlError;
import exc.AuthenticationError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jwt.JWTAccount;
import socialNetwork.SN;
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

@WebServlet(name = "FollowersRequests", urlPatterns = {"/FollowersRequests"})
public class FollowersRequestsServlet extends HttpServlet {

    private Auth auth;
    private AccessController accessController;

    private Logger logger;
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
            String accountName = authUser.getAccountName();

            String pageId = request.getParameter("pageId");

            if(pageId != null) {
                HttpSession session = request.getSession();
                List<String> capabilities = JWTAccount.getInstance().getCapabilities(accountName, (String) session.getAttribute("Capability"));

                DBcheck c = (cap) -> {
                    boolean res = SN.getInstance().getPages(accountName).stream().anyMatch(p -> p.getPageId() == Integer.parseInt(pageId));
                    if(res) {
                        capabilities.add(cap);
                        session.setAttribute("Capability",JWTAccount.getInstance().createJWTCapability(accountName, capabilities));
                    }
                    return res;
                };
                accessController.checkIfNeedsToRefreshCapabilities(accountName, session);
                accessController.checkPermission(capabilities,  new ResourceClass("page", pageId), new OperationClass(OperationValues.AUTHORIZE_FOLLOW), c);

                request.getRequestDispatcher("/WEB-INF/followrequests.jsp").forward(request, response);
                logger.log(Level.INFO, accountName + " is viewing the follow requests in the social network.");
            }
            else {
                logger.log(Level.WARNING, authUser.getAccountName() + "tried to follow a null page");
                request.setAttribute("errorMessage", "No pageId or pageRequestId was provided!");
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
        catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation - Access follower requests");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        }
        catch (SignatureException e) {
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