package sth.app.representative;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;
import sth.exceptions.NoSuchDisciplineSelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.NoSurveySelectionException;
import sth.exceptions.OpeningSurveySelectionException;
import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.NoSurveyException;
import sth.app.exceptions.OpeningSurveyException;

/**
 * 4.5.3. Open survey.
 */
public class DoOpenSurvey extends Command<SchoolManager> {

  Input<String> _discipline;
  Input<String> _project;

  /**
   * @param receiver
   */
  public DoOpenSurvey(SchoolManager receiver) {
    super(Label.OPEN_SURVEY, receiver);
    _discipline = _form.addStringInput(Message.requestDisciplineName());
    _project = _form.addStringInput(Message.requestProjectName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.openSurvey(_discipline.value(), _project.value());

    } catch (NoSuchDisciplineSelectionException e) {
      throw new NoSuchDisciplineException(e.getDiscipline());

    } catch (NoSuchProjectSelectionException e) {
      throw new NoSuchProjectException(e.getDiscipline(), e.getProject());

    } catch(NoSurveySelectionException e) {
      throw new NoSurveyException(e.getDiscipline(), e.getProject());

    } catch (OpeningSurveySelectionException e) {
      throw new OpeningSurveyException(e.getDiscipline(), e.getProject());
    }
  }
}
