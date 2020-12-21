package example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jws.soap.InitParam;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		name = "initparam",
		urlPatterns = {
				"/init1"
		},
		initParams = {
				@WebInitParam(name="db1" ,value="mysql1"),
				@WebInitParam(name="db2" ,value="oracle1")
		}
		)
public class InitParamServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ServletConfig config=this.getServletConfig();
		PrintWriter printWriter=resp.getWriter();
		
		printWriter.append("Databsase 1:" + config.getInitParameter("db1"))
		.append("Database 2:" + config.getInitParameter("db2"));
		
	}
	

}
