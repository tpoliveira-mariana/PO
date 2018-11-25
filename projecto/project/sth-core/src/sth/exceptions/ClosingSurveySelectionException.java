package sth.exceptions;

public class ClosingSurveySelectionException extends Exception {

	/** Serial number for serialization. */
 	private static final long serialVersionUID = 201811241518L;
 	private String _discipline;
 	private String _project;

 	public ClosingSurveySelectionException(String disc, String proj) {
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