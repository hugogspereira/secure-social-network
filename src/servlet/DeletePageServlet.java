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
import jwt.JWTAccount;
import socialNetwork.PageObject;
import socialNetwork.SN;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DeletePage", urlPatterns = {"/DeletePage"})
public class DeletePageServlet extends HttpServlet {

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
            Acc userAcc = auth.checkAuthenticatedRequest(request, response);
            String accountName = userAcc.getAccountName();

            HttpSession session = request.getSession();
            List<String> capabilities = JWTAccount.getInstance().getCapabilities(accountName, (String) session.getAttribute("Capability"));

            DBcheck c = createDBchecker(session, capabilities, accountName);
            accessController.checkPermission(capabilities,  new ResourceClass("page", ""), new OperationClass(OperationValues.DELETE_PAGE), c);


            request.getRequestDispatcher("/WEB-INF/deletePage.jsp").forward(request, response);
            logger.log(Level.INFO, accountName + "is deleting a page");
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (ExpiredJwtException e){
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation - delete page");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Acc userAcc = auth.checkAuthenticatedRequest(request, response);
            String accountName = userAcc.getAccountName();

            HttpSession session = request.getSession();
            List<String> capabilities = JWTAccount.getInstance().getCapabilities(accountName, (String) session.getAttribute("Capability"));

            DBcheck c = createDBchecker(session, capabilities, accountName);
            accessController.checkPermission(capabilities,  new ResourceClass("page", ""), new OperationClass(OperationValues.DELETE_PAGE), c);

            int pageId = Integer.parseInt(request.getParameter("pageid"));
            SN socialNetwork = SN.getInstance();
            PageObject page = socialNetwork.getPage(pageId);
            socialNetwork.deletePage(page);

            logger.log(Level.INFO, "Deleted successfully the page of user: "+page.getUserId()+", by the user: "+accountName+".");

            // Redirect to home page after successful page deletion
            response.sendRedirect(request.getContextPath() + "/ManageUsers");
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (ExpiredJwtException e){
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, "PageId does not exist cannot delete page");
            request.setAttribute("errorMessage", "PageId does not exist cannot delete page");
            request.getRequestDispatcher("/WEB-INF/deletePage.jsp").forward(request, response);
        }
        catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation - delete page");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/deletePage.jsp").forward(request, response);
        }
    }

    private DBcheck createDBchecker(HttpSession session, List<String> capabilities, String accountName) {
        return (cap) -> {
            capabilities.add(cap);
            session.setAttribute("Capability", JWTAccount.getInstance().createJWTCapability(accountName, capabilities));
            return true;
        };
    }
}
