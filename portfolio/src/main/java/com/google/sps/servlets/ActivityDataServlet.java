package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/activity-data")
public class ActivityDataServlet extends HttpServlet {
  private static final String ACTIVITY_OBJ = "Activity";
  private static final String ACTIVITY = "activity";
  private static final String ACTIVITY_COUNT = "count";
  

  // write javadoc
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    PreparedQuery results = datastore.prepare(new Query(ACTIVITY_OBJ));
    Map<String, Long> activityVotes = new HashMap<>();
    for (Entity entity : results.asIterable()) {
      String activityName = (String) entity.getProperty(ACTIVITY);
      long count = (long) entity.getProperty(ACTIVITY_COUNT);

      if (activityVotes.containsKey(activityName)) {
        activityVotes.put(activityName, activityVotes.get(activityName) + count);
      } else {
        activityVotes.put(activityName, count);
      }
    }

    response.setContentType("application/json");
    response.getWriter().println(new Gson().toJson(activityVotes));
  }

  /** Takes single activity input and writes into database*/
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    writeToDatastore(request.getParameter(ACTIVITY));
    response.sendRedirect("/fun-features.html");
  }

  private void writeToDatastore(String activity) {
    Entity activityEntity = new Entity(ACTIVITY_OBJ);
    activityEntity.setProperty(ACTIVITY, activity);
    activityEntity.setProperty(ACTIVITY_COUNT, 1);
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(activityEntity);
  }
}
