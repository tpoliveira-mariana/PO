package sth.app.person;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;
import sth.exceptions.InvalidPhoneNumberException;


/**
 * 4.2.2. Change phone number.
 */
public class DoChangePhoneNumber extends Command<SchoolManager> {

  Input<String> _newPhoneNumber;


  /**
   * @param receiver
   */
  public DoChangePhoneNumber(SchoolManager receiver) {
    super(Label.CHANGE_PHONE_NUMBER, receiver);
    _newPhoneNumber = _form.addStringInput(Message.requestPhoneNumber());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    try {
      _form.parse();
      _receiver.changePhoneNumber(_newPhoneNumber.value());
      (new DoShowPerson(_receiver)).execute();
    } catch (InvalidPhoneNumberException e) {
      e.printStackTrace(); 
    }
  }

}
