import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import com.mysql.jdbc.PreparedStatement;

public class DB {

	private static final String url = "jdbc:mysql://mysql51.websupport.sk:3309/rpi";
	private static final String user = "rpi";
	private static final String pass = "cvak";

	static String expireFrom = null;
	static String expireTo = null;
	public DB() {
		// TODO Auto-generated constructor stub
	}

	public static int readItems(String idTag) {
		int id = 0;
		ResultSet rs = null;
		Statement statement = null;
		String query = "SELECT * FROM users WHERE idtag like '" + idTag + "%' ";
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(url, user, pass);
			statement = (Statement) con.createStatement();
			rs = ((java.sql.Statement) statement).executeQuery(query);
			while (rs.next()) {
				id = rs.getInt("id");
				idTag = rs.getString("idTag");
				expireFrom = rs.getString("expire_from");
				expireTo = rs.getString("expire_to");
			}
			
//			checkExpiration(expireFrom, expireTo);
			
			rs.close();
			statement.close();
			con.close();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// tvRead.setText(resulString);
		return 0;
	}
	public static String getExpireFrom() {
		return expireFrom;
	}
	public static String getExpireTo() {
		return expireTo;
	}
	public static void checkExpiration(String expireFrom, String expireTo) throws ParseException {
		
//		 expireFrom= "13/04/13";
		java.util.Date dateExpireFrom =  new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse(expireFrom);
		System.out.println(dateExpireFrom); 
		
		java.util.Date dateExpireTo =  new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse(expireTo);
		System.out.println(dateExpireTo); 
		
		Calendar currentDate = Calendar.getInstance(); 
		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd/MM/yy"); 
		String dateNow = formatter.format(currentDate.getTime());
		System.out.println(dateNow);
		
		java.util.Date dateCurrent =  new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse(dateNow);
		System.out.println(dateExpireTo); 
		
		
		if (dateExpireFrom.compareTo(dateCurrent) * dateCurrent.compareTo(dateExpireTo) > 0){
			System.out.println("Entrance allow");

			Thread launchDevice = new Thread(new launchDevice(),
					"launchDevice");
			launchDevice.start();
		}else {
			System.out.println("Entrance DISallow");
		}
	}

	public static boolean updateAdjustable(String idUser)
			throws ClassNotFoundException, SQLException {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(url, user, pass);
			java.sql.PreparedStatement ps = null;
			ps = con.prepareStatement("UPDATE  history SET exitout = ? WHERE  iduser = + "
					+ idUser);
			// ps.setString(1, idTag);
			int count = ps.executeUpdate();
			ps.close();
			con.close();

			return true;
		} finally {

		}
	}

	public static int checkAdjustable(int idUser) {
		int id = 0;
		int adjustable = 0;
		ResultSet rs = null;
		Statement statement = null;
		String query = "SELECT * FROM history WHERE iduser  =  '" + idUser
				+ "' AND adjustable  =  '" + adjustable + "'";

		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(url, user, pass);
			statement = (Statement) con.createStatement();
			rs = ((java.sql.Statement) statement).executeQuery(query);
			while (rs.next()) {
				id = rs.getInt("id");
			}
			rs.close();
			statement.close();
			con.close();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// tvRead.setText(resulString);
		return 0;
	}

	public static int insertHistoryDB(int id, int idUser, String entry,
			String exit, Boolean adjustable) throws ClassNotFoundException,
			SQLException {
		// TODO Auto-generated method stub

		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(url, user, pass);
			if (adjustable) {
				java.sql.PreparedStatement ps = null;
				ps = con.prepareStatement("UPDATE  history SET exitout = ? , adjustable = ? WHERE  id = + "
						+ id);
				ps.setString(1, exit);
				ps.setInt(2, 1);
				int count = ps.executeUpdate();
				ps.close();
			} else {
				java.sql.PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO history (iduser, entry, exitout, adjustable) VALUES (?,?,?,?)");
				ps.setInt(1, idUser);
				ps.setString(2, entry);
				ps.setString(3, exit);
				ps.setBoolean(4, adjustable);
				int count = ps.executeUpdate();
				System.out.println("user: "+ idUser+" entry: "+ entry+ "in ");
				ps.close();
			}
			con.close();
		} finally {

		}
		return id;
	}
}
