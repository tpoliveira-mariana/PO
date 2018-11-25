package sth.app.person;

import pt.tecnico.po.ui.Command;
import sth.SchoolManager;
import java.util.List;


/**
 * 4.2.1. Show person.
 */
public class DoShowPerson extends Command<SchoolManager> {

  /**
   * @param receiver
   */
  public DoShowPerson(SchoolManager receiver) {
    super(Label.SHOW_PERSON, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    String personInfo = _receiver.showPerson();
    _display.add(personInfo);
    _display.display();
  }

}
