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
import socialNetwork.PostObject;
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

@WebServlet(name = "DeletePost", urlPatterns = {"/DeletePost"})
public class DeletePostServlet extends HttpServlet {

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
            Acc user = auth.checkAuthenticatedRequest(request, response);
            String pageId = request.getParameter("pageId");

            HttpSession session = request.getSession();
            List<String> capabilities = (List<String>) request.getSession().getAttribute("Capability");

            DBcheck c = (cap) -> {
                boolean res = SN.getInstance().getPages(user.getAccountName()).stream().anyMatch(p -> p.getPageId() == Integer.parseInt(pageId));
                if (res) {
                    capabilities.add(cap);
                    session.setAttribute("Capability", capabilities);
                }
                return res;
            };
            accessController.checkPermission(capabilities,  new ResourceClass("page", pageId), new OperationClass(OperationValues.DELETE_POST), c);


            int postId = Integer.parseInt(request.getParameter("postId"));
            SN socialNetwork = SN.getInstance();
            PostObject post = socialNetwork.getPost(postId);
            socialNetwork.deletePost(post);

            request.getRequestDispatcher("/WEB-INF/deletePost.jsp").forward(request, response);
            logger.log(Level.INFO, "Deleting a post.");
        }
        catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);  // TODO: ?????????????????????????????????
        }
        catch (ExpiredJwtException e){
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);  // TODO: ?????????????????????????????????
        } catch (Exception e) {
            throw new RuntimeException(e);  // TODO: ?????????????????????????????????
        }
    }

}
