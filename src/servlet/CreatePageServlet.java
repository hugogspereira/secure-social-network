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

@WebServlet(name = "CreatePage", urlPatterns = {"/CreatePage"})
public class CreatePageServlet extends HttpServlet {

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
            HttpSession session = request.getSession();
            List<String> capabilities = (List<String>) session.getAttribute("Capability");

            DBcheck c = createDBchecker(session, capabilities);
            accessController.checkPermission(capabilities, new ResourceClass("page", ""), new OperationClass(OperationValues.CREATE_PAGE), c);

            request.getRequestDispatcher("/WEB-INF/createPage.jsp").forward(request, response);
            logger.log(Level.INFO, userAcc.getAccountName() + " is creating a page.");
        } catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response);
        } catch (ExpiredJwtException e) {
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        } catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/createPage.jsp").forward(request, response);     // TODO: NAO FAZ SENTIDO SE ELE N TEM A PERMISSAO VAI ACEDER AO RECURSO DE CRIAR PAGINAS NA MESMA?
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Acc userAcc = auth.checkAuthenticatedRequest(request, response);

            HttpSession session = request.getSession();
            List<String> capabilities = (List<String>) session.getAttribute("Capability");

            DBcheck c = createDBchecker(session, capabilities);
            accessController.checkPermission(capabilities, new ResourceClass("page", ""), new OperationClass(OperationValues.CREATE_PAGE), c);

            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String pageTitle = request.getParameter("pageTitle");
            String pagePic = request.getParameter("pagePic");

            SN socialNetwork = SN.getInstance();
            socialNetwork.newPage(username, email, pageTitle, pagePic);

            logger.log(Level.INFO, "Page for user " + username + " was created successfully. By user " + userAcc.getAccountName() + ".");
            // Redirect to home page after successful page creation
            response.sendRedirect(request.getContextPath() + "/ManageUsers");
        } catch (AuthenticationError e) {
            logger.log(Level.WARNING, "Invalid username or password");
            request.setAttribute("errorMessage", "Invalid username and/or password");
            request.getRequestDispatcher("/WEB-INF/createAcc.jsp").forward(request, response); // TODO: ?????????????????????????????????
        } catch (ExpiredJwtException e) {
            logger.log(Level.WARNING, "JWT has expired");
            request.setAttribute("errorMessage", "Session has expired and/or is invalid");
            request.getRequestDispatcher("/WEB-INF/expired.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQL Exception on creating page");
            request.setAttribute("errorMessage", "Problems regarding the creation of the page. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/createPage.jsp").forward(request, response);
        } catch (AccessControlError e) {
            logger.log(Level.WARNING, "Invalid permissions for this operation");
            request.setAttribute("errorMessage", "Invalid permissions for this operation");
            request.getRequestDispatcher("/WEB-INF/permissionError.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Problems regarding the social network. Please try again later.");
            request.setAttribute("errorMessage", "Problems regarding the social network. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/createPage.jsp").forward(request, response);
        }
    }

    private DBcheck createDBchecker(HttpSession session, List<String> capabilities) {
        return (cap) -> {
            capabilities.add(cap);
            session.setAttribute("Capability", capabilities);
            return true;
        };
    }
}
