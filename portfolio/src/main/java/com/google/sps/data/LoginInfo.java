package com.google.sps.data;

/** Class containing user login info */
public class LoginInfo {
  private final String email;
  private final boolean loggedIn;
  private final String link;

  public LoginInfo(boolean loggedIn, String email, String link) {
    this.loggedIn = loggedIn;
    this.email = email;
    this.link = link;
  }

  public boolean getLoggedIn() {
    return loggedIn;
  }

  public String getLink() {
    return link;
  }

  public String getEmail() {
    return email;
  }
}
