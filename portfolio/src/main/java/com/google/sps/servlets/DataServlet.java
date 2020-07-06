// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns comments that users input into webpage*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String COMMENT_OBJ = "Comment";
  private static final String NAME = "name";
  private static final String TIMESTAMP = "timestamp";
  private static final String MESSAGE = "message";

  /**Retrieves previously inputted comments from database */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    PreparedQuery results =
        datastore.prepare(new Query(COMMENT_OBJ).addSort(TIMESTAMP, SortDirection.DESCENDING));
    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      comments.add(buildComment(entity));
    }
    response.setContentType("text/html;");
    response.getWriter().println(new Gson().toJson(comments));
  }

  /**Puts inputted comments into database and redirects result back to page */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    writeToDatastore(request);
    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter was not specified by the
   * client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return (value == null || value.isEmpty()) ? defaultValue : value;
  }

  /**
   * @param entity - Comment entity
   * @return Comment object
   */
  private static Comment buildComment(Entity entity) {
    long commentId = entity.getKey().getId();
    String commentName = (String) entity.getProperty(NAME);
    long commentTimestamp = (long) entity.getProperty(TIMESTAMP);
    String commentMessage = (String) entity.getProperty(MESSAGE);
    return new Comment(commentId, commentName, commentTimestamp, commentMessage);
  }

  /** Writes comments inputted into form into datastore*/
  private void writeToDatastore(HttpServletRequest request) {
    Entity commentEntity = new Entity(COMMENT_OBJ);
    commentEntity.setProperty(NAME, getParameter(request, "name-input", "Anonymous"));
    commentEntity.setProperty(MESSAGE, getParameter(request, "text-input", /* defaultValue=*/""));
    commentEntity.setProperty(TIMESTAMP, System.currentTimeMillis());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    if (!((String) commentEntity.getProperty(MESSAGE)).isEmpty()) {
      datastore.put(commentEntity);
    }
  }
}