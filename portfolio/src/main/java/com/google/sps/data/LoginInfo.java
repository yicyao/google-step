package com.google.sps.data;

/** Class containing user login info */
public final class LoginInfo {
  /** Log-in link if user is logged out, log-out link if the user is logged in.*/
  private final Boolean loggedIn;
  private final String link;

  public LoginInfo(Boolean loggedIn, String link) {
    this.loggedIn = loggedIn;
    this.link = link;
  }

  public Boolean getLoggedIn() {
    return loggedIn;
  }
}
