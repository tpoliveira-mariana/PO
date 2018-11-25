package sth.exceptions;

public class NonEmptySurveySelectionException extends Exception {

	/** Serial number for serialization. */
 	private static final long serialVersionUID = 201811241510L;
 	private String _discipline;
 	private String _project;

 	public NonEmptySurveySelectionException(String disc, String proj) {
 		_discipline = disc;
 		_project = proj;
 	}

 	public String getDiscipline() {
 		return _discipline;
 	}

 	public String getProject() {
 		return _project;
 	}
}