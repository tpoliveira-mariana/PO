package sth.exceptions;

/** Exception thrown when phone number is not valid */
public class InvalidPhoneNumberException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201811091355L;

  /** Phone number */
  private String _number;

  /**
   * @param number
   */
  public InvalidPhoneNumberException(String number) {
    _number = number;
  }

  /** @return phone number */
  public String getNumber() {
    return _number;
  }
}