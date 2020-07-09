
package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  /**Retrieves user login status and returns appropriate form */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    StringBuilder loginString = new StringBuilder();

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      loginString.append("<p>Hello " + userService.getCurrentUser().getEmail() + "!</p>");
      String urlToRedirectToAfterUserLogsOut = "/fun-features.html";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
      loginString.append("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
    } else {
      String urlToRedirectToAfterUserLogsIn = "/fun-features.html";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
      loginString.append("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    }
    response.setContentType("text/html;");
    response.getWriter().println(new Gson().toJson(loginString.toString()));
  }
}
