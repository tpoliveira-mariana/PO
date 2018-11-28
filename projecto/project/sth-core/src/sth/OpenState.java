package sth;

import java.io.Serializable;
import sth.exceptions.NonEmptySurveySelectionException;
import sth.exceptions.SurveyFinishedSelectionException;
import sth.exceptions.OpeningSurveySelectionException;
import sth.exceptions.ClosingSurveySelectionException;
import sth.exceptions.NoSurveySelectionException;

class OpenState extends Survey.SurveyState {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811241526L;

	OpenState(Survey survey) {
		survey.super();
	}

	// Excecao se o inquerito ja tiver, no min, 1 resposta
	// Remove-se o inquérito em caso contrário
	@Override
	void cancel(String disc, Project proj) throws NonEmptySurveySelectionException, 
													SurveyFinishedSelectionException {

		if (!getSurvey().isEmpty()) {
			throw new NonEmptySurveySelectionException(disc, proj.getName());
		}
		proj.surveyRemoved();
	}

	@Override
	// Um inquérito aberto pode ser fechado
	void close(String disc, String proj) throws ClosingSurveySelectionException {
		getSurvey().setState(new ClosedState(getSurvey()));
	}

	@Override
	// Um aluno pode responder a um inquérito aberto
	SurveyAnswer answer(String disc, String proj, int hours, String msg) throws NoSurveySelectionException {
		Survey survey = getSurvey();
		return new SurveyAnswer(hours, msg);
	}

	@Override
	String showResults(School school, Person p, String discName, Project proj) {
		return discName + " - " + proj.getName() + " (aberto)";
	}
}