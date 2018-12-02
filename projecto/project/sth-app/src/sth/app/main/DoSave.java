package sth.app.main;

import java.io.IOException;
import java.io.FileNotFoundException;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

/**
 * 4.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<SchoolManager> {

  Input<String> _fileName;

  /**
   * @param receiver
   */
  public DoSave(SchoolManager receiver) {
    super(Label.SAVE, receiver);
    _fileName = _form.addStringInput(Message.newSaveAs());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    try {
      if (!_receiver.hasFile(_fileName.value())) {
        _form.parse(); 
      }
      _receiver.saveDataToFile(_fileName.value());

    } catch (FileNotFoundException fnfe) {
      _display.popup(Message.fileNotFound());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
