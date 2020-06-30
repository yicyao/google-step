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

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet that returns comments that users input
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private List<String> comments = new ArrayList<>();

  // Retrieves previously inputted comments
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    response.getWriter().println(new Gson().toJson(comments));
  }

  // Retrieves inputted comments from form and redirects result back to page
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = gson.toJson(getParameter(request, "text-input", /* defaultValue=*/""));
    comments.add(json);

    response.setContentType("text/html;");
    response.getWriter().println(json);
    response.sendRedirect("/index.html");
  }

  //@return the request parameter, or the default value if the parameter was not specified by the
  //client
  Optional String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return (value == null) ? defaultValue : value;
  }
}