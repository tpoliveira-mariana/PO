package sth;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
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
	List<String> showResults(String discName, Project proj) {
		List<String> results = new ArrayList<String>();
		results.add("" + discName + " - " + proj.getName());
		
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
		minHours = (minHours == -1 ? 0 : minHours);
		maxHours = (maxHours == -1 ? 0 : maxHours);

		results.add(Integer.toString(numAnswers));
		results.add(Integer.toString(minHours));
		results.add(Integer.toString(avgTime));
		results.add(Integer.toString(maxHours));

		return results;
	}

	

	private String showToProfessor(String results, Project proj, int minHours, int maxHours, int avgTime) {
		int numAnswers = getSurvey().getNumAnswers();

		return results + "\n * Número de submissões: " + proj.numSubmissions() + "\n" + 
						 " * Número de respostas: " + numAnswers + "\n" +
						 " * Tempos de resolução (horas) (mínimo, médio, máximo): " +
						minHours + ", " + avgTime + ", " + maxHours;
	}

	private String showToRepresentative(String results, Project proj, int avgTime) {
		int numAnswers = getSurvey().getNumAnswers();

		return results + " - Número de respostas " + numAnswers + 
						" - Tempo médio de execução " + avgTime;
	}

	private String showToStudent(String results, Project proj, int avgTime) {
		return results + "\n * Número de submissões: " + proj.numSubmissions() + "\n" +
					" * Tempo médio (horas): " + avgTime;
	}
}
