package sth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import sth.exceptions.DuplicateProjectSelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.OpeningSurveySelectionException;


class Professor extends Person implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811081103L;
	private HashMap<String, Discipline> _disciplines = new HashMap<String, Discipline>();

	Professor(String name, String number, int id) {
		super(name, number, id);
	}

	//========== GETTERS ===========//

	private Discipline getDisciplineByName(String discName) {
		return _disciplines.get(discName);
	}


	//========== SETTERS ===========//

	void addDiscipline(Discipline disc) {
		String key = disc.getName();
		if (!(_disciplines.containsKey(key)))
			_disciplines.put(key, disc);
	}


	//========== BOOLEANS ===========//

	boolean teachesDiscipline(String discName) {
		return _disciplines.containsKey(discName);
	}


	//========== SHOW ===========//

	@Override
	public String toString() {
		return "DOCENTE|" + super.toString();
	}
	

	//========== PROJECT ===========//

	void createProject(String discName, String projName) throws DuplicateProjectSelectionException {
		Discipline disc = getDisciplineByName(discName);
      	if (disc.projectExists(projName)) {
      		throw new DuplicateProjectSelectionException(discName, projName);
		}

		Project project = new Project(projName);
		disc.addProject(project);
	}

	void closeProject(String discName, String projName) throws NoSuchProjectSelectionException,
																OpeningSurveySelectionException {
		Discipline disc = getDisciplineByName(discName);
     	if (!disc.projectExists(projName)) {
     		throw new NoSuchProjectSelectionException(discName, projName);
     	}

		Project proj = disc.getProject(projName);
		proj.close(discName);
	}

	List<String> seeDisciplineStudents(School school, String discName) {
		Discipline disc = getDisciplineByName(discName);

		return disc.showStudents(school);      	
	}

	List<String> seeProjectSubmissions(String discName, String projName) 
														throws NoSuchProjectSelectionException {
		Discipline disc = getDisciplineByName(discName);
     	if (!disc.projectExists(projName)) {
     		throw new NoSuchProjectSelectionException(discName, projName);
     	}

		return disc.showProjectSubmissions(projName);
	}
}