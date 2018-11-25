package sth;

import java.io.Serializable;
import java.util.HashMap;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.NoSurveySelectionException;
import sth.exceptions.DuplicateSurveySelectionException;
import sth.exceptions.OpeningSurveySelectionException;
import sth.exceptions.NonEmptySurveySelectionException;
import sth.exceptions.SurveyFinishedSelectionException;
import sth.exceptions.ClosingSurveySelectionException;


class Student extends Person implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811081104L;
	private static final int MAX_DISCIPLINES = 6;
	private HashMap<String, Discipline> _disciplines = new HashMap<String, Discipline>();	

	Student(String name, String number, int id) {
		super(name, number, id);
	}


	//========== GETTERS ===========//

	Discipline getDisciplineByName(String discName) {
		return _disciplines.get(discName);
	}
	
	
	//========== SETTERS ===========//

	void enrollDiscipline(Discipline disc) {
		String key = disc.getName();
		if ((!(_disciplines.containsKey(key))))
			_disciplines.put(key, disc);
	}


	//========== BOOLEANS ===========//
	
	boolean canEnrollDiscipline() {
		return _disciplines.size() < MAX_DISCIPLINES;
	}

	boolean attendsDiscipline(String discName) {
		return _disciplines.containsKey(discName);
	}

	
	//========== SHOW ===========//	

	@Override
	public String toString() {
		return "ALUNO|" + super.toString();
	}

	//========== PROJECT ===========//

	Project validateProject(String discName, String projName) throws NoSuchProjectSelectionException {
     	
     	Discipline disc = getDisciplineByName(discName);
    	if (!disc.projectExists(projName)) {
    		throw new NoSuchProjectSelectionException(discName, projName);
    	}

    	return disc.getProject(projName);
    }

	void submitProject(String discName, String projName, String message) 
													throws NoSuchProjectSelectionException {

		Discipline disc = getDisciplineByName(discName);

     	if (disc.projectExists(projName) && disc.projectIsOpen(projName)) {
     		Project proj = disc.getProject(projName);
     		proj.addSubmission(this, message);
     	}
     	else {
     		throw new NoSuchProjectSelectionException(discName, projName);
     	}
    }


    //========== SURVEY ===========//

    void answerSurvey(String discName, String projName, int hours, String message) 
    											throws NoSuchProjectSelectionException,
    											NoSurveySelectionException {

    	Project proj = validateProject(discName, projName);
    	proj.addSurveyAnswer(getId(), discName, hours, message);
    }

    void createSurvey(String discName, String projName) 
    											throws NoSuchProjectSelectionException,
    											DuplicateSurveySelectionException {

    	Project proj = validateProject(discName, projName);
    	proj.addSurvey(discName);
    }

    void openSurvey(String discName, String projName) 
    											throws NoSuchProjectSelectionException,
    											NoSurveySelectionException, 
    											OpeningSurveySelectionException {

    	Project proj = validateProject(discName, projName);
    	proj.openSurvey(discName);
    }

    void cancelSurvey(String discName, String projName)
    											throws NoSuchProjectSelectionException,
    											NoSurveySelectionException,
    											NonEmptySurveySelectionException,
    											SurveyFinishedSelectionException {

    	Project proj = validateProject(discName, projName);
    	proj.cancelSurvey(discName);
   	}
     
    void closeSurvey(String discName, String projName)
    											throws NoSuchProjectSelectionException,
    											NoSurveySelectionException,
    											ClosingSurveySelectionException {

    	Project proj = validateProject(discName, projName);
    	proj.closeSurvey(discName);
    }

//	void promoteToRepresentative() {}
//	void demoteFromRepresentative() {}	
}
