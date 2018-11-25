package sth;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import sth.exceptions.NoSurveySelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.DuplicateSurveySelectionException;
import sth.exceptions.OpeningSurveySelectionException;
import sth.exceptions.NonEmptySurveySelectionException;
import sth.exceptions.SurveyFinishedSelectionException;
import sth.exceptions.ClosingSurveySelectionException;

class Project implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811081106L;

	private String _name;
	private String _description = "";
	private boolean _isOpen = true; 
	private Map<Integer, ProjectSubmission> _submissions = new TreeMap<Integer, ProjectSubmission>();
	private Survey _survey = null;
	
	Project(String name) {
		_name = name;
	}

	Project(String name, String dscrpt) {
		this(name);
		_description = dscrpt;
	}


	//========== GETTERS ===========//

	String getName() {
		return _name;
	}

	String getDescription() {
		return _description;
	}

	ProjectSubmission getSubmission(int id) {
		return _submissions.get(id);
	}


	//========== SETTERS ===========//

	void close(String discName) throws OpeningSurveySelectionException {
		_isOpen = false;
		if (surveyAlreadyExists()) {
			_survey.open(discName, getName());
		}
	}

	void addSubmission(Student student, String message) {
		ProjectSubmission submission = new ProjectSubmission(student, message);
		_submissions.put(student.getId(), submission);
	}

	void surveyRemoved() {
		_survey = null;
	}

	void surveyAnswered(int id) {
		ProjectSubmission sub = getSubmission(id);
		sub.surveyAnswered();
	}


	//========== BOOLEANS ===========//

	boolean isOpen() {
		return _isOpen;
	}

	boolean surveyAlreadyExists() {
		return _survey != null;
	}

	boolean hasSubmitted(int id) {
		return _submissions.containsKey(id);
	}

	boolean answeredSurvey(int id) {
		ProjectSubmission sub = getSubmission(id);
		return sub.answeredSurvey();	
	}


	//========== SHOW ===========//

	List<String> showSubmissions() {
		List<String> result = new ArrayList<String>();

		for (ProjectSubmission sub : _submissions.values()) {
			String info = sub.toString();
			result.add(info);
		}

		return result;
	}

	//========== SURVEY ===========//

	void addSurveyAnswer(int id, String discName, int hours, String message) 
														throws NoSuchProjectSelectionException,
														NoSurveySelectionException {

		if (!hasSubmitted(id)) {
			throw new NoSuchProjectSelectionException(discName, getName());
		}

		else if (!surveyAlreadyExists()) {
			throw new NoSurveySelectionException(discName, getName());
		}

		else if (!answeredSurvey(id)) {
			_survey.answer(discName, getName(), hours, message);
			surveyAnswered(id);
		}
	}

	void addSurvey(String discName) throws DuplicateSurveySelectionException {
		if (surveyAlreadyExists()) {
			throw new DuplicateSurveySelectionException(discName, getName());
		}

		_survey = new Survey();	
	}

	void openSurvey(String discName) throws NoSurveySelectionException,
											OpeningSurveySelectionException {
		if (!surveyAlreadyExists()) {
			throw new NoSurveySelectionException(discName, getName());
		}

		_survey.open(discName, getName());
	}

	void cancelSurvey(String discName) throws NoSurveySelectionException,
											  NonEmptySurveySelectionException,
											  SurveyFinishedSelectionException {

		if (!surveyAlreadyExists()) {
			throw new NoSurveySelectionException(discName, getName());
		}

		_survey.cancel(discName, this);
	}

	void closeSurvey(String discName) throws NoSurveySelectionException,
											 ClosingSurveySelectionException {

		if (!surveyAlreadyExists()) {
			throw new NoSurveySelectionException(discName, getName());
		}

		_survey.close(discName, getName());
	}
}