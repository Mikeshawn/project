package manageProjects;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewCommentServlet
 */
@WebServlet(description = "See the latest comments by lecturer on your project", urlPatterns = { "/ViewCommentServlet" })
public class ViewCommentServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
       
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Project project= new Project();
		//RequestDispatcher rDispatcher = request.getRequestDispatcher("target.jsp");
		PrintWriter printWriter = response.getWriter();
		try {
			
			String stateReturned = project.viewComment(request.getParameter("theId"));
			printWriter.println(stateReturned);
			/*request.setAttribute("plots", list);
			rDispatcher.forward(request, response);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
