package sth;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import sth.Observable;

class Discipline extends sth.Observable implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID =201811081105L;
	private int _maxStudents;	
	private String _name = "";
	private int _currentStudents = 0;
	private HashMap<Integer, Professor> _professors = new HashMap<Integer, Professor>();
	private TreeMap<Integer, Student> _students = new TreeMap<Integer, Student>();
	private HashMap<String, Project> _projects = new HashMap<String, Project>();

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

		return proj.showSubmissions();
	}
//


//========== NOTIFICATION ===========//

	void sendOpenNotification(String projName) {
		String notification = "Pode preencher inquérito do projeto " + projName + 
								" da disciplina " + getName();

		notifyObservers(notification);
	}

	void sendFinishNotification(String projName) {
		String notification = "Resultados do inquérito do projeto " + projName + 
								" da disciplina " + getName();

		notifyObservers(notification);
	}
//
}
