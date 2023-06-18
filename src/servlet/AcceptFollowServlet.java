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
import exc.NotAbleToAccept;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jwt.JWTAccount;
import socialNetwork.FState;
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

@WebServlet(name = "AcceptFollow", urlPatterns = {"/AcceptFollow"})
public class AcceptFollowServlet extends HttpServlet {

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
            String accountName = authUser.getAccountName();

            String pageId = request.getParameter("pageId");
            String pageRequestId = request.getParameter("pageRequestId");


            HttpSession session = request.getSession();
            List<String> capabilities = JWTAccount.getInstance().getCapabilities(accountName, (String) session.getAttribute("Capability"));
            DBcheck check = (cap) -> {
                if(SN.getInstance().getPages(accountName).stream().noneMatch(p -> p.getPageId() == Integer.parseInt(pageId)))
                    return false;
                capabilities.add(cap);
                session.setAttribute("Capability", JWTAccount.getInstance().createJWTCapability(authUser.getAccountName(), capabilities));
                return true;
            };
            accessController.checkPermission(capabilities,  new ResourceClass("page", pageId), new OperationClass(OperationValues.AUTHORIZE_FOLLOW), check);


            if(pageId != null && pageRequestId != null) {
                SN sn = SN.getInstance();
                FState state = sn.getfollowState(Integer.parseInt(pageRequestId), Integer.parseInt(pageId));
                if(state != null && state.equals(FState.PENDING)) {
                    SN.getInstance().updatefollowsstatus(Integer.parseInt(pageRequestId), Integer.parseInt(pageId), FState.OK);
                }
                else { // if the following state is already accepted or none
                    throw new NotAbleToAccept();
                }
                request.getRequestDispatcher("/WEB-INF/sn.jsp").forward(request, response);
                logger.log(Level.INFO, accountName + " accepted the follow request in the social network.");
            }
            else {
                logger.log(Level.WARNING, authUser.getAccountName() + "had an error accepting follow.");
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
        catch (SignatureException e){
            logger.log(Level.WARNING, "JWT has been tampered with or is invalid");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (NotAbleToAccept e) {
            logger.log(Level.WARNING, "Problems in the state of the invite.");
            request.setAttribute("errorMessage", "Problems regarding state of the acceptance. The user cannot accept.");
            request.getRequestDispatcher("/WEB-INF/sn.jsp").forward(request, response);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }

}