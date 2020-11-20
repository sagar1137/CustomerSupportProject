package example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet(
		name = "CustomerTicket",
		urlPatterns = {
				"/tickets"
		},
		
		loadOnStartup = 1)

@MultipartConfig(
		fileSizeThreshold = 5_242_880, //5MB 
		maxFileSize = 20_971_520L, //20MB 
		maxRequestSize = 41_943_040L //40MB
)
public class TicketServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private volatile int TICKET_ID_SEQUENCE=1;
    private Map<Integer, Ticket> ticketDatabase = new LinkedHashMap<>();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action=request.getParameter("action");
		
		if(action==null)
			action="list";
		
		switch (action) {
		case "create":
			this.showTicketForm(response);
			
			break;
		case "view":
			this.viewTicket(request,response);
			break;
		case "download":
			this.downloadAttachment(request,response);
			break;
			
		
		default:
			this.listTickets(response);
			break;
		}
		
	}

	
	private void listTickets(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		PrintWriter printWriter=response.getWriter();
		printWriter.append("<h1>Tickets</h1>");
		
		printWriter.append("<a href=\"tickets?action=create\"> Create ticket </a><br><br>");
		
		if(this.ticketDatabase.size()==0)
			printWriter.append("There are not tickets in System.\n");
		else
		{
			for(int id:this.ticketDatabase.keySet())
			{
				String idString=Integer.toString(id);
				Ticket ticket=this.ticketDatabase.get(id);
				printWriter.append("Ticket").append(idString)
				.append(": <a href=\"tickets?action=view&ticketId=")
						.append(idString).append("\">").append(ticket.getSubject())
						.append("</a>(customer:").append(ticket.getCustomerName())
						.append(")<br>\n");
			}
		}
		
	}


	private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String idString= request.getParameter("ticketid");
		Ticket ticket=this.getTicket(idString, response);
		
	}
	
	private Ticket getTicket(String idString,HttpServletResponse response) throws IOException,ServletException
	{
		if(idString==null || idString.length() ==0)
		{
			response.sendRedirect("tickets");
					return null;
		}
		
		try
		{
			Ticket ticket=this.ticketDatabase.get(Integer.parseInt(idString));
			if(ticket == null)
			{
				response.sendRedirect("tickets");
				return null;
			}
			return ticket;
		}
		catch (Exception e) {
			response.sendRedirect("tickets");
			return null;
			// TODO: handle exception
		}
	}

	private void showTicketForm(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		//response.setContentType("UTF-8");
		PrintWriter printWriter=this.writeHeader(response);
		printWriter.append("<html>");
		printWriter.append("<head><title>SHow ticket</title></head>");
		printWriter.append("<body><b><h1>Ticket Form</b></h1>"
				+ "<form method=\"post\" action=\"tickets\" enctype=\"multipart/formdata\" value=\"create\"/>"
				);
		printWriter.append("Your Name:").append("<input type=\"text\" name=\"customerName\"/><br><br>\r")
		.append("Subject:").append("<input type=\"text\" name=\"subject\" /><br><br>")
		.append("Body:")
		.append("<textarea name=\"body\" rows=\"5\" cols=\"30\" >")
		.append("</textarea><br><br>")
		.append("Attachments: <input type=\"file\" name=\"file\" /><br><br>" )
		.append("<input type=\"submit\" value=\"submit\" /> \n");
	
	
		printWriter.append("</html>");
		
	}


	private PrintWriter writeHeader(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter printWriter=response.getWriter();
		
		printWriter.append("<html> <head> <title>Customer Support</title></head>")
		.append("<body>\n");
		
		return printWriter;
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
