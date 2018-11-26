package sth.app.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.DialogException;
import sth.SchoolManager;
import sth.exceptions.NoSuchPersonIdException;
import sth.app.exceptions.NoSuchPersonException;


/**
 * 4.1.1. Open existing document.
 */
public class DoOpen extends Command<SchoolManager> {

  Input<String> _filename;
  
  /**
   * @param receiver
   */
  public DoOpen(SchoolManager receiver) {
    super(Label.OPEN, receiver);
     _filename = _form.addStringInput(Message.openFile());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    
    try {   
      _form.parse();
      _receiver.setFile(_filename.value());  
      List<String> notifications = _receiver.openDataFile();

      for (String note : notifications) 
        _display.addLine(note);

      _display.display();
 
    } catch (FileNotFoundException fnfe) {       
       _display.popup(Message.fileNotFound());
    } catch (ClassNotFoundException | IOException e) {
       e.printStackTrace();
    } catch (NoSuchPersonIdException e) {
       throw new NoSuchPersonException(e.getId());
    }
  }

}
