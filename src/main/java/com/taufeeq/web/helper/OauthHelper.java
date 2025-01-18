package com.taufeeq.web.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.json.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.taufeeq.web.config.GoogleAuthConfig;
import com.taufeeq.web.dao.ContactDAO;
import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.dao.TokenDAO;
import com.taufeeq.web.dao.TokenDAOImpl;
import com.taufeeq.web.model.Contact;

public class OauthHelper {
	public static JSONObject exchangeCodeForTokens(String code, String redirectUri)
			throws IOException, URISyntaxException {
		String postData = "code=" + code + "&client_id=" + GoogleAuthConfig.CLIENT_ID + "&client_secret="
				+ GoogleAuthConfig.CLIENT_SECRET + "&redirect_uri=" + redirectUri + "&grant_type=authorization_code";

		URI uri = new URI(GoogleAuthConfig.TOKEN_URL);
		HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);

		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = postData.getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);
		}

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			return new JSONObject(response.toString());
		}
	}

	public static JSONObject getUserInfoFromGoogle(String accessToken) throws IOException, URISyntaxException {
		URI uri = new URI(
				GoogleAuthConfig.PEOPLE_API_BASE_URL + "?personFields=names,emailAddresses,genders,addresses");
		HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			return new JSONObject(response.toString());
		}
	}

	public static String getValidAccessToken(int tokenId) throws Exception {
		TokenDAO tokenDAO = new TokenDAOImpl();
		String accessToken = tokenDAO.getAccessToken(tokenId);

		if (tokenDAO.isTokenExpired(tokenId)) {
			String refreshToken = tokenDAO.getRefreshToken(tokenId);
			if (refreshToken != null) {

				JSONObject newTokens = refreshAccessToken(refreshToken);
				accessToken = newTokens.getString("access_token");

				tokenDAO.updateAccessToken(tokenId, accessToken);
			} else {
				return null;
			}
		}

		return accessToken;
	}

	private static JSONObject refreshAccessToken(String refreshToken) throws Exception {
		String postData = "client_id=" + GoogleAuthConfig.CLIENT_ID + "&client_secret=" + GoogleAuthConfig.CLIENT_SECRET
				+ "&refresh_token=" + refreshToken + "&grant_type=refresh_token";

		URI uri = new URI("https://oauth2.googleapis.com/token");
		HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);

		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = postData.getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);
		}

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			return new JSONObject(response.toString());
		}
	}

	public static List<Contact> fetchGoogleContacts(String accessToken) throws Exception {
		List<Contact> contacts = new ArrayList<>();
		String pageToken = null;

		do {
			String url = "https://people.googleapis.com/v1/people/me/connections"
					+ "?personFields=names,phoneNumbers,emailAddresses" + "&pageSize=1000"
					+ (pageToken != null ? "&pageToken=" + pageToken : "");

			URI uri = new URI(url);
			HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			StringBuilder response = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
				String line;
				while ((line = br.readLine()) != null) {
					response.append(line);
				}
			}

			JSONObject responseJson = new JSONObject(response.toString());
			JSONArray connections = responseJson.optJSONArray("connections");

			if (connections != null) {
				for (int i = 0; i < connections.length(); i++) {
					JSONObject person = connections.getJSONObject(i);
					Contact contact = extractContactInfo(person);
					if (contact != null) {
						contacts.add(contact);
					}
				}
			}

			pageToken = responseJson.optString("nextPageToken", null);

		} while (pageToken != null);

		return contacts;
	}

	private static Contact extractContactInfo(JSONObject person) {
		String name = null;
		JSONArray names = person.optJSONArray("names");
		if (names != null && names.length() > 0) {
			name = names.getJSONObject(0).getString("displayName");
		}

		List<String> phoneNumbersList = new ArrayList<>();
		JSONArray phoneNumbers = person.optJSONArray("phoneNumbers");
		if (phoneNumbers != null) {
			for (int i = 0; i < phoneNumbers.length(); i++) {
				JSONObject phoneNumberObject = phoneNumbers.getJSONObject(i);
				String phoneNumber = phoneNumberObject.optString("canonicalForm", phoneNumberObject.optString("value"));
				phoneNumbersList.add(phoneNumber);
			}
		}
		List<String> emailList = new ArrayList<>();
		JSONArray emails = person.optJSONArray("emailAddresses");
		if (emails != null) {
			for (int i = 0; i < emails.length(); i++) {
				String email = emails.getJSONObject(i).getString("value");
				emailList.add(email);
			}
		}

		Contact contact = new Contact();
		contact.setUsername(name);
		contact.setPhoneNumbers(phoneNumbersList);
		contact.setEmails(emailList);
		contact.setCreated_time(System.currentTimeMillis() / 1000);
		return contact;
	}

	public static String emailFromIdToken(String idToken) {
		String[] parts = idToken.split("\\.");
		String payload = parts[1];

		String decodedPayload = new String(Base64.getUrlDecoder().decode(payload));

		JsonObject jsonPayload = new Gson().fromJson(decodedPayload, JsonObject.class);

		String email = jsonPayload.get("email").getAsString();
		return email;
	}

	public static String subFromIdToken(String idToken) {
		String[] parts = idToken.split("\\.");
		String payload = parts[1];

		String decodedPayload = new String(Base64.getUrlDecoder().decode(payload));

		JsonObject jsonPayload = new Gson().fromJson(decodedPayload, JsonObject.class);

		String sub = jsonPayload.get("sub").getAsString();
		return sub;
	}

	public static int syncContacts(int userId, List<Contact> googleContacts) {
		int newContactsCount = 0;
		for (Contact contact : googleContacts) {
			if (contact.getPhoneNumbers() == null || contact.getPhoneNumbers().isEmpty()) {
				continue;
			}

			contact.setUserId(userId);
			contact.setFavorite(false);
			contact.setArchive(false);

			ContactDAO contactDAO = new ContactDAOImpl();

			String primaryPhone = contact.getPhoneNumbers().get(0);

			if (!contactDAO.doesContactExist(userId, primaryPhone)) {
				int contactId = contactDAO.addContact(contact);

				for (String phoneNumber : contact.getPhoneNumbers()) {
					contactDAO.addContactPhoneNumber(contactId, phoneNumber);
				}

				if (contact.getEmails() != null && !contact.getEmails().isEmpty()) {
					for (String email : contact.getEmails()) {
						contactDAO.addContactEmail(contactId, email);
					}
				}

				newContactsCount++;
			}
		}
		return newContactsCount;
	}

}
