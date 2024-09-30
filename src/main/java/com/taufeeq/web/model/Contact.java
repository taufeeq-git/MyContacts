package com.taufeeq.web.model;

import java.util.List;

public class Contact {
    private int userId;
    private int ContactId;
    private String username;
    private String gender;
    private String birthday;
    private int favorite;
    private int archive;
    public String email,phone;
    public List<String> emails;        
    public List<String> phoneNumbers;  

   
public int getContactId() {
		return ContactId;
	}


	public void setContactId(int contactId) {
		ContactId = contactId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	//    public Contact(int userId, String username, String gender, String birthday, String location, int favorite, int archive) {
//        this.userId = userId;
//        this.username = username;
//        this.gender = gender;
//        this.birthday = birthday;
//        this.favorite=favorite;
//        this.archive=archive;
//        
//    }
    public Contact() {
    	
    }

  
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

 

    public int getFavorite() {
		return favorite;
	}


	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}


	public int getArchive() {
		return archive;
	}


	public void setArchive(int archive) {
		this.archive = archive;
	}


	public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
