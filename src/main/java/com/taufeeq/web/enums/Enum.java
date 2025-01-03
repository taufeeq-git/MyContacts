package com.taufeeq.web.enums;

import com.taufeeq.web.model.*;

public class Enum{
	
	public enum Table {
		userdetails(User.class),
		mails(Email.class),
		phonenumbers(PhoneNumber.class),
		usergroups(Group.class),
		groupcontacts(Group.class),
		contactdetails(Contact.class),
		contactemail(Email.class),
		contactnumber(PhoneNumber.class),
		usersessions(Session.class),
		audit_logs;
		
		private final Class<?> clazz;
		
		public Class<?> getClazz() {
			return clazz;
		}
		Table(Class<?>clazz){
			this.clazz=clazz;
		}
		Table(){
			this.clazz = null;
		}
		

	
	}
	
	public enum userdetails implements Column {
		User_ID,
	    Password,
	    Username,
	    Gender,
	    Birthday,
	    Location,
		dtformat,
		created_time,
		modified_time;

	    public String getColumnName() {
	        return "userdetails."+this.name();
	    }
	    public String getSimpleColumnName() {
	        return this.name();
	    }
	    public Column getPrimaryKey() {
	        return User_ID;
	    }
	}
	
	public enum mails implements Column {
	    User_ID,
	    Mail,
	    created_time,
		modified_time;
	
	    @Override
	    public String getColumnName() {
	        return "mails."+this.name();
	    }
	    public String getSimpleColumnName() {
	        return this.name();
	    }
	    public Column getPrimaryKey() {
	        return User_ID;
	    }
	}
	public enum phonenumbers implements Column{
		User_Id,
		Phone_number,
		created_time,
		modified_time;
		
		
		public String getColumnName() {
			return "phonenumbers."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return User_Id;
	    }
	}
	
	
	public enum usergroups implements Column{
		Group_ID,
		User_ID,
		Group_Name,
		created_time,
		modified_time;
		public String getColumnName() {
			return "usergroups."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return Group_ID;
	    }
	}
	public enum groupcontacts implements Column{
		Group_ID,
		Contact_ID,
		created_time,
		modified_time;
		public String getColumnName() {
			return "groupcontacts."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return Group_ID;
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
		created_time,
		modified_time;
		public String getColumnName() {
			return "contactdetails."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return Contact_ID;
	    }
	}
	public enum contactemail implements Column{
		Email_ID,
		Contact_ID,
		Email,
		created_time;
		public String getColumnName() {
			return "contactemail."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return Email_ID;
	    }
	}
	public enum contactnumber implements Column{
		Number_ID,
		Contact_ID,
		Phone_number,
		created_time;
		public String getColumnName() {
			return "contactnumber."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return Number_ID;
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
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return Session_ID;
	    }
	}
	public enum audit_logs implements Column{
		logId,
		tableName,
		action,
		oldValue,
		newValue,
		timestamp;
		
		public String getColumnName() {
			return "audit_logs."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return logId;
	    }
	}
}


