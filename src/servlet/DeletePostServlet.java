package servlet;

import acc.Acc;
import accCtrl.AccessController;
import accCtrl.AccessControllerClass;
import accCtrl.Capability;
import accCtrl.operations.OperationClass;
import accCtrl.operations.OperationValues;
import accCtrl.resources.ResourceClass;
import auth.Auth;
import auth.Authenticator;
import exc.AccessControlError;
import exc.AuthenticationError;
import io.jsonwebtoken.ExpiredJwtException;
import socialNetwork.PageObject;
import socialNetwork.PostObject;
import socialNetwork.SN;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            auth.checkAuthenticatedRequest(request, response);

            // TODO - ver este com atencao, n sei se esta bem - olhar par o onwpage servlet
            // Lembrar que as pessoas podem n usar o jsp e fazer diretamente o pedido estilo postman, n serao quebradas as regras de seguranca????

            List<Capability> capabilities = (List<Capability>) request.getSession().getAttribute("Capability");
            // TODO check permission para ver se este role tem a permissao de dar delete de posts
            accessController.checkPermission(capabilities,  new ResourceClass("page"), new OperationClass(OperationValues.DELETE_POST));
            // TODO Check permission se o user pode eliminar posts nesta pagina
            // ???

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
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        }
        catch (ExpiredJwtException e){
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        }
        catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation");
            // TODO: Redirect to home page
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
