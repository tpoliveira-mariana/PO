package sth;


import java.util.List;


class SurveyPrinter implements Visitor {
	private String _header, _numSubmissions, _numAnswers, _minHours, _avgTime, _maxHours;
	private boolean _surveyIsFinished = false;

	SurveyPrinter(List<String> results) {

		_header = results.get(0);

		if (results.size() > 1) {
			_surveyIsFinished = true;
			_numAnswers = results.get(1);
			_minHours = results.get(2);
			_avgTime	= results.get(3);
			_maxHours = results.get(4);
			_numSubmissions = results.get(5);

		}
	}

	@Override
	public String visit(Administrative admin) {
		return null;
	}

	@Override
	public String visit(Professor prof) {
		if (!_surveyIsFinished)
			return _header;

		return _header + "\n * Número de submissões: " + _numSubmissions+ "\n" + 
						 " * Número de respostas: " + _numAnswers + "\n" +
						 " * Tempos de resolução (horas) (mínimo, médio, máximo): " +
						_minHours + ", " + _avgTime + ", " + _maxHours;
	}


	@Override
	public String visit(Student student) {
		if (!_surveyIsFinished)
			return _header;
		
		return _header +  "\n * Número de submissões: " + _numSubmissions + "\n" +
					" * Tempo médio (horas): " + _avgTime;
	}
}