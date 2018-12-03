package sth;

import java.util.List;
import java.util.ArrayList;
import sth.exceptions.NonEmptySurveySelectionException;
import sth.exceptions.SurveyFinishedSelectionException;
import sth.exceptions.OpeningSurveySelectionException;
import sth.exceptions.ClosingSurveySelectionException;
import sth.exceptions.FinishingSurveySelectionException;

class ClosedState extends Survey.SurveyState {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811241549L;

	ClosedState(Survey survey) {
		survey.super();
	}

	// Cancelar um inquérito fechado corresponde a abri-lo
	@Override
	void cancel(String disc, Project proj) throws NonEmptySurveySelectionException, 
													SurveyFinishedSelectionException {

		getSurvey().setState(new OpenState(getSurvey()));
	}

	// Um inquérito fechado pode ser reaberto
	@Override
	void open(Discipline disc, String proj) throws OpeningSurveySelectionException {
		getSurvey().setState(new OpenState(getSurvey()));
	}

	// Se estiver fechado, a operação não tem efeito
	@Override
	void close(String disc, String proj) throws ClosingSurveySelectionException {
		// intentionally left blanc 
	}

	// Um inquérito fechado pode ser finalizado
	@Override
	void finish(String disc, String proj) throws FinishingSurveySelectionException {
		getSurvey().setState(new FinishedState(getSurvey()));
	}

	@Override
	List<String> showResults(String discName, Project proj) {
		List<String> results = new ArrayList<String>();
		results.add("" + discName + " - " + proj.getName() + " (fechado)");
		return results;
	}
}