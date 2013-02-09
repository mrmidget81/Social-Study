package com.bill.socialstudy.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Properties;


import com.bill.socialstudy.dataobjects.ClassObject;
import com.bill.socialstudy.dataobjects.CollegeObject;
import com.bill.socialstudy.dataobjects.StudySessionObject;
import com.bill.socialstudy.dataobjects.UserObject;

public class DatabaseHelper {
	
	private static Connection getConnection() {

		try {
			Class.forName("org.postgresql.Driver").newInstance();
			String url = DatabaseInfo.url;
			Properties props = new Properties();
			props.put("user", DatabaseInfo.user);
			props.put("password", DatabaseInfo.password);
			props.put("ssl", "true");
			props.put("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
			Connection conn = DriverManager.getConnection(url, props);
			return conn;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static UserObject getUserObject(int id) throws SQLException, NoSuchElementException {
		Statement stmt = getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE U_Id = " + id + ";");
		
		if (rs.next()) {
			int college = rs.getInt("college");
			String name = rs.getString("name");
			
			ArrayList<Integer> classIds = new ArrayList<Integer>();
			if (stmt.execute("SELECT * FROM userclasses WHERE userid = " + id + ";")) {
				ResultSet res = stmt.getResultSet();
				
				while (res != null) {
					classIds.add(Integer.valueOf(res.getInt("class")));
				}
			}

			ArrayList<Integer> sessionIds = new ArrayList<Integer>();
			if (stmt.execute("SELECT * FROM sessionusers WHERE userid = " + id + ";")) {
				ResultSet res = stmt.getResultSet();
				
				while (res != null) {
					sessionIds.add(Integer.valueOf(res.getInt("session")));
				}
			}
			
			stmt.close();
			
			return new UserObject(id, college, name, classIds.toArray(new Integer[] {}), sessionIds.toArray(new Integer[] {}));
		}
		
		stmt.close();
		
		throw new NoSuchElementException();
	}
	
	public static boolean userExists(int id) {
		try {
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE U_Id = " + id + ";");
			
			if (rs.next())
				return true;
			
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static CollegeObject getCollegeObject(int id) throws SQLException, NoSuchElementException {
		Statement stmt = getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM colleges WHERE CO_Id = " + id + ";");
		
		if (rs.next()) {
			String name = rs.getString("name");
			
			ArrayList<Integer> classIds = new ArrayList<Integer>();
			if (stmt.execute("SELECT * FROM collegeclasses WHERE college = " + id + ";")) {
				ResultSet res = stmt.getResultSet();
				
				while (res != null) {
					classIds.add(Integer.valueOf(res.getInt("class")));
				}
			}

			stmt.close();
			
			return new CollegeObject(id, name, classIds.toArray(new Integer[] {}));
		}

		stmt.close();
		
		throw new NoSuchElementException();
	}
	
	public static StudySessionObject getStudySessionObject(int id) throws SQLException, NoSuchElementException {
		Statement stmt = getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM sessions WHERE S_Id = " + id + ";");
		
		if (rs.next()) {
			String name = rs.getString("name");
			
			Date day = rs.getDate("day");
			Time time = rs.getTime("session_time");
			Calendar date = Calendar.getInstance();
			date.set(day.getYear(), day.getMonth(), day.getDate(), time.getHours(), day.getMinutes());

			stmt.close();
			
			return new StudySessionObject(id, name, date);
		}

		stmt.close();
		
		throw new NoSuchElementException();
	}
	
	public static ClassObject getClassObject(int id) throws SQLException, NoSuchElementException {
		Statement stmt = getConnection().createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM classes WHERE C_Id = " + id + ";");
		
		if (rs.next()) {
			String name = rs.getString("name");
			String instructor = rs.getString("instructor");
			
			ArrayList<Integer> sessionIds = new ArrayList<Integer>();
			if (stmt.execute("SELECT * FROM classsessions WHERE class = " + id + ";")) {
				ResultSet res = stmt.getResultSet();
				
				while (res != null) {
					sessionIds.add(Integer.valueOf(res.getInt("session")));
				}
			}

			stmt.close();
			
			return new ClassObject(id, name, instructor, sessionIds.toArray(new Integer[] {}));
		}

		stmt.close();
		
		throw new NoSuchElementException();
	}
	
	public static int addClassObject(ClassObject co, int collegeId) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		String name = co.getName().replaceAll("\'", "\'\'");
		String instr = co.getInstructor().replaceAll("\'", "\'\'");
		
		String query = "INSERT INTO classes (name, instructor) VALUES (\'" +
				name + "\', \'" +	instr + "\');";
		
		int update = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		ResultSet keys = stmt.getGeneratedKeys();
		
		if (!keys.next() || update != 1) {
			stmt.close();
			throw new SQLException();
		}
		
		int id = keys.getInt("c_id");
		stmt.executeUpdate("INSERT INTO collegeclasses (college, class) VALUES (" +
				collegeId + ", " + id + ");");
		
		if (update != 1) {
			stmt.close();
			throw new SQLException();
		}
		
		stmt.close();
		return id;
	}
	
	public static void addUserObject(UserObject uo) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		int id = uo.getFacebookId();
		String name = uo.getName().replaceAll("\'", "\'\'");
		int college = uo.getCollege();
		
		
		String query = "INSERT INTO users (u_id, name, college) VALUES (" +
				id + ", \'" + name + "\', " + college + ");";
		
		int update = stmt.executeUpdate(query);

		stmt.close();
		
		if (update != 1)
			throw new SQLException();
	}
	
	public static int addCollegeObject(CollegeObject co) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		String name = co.getName().replaceAll("\'", "\'\'");
		
		String query = "INSERT INTO colleges (name) VALUES (\'" +
				name + "\');";
		
		int update = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		ResultSet keys = stmt.getGeneratedKeys();

		stmt.close();
		
		if (!keys.next() || update != 1)
			throw new SQLException();

		return keys.getInt("co_id");
	}
	
	public static int addSessionObject(StudySessionObject so, int classId) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		Calendar date = so.getDate();
		
		String name = so.getName().replaceAll("\'", "\'\'");
		String day = String.format("%tF", date);
		String time = String.format("%tR", date);
		
		String query = "INSERT INTO sessions (name, day, session_time) VALUES (\'" +
				name + "\', \'" + day + "\', \'" + time + "\');";
		
		int update = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		ResultSet keys = stmt.getGeneratedKeys();

		if (!keys.next() || update != 1) {
			stmt.close();
			throw new SQLException();
		}
		
		int id = keys.getInt("s_id");
		stmt.executeUpdate("INSERT INTO classsessions (class, session) VALUES (" +
				classId + ", " + id + ");");
		
		if (update != 1) {
			stmt.close();
			throw new SQLException();
		}
		
		stmt.close();
		return id;
	}
	
	public static void addUserToSession(UserObject user, StudySessionObject session) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		int update = stmt.executeUpdate("INSERT INTO sessionusers (session, userid) VALUES (" +
				session.getId() + ", " + user.getFacebookId() + ");");
		
		if (update != 1) {
			stmt.close();
			throw new SQLException();
		}
		
		stmt.close();
	}
	
	public static void addClassToUser(UserObject user, ClassObject co) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		int update = stmt.executeUpdate("INSERT INTO userclasses (userid, class) VALUES (" +
				user.getFacebookId() + ", " + co.getId() + ");");
		
		if (update != 1) {
			stmt.close();
			throw new SQLException();
		}
		
		stmt.close();
	}
	
	public static void deleteUser(int id) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		stmt.executeUpdate("DELETE FROM users WHERE u_id = " + id + ";");
		
		stmt.close();
	}
	
	public static void deleteSession(int id) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		stmt.executeUpdate("DELETE FROM sessions WHERE s_id = " + id + ";");
		
		stmt.close();
		
	}
	
	public static void deleteClass(int id) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		stmt.executeUpdate("DELETE FROM classes WHERE c_id = " + id + ";");
		
		stmt.close();
		
	}
	
	public static void deleteCollege(int id) throws SQLException {
		Statement stmt = getConnection().createStatement();
		
		stmt.executeUpdate("DELETE FROM colleges WHERE co_id = " + id + ";");
		
		stmt.close();
		
	}
	
}
