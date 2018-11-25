package sth.exceptions;

/** Exception thrown when trying to create a survey that already exists */
public class DuplicateSurveySelectionException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201811251120L;

  /** Discipline name. */
  private String _discipline;

  /** Project name. */
  private String _project;

  /**
   * @param disc
   * @param proj
   */
  public DuplicateSurveySelectionException(String disc, String proj) {
    _discipline = disc;
    _project = proj;
  }

  /** @return discipline's name */
  public String getDiscipline() {
    return _discipline;
  }

  /** @return project's name */
  public String getProject() {
    return _project;
  }
}