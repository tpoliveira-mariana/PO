package sth.app.student;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;
import sth.exceptions.NoSuchDisciplineSelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.NoSurveySelectionException;
import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.NoSurveyException;

/**
 * 4.4.2. Answer survey.
 */
public class DoAnswerSurvey extends Command<SchoolManager> {

  Input<String> _discipline;
  Input<String> _project;
  Input<Integer> _hours;
  Input<String> _comment;

  /**
   * @param receiver
   */
  public DoAnswerSurvey(SchoolManager receiver) {
    super(Label.ANSWER_SURVEY, receiver);
    _discipline = _form.addStringInput(Message.requestDisciplineName());
    _project = _form.addStringInput(Message.requestProjectName());
    _hours =  _form.addIntegerInput(Message.requestProjectHours());
    _comment = _form.addStringInput(Message.requestComment());
    
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.answerSurvey(_discipline.value(), _project.value(), 
                                _hours.value(), _comment.value());

    } catch (NoSuchDisciplineSelectionException e) {
      throw new NoSuchDisciplineException(e.getDiscipline());

    } catch (NoSuchProjectSelectionException e) {
      throw new NoSuchProjectException(e.getDiscipline(), e.getProject());

    } catch (NoSurveySelectionException e) {
      throw new NoSurveyException(e.getDiscipline(), e.getProject());
    }
  }
}
