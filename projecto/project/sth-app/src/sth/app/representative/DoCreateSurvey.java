package sth.app.representative;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;
import sth.exceptions.NoSuchDisciplineSelectionException;
import sth.exceptions.NoSuchProjectSelectionException;
import sth.exceptions.DuplicateSurveySelectionException;
import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.DuplicateSurveyException;

/**
 * 4.5.1. Create survey.
 */
public class DoCreateSurvey extends Command<SchoolManager> {

  Input<String> _discipline;
  Input<String> _project;


  /**
   * @param receiver
   */
  public DoCreateSurvey(SchoolManager receiver) {
    super(Label.CREATE_SURVEY, receiver);
    _discipline = _form.addStringInput(Message.requestDisciplineName());
    _project = _form.addStringInput(Message.requestProjectName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.createSurvey(_discipline.value(), _project.value());

   } catch (NoSuchDisciplineSelectionException e) {
     throw new NoSuchDisciplineException(e.getDiscipline());

   } catch (NoSuchProjectSelectionException e) {
     throw new NoSuchProjectException(e.getDiscipline(), e.getProject());

   } catch(DuplicateSurveySelectionException e) {
     throw new DuplicateSurveyException(e.getDiscipline(), e.getProject());
   }
  }

}
