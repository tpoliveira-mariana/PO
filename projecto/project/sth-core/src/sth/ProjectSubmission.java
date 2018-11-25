package sth;

import java.io.Serializable;

class ProjectSubmission implements Serializable {

	/** Serial number for serialization. */
 	private static final long serialVersionUID = 201811101154L;
 	private String _description;
 	private Student _student;
 	private boolean _answeredSurvey = false;

 	ProjectSubmission(Student student, String deliveryMessage) {
 		_description = deliveryMessage;
 		_student = student;
 	}


 	//========== GETTERS ===========//

 	String getDescription() {
 		return _description;
 	}

 	boolean answeredSurvey() {
 		return _answeredSurvey;
 	}

 	//========== SETTERS ===========//

 	void surveyAnswered() {
 		_answeredSurvey = true;
 	}

 	//========== SHOW ===========//

 	@Override
 	public String toString() {
 		return "* " +  _student.getId() + " - " + _description;
 	}
}