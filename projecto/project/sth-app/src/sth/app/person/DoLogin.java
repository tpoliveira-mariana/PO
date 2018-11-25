package sth.app.person;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;
import sth.app.exceptions.NoSuchPersonException;
import sth.exceptions.NoSuchPersonIdException;
import java.util.List;

/**
 * 4.2.1. Show person.
 */
public class DoLogin extends Command<SchoolManager> {

  /** Login identifier. */
  Input<Integer> _login;

  /**
   * @param receiver
   */
  public DoLogin(SchoolManager receiver) {
    super(Label.LOGIN, receiver);
    _login = _form.addIntegerInput(Message.requestLoginId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.login(_login.value());
    } catch (NoSuchPersonIdException e) {
      throw new NoSuchPersonException(_login.value());
    }
  }
}
