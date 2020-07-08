package com.google.sps.servlets;

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
  private Map<String, Integer> activityVotes = new HashMap<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getWriter().println(new Gson().toJson(activityVotes));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String activity = request.getParameter("activity");
    int currentVotes = activityVotes.containsKey(activity) ? activityVotes.get(activity) : 0;
    activityVotes.put(activity, currentVotes + 1);

    response.sendRedirect("/fun-features.html");
  }
}
