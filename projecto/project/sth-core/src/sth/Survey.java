package sth;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import sth.exceptions.NonEmptySurveySelectionException;
import sth.exceptions.SurveyFinishedSelectionException;
import sth.exceptions.OpeningSurveySelectionException;
import sth.exceptions.ClosingSurveySelectionException;
import sth.exceptions.FinishingSurveySelectionException;
import sth.exceptions.NoSurveySelectionException;

class Survey implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811241523L;
	private SurveyState _state = new CreatedState(this);
	private int _numberAnswers = 0;
	private Set<SurveyAnswer> _answers = new HashSet<SurveyAnswer>();

	abstract class SurveyState implements Serializable {
		/** Serial number for serialization. */
		private static final long serialVersionUID = 201811241525L;

		Survey getSurvey() {
			return Survey.this;
		}

		abstract void cancel(String discName, Project proj) throws NonEmptySurveySelectionException, 
															SurveyFinishedSelectionException;

		void open(Discipline disc, String projName) throws OpeningSurveySelectionException {
			throw new OpeningSurveySelectionException(disc.getName(), projName);
		}

		void close(String discName, String projName) throws ClosingSurveySelectionException {
			throw new ClosingSurveySelectionException(discName, projName);
		}

		void finish(String discName, String projName) throws FinishingSurveySelectionException {
			throw new FinishingSurveySelectionException(discName, projName);
		}

		SurveyAnswer answer(String discName, String projName, int hours, String msg) 
														throws NoSurveySelectionException {

			throw new NoSurveySelectionException(discName, projName);
		}

		abstract List<String> showResults(String discName, Project proj);
	}


	//========== SETTERS ===========//

	void setState(SurveyState state) {
		_state = state;
	}

	void addAnswer(SurveyAnswer answer) {
		_answers.add(answer);
		_numberAnswers++;
	}

	//========== GETTERS ===========//

	Set<SurveyAnswer> getAnswers() {
		return _answers;
	}

	int getNumAnswers() {
		return _numberAnswers;
	}

	//========== BOOLEANS ===========//

	boolean isEmpty() {
		return _numberAnswers == 0;
	}


	//========== ACTIONS ===========//

	void cancel(String discName, Project proj) throws NonEmptySurveySelectionException, 
												SurveyFinishedSelectionException {
		_state.cancel(discName, proj);
	}

	void open(Discipline disc, String projName) throws OpeningSurveySelectionException {
		_state.open(disc, projName);
		disc.sendOpenNotification(projName);
	}

	void close(String discName,String projName) throws ClosingSurveySelectionException {
		_state.close(discName, projName);
	}

	void finish(Discipline disc, String projName) throws FinishingSurveySelectionException {
		_state.finish(disc.getName(), projName);
		disc.sendFinishNotification(projName);
	}

	void answer(String discName, String projName, int hours, String msg) 
														throws NoSurveySelectionException {

		SurveyAnswer answer = _state.answer(discName, projName, hours, msg);
		addAnswer(answer);
	}

	List<String> showResults(String discName, Project proj) {
		return _state.showResults(discName, proj);
	}
}
