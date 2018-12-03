package sth;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import sth.Observable;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.NoSurveySelectionException;
import sth.exceptions.DuplicateSurveySelectionException;
import sth.exceptions.OpeningSurveySelectionException;
import sth.exceptions.NonEmptySurveySelectionException;
import sth.exceptions.SurveyFinishedSelectionException;
import sth.exceptions.ClosingSurveySelectionException;
import sth.exceptions.FinishingSurveySelectionException;

class Discipline extends sth.Observable implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID =201811081105L;
	private int _maxStudents;	
	private String _name = "";
	private int _currentStudents = 0;
	private Map<Integer, Professor> _professors = new HashMap<Integer, Professor>();
	private Map<Integer, Student> _students = new TreeMap<Integer, Student>();
	private Map<String, Project> _projects = new TreeMap<String, Project>();

	Discipline(String name) {
		_name = name;
		_maxStudents = 10 + ((int) Math.random() * 200);
	}	

//========== GETTERS ===========//

	String getName() {
		return _name;
	}

	int getMaxStudents() {
		return _maxStudents;
	}

	Project getProject(String projName) {
		return _projects.get(projName);
	}

	Project validateProject(String projName) throws NoSuchProjectSelectionException {
		if (!projectExists(projName)) {
			throw new NoSuchProjectSelectionException(getName(), projName);
		}

		return getProject(projName);
	}
//	

//========== SETTERS ===========//

	void setMaxStudents(int max) {
		_maxStudents = max;
	}

	void addProfessor(Professor prof) {
		int id = prof.getId();
		if (!professorExists(id))
			_professors.put(id, prof);
	}

	void addStudent(Student student) {
		int id = student.getId();
		if (!studentExists(id)) {
			_students.put(id, student);
		}
		_currentStudents++;
	}

	void addProject(Project proj) {
		_projects.put(proj.getName(), proj);
	}
//

//========== BOOLEANS ===========//

	boolean canAddStudent() {
		return _students.size() < _maxStudents;
	}

	boolean professorExists(int id) {
		return _professors.containsKey(id);
	}

	boolean studentExists(int id) {
		return _students.containsKey(id);
	}

	boolean projectExists(String projName) {
		return _projects.containsKey(projName);
	}

	boolean projectIsOpen(String projName) {
		Project proj = _projects.get(projName);

		return proj.isOpen();
	}
//

//========== SHOW ===========//

	List<String> showStudents(School school) {
		List<String> studentsInfo = new ArrayList<String>();

		for (Student student : _students.values())
        	studentsInfo.add(school.showPerson(student));

      	return studentsInfo;
	}

	List<String> showProjectSubmissions(String projName) {
		Project proj = getProject(projName);

		return proj.showSubmissions(getName());
	}

	String showSurveyResults(School school, Student student, String projName, boolean isRep) 
														throws NoSurveySelectionException,
														NoSuchProjectSelectionException {
		Project proj = getProject(projName);

		if (!proj.surveyAlreadyExists()) {
			throw new NoSurveySelectionException(getName(), projName);
		}

		else if (!isRep && !proj.hasSubmitted(student.getId())) {
			throw new NoSuchProjectSelectionException(getName(), projName);
		}

		return proj.showSurveyResults(school, student,getName());
	}

	List<String> showSurveys(School school, Person rep) throws NoSurveySelectionException {
		List<String> allSurveys = new ArrayList<String>();

		for (Project proj : _projects.values()) {
			if (proj.surveyAlreadyExists()) {
				allSurveys.add(proj.showSurveyResults(school, rep, getName()));
			}
		}

		return allSurveys;
	}
//

//========== SURVEYS ===========//
	void createSurvey(String projName) throws NoSuchProjectSelectionException, 
											  DuplicateSurveySelectionException {

		if (!projectExists(projName) || !projectIsOpen(projName)) {
			throw new NoSuchProjectSelectionException(getName(), projName);
		}

		getProject(projName).addSurvey(getName());
	}

	void openSurvey(String projName) throws NoSuchProjectSelectionException,
                                            NoSurveySelectionException, 
                                            OpeningSurveySelectionException {


		if (!projectExists(projName)) {
			throw new NoSuchProjectSelectionException(getName(), projName);
		}

		getProject(projName).openSurvey(this);
    }

    void cancelSurvey(String projName) throws NoSuchProjectSelectionException,
                                              NoSurveySelectionException,
                                              NonEmptySurveySelectionException,
                                              SurveyFinishedSelectionException {

    	if (!projectExists(projName)) {
			throw new NoSuchProjectSelectionException(getName(), projName);
		}

		getProject(projName).cancelSurvey(getName());
    }

    void closeSurvey(String projName) throws NoSuchProjectSelectionException,
                                             NoSurveySelectionException,
                                             ClosingSurveySelectionException {

    	if (!projectExists(projName)) {
			throw new NoSuchProjectSelectionException(getName(), projName);
		}

		getProject(projName).closeSurvey(getName());
    }

    void finishSurvey(String projName) throws NoSuchProjectSelectionException,
                                              NoSurveySelectionException,
                                              FinishingSurveySelectionException {
    	if (!projectExists(projName)) {
			throw new NoSuchProjectSelectionException(getName(), projName);
		}

		getProject(projName).finishSurvey(this);
    }
//

//========== NOTIFICATION ===========//

	void sendOpenNotification(String projName) {
		String notification = "Pode preencher inquérito do projecto " + projName + 
								" da disciplina " + getName();

		notifyObservers(notification);
	}

	void sendFinishNotification(String projName) {
		String notification = "Resultados do inquérito do projecto " + projName + 
								" da disciplina " + getName();

		notifyObservers(notification);
	}
//
}
