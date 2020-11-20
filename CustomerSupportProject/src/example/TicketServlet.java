package example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;






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


	private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		   String idString = request.getParameter("ticketId");
	        Ticket ticket = this.getTicket(idString, response);
	        if(ticket == null)
	            return;

	        String name = request.getParameter("attachment");
	        if(name == null)
	        {
	            response.sendRedirect("tickets?action=view&ticketId=" + idString);
	            return;
	        }

	        Attachment attachment = ticket.getAttachment(name);
	        if(attachment == null)
	        {
	            response.sendRedirect("tickets?action=view&ticketId=" + idString);
	            return;
	        }

	        response.setHeader("Content-Disposition",
	                "attachment; filename=" + attachment.getName());
	        response.setContentType("application/octet-stream");

	        ServletOutputStream stream = response.getOutputStream();
	        stream.write(attachment.getContents());
		
	}


	private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String idString= request.getParameter("ticketId");
		Ticket ticket=this.getTicket(idString, response);
		if(ticket == null)
			return;
		
		PrintWriter printWriter=this.writeHeader(response);
		
		printWriter.append("<h2>Ticket #").append(idString).append(":").append(ticket.getSubject())
		.append("</h2>\n");
		
		printWriter.append("<i>Customer name - ").append(ticket.getCustomerName()).append("</i><br><br>\n");
        printWriter.append(ticket.getBody()).append("<br/><br/>\r\n");

		if(ticket.getNumberOfAttachment() > 0)
		{
			printWriter.append("Attachments: ");
			int i=0;
			for(Attachment attachment : ticket.getAttachments())
			{
				if(i++ > 0)
printWriter.append(",");
				printWriter.append("<a href=\"tickets?action=download&ticketId=")
				.append(idString).append("&attachment=")
				.append(attachment.getName()).append("\">")
				.append(attachment.getName()).append("</a>");
				
			}
			printWriter.append("<br><br>");
		}
		printWriter.append("<a href=\"tickets\"> Return to tickets list</a><br>");
		this.writeFooter(printWriter);
		
	}
	
	private void writeFooter(PrintWriter printWriter) {
		// TODO Auto-generated method stub
		printWriter.append("</body>").append("</html>\n");
		
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
		printWriter.append("<h1>Ticket Form</h1>"
				+ "<form method=\"POST\" action=\"tickets\" enctype=\"multipart/form-data\">");
		printWriter.append("<input type=\"hidden\" name=\"action\" value=\"create\"/>");
				
		printWriter.append("Your Name:").append("<input type=\"text\" name=\"customerName\"/><br><br>\r")
		.append("Subject:").append("<input type=\"text\" name=\"subject\" /><br><br>")
		.append("Body:")
		.append("<textarea name=\"body\" rows=\"5\" cols=\"30\" >")
		.append("</textarea><br><br>")
		.append("Attachments: <input type=\"file\" name=\"file1\" /><br><br>" )
		.append("<input type=\"submit\" value=\"Submit\" /> \n");
	
	
		printWriter.append("</form></html>");
		
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
		
		String action=request.getParameter("action");
		if(action==null)
			action="list";
		switch(action)
		{
		case "create":
			this.createTicket(request,response);
			break;
		case "list":
			default:
				response.sendRedirect("tickets");
				break;
				
		}
	}


	private void createTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		Ticket ticket=new Ticket();
		ticket.setCustomerName(request.getParameter("customerName"));
		ticket.setSubject(request.getParameter("subject"));
		ticket.setBody(request.getParameter("body"));
		
		Part filePart=request.getPart("file1");
		if(filePart != null && filePart.getSize() >0)
		{
			Attachment attachment= this.processAttachment(filePart);
			if(attachment!=null)
				ticket.addAttachment(attachment);
		}
		int id;
		synchronized (this) {
			id=this.TICKET_ID_SEQUENCE++;
			this.ticketDatabase.put(id, ticket);
		}
		response.sendRedirect("tickets?action=view&ticketId=" + id);
	}


	private Attachment processAttachment(Part filePart) throws IOException {
		// TODO Auto-generated method stub
		 InputStream inputStream = filePart.getInputStream();
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	        int read;
	        final byte[] bytes = new byte[1024];

	        while((read = inputStream.read(bytes)) != -1)
	        {
	            outputStream.write(bytes, 0, read);
	        }

	        Attachment attachment = new Attachment();
	        attachment.setName(filePart.getSubmittedFileName());
	        attachment.setContents(outputStream.toByteArray());

	        return attachment;	
	        }

}
