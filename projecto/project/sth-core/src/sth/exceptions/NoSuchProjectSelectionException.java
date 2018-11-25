package sth.exceptions;

/** Exception thrown when trying to close project that doesn't exist */
public class NoSuchProjectSelectionException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201811081240L;

  /** Discipline name. */
  private String _discipline;

  /** Project name. */
  private String _project;

  /**
   * @param disc
   * @param proj
   */
  public NoSuchProjectSelectionException(String disc, String proj) {
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