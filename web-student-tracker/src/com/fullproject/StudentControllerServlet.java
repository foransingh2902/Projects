package com.fullproject;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;
	private StudentDbUtil studentDbUtil;
    /**
     * @see HttpServlet#HttpServlet()
     */

    public StudentControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

// over ride the init method of parent class Generic Servlet
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		// create our student db util object... and pass in the conn pool/ datasource
	try {
		studentDbUtil=new StudentDbUtil(dataSource);
	}	
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// List the students.... in MVC fashion
		try {
			// read the command parameter
			String theCommand = request.getParameter("command");
			// if the command is missing, then default to listing students
			if (theCommand == null) {
				theCommand = "LIST";
			}
			// route to appropriate method
			switch (theCommand) {
			case "List":
				// list students
				listStudents(request, response);
			case "ADD":
				addStudents(request, response);
				break;
			case "LOAD":
				loadStudents(request, response);
				break;
			case "UPDATE":
				updateStudents(request, response);
				break;
			case "DELETE":
				deleteStudents(request, response);
			default:
				// default is listing
				listStudents(request, response);
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void deleteStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		// delete the student from database
		studentDbUtil.deleteStudent(theStudentId);
		// send them back to "list students" page
		listStudents(request, response);

	}

	private void updateStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from the form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		// perform update on database
		studentDbUtil.updateStudent(theStudent);
		// send then back to the "list students" page
		listStudents(request, response);

	}

	private void loadStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		// get student from database(db util)
		Student thestudent = studentDbUtil.getStudent(theStudentId);
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", thestudent);
		// send to jsp page: update-student-form.jsp (pre-populating form)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);


	}

	private void addStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);

		// add the student to the database
		studentDbUtil.addStudent(theStudent);
		// send back to main page (the student list)
		listStudents(request, response);

	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get students from db util
		List<Student> students = studentDbUtil.getStudents();
		// add students to the request
		request.setAttribute("STUDENT_LIST", students);
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);

	}

}
