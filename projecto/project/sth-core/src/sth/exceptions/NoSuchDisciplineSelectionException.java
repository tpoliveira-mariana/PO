package sth.exceptions;

/** Exception thrown when discipline doesn't exist or when professor doesn't teach it */
public class NoSuchDisciplineSelectionException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201811081236L;

  /** Discipline name. */
  private String _discipline;

  /**
   * @param disc
   */
  public NoSuchDisciplineSelectionException(String disc) {
    _discipline = disc;
  }

  /** @return discipline's name */
  public String getDiscipline() {
    return _discipline;
  }
}