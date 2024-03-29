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
import socialNetwork.SN;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CreatePost", urlPatterns = {"/CreatePost"})
public class CreatePostServlet extends HttpServlet {

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

            String pageId = request.getParameter("pageId");
            HttpSession session = request.getSession();
            List<String> capabilities = JWTAccount.getInstance().getCapabilities(accountName, (String) session.getAttribute("Capability"));

            DBcheck c = createDBchecker(accountName, pageId, session, capabilities);
            accessController.checkPermission(capabilities,  new ResourceClass("page", pageId), new OperationClass(OperationValues.CREATE_POST), c);

            request.getRequestDispatcher("/WEB-INF/createPost.jsp").forward(request, response);
            logger.log(Level.INFO, userAcc.getAccountName() + " is creating a post.");
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
            logger.log(Level.WARNING, "Invalid permissions for this operation - GET");
            request.setAttribute("errorMessage", "Invalid permissions for this operation - create post");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Acc userAcc = auth.checkAuthenticatedRequest(request, response);
            String accountName = userAcc.getAccountName();

            String pageIdString = request.getParameter("pageId");
            int pageId = Integer.parseInt(pageIdString);
            HttpSession session = request.getSession();
            List<String> capabilities = JWTAccount.getInstance().getCapabilities(accountName, (String) session.getAttribute("Capability"));

            DBcheck c = createDBchecker(accountName, pageIdString, session, capabilities);
            accessController.checkPermission(capabilities,  new ResourceClass("page", pageIdString), new OperationClass(OperationValues.CREATE_POST), c);

            String postDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
            String postText = request.getParameter("postText");

            SN socialNetwork = SN.getInstance();
            socialNetwork.newPost(pageId, postDate, postText);

            logger.log(Level.INFO, "Post for page "+pageId+" was created successfully. By user "+userAcc.getAccountName()+".");
            // Redirect to home page after successful page creation
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
            logger.log(Level.WARNING, "SQL Exception on creating page");
            request.setAttribute("errorMessage", "Problems regarding the creation of the post. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
        catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation - create post");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }

    private DBcheck createDBchecker(String userId, String pageId, HttpSession session, List<String> capabilities){
        return (cap) -> {
            boolean res = SN.getInstance().getPages(userId).stream().anyMatch(p -> p.getPageId() == Integer.parseInt(pageId));
            if (res) {
                capabilities.add(cap);
                session.setAttribute("Capability", JWTAccount.getInstance().createJWTCapability(userId, capabilities));
            }
            return res;
        };
    }
}
