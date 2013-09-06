package com.app.closet;

public class UserData {
	public static final String	EMAIL_KEY			= "email";
	public static final String	NAME_KEY			= "Name";
	public static final String	USERNAME_KEY		= "username";
	public static final String	SECONDARY_NAME_KEY	= "SecondaryName";
	public static final String	PROFILE_PICTURE_KEY	= "ProfilePicture";
	public static final String	USER_ID_KEY			= "objectId";
	public static final String	OBJECT_CLOSET_KEY	= "Closet";
	public static final String	OBJECT_FRIEND_KEY	= "friends";

	public static String				_username, _name, _email, _secondaryName;
	public static byte[]				_profileImage;

	public UserData() {
		_username = "";
		_name = "";
		_email = "";
		_secondaryName = "";
		_profileImage = null;
	}

	public String getUsername() {
		return _username;
	}

	public String getName() {
		return _name;
	}

	public String getEmail() {
		return _email;
	}

	public String getSecondayName() {
		return _secondaryName;
	}

	public byte[] getProfilePicture() {
		return _profileImage;
	}
	public void setUsername (String username) {
		_username = username;
	}
	public void setName (String name) {
		_name = name;
	}
	public void setEmail (String email) {
		_email = email;
	}
	public void setImage (byte[] image) {
		_profileImage = image;
	}

}
