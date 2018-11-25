package sth.app.teaching;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;
import sth.exceptions.NoSuchDisciplineSelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.OpeningSurveySelectionException;
import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.OpeningSurveyException;

/**
 * 4.3.2. Close project.
 */
public class DoCloseProject extends Command<SchoolManager> {

  Input<String> _projDiscipline;
  Input<String> _projName;

  /**
   * @param receiver
   */
  public DoCloseProject(SchoolManager receiver) {
    super(Label.CLOSE_PROJECT, receiver);
    _projDiscipline = _form.addStringInput(Message.requestDisciplineName());
    _projName = _form.addStringInput(Message.requestProjectName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.closeProject(_projDiscipline.value(), _projName.value());

    } catch (NoSuchDisciplineSelectionException e) {
      throw new NoSuchDisciplineException(e.getDiscipline());

    } catch (NoSuchProjectSelectionException e) {
      throw new NoSuchProjectException(e.getDiscipline(), e.getProject());

    } catch (OpeningSurveySelectionException e) {
      throw new OpeningSurveyException(e.getDiscipline(), e.getProject());
    }
  }
}
