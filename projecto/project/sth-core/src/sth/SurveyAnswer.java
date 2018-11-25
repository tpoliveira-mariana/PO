package sth;

import java.io.Serializable;

class SurveyAnswer implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811241614L;
	private int _hoursSpent;
	private String _comment;

	SurveyAnswer(int hours, String msg) {
		_hoursSpent = hours;
		_comment = msg;
	}

	int getHoursSpent() {
		return _hoursSpent;
	}

	String getComment() {
		return _comment;
	}
}