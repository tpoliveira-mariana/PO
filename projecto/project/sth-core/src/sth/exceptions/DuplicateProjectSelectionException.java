package sth.exceptions;

/** Exception thrown when trying to create a project that already exists */
public class DuplicateProjectSelectionException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201811081228L;

  /** Discipline name. */
  private String _discipline;

  /** Project name. */
  private String _project;

  /**
   * @param disc
   * @param proj
   */
  public DuplicateProjectSelectionException(String disc, String proj) {
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