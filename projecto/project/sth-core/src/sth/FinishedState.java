package sth;

import sth.exceptions.NonEmptySurveySelectionException;
import sth.exceptions.SurveyFinishedSelectionException;
import sth.exceptions.FinishingSurveySelectionException;

class FinishedState extends Survey.SurveyState {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811241603L;

	FinishedState(Survey survey) {
		survey.super();
	}

	@Override
	//Cancelar um inquérito finalizado é impossível, pelo que deve ser assinalado um erro
	void cancel(String disc, Project proj) throws NonEmptySurveySelectionException, 
													SurveyFinishedSelectionException {

		throw new SurveyFinishedSelectionException(disc, proj.getName());
	}

	@Override
	// Se o inquérito ja estiver finalizado, a operação não tem efeito
	void finish(String disc, String proj) throws FinishingSurveySelectionException {
		// intentionally left blanc
	}
}