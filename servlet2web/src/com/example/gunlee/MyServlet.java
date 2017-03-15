package com.example.gunlee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 3. 14.
 */
public class MyServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String msg = req.getParameter("msg");
		resp.setContentType("text/html");

		// Actual logic goes here.
		PrintWriter out = resp.getWriter();
		out.println("<h1>" + msg + "</h1>");
	}
}
