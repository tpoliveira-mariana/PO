package sth;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.HashMap;
import sth.exceptions.BadEntryException;


class Course implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID =201811081100L;
	private static final int MAX_REPRESENTATIVES = 7;
	private String _name;
	private int _currentRepresentatives = 0;
	private TreeMap<String, Discipline> _disciplines = new TreeMap<String, Discipline>(new AccentedComparator()); //TODO comparator que considere acentos
	private HashMap<Integer, Student> _students = new HashMap<Integer, Student>();
	private HashMap<Integer, Student> _representatives = new HashMap<Integer, Student>();
	private HashMap<Integer, Professor> _professors = new HashMap<Integer, Professor>();

	Course(String name) {
		_name = name;
	}


	//========== GETTERS ===========//

	String getName() {
		return _name;
	}

	Discipline getDiscipline(String discName) {
		return _disciplines.get(discName);
	}


	//========== SETTERS ===========//
	void addDiscipline(Discipline disc) {		
		_disciplines.put(disc.getName(), disc);
	}	

	void addStudent(Student student) {
		if (!studentExists(student.getId()))
			_students.put(student.getId(), student);
	}

	void addRepresentative(Student rep) {
		if (!representativeExists(rep.getId())) {
			_representatives.put(rep.getId(), rep);
			_currentRepresentatives++;
		}
	}

	void addProfessor(Professor prof) {
		if (!professorExists(prof.getId()))
			_professors.put(prof.getId(), prof);
	}

	void removeRepresentative(Student rep) {
		_representatives.remove(rep.getId());
	}


	//========== BOOLEANS ===========//

	boolean studentExists(int id) {
		return _students.containsKey(id);
	}

	boolean professorExists(int id) {
		return _professors.containsKey(id);
	}

	boolean representativeExists(int id) {
		return _representatives.containsKey(id);
	}

	
	boolean canAddRepresentative() {
		return _currentRepresentatives < MAX_REPRESENTATIVES;
	}


	//========== SHOW ===========//

	String showDisciplinesOf(Person p) {
		String discInfo = "";
		String courseName = getName();

		for (Discipline disc : _disciplines.values()) {
			if (disc.professorExists(p.getId())) 
				discInfo += "\n* " + courseName + " - " + disc.getName();

			else if (disc.studentExists(p.getId())) 
				discInfo += "\n* " + courseName + " - " + disc.getName();
		}

		return discInfo;
	}
}