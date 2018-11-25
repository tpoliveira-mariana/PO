package sth;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import sth.exceptions.InvalidPhoneNumberException;

abstract class Person implements Comparable<Person>, Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811081101L;	
	private String _name = "";
	private String _phoneNumber = "";
	private int _id;
	private MailBox _mail = new MailBox();

	Person(String name, String phoneN, int id) {
		_name = name;
		_phoneNumber = phoneN;
		_id = id;
	}

	
	//========== GETTERS ===========//

	int getId() {
		return _id;
	}

	String getPhoneNumber() {
		return _phoneNumber;
	}

	String getName() {
		return _name;
	}
	

	List<String> seeAllNotifications() {
		return _mail.seeNotifications();
	}


	//========== SETTERS ===========//

	void changePhoneNumber(String number) throws InvalidPhoneNumberException {
		if (phoneNumberIsValid(number))
			_phoneNumber = number;
		else
			throw new InvalidPhoneNumberException(number);
	}

	void receiveMail(String mail) {
		_mail.addNotification(mail);
	}


	//========== BOOLEANS ===========//

	private boolean phoneNumberIsValid(String number) {
		int numberDigits = number.length();
		if (numberDigits == 9) {
			if (number.charAt(0) >= '1' && number.charAt(0) <= '9') {
				for (int i = 1; i < numberDigits; i++) 
					if (number.charAt(i) < '0' || number.charAt(i) > '9')
						return false;

				return true;
			}
			return false;
		} 
		return false;
	}	


	//========== SHOW ===========//

	@Override
	public String toString() {
		return "" + getId() + "|" + getPhoneNumber() + "|" + getName();
	}


	//========== COMAPARATORS ===========//

	@Override
	public int compareTo(Person other) {
		return getId() - other.getId();
	}

//	void enableNotificationsFrom(Disicipline disc) {}

//	void disableNotificationsFrom(Discipline disc) {}
}