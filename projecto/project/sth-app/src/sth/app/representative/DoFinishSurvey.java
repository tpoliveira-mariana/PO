package sth.app.representative;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;
import sth.exceptions.NoSuchDisciplineSelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.NoSurveySelectionException;
import sth.exceptions.FinishingSurveySelectionException;
import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.NoSurveyException;
import sth.app.exceptions.FinishingSurveyException;


/**
 * 4.5.5. Finish survey.
 */
public class DoFinishSurvey extends Command<SchoolManager> {

  Input<String> _discipline;
  Input<String> _project;

  /**
   * @param receiver
   */
  public DoFinishSurvey(SchoolManager receiver) {
    super(Label.FINISH_SURVEY, receiver);
    _discipline = _form.addStringInput(Message.requestDisciplineName());
    _project = _form.addStringInput(Message.requestProjectName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.finishSurvey(_discipline.value(), _project.value());

    } catch (NoSuchDisciplineSelectionException e) {
      throw new NoSuchDisciplineException(e.getDiscipline());

    } catch (NoSuchProjectSelectionException e) {
      throw new NoSuchProjectException(e.getDiscipline(), e.getProject());

    } catch (NoSurveySelectionException e) {
      throw new NoSurveyException(e.getDiscipline(), e.getProject());

    } catch (FinishingSurveySelectionException e) {
      throw new FinishingSurveyException(e.getDiscipline(), e.getProject());
    }
  }
}
