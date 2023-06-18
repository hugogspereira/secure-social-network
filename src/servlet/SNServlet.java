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
import exc.*;
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

@WebServlet(name = "Social Network", urlPatterns = {"/SocialNetwork"})
public class SNServlet extends HttpServlet {

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
            String accountName = authUser.getAccountName();

            String pageId = request.getParameter("pageId");

            HttpSession session = request.getSession();
            List<String> capabilities = JWTAccount.getInstance().getCapabilities(accountName, (String) session.getAttribute("Capability"));
            DBcheck check = (cap) -> {
                if(SN.getInstance().getPages(accountName).stream().noneMatch(p -> p.getPageId() == Integer.parseInt(pageId)))
                    return false;
                capabilities.add(cap);
                session.setAttribute("Capability", JWTAccount.getInstance().createJWTCapability(authUser.getAccountName(), capabilities));
                return true;
            };
            AccessControllerClass.getInstance().checkPermission(capabilities,  new ResourceClass("page", pageId), new OperationClass(OperationValues.AUTHORIZE_FOLLOW), check);

            if(pageId != null) {


                request.getRequestDispatcher("/WEB-INF/sn.jsp").forward(request, response);
                logger.log(Level.INFO, accountName + " is viewing the social network.");
            }
            else {
                logger.log(Level.WARNING, accountName + " did not provide a pageId.");
                response.sendRedirect(request.getHeader("referer"));
                request.setAttribute("errorMessage", "No a pageId or pageRequestId was provided.");
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
            logger.log(Level.WARNING, e.getMessage());
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Something went wrong");
            logger.log(Level.WARNING, e.getMessage());
            request.setAttribute("errorMessage", "Something went wrong");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }

}
