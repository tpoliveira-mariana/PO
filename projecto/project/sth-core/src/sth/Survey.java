package sth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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
	private Map<Integer, SurveyAnswer> _answers = new HashMap<Integer,SurveyAnswer>();

	abstract class SurveyState implements Serializable {
		/** Serial number for serialization. */
		private static final long serialVersionUID = 201811241525L;

		Survey getSurvey() {
			return Survey.this;
		}

		abstract void cancel(String disc, Project proj) throws NonEmptySurveySelectionException, 
															SurveyFinishedSelectionException;

		void open(String disc, String proj) throws OpeningSurveySelectionException {
			throw new OpeningSurveySelectionException(disc, proj);
		}

		void close(String disc, String proj) throws ClosingSurveySelectionException {
			throw new ClosingSurveySelectionException(disc, proj);
		}

		void finish(String disc, String proj) throws FinishingSurveySelectionException {
			throw new FinishingSurveySelectionException(disc, proj);
		}

		SurveyAnswer answer(String disc, String proj, int hours, String msg) 
														throws NoSurveySelectionException {
															
			throw new NoSurveySelectionException(disc, proj);
		}
	}


	//========== SETTERS ===========//

	void setState(SurveyState state) {
		_state = state;
	}

	void addAnswer(SurveyAnswer answer) {
		_answers.put(_numberAnswers++, answer);
	}


	//========== BOOLEANS ===========//

	boolean isEmpty() {
		return _numberAnswers == 0;
	}


	//========== ACTIONS ===========//

	void cancel(String disc, Project proj) throws NonEmptySurveySelectionException, 
												SurveyFinishedSelectionException {
		_state.cancel(disc, proj);
	}

	void open(String disc, String proj) throws OpeningSurveySelectionException {
		_state.open(disc, proj);
	}

	void close(String disc,String proj) throws ClosingSurveySelectionException {
		_state.close(disc, proj);
	}

	void finish(String disc, String proj) throws FinishingSurveySelectionException {
		_state.finish(disc, proj);
	}

	void answer(String disc, String proj, int hours, String msg) 
														throws NoSurveySelectionException {

		SurveyAnswer answer = _state.answer(disc, proj, hours, msg);
		addAnswer(answer);
	}

}