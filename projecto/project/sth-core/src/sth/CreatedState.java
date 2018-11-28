package sth;

import sth.exceptions.NonEmptySurveySelectionException;
import sth.exceptions.SurveyFinishedSelectionException;
import sth.exceptions.OpeningSurveySelectionException;

class CreatedState extends Survey.SurveyState {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811241637L;

	CreatedState(Survey survey) {
		survey.super();
	}

	@Override
	// Cancelar um inquérito criado corresponde a remover o inquérito
	void cancel(String disc, Project proj) throws NonEmptySurveySelectionException, 
													SurveyFinishedSelectionException {

		proj.surveyRemoved();
	}

	@Override
	// Um inquérito criado é aberto quando o projeto que lhe está associado é fechado
	void open(Discipline disc, String proj) throws OpeningSurveySelectionException {
		getSurvey().setState(new OpenState(getSurvey()));
	}

	@Override
	String showResults(School school, Person p, String discName, Project proj) {
		return discName + " - " + proj.getName() + " (por abrir)";
	}
}