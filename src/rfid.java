import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class rfid {
	public static launchDevice device;
	public static void main(String[] args) throws IOException, ParseException {
	
		DB.readItems("");
		Thread rfidReadThread = new Thread(new threadRfidRead(),
				"rfidReadThread");
		 rfidReadThread.start();
//		String idTag = "670063C831FD";
//		Thread threadCheckDB = new Thread(new threadMysqlCheck(idTag),
//				"checkDB");
//		threadCheckDB.start();
//		DB db = new DB();
//		db.checkExpiration("13/04/13", "13/04/15");
//		 device = new launchDevice();
		 
	}

	public static String readIdTag() throws UnsupportedEncodingException,
			IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new FileReader("/dev/ttyUSB0"));
		String idTagStr = "";
		if (in.read() == 2) {
			for (int i = 0; i < 12; i++) {
				int idTagInt = in.read();
				String b = Integer.toBinaryString(idTagInt);
				byte[] bval = new BigInteger(b, 2).toByteArray();
				idTagStr += new String(bval, "UTF-8");
			}
		}
		return idTagStr;
	}

}

class threadRfidRead implements Runnable {

	Thread runner;

	public threadRfidRead() {
	}

	public threadRfidRead(String threadName) {
	}

	public void run() {
		while (true) {
			try {
				String idTag = rfid.readIdTag();
				if (idTag != "") {
					Thread threadCheckDB = new Thread(new threadMysqlCheck(
							idTag), "checkDB");
					threadCheckDB.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class threadMysqlCheck implements Runnable {

	Thread runner;
	String idTag;

	public threadMysqlCheck() {
	}

	public threadMysqlCheck(String idTag) {
		this.idTag = idTag;
	}

	public void run() {
		try {
			checkinDB(idTag);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void checkinDB(String idTag) throws ClassNotFoundException,
			SQLException, ParseException {
		// TODO Auto-generated method stub
		DB database = new DB();
		int idUser = DB.readItems(idTag);
		if (idUser > 0) {
			System.out.println(idTag+ "is in DB");
			Calendar currentDate = Calendar.getInstance(); 
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss"); // format it as per your requirement
		
			String dateNow = formatter.format(currentDate.getTime());
d
			int id = database.checkAdjustable(idUser);
			if (id > 0) {
				database.insertHistoryDB(id, 0, "", dateNow, true);
			} else {

				database.insertHistoryDB(0, idUser, dateNow, "0", false);
			}
			database.checkExpiration(database.getExpireFrom(),database.getExpireTo());
		} else {
			 System.out.println(idTag+ "is NOT in DB");
		}
	}

}