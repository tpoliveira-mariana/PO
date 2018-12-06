//java -Dimport="sth-app/people.import" -cp "/usr/share/java/po-uuilib.jar:sth-core/sth-core.jar:sth-app/sth-app.jar" sth.app.App

package sth;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import sth.exceptions.*;


/**
 * The façade class.
 */
public class SchoolManager {

  private School _school;
  private Person _user;
  private String _infoFileName;
  private boolean _upToDate = true;

  public SchoolManager() {
    _school = new School();
  }


//========== GETTERS ===========//

  public Person getUser() {
    return _user;
  }
//

//========== SETTERS ===========/

  /**
   * @param fileName
   */
  void updateFileName(String fileName) {
    _infoFileName = fileName;
  }
//

//========== BOOLEANS ===========//

  public boolean hasFile(String fileName) {
    return _infoFileName != null;
  }
//

//========== UPDATE ===========//
  /**
   * @return Whether the school has been changed since last save.
   */
  boolean isUpToDate() {
    return _upToDate;
  }

  /**
   * Flags the school as changed since last save.
   */
  void modify() {
    _upToDate = false;
  }

  /**
   * Flags the school as nonchanged since last save (or has just been saved).
   */
  void update() {
    _upToDate = true;
  }
//

//========== IMPORTFILE ===========//

  /**
   * @param datafile
   * @throws ImportFileException
   */
  public void importFile(String datafile) throws ImportFileException {
    try {
      _school.importFile(datafile);
    } catch (IOException | BadEntryException e) {
      throw new ImportFileException(e);
    }
    modify();
  }
//

//======= LOGIN =======//

  /**
   * @return true when the currently logged in person is an administrative
   */
  public boolean hasAdministrative() {
    return _school.adminExists(_user.getId());
  }

  /**
   * @return true when the currently logged in person is a professor
   */
  public boolean hasProfessor() {
    return _school.professorExists(_user.getId());
  }

  /**
   * @return true when the currently logged in person is a student
   */
  public boolean hasStudent() {
    return _school.studentExists(_user.getId());
  }

  /**
   * @return true when the currently logged in person is a representative
   */
  public boolean hasRepresentative() {
    return _school.representativeExists(_user.getId());
  }

  /**
   * @param id
   * @throws NoSuchPersonIdException
   */
  public void login(int id) throws NoSuchPersonIdException {
    if (_school.personExists(id)) { 
      _user = _school.getPersonById(id);
    }
    else throw new NoSuchPersonIdException(id);
  }

  /**
   * @return List of unseen notifications the user has
   */
  public List<String> showInbox() {
    List<String> notifications = _user.seeAllNotifications();
    modify();
    return notifications;
  }
//

//======= MAIN =======//

  /**
   * @throws IOException
   * @throws FileNotFoundException
   * @throws ClassNotFoundException
   * @throws NoSuchPersonIdException
   */
  public void openDataFile(String fileName) throws IOException, FileNotFoundException, 
                                  ClassNotFoundException, NoSuchPersonIdException {

      updateFileName(fileName);

      ObjectInputStream in = 
          new ObjectInputStream(new BufferedInputStream (new FileInputStream(_infoFileName)));

      School newSchool = (School) in.readObject(); 
      if (!newSchool.personExists(_user.getId())) {
        in.close();
        throw new NoSuchPersonIdException(_user.getId());
      }

      _school = newSchool;
      login(_user.getId());
      in.close();
      modify();
  }

  /**
   * @throws IOException
   * @throws FileNotFoundException
   */
  public void saveDataToFile(String fileName) throws IOException, FileNotFoundException {

    if (fileName != null) {
      updateFileName(fileName);
    }

    if (!isUpToDate()) {
      ObjectOutputStream out = 
          new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_infoFileName)));

      out.writeObject(_school);
      out.close();

      update();
    }
  }
//

//======= PESSOAL =======//  

  /**
   * @param newNumber
   * @throws InvalidPhoneNumberException
   */
  public void changePhoneNumber(String newNumber) throws InvalidPhoneNumberException {
      _user.changePhoneNumber(newNumber);
      modify();
  }

  /**
   * @param person
   * @return String that represents the person's information
   */
  public String showPerson() {
    return _school.showPerson(_user);
  }

  /**
   * @return List of strings related to each person's information
   */
  public List<String> showAllPersons() {
    return _school.showPersonnel();
  }


  /**
   * @param nameToFind
   * @return List of strings where every string represents a person whose name is similar to the name to find
   */
  public List<String> searchPerson(String nameToFind) {
    return _school.searchPersonByName(nameToFind);
  }
//

//======= DOCENTE =======//  

  /**
   * @param discName
   * @param projName
   * @throws DuplicateProjectSelectionException
   * @throws NoSuchDisciplineSelectionException
   */
  public void createProject(String discName, String projName) 
                                              throws DuplicateProjectSelectionException, 
                                              NoSuchDisciplineSelectionException {
    if (hasProfessor()) {
      _school.letCreateProject(_user, discName, projName); 
      modify();
    }
  }

  /**
   * @param discName
   * @param projName
   * @throws NoSuchProjectSelectionException
   * @throws NoSuchDisciplineSelectionException
   */
  public void closeProject(String discName, String projName) 
                                            throws NoSuchDisciplineSelectionException, 
                                            NoSuchProjectSelectionException,
                                            OpeningSurveySelectionException {
    if (hasProfessor()) {
      _school.letCloseProject(_user, discName, projName);
      modify();
    }
  }

  /**
   * @param discName
   * @throws NoSuchDisciplineSelectionException
   * @return List of strings where each strings corresponds to a student enrolled in the discipline named discName
   */
  public List<String> showDisciplineStudents(String discName) 
                                              throws NoSuchDisciplineSelectionException {
    if (hasProfessor()) {
      return _school.letSeeDisciplineStudents(_user, discName);
    }
    return null;
  }

  /**
   * @param discName
   * @param projName 
   * @throws NoSuchDisciplineSelectionException
   * @throws NoSuchProjectSelectionException
   * @return List of strings where each strings corresponds to a submission related to the project named projName
   */
  public List<String> showProjectSubmissions(String discName, String projName) 
                                              throws NoSuchDisciplineSelectionException, 
                                              NoSuchProjectSelectionException {
    if (hasProfessor()) {
      return _school.letSeeProjectSubmissions(_user, discName, projName);
    }
    return null;
  }

  public String showSurveyResultsProf(String discName, String projName)
                                              throws NoSuchDisciplineSelectionException, 
                                              NoSuchProjectSelectionException,
                                              NoSurveySelectionException {  

    if (hasProfessor()) {
      return _school.letProfSeeSurveyResults(_user, discName, projName);
    }
    return null;
  }
//

//======= ALUNO =======//

  /**
   * @param discName
   * @param projName
   * @param message
   * @throws NoSuchDisciplineSelectionException
   * @throws NoSuchProjectSelectionException
   */
  public void submitProject(String discName, String projName, String message) 
                                                    throws NoSuchDisciplineSelectionException, 
                                                    NoSuchProjectSelectionException {
    if (hasStudent()) {
      _school.letSubmitProject(_user, discName, projName, message);
      modify();
    }
  }

  /**
   * The user becomes one of the representatives of his course
   */
  public void becomeRepresentavive() {  // TODO lancar excecao
    if (hasStudent()) {
      _school.letBecomeRepresentative(_user);
      modify();
    } 
  }

  /**
   * The user stops being one of the representatives of his course
   */
  public void stopBeingRepresentative() {
    if (hasRepresentative()) {
      _school.letStopBeingRepresentative(_user);
      modify();
    }
  }

  public void answerSurvey(String discName, String projName, int hours, String message) 
                                                    throws NoSuchDisciplineSelectionException,
                                                    NoSuchProjectSelectionException,
                                                    NoSurveySelectionException {
    if (hasStudent()) {
      _school.letAnswerSurvey(_user, discName, projName, hours, message);
      modify();
    }
  }

  public String showSurveyResultsStudent(String discName, String projName)
                                              throws NoSuchDisciplineSelectionException, 
                                              NoSuchProjectSelectionException,
                                              NoSurveySelectionException {  

    if (hasStudent()) {
      return _school.letStudentSeeSurveyResults(_user, discName, projName);
    }
    return null;
  }
//

//======= DELEGADO =======//

  public void createSurvey(String discName, String projName) 
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException,
                                                  DuplicateSurveySelectionException {
    if (hasRepresentative()) {
      _school.letCreateSurvey(_user, discName, projName);
      modify();
    }
  }

  public void openSurvey(String discName, String projName) 
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException,
                                                  OpeningSurveySelectionException {
    if (hasRepresentative()) {
      _school.letOpenSurvey(_user, discName, projName);
      modify();
    }
  }

  public void cancelSurvey(String discName, String projName) 
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException,
                                                  NonEmptySurveySelectionException,
                                                  SurveyFinishedSelectionException {

    if (hasRepresentative()) {
      _school.letCancelSurvey(_user, discName, projName);
      modify();
    }
  }

  public void closeSurvey(String discName, String projName) 
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException,
                                                  ClosingSurveySelectionException {

    if (hasRepresentative()) {
      _school.letCloseSurvey(_user, discName, projName);
      modify();
    }
  }

  public void finishSurvey(String discName, String projName)
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException,
                                                  FinishingSurveySelectionException {

    if (hasRepresentative()) {
      _school.letFinishSurvey(_user, discName, projName);
      modify();
    }
  }


  public List<String> showDisciplineSurveys(String discName)
                                              throws NoSuchDisciplineSelectionException, 
                                              NoSurveySelectionException {  

    if (hasRepresentative()) {
      return _school.letSeeDisciplineSurveys(_user, discName);
    }
    return null;
  }
// 


//======= OBSERVERS =======//

  void disableNotifications(String discName) throws NoSuchDisciplineSelectionException {
  	_user.disableNotifications(discName);
  }

  void enableNotifications(String discName) throws NoSuchDisciplineSelectionException {
    _user.enableNotifications(discName);
  }
//

}
