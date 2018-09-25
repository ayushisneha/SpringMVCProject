package com.decorators;

import java.sql.*;
import java.sql.DriverManager;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {
	@RequestMapping("/register")
	public void register(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.sendRedirect("register.jsp");
	}
	@RequestMapping("/register1")
	public ModelAndView register1(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException, IOException
	{
		String fullname=request.getParameter("fullname");
		String email=request.getParameter("email");
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String isTaken="select * from users where username='"+ username+"'";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/customers", "root", "Qwerty@123");
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(isTaken);
		if((rs.next()))
		{
			ModelAndView mv=new ModelAndView();
			mv.setViewName("register.jsp");
			mv.addObject("errMessage","Username already exists...<br>");
			st.close();
			con.close();
			return mv;
		}
		else
		{
			String registerQuery="insert into users set fullname='"+fullname+"',email='"+email+"',username='"+username+"',password='"+password+"'";
			Statement st1=con.createStatement();
			int res=st1.executeUpdate(registerQuery);
			HttpSession session=request.getSession();
			session.setAttribute("name",username);
			response.sendRedirect("Home.jsp");
			return null;
		}
	}
	@RequestMapping("/login")
	public void login(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.sendRedirect("login.jsp");
	}
	@RequestMapping("/login1")
	public ModelAndView login1(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException, IOException
	{
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String isTaken="select * from users where username='"+ username+"' and password='"+password+"'" ;
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/customers", "root", "Qwerty@123");
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(isTaken);
		if(!rs.next())
		{
			ModelAndView mv=new ModelAndView();
			mv.setViewName("login.jsp");
			mv.addObject("errMessage","Invalid Credentials...<br>");
			st.close();
			con.close();
			return mv;
		}
		else
		{
			HttpSession session=request.getSession();
			session.setAttribute("name",username);
			response.sendRedirect("Products.jsp");
			return null;
		}
	}
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		HttpSession session=request.getSession();
		session.removeAttribute("username");
		session.invalidate();
		response.sendRedirect("index.jsp");
	}
}
