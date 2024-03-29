package com.fullproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;

	public StudentDbUtil(DataSource theDataSource) {
		super();
		this.dataSource = theDataSource;
	}

	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<Student>();
		Connection myConn = null;
		Statement myStmnt = null;
		ResultSet myRs = null;
		try {
			// get a connection
			myConn = dataSource.getConnection();
			// create sql statement
			String sql = "select * from student order by last_name";
			myStmnt = myConn.createStatement();

			// execute query
			myRs = myStmnt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstname = myRs.getString("first_name");
				String lastname = myRs.getString("last_name");
				String email = myRs.getString("email");

				// create new student object
				Student tempStudent = new Student(id, firstname, lastname, email);

				// add it to the list of students
				students.add(tempStudent);

			}
			return students;
		} finally {
			// close JDBC objects
			myRs.close();
			myStmnt.close();
			// put it back in the connection pool for reuse
			myConn.close();
		}
	}

	public void addStudent(Student theStudent) throws SQLException {
		Connection myConn=null;
		PreparedStatement myStmt=null;
		try {
			// get db connection
			myConn = dataSource.getConnection();
		// create sql for insert
			String sql = "insert into student " + "(first_name,last_name,email) " + "values(?,?,?)";
			myStmt = myConn.prepareStatement(sql);
		// set the param values for the student
			myStmt.setString(1, theStudent.getFirstname());
			myStmt.setString(2, theStudent.getLastname());
			myStmt.setString(3, theStudent.getEmail());

		// execute sql insert
			myStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			// clean up JDBC objects
			myConn.close();
		}

	}

	public Student getStudent(String theStudentId) throws Exception {
		//
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		try {
			// convert student id to int
			studentId = Integer.parseInt(theStudentId);
			// get connection to database
			myConn = dataSource.getConnection();
			// create sql to get selected student
			String sql = "select * from student where id=?";
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			// set params
			myStmt.setInt(1, studentId);
			// execute statement
			myRs = myStmt.executeQuery();
			// retrieve data from result set row
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				// use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			} else {
				throw new Exception("Could not find the student id :" + studentId);
			}
			return theStudent;

		} finally {
			// clean up JDBC object
			myRs.close();
			myStmt.close();
			myConn.close();
		}
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
		// get db connection
		myConn = dataSource.getConnection();
		// create SQL update statement
		String sql = "update student " + "set first_name=?,last_name=?,email=? " + "where id=?";

		// prepare Statement
		myStmt = myConn.prepareStatement(sql);
		// set params
		myStmt.setString(1, theStudent.getFirstname());
		myStmt.setString(2, theStudent.getLastname());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4, theStudent.getId());
		// execute SQL statement
		myStmt.execute();
		} finally {
			myStmt.close();
			myConn.close();
		}

	}

	public void deleteStudent(String theStudentId) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			// convert student id to int
			int studentId = Integer.parseInt(theStudentId);
			// get connection to database
			myConn = dataSource.getConnection();
			// create sql to delete student
			String sql = "delete from student where id=?";
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			// set params
			myStmt.setInt(1, studentId);
			// execute sql statement
			myStmt.execute();
		} finally {
			myStmt.close();
			myConn.close();
		}

	}
}
