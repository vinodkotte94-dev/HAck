package com.smartagri;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SoilServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String location = req.getParameter("location"); // e.g. "Punjab" or "Ludhiana"
    if (location == null) location = "";

    resp.setContentType("application/json;charset=UTF-8");

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(
            "SELECT Location_Name, Latitude, Longitude, WRB_Reference_Soil_Group, USDA_Soil_Order, Description, Dominant_Soil_Forming_Process, Suitable_Crops FROM Soil_Dataset WHERE Location_Name LIKE ? LIMIT 1")) {
      ps.setString(1, "%" + location + "%");
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String json = String.format(
            "{\"location\":\"%s\",\"latitude\":%s,\"longitude\":%s,\"wrb\":\"%s\",\"usda\":\"%s\",\"description\":\"%s\",\"process\":\"%s\",\"crops\":\"%s\"}",
            esc(rs.getString("Location_Name")),
            rs.getObject("Latitude"),
            rs.getObject("Longitude"),
            esc(rs.getString("WRB_Reference_Soil_Group")),
            esc(rs.getString("USDA_Soil_Order")),
            esc(rs.getString("Description")),
            esc(rs.getString("Dominant_Soil_Forming_Process")),
            esc(rs.getString("Suitable_Crops"))
          );
          resp.getWriter().write(json);
        } else {
          resp.getWriter().write("{\"error\":\"No data found\"}");
        }
      }
    } catch (SQLException e) {
      resp.getWriter().write("{\"error\":\"" + esc(e.getMessage()) + "\"}");
    }
  }

  private static String esc(String s) {
    if (s == null) return "";
    return s.replace("\\", "\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","");
  }
}