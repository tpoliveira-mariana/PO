package sth;

import java.util.Set;
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
		// intentionally left blank
	}

	@Override
	String showResults(School school, Person p, String discName, Project proj) {
		String results = discName + " - " + proj.getName();
		
		int minHours =-1, maxHours = -1, totalHours = 0;
		Set<SurveyAnswer> answers = getSurvey().getAnswers();
		for (SurveyAnswer answer : answers) {
			int hoursSpent = answer.getHoursSpent();
			totalHours += hoursSpent;
			minHours = (minHours == -1 ? hoursSpent : Math.min(minHours, hoursSpent));
			maxHours = Math.max(maxHours, hoursSpent);
		}

		int numAnswers = getSurvey().getNumAnswers();
		int avgTime = (numAnswers == 0 ? 0 : totalHours/numAnswers);

		if (school.professorExists(p.getId())) {
			results += "\n * Número de submissões: " + proj.numSubmissions() + "\n" + 
						 " * Número de respostas: " + numAnswers + "\n" +
						 " * Tempos de resolução (horas) (mínimo, médio, máximo): " +
						minHours + ", " + avgTime + ", " + maxHours;
		}
		else if (school.representativeExists(p.getId())) {
			results += " - Número de respostas " + numAnswers + 
						" - Tempo médio de execução " + avgTime; 
		}
		else {
			results += "\n * Número de submissões: " + proj.numSubmissions() + "\n" +
					" * Tempo médio (horas): " + avgTime;
		}

		return results;
	}
}
