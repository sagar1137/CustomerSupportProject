package example;

import java.util.Map;


import java.util.Collection;
import java.util.LinkedHashMap;
public class Ticket {
	
	private String CustomerName;
	private String Subject;
	private String Body;
	
	private Map<String, Attachment> attachments=new LinkedHashMap<>();

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}

	public Collection <Attachment> getAttachments() {
		return this.attachments.values();
	}

	public void setAttachments(Map<String, Attachment> attachments) {
		this.attachments = attachments;
	}
	
	public void addAttachment(Attachment attachment)
	{
		this.attachments.put(attachment.getName(), attachment);
	}
	
	public int getNumberOfAttachment()
	{
		return this.attachments.size();
	}
	
	 public Attachment getAttachment(String name)
	    {
	        return this.attachments.get(name);
	    }
	
}
