package sth;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

class MailBox implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID =201811091918L;
	private List<String> _inBox = new ArrayList<String>();

	private List<String> getInBox() {
		return _inBox;
	}

	void addMail(String note) {
		_inBox.add(note);
	}

	List<String> seeMail() {
		List<String> all = new ArrayList<String>(_inBox);
		_inBox.clear();

		return all;
	}
}