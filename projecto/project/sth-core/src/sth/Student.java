package sth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import sth.exceptions.NoSuchDisciplineSelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.NoSurveySelectionException;


class Student extends Person implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811081104L;
	private static final int MAX_DISCIPLINES = 6;
	private Map<String, Discipline> _disciplines = new HashMap<String, Discipline>();	

	Student(String name, String number, int id) {
		super(name, number, id);
	}


//========== GETTERS ===========//

	Discipline getDisciplineByName(String discName) {
		return _disciplines.get(discName);
	}
//	
	
//========== SETTERS ===========//

	void enrollDiscipline(Discipline disc) {
		String key = disc.getName();
		if ((!(_disciplines.containsKey(key))))
			_disciplines.put(key, disc);
	}
//

//========== BOOLEANS ===========//
	
	boolean canEnrollDiscipline() {
		return _disciplines.size() < MAX_DISCIPLINES;
	}

	boolean attendsDiscipline(String discName) {
		return _disciplines.containsKey(discName);
	}
//
	
//========== SHOW ===========//	

    @Override
    public String accept(Visitor v) {
        return v.visit(this);
    }
//

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
//

//========== SURVEY ===========//

  void answerSurvey(String discName, String projName, int hours, String message) 
                          											throws NoSuchProjectSelectionException,
    											                      NoSurveySelectionException {

    Project proj = validateProject(discName, projName);
    proj.addSurveyAnswer(getId(), discName, hours, message);
  }

  String seeSurveyResults(String discName, String projName)
                                                    throws NoSuchDisciplineSelectionException,
                                                    NoSuchProjectSelectionException,
                                                    NoSurveySelectionException {


    Discipline disc = getDisciplineByName(discName);
    Project proj = disc.getProject(projName);

    if (proj == null || (!proj.hasSubmitted(getId()))) {
      throw new NoSuchProjectSelectionException(discName, projName);
    }

    List<String> results = proj.showSurveyResults(discName);

    return accept(new SurveyPrinter(results));
  }
//	
}
