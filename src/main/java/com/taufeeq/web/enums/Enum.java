package com.taufeeq.web.enums;

public class Enum{
	
	public enum Table {
		userdetails,
		mails,
		phonenumbers,
		usergroups,
		groupcontacts,
		contactdetails,
		contactemail,
		contactnumber,
		usersessions;
	
	}
	
	public enum userdetails implements Column {
		User_ID,
	    Password,
	    Username,
	    Gender,
	    Birthday,
	    Location,
		dtformat;

	    public String getColumnName() {
	        return "userdetails."+this.name();
	    }
	}
	
	public enum mails implements Column {
	    User_ID,
	    Mail,
		createdTime;
	
	    @Override
	    public String getColumnName() {
	        return "mails."+this.name();
	    }
	}
	public enum phonenumbers implements Column{
		User_Id,
		Phone_number,
		createdTime;
		
		
		public String getColumnName() {
			return "phonenumbers."+ this.name();
		}
	}
	public enum usergroups implements Column{
		Group_ID,
		User_ID,
		Group_Name;
		public String getColumnName() {
			return "usergroups."+ this.name();
		}
	}
	public enum groupcontacts implements Column{
		Group_ID,
		Contact_ID;
		public String getColumnName() {
			return "groupcontacts."+ this.name();
		}
	}
	public enum contactdetails implements Column{
		Contact_ID,
		User_ID,
		Name,
		gender,
		birthday,
		favorite,
		archive,
		created_time;
		public String getColumnName() {
			return "contactdetails."+ this.name();
		}
	}
	public enum contactemail implements Column{
		Email_ID,
		Contact_ID,
		Email;
		public String getColumnName() {
			return "contactemail."+ this.name();
		}
	}
	public enum contactnumber implements Column{
		Number_ID,
		Contact_ID,
		Phone_number;
		public String getColumnName() {
			return "contactnumber."+ this.name();
		}
	}
	public enum usersessions implements Column{
		Session_ID,
		User_ID,
		Created_Time,
		Expiration_time,
		timezone;
		public String getColumnName() {
			return "usersessions."+ this.name();
		}
	}
	
}


