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
		audit_logs,
		oauth_tokens(Token.class),
		oauth_identity,
		servers(Server.class);
		
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
		modified_time,
		login_method;

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
	
	
	public enum oauth_tokens implements Column{
		id,
		user_id,
		unique_id,
		email,
		access_token,
		refresh_token,
		created_time,
		modified_time,
		provider,
		sync_interval,
		last_sync_time;
		
		public String getColumnName() {
			return "oauth_tokens."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return id;
	    }
	}
	
	
	public enum oauth_identity implements Column{
		id,
		user_id,
		provider,
		unique_identifier,
		created_time,
		modified_time;
		
		public String getColumnName() {
			return "oauth_identity."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return id;
	    }
	}
	
	
	public enum servers implements Column{
		server_id,
		ip_address,
		port_number,
		created_time;
		
		public String getColumnName() {
			return "servers."+ this.name();
		}
		public String getSimpleColumnName() {
	        return this.name();
	    }
		public Column getPrimaryKey() {
	        return server_id;
	    }
	}
	
	
	public enum EnumComparator {
	    EQUAL("="),
	    GREATER_THAN(">"),
	    LESS_THAN("<"),
	    GREATER_THAN_OR_EQUAL(">="),
	    LESS_THAN_OR_EQUAL("<="),
	    NOT_EQUAL("!="),
	    LIKE("LIKE");

	    private final String symbol;

	    EnumComparator(String symbol) {
	        this.symbol = symbol;
	    }

	    public String getSymbol() {
	        return symbol;
	    }
	}
	
	public enum EnumConjunction {
	    AND("AND"),
	    OR("OR");

	    private final String conjucntion;

	    EnumConjunction(String conjucntion) {
	        this.conjucntion = conjucntion;
	    }

	    public String getConjucntion() {
	        return conjucntion;
	    }
	}
	
	public enum EnumJoin {
	    INNER_JOIN("INNER JOIN"),
	    OUTER_JOIN("OUTER JOIN"),
	    LEFT_JOIN("LEFT JOIN"),
	    RIGHT_JOIN("RIGHT JOIN");

	    private final String join;

	    EnumJoin(String join) {
	        this.join = join;
	    }

	    public String getJoin() {
	        return join;
	    }
	}
	
	public enum EnumOrder {
	    ASC("ASC"),
	    DESC("DESC");

	    private final String order;

	    EnumOrder(String order) {
	        this.order = order;
	    }

	    public String getOrder() {
	        return order;
	    }
	}
	
}


