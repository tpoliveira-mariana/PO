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
import sth.exceptions.FinishingSurveySelectionException;

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

    int numSubmissions() {
        return _submissions.size();
    }


	//========== SETTERS ===========//

	void close(Discipline disc) throws OpeningSurveySelectionException {
		_isOpen = false;
		if (surveyAlreadyExists()) {
			_survey.open(disc, getName());
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
//

//========== SHOW ===========//

	List<String> showSubmissions(String discName) {
		List<String> result = new ArrayList<String>();

		result.add(discName + " - " + getName());

		for (ProjectSubmission sub : _submissions.values()) {
			String info = sub.toString();
			result.add(info);
		}

		return result;
	}

	String showSurveyResults(School school, Person p, String discName)
													throws NoSurveySelectionException {
		if (!surveyAlreadyExists()) {
			throw new NoSurveySelectionException(discName, getName());
		}

		return _survey.showResults(school, p, discName, this);
	}
//
	
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

	void openSurvey(Discipline disc) throws NoSurveySelectionException,
											OpeningSurveySelectionException {
		if (!surveyAlreadyExists()) {
			throw new NoSurveySelectionException(disc.getName(), getName());
		}

		_survey.open(disc, getName());
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

	void finishSurvey(Discipline disc) throws NoSurveySelectionException,
											  FinishingSurveySelectionException {

		if (!surveyAlreadyExists()) {
			throw new NoSurveySelectionException(disc.getName(), getName());
		}
		_survey.finish(disc, getName());
	}
//
}
