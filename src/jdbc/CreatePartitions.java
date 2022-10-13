package jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import dbConnections.dbConnection;

public class CreatePartitions {
	Connection conn = null;

	public static void main(String[] args) throws SQLException {

		CreatePartitions p = new CreatePartitions();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a table name");
		String tableName = sc.next();
		System.out.println("Enter A year ");
		int partitionYear = sc.nextInt();
		String output = p.createPartition(tableName, partitionYear);
		System.out.println(output);

	}

	public String createPartition(String tableName, int year) throws SQLException {
		dbConnection con = new dbConnection();
		Connection conn = con.connect();
		Statement st = conn.createStatement();

		int month = 1;
		String for_clause = " for values from (";
		String tabName = tableName;
		;
		int yearVal = year;
		String nextMonthVal = String.format("%02d", month + 1);
		int nextYear = year;

		while (month <= 12) {

			if (month == 12) {
				nextMonthVal = String.format("%02d", 1);
				nextYear = year + 1;

			} else {
				nextMonthVal = String.format("%02d", (month + 1));
				yearVal = year;

			}
			String monthVal = String.format("%02d", month);
			String fromDate = "'" + yearVal + "-" + monthVal + "-01') to ";
			String toDate = "('" + nextYear + "-" + nextMonthVal + "-01');";
			String query = "create table " + tabName + "_" + yearVal + monthVal + " partition of " + tabName
					+ for_clause + fromDate + toDate;
	
				st.execute(query);

		

			month++;

		}
		st.close();

		return "Partitions created";
	}

}
