package sth.app.teaching;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;
import sth.exceptions.NoSuchDisciplineSelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import java.util.List;

/**
 * 4.3.3. Show project submissions.
 */
public class DoShowProjectSubmissions extends Command<SchoolManager> {

  Input<String> _projDiscipline;
  Input<String> _projName;

  /**
   * @param receiver
   */
  public DoShowProjectSubmissions(SchoolManager receiver) {
    super(Label.SHOW_PROJECT_SUBMISSIONS, receiver);
    _projDiscipline = _form.addStringInput(Message.requestDisciplineName());
    _projName = _form.addStringInput(Message.requestProjectName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      List<String> submissions = _receiver.showProjectSubmissions(_projDiscipline.value(), _projName.value());
      for (String info : submissions) 
        _display.addLine(info);

      _display.display();

    } catch (NoSuchDisciplineSelectionException e) {
      throw new NoSuchDisciplineException(e.getDiscipline());
    } catch (NoSuchProjectSelectionException e) {
      throw new NoSuchProjectException(e.getDiscipline(), e.getProject());
    } 
  }
}
