package sth;

import java.io.Serializable;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import sth.exceptions.*;
import java.util.regex.Pattern;



/**
 * School implementation.
 */
class School implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201810051538L;
  /** School's name. */
  private String _name = "";
  /** The ID to which the last registered person was assigned to. */
  private int _currentID = 100000;   
  /** Courses in the school. */
  private Map<String,Course> _courses = new TreeMap<String,Course>(new AccentedComparator());
  /** Students registered in the school. */
  private Map<Integer, Student> _students = new HashMap<Integer, Student>();
  /** Professors registered in the school. */
  private Map<Integer, Professor> _professors = new HashMap<Integer, Professor>();
  /** Administratives registered in the school. */
  private Map<Integer, Administrative> _administratives = new HashMap<Integer, Administrative>();


//========== IMPORTFILE ===========//

 /**
  * Imports an input file as a School object.
  * @param filename
  * Name of the file to open.
  * @throws BadEntryException
  * @throws IOException
  */
  void importFile(String filename) throws IOException, BadEntryException {
    BufferedReader input = new BufferedReader(new FileReader(filename)); 

    String line = input.readLine();
    while(line != null) {
      String[] info = line.split("[|]");
      line = registerFromInfo(info, input);   
    }
    input.close();
  }

 /**
  * Registers a person from given info array and input reader.
  * @param info
  * Array of each individual input information.
  * @param input
  * The input buffered reader.
  * @throws BadEntryException
  * @throws IOException
  */
  private String registerFromInfo(String[] info, BufferedReader input) throws IOException, BadEntryException {
    Pattern patProfessor = Pattern.compile("^(DOCENTE)");
    Pattern patAdministrative = Pattern.compile("^(FUNCIONÁRIO)");
    Pattern patStudentOrRep = Pattern.compile("^(ALUNO|DELEGADO)");

    if (patProfessor.matcher(info[0]).matches())
      return registerProfessor(info, input);

    else if (patStudentOrRep.matcher(info[0]).matches())
      return registerStudent(info, input);

    else if (patAdministrative.matcher(info[0]).matches()) {
      registerAdministrative(info);
      return input.readLine();
    }

    else 
      throw new BadEntryException("Formato de input errado.");
  }

 /**
  * Register a professor from given info array and input reader.
  * @param info
  * Array of each professor's registering attributes.
  * @param input
  * The input buffered reader.
  * @throws BadEntryException
  * @throws IOException
  */
  private String registerProfessor(String[] info, BufferedReader input) throws IOException, BadEntryException{
    if (!personInfoIsValid(info))
      throw new BadEntryException("Formato de input errado.");

    Professor prof = new Professor(info[3], info[2], Integer.parseInt(info[1]));
    List<String> extraLines = getExtraLines(input);
    int numberLines = extraLines.size();

    for (int i = 0; i < numberLines - 1; i++) {
      String[] line  = extraLines.get(i).split("[|]");
      if ((line[0].charAt(0) != '#') || (line.length != 2))
        throw new BadEntryException("Formato de input errado.");

      String courseName = cleanCourseName(line[0]);
      String discName = line[1];

      Course course = treatCourse(courseName);
      Discipline disc = treatDiscipline(discName, course);

      course.addProfessor(prof);
      disc.addProfessor(prof);
      prof.addDiscipline(disc);
      disc.addObserver(prof);
    }
    addProfessor(prof);
    updateCurrentId(Integer.parseInt(info[1]));

    return extraLines.get(numberLines-1);
  }

 /**
  * Register a student from given info array and input reader.
  * @param info
  * Array of each student's registering attributes.
  * @param input
  * The input buffered reader.
  * @throws BadEntryException
  * @throws IOException
  */
  private String registerStudent(String[] info, BufferedReader input) throws IOException, BadEntryException {
    if (!personInfoIsValid(info))
      throw new BadEntryException("Formato de input errado.");

    Student student = new Student(info[3], info[2], Integer.parseInt(info[1]));
    List<String> extraLines = getExtraLines(input);
    int numberLines = extraLines.size();
    Course course = null;

    for (int i = 0; i < numberLines - 1; i++) {
      String[] line  = extraLines.get(i).split("[|]");
      if ((line[0].charAt(0) != '#') || (line.length != 2))
        throw new BadEntryException("Formato de input errado.");

      String courseName = cleanCourseName(line[0]);
      String discName = line[1];

      Course otherCourse = treatCourse(courseName);
      if (course != null && otherCourse.getName() != course.getName()) {
         throw new BadEntryException ("Um aluno só se pode inscrever em disciplinas do mesmo curso.");
      }

      course = otherCourse;
      Discipline disc = treatDiscipline(discName, course);

      if(!student.canEnrollDiscipline() || !disc.canAddStudent())
        throw new BadEntryException ("O aluno " + student.getName() + " nao se pode inscrever em " + disc.getName());
      
      course.addStudent(student);
      disc.addStudent(student);
      student.enrollDiscipline(disc);
      disc.addObserver(student);
    }

    if (info[0].equals("DELEGADO") && !course.canAddRepresentative())
      throw new BadEntryException("O curso " + course.getName() + " não pode ter mais delegados.");
    else if (info[0].equals("DELEGADO") && course.canAddRepresentative()) 
      course.addRepresentative(student);
    

    addStudent(student);
    updateCurrentId(Integer.parseInt(info[1]));

    return extraLines.get(numberLines-1);
  }

 /**
  * Register an administrative from given info array and input reader.
  * @param info
  * Array of each administrative's registering attributes.
  * @throws BadEntryException
  */
  private void registerAdministrative(String[] info) throws BadEntryException {
    if (!personInfoIsValid(info))
      throw new BadEntryException("Formato de input errado.");

    Administrative admin = new Administrative(info[3], info[2],Integer.parseInt(info[1]));
    addAdministrative(admin);
    updateCurrentId(Integer.parseInt(info[1]));
  }

 /**
  * Returns whether an array of strings is valid input for importing or not.
  * @param info
  * Array of an individual's (should be) input information.
  * @return True if the info line corresponds to the expected input. False otherwise.
  */
  private boolean personInfoIsValid(String[] info) throws BadEntryException {
    try {
      int id = Integer.parseInt(info[1]);

      return (info.length == 4) && 
             (info[1].length() == 6) && 
              (!idAlreadyTaken(id));
    } catch (NumberFormatException e) {
      throw new BadEntryException("Formato de id errado.");
    }
  }

 /**
  * Fetches the additional input lines, for example disciplines assigned to a student.
  * @param input
  * The input buffered reader.
  * @return List of the additinal lines assigned to said person. 
  */
  private List<String> getExtraLines(BufferedReader input) throws IOException {
    String line = input.readLine();
    List<String> lines = new ArrayList<String>();

    while (line != null && line.charAt(0) == '#') {
      lines.add(line);
      line = input.readLine();
    }

    lines.add(line);
    return lines;
  }

 /**
  * Returns a course with name courseName, if it doesn't exist, creates one.
  * @param courseName
  * The name of the course.
  * @return A course with name courseName.
  */
  private Course treatCourse(String courseName) {
    Course course = _courses.get(courseName);
    if (course == null) {
      course = new Course(courseName);
      addCourse(course);
    }

    return course;
  }

 /**
  * Returns a Discipline assigned to course, if it doesn't exist, creates one.
  * @param discName
  * Name of the discipline.
  * @param course
  * The course to which the discipline belongs.
  * @return A discipline with name discName associated with course course.
  */
  private Discipline treatDiscipline(String discName, Course course) {
    Discipline disc = course.getDiscipline(discName);   
    if (disc == null) {
      disc = new Discipline(discName);
      course.addDiscipline(disc);
    }
    return disc;
  }

 /**
  * Cleans the course name string that was read from an import file.
  * @param dirty
  * The string fetched from the import file.
  * @return The clean version of the input string (course name only).
  */
  private String cleanCourseName(String dirty) {
    String[] words = dirty.split(" ");
    String clean = "";

    for (int i = 1; i < words.length; i++)
      clean += words[i];

    return clean;
  }

 /**
  * Returns whether an id is already taken or not.
  * @param id
  * @return True if the id is already taken, false otherwise.
  */
  private boolean idAlreadyTaken(int id) {
    return personExists(id);
  }

 /**
  * Updates the school's "currentId" attribute.
  * @param id
  * ID of registered person.
  */
  private void updateCurrentId(int id) {
    if (id > _currentID)
      _currentID = id;
  }
//

//========== GETTERS ===========//

 /**
  * @return Sorted list of personnel in the school.
  */
  List<Person> getPersonnel() {
    List<Person> profs = new ArrayList<Person>(_professors.values());
    List<Person> students = new ArrayList<Person>(_students.values());
    List<Person> admins = new ArrayList<Person>(_administratives.values());

    List<Person> personnel = new ArrayList<Person>();
    personnel.addAll(profs);
    personnel.addAll(students);
    personnel.addAll(admins);

    Collections.sort(personnel);

    return personnel;
  }

 /**
  * @return School's id counter (last registered person). 
  */
  private int getCurrentId() {
    return _currentID;
  }

 /**
  * @param id
  * The id to search for. 
  * @return Person associated with the id argument.
  */
  Person getPersonById(int id) {
    if (studentExists(id))
      return getStudentById(id);

    else if (professorExists(id))
      return getProfessorById(id);

    else 
      return getAdminById(id);
  }

  /**
  * @param id
  * The id to search for. 
  * @return Student associated with the id argument.
  */
  private Student getStudentById(int id) {
    return _students.get(id);
  }

 /**
  * @param id
  * The id to search for. 
  * @return Professor associated with the id argument.
  */
  private Professor getProfessorById(int id) {
    return _professors.get(id);
  }

 /**
  * @param id
  * The id to search for. 
  * @return Administrative associated with the id argument.
  */
  private Administrative getAdminById(int id) {
    return _administratives.get(id);
  }

 /**
  * @param student
  * The student to which we want to know the course.
  * @return The student's course.
  */
  private Course getStudentCourse(Student student) {
    for (Course c : _courses.values())
      if (c.studentExists(student.getId()))
        return c;

    return null;
  }
//
  
//========== MAIN ==========//

  /**
   * Adds student to school.
   * @param student
   * The student to add.
   */
  private void addStudent(Student student) {
    _students.put(student.getId(), student);
  }

  /**
   * Adds professor to school.
   * @param prof
   * The professor to add.
   */
  private void addProfessor(Professor prof) {
    _professors.put(prof.getId(), prof);
  }

  /**
   * Adds administrative to school.
   * @param admin
   * The administrative to add.
   */
  private void addAdministrative(Administrative admin) {
    _administratives.put(admin.getId(), admin);
  }

  /**
   * Adds course to school.
   * @param course
   * The course to add.
   */
  private void addCourse(Course course) {
     _courses.put(course.getName(), course);
  }

  /**
   * @param id
   * The id to search for.
   * @return Whether the professor is registered in the school.
   */
  boolean professorExists(int id) {
    return _professors.containsKey(id);
  }

  /**
   * @param id
   * The id to search for.
   * @return Whether the student is registered in the school.
   */
  boolean studentExists(int id) {
    return _students.containsKey(id);
  }

  /**
   * @param id
   * The id to search for.
   * @return Whether the administrative is registered in the school.
   */
  boolean adminExists(int id) {
    return _administratives.containsKey(id);
  }

  /**
   * @param id
   * The id to search for.
   * @return Whether the person is registered in the school.
   */
  boolean personExists(int id) {
    return (studentExists(id) || professorExists(id) || adminExists(id));
  }

  /**
   * @param id
   * The id to search for.
   * @return Whether the representative is registered in the school.
   */
  boolean representativeExists(int id) {
    for (Course course : _courses.values()) {
      if (course.representativeExists(id))
        return true;
    }

    return false;
  }
//

//========== PESSOAL ==========//

  /**
   * @param p
   * The person to show.
   * @return The string representing the person.
   */
  String showPerson(Person p) {
    Administrative admin = null;
    Student student = null;

    if ((admin = getAdminById(p.getId())) != null)
      return showAdministrative(admin);

    else if ((student = getStudentById(p.getId())) != null)
      return showStudent(student);

    else {
      Professor prof = getProfessorById(p.getId());
      return showProfessor(prof);
    }
  }

  /**
   * @param admin
   * The administrative to show.
   * @return The string representing the administrative.
   */
  private String showAdministrative(Administrative admin) {
    return admin.toString();
  }

  /**
   * @param student
   * The student to show.
   * @return The string representing the student.
   */
  private String showStudent(Student student) {
    String studentInfo;

    if (!representativeExists(student.getId())) {
      studentInfo = student.toString();
    }
    else {
      studentInfo = "DELEGADO|" + student.getId() + "|" + student.getPhoneNumber() 
            + "|" + student.getName();
    }
    
    Course course = getStudentCourse(student);

    return studentInfo + course.showDisciplinesOf(student);
  }

  /**
   * @param prof
   * The professor to show.
   * @return The string representing the professor.
   */
  private String showProfessor(Professor prof) {
    String profInfo = prof.toString();

     for (Course course : _courses.values()) {
      if (course.professorExists(prof.getId()))
        profInfo += course.showDisciplinesOf(prof);
    }

    return profInfo;
  }

  /**
   * @return The list of strings that represent each person on the school.
   */
  List<String> showPersonnel() {
    List<String> allPersonsInfo = new ArrayList<String>();
    List<Person> personnel = getPersonnel();

    for (Person p : personnel) 
      allPersonsInfo.add(showPerson(p));

    return allPersonsInfo;
  }

  /**
   * @param nameToFind
   * The (partial) name to search for
   * @return A list of strings of people that contain nameToFind in their name.
   */
  List<String> searchPersonByName(String nameToFind) {
    Map<String, Person> peopleAlike = new TreeMap<String, Person>(new AccentedComparator()); 
    List<Person> personnel = getPersonnel();
    List<String> info = new ArrayList<String>();

    for (Person p : personnel) {
      String name = p.getName();
      if (name.contains(nameToFind))
        peopleAlike.put(name, p);
    }

    for (Person p : peopleAlike.values())
      info.add(showPerson(p));

    return info;
  }
//

//========== DOCENTE ==========//

  /**
   * Verifies if a person can create a project, creates it if he can.
   * @param p
   * The person that wants to create the project.
   * @param discName
   * The name of the discipline.
   * @param projName
   * The name of the project.
   * @throws DuplicateProjectSelectionException
   * @throws NoSuchDisciplineSelectionException
   */
  void letCreateProject(Person p, String discName, String projName) 
                                                  throws DuplicateProjectSelectionException, 
                                                  NoSuchDisciplineSelectionException {
    Professor prof = getProfessorById(p.getId());

    if (!prof.teachesDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    } 

    prof.createProject(discName, projName);
  }

  /**
   * Verifies if a person can close a project, closes it if he can.
   * @param p
   * The person that wants to close the project.
   * @param discName
   * The name of the discipline.
   * @param projName
   * The name of the project.
   * @throws NoSuchProjectSelectionException
   * @throws NoSuchDisciplineSelectionException
   */
  void letCloseProject(Person p, String discName, String projName) 
                                                  throws NoSuchProjectSelectionException, 
                                                  NoSuchDisciplineSelectionException, 
                                                  OpeningSurveySelectionException {
    Professor prof = getProfessorById(p.getId());
    
    if (!prof.teachesDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }  

    prof.closeProject(discName, projName);
  }

  /**
   * Verifies if the person can see the discipline's students and returns.
   * @param p
   * The person that wants to see the discipline's students.
   * @param discName
   * The discipline to see its students.
   * @throws NoSuchDisciplineSelectionException
   * @return List of strings of the student's in discipline discName if succeeds.
   */
  List<String> letSeeDisciplineStudents(Person p, String discName) 
                                                  throws NoSuchDisciplineSelectionException {
    Professor prof = getProfessorById(p.getId());

    if (!prof.teachesDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    return prof.seeDisciplineStudents(this, discName);   
  }

  /**
   * Verifies if the person can see project submissions.
   * @param p
   * The person that wants to see the project submissions.
   * @param discName
   * The project's discipline.
   * @param projName
   * The name of the project to see.
   * @throws NoSuchDisciplineSelectionException
   * @throws NoSuchProjectSelectionException
   * @return List of strings of the student's in discipline discName if succeeds.
   */
  List<String> letSeeProjectSubmissions(Person p, String discName, String projName) 
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException {
    Professor prof = getProfessorById(p.getId());
    if (!prof.teachesDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    return prof.seeProjectSubmissions(discName, projName); 
  }

  String letSeeSurveyResultsProf(Person p, String discName, String projName)
                                                  throws NoSuchDisciplineSelectionException,
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException {
    
    Professor prof = getProfessorById(p.getId());
    if (!prof.teachesDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }  


    return prof.seeSurveyResults(this, discName, projName);
  }
//

//========== ALUNO ==========//

  /**
   * Verifies if the person can submit a project, submits if he can.
   * @param p
   * The person that wants to submit a project.
   * @param discName
   * The project's discipline.
   * @param projName
   * The name of the project to submit.
   * @param message
   * Message of the project submission.
   * @throws NoSuchDisciplineSelectionException
   * @throws NoSuchProjectSelectionException
   */
  void letSubmitProject(Person p, String discName, String projName, String message) 
                                                      throws NoSuchDisciplineSelectionException, 
                                                      NoSuchProjectSelectionException {
    Student student = getStudentById(p.getId());

    if (!student.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    student.submitProject(discName, projName, message);
  }

  void letAnswerSurvey(Person p, String discName, String projName, int hours, String message) 
                                                      throws NoSuchDisciplineSelectionException,
                                                      NoSuchProjectSelectionException,
                                                      NoSurveySelectionException {

    Student student = getStudentById(p.getId());

     if (!student.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    student.answerSurvey(discName, projName, hours, message);
  }

  String letSeeSurveyResultsStudent(Person p, String discName, String projName)
                                                  throws NoSuchDisciplineSelectionException,
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException {
    
    Student student = getStudentById(p.getId());
    if (!student.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }  

    return student.showSurveyResults(this, discName, projName);
  }

  /**
   * Verifies if the person can become a representative, becomes if he can.
   * @param p
   * The person that wants to become a representative.
   */
  void letBecomeRepresentative(Person p) {  //TODO mandar excecao
    Student student = getStudentById(p.getId());
    Course course = getStudentCourse(student);

    if (course.canAddRepresentative()) {
      course.addRepresentative(student);
    }
  }

  /**
   * Verifies if the person can demote from representative, demotes if he can.
   * @param p
   * The person that wants to demote from representative.
   */
  void letStopBeingRepresentative(Person p) {
    Student rep = getStudentById(p.getId());
    Course course = getStudentCourse(rep);
    course.removeRepresentative(rep);
  }
//

//========== DELEGADO ==========// 

  void letCreateSurvey(Person p, String discName, String projName) 
                                                      throws NoSuchDisciplineSelectionException,
                                                      NoSuchProjectSelectionException,
                                                      DuplicateSurveySelectionException {

    Student rep = getStudentById(p.getId());
    if (!rep.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    rep.createSurvey(discName, projName);
  }

  void letOpenSurvey(Person p, String discName, String projName) 
                                                      throws NoSuchDisciplineSelectionException,
                                                      NoSuchProjectSelectionException,
                                                      NoSurveySelectionException, 
                                                      OpeningSurveySelectionException {

    Student rep = getStudentById(p.getId());
    if (!rep.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    rep.openSurvey(discName, projName);
  }

  void letCancelSurvey(Person p, String discName, String projName) 
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException,
                                                  NonEmptySurveySelectionException,
                                                  SurveyFinishedSelectionException {

    Student rep = getStudentById(p.getId());
    if (!rep.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    rep.cancelSurvey(discName, projName);
  }

  void letCloseSurvey(Person p, String discName, String projName) 
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException,
                                                  ClosingSurveySelectionException {

    Student rep = getStudentById(p.getId());
    if (!rep.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    rep.closeSurvey(discName, projName);
  }

  void letFinishSurvey(Person p, String discName, String projName)
                                                  throws NoSuchDisciplineSelectionException, 
                                                  NoSuchProjectSelectionException,
                                                  NoSurveySelectionException,
                                                  FinishingSurveySelectionException {

    Student rep = getStudentById(p.getId());
    if (!rep.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    rep.finishSurvey(discName, projName);
  }

  List<String> letSeeDisciplineSurveys(Person p, String discName)
                                                   throws NoSuchDisciplineSelectionException, 
                                                   NoSurveySelectionException {

    Student rep = getStudentById(p.getId());
    if (!rep.attendsDiscipline(discName)) {
      throw new NoSuchDisciplineSelectionException(discName);
    }

    return rep.seeDisciplineSurveys(this, discName);
  }
// 
}

