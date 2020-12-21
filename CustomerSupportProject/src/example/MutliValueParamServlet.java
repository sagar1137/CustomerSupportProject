package example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(
		name="mutlivalueparameterservlet",
		urlPatterns= {
				"/checkbox"
				}
		
		)
public class MutliValueParamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter printWriter=response.getWriter();
		
		printWriter.append("<DOCTYPE html> \n")
		.append("<html>")
		.append("<head>")
		.append("<title>Hello User Application </title>")
		.append("</head>")
		.append("<body>")
		.append("<form action=\"checkbox\" method=\"post\">")
		.append("Select the fruits you would like to eat: \n <br>")
		.append("<input type=\"checkbox\" name=\"fruit\" value=\"banana\"/> BANANA \r\n<br>" )
		
		.append("<input type=\"checkbox\" name=\"fruit\" value=\"apple\"/>APPLE <br>\n")
		.append("<input type=\"checkbox\" name=\"fruit\" value=\"guava\"/> GUAVA <br>\n")
		.append("<input type=\"checkbox\" name=\"fruit\" value=\"chickoo\"/> CHICKOO <br>\n")
		.append("<input type=\"submit\" value=\"Submit\"/> ")
		
		.append("</form>")
		.append("</body>")
		.append("</html>");
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String [] Fruits=request.getParameterValues("fruit");
		PrintWriter printWriter=response.getWriter();
		printWriter.append("<DOCTYPE html> \n")
		.append("<html> <head> <title> Hello User Application </title></head>")
		.append("<body> Your selections are : <br><br> ");
		
		if(Fruits==null)
			printWriter.append("You Didnt selected any fruits<br><br>");
		
		else
		{
			printWriter.append("<ul>");
			for(int i=0;i<Fruits.length;i++)
			{
				printWriter.append("<li>").append(Fruits[i]).append("</li><br>");
				
			}
			printWriter.append("</ul>");
		}
		
		printWriter.append("</body></html>");
		
		
		
		
		
		
		
		this.doGet(request, response);
	}

}
