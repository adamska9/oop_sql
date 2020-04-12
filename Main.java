package lab2;
import java.sql.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		Connection con = getConnection();
		String select = "SELECT * FROM contract;";
		Scanner scan = new Scanner(System.in);
		boolean exit = false;
		try {
			Statement s = con.createStatement();
			ResultSet rows = s.executeQuery(select);
			output(rows);
			
			while(!exit) {
			System.out.println("Press: add - 1, edit - 2, delete - 3, exit - 4:");
			int n = scan.nextInt();
			switch(n) {
			case 1: s.addBatch(add()); break;
			case 2: s.addBatch(edit()); break;
			case 3: s.addBatch(delete()); break;
			case 4: exit = true; System.exit(0);;
			}
			s.executeBatch();
			
			rows = s.executeQuery(select);
			output(rows);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//---------------------------------------------------------------------------------------------	 	
	private static Connection getConnection()
	{	//get a connection to database
		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/Contracts";
		String user = "root";
		String pw = "npass01";
		try {
			con = DriverManager.getConnection(url, user, pw);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
		}
//---------------------------------------------------------------------------------------------	 	
	static void output(ResultSet rows) {
		try {
			System.out.format("%-3s|%-20s|%-10s|%-10s|%-10s", "ID", "customer", "date_start", "date_end", "price");
			System.out.println("\n+--+--------------------+----------+----------+----------");
			while (rows.next()) {
				System.out.format("%-3d|%-20s|%-10d|%-10d|%-10f", rows.getInt("id"), 
						rows.getString("customer"), rows.getInt("date_start"),
						rows.getInt("date_end"), rows.getDouble("price"));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}  
//---------------------------------------------------------------------------------------------	 	
	static String add() {
		ArrayList<String> arrValues = new ArrayList<String>();
		
		System.out.println("Please enter name, start date, end date and price (press Enter after each entry): ");
		Scanner scan = new Scanner(System.in);
		for (int i = 0 ; i < 4; i++) {
			 arrValues.add(scan.nextLine());
		}
		String customer = arrValues.get(0);
		int date_start = Integer.parseInt(arrValues.get(1));
		int date_end = Integer.parseInt(arrValues.get(2));
		Double price = Double.parseDouble(arrValues.get(3));
		String values = "\""+customer+"\""+ ", " + date_start+ ", " + date_end+", " + price;
		String add = "INSERT INTO contract (customer, date_start, date_end, price)"
				+ "VALUES" + "(" + values + ")" +";";
		return add;
	}
//---------------------------------------------------------------------------------------------	 
	static String edit() {
		 ArrayList<String> arrValues = new ArrayList<String>() {{add("0");add("0");add("0");}};
		 String update = "";
		 Scanner scan = new Scanner(System.in);
		 boolean check = false;
		 System.out.println("Please enter the number of the row, column you want to change and the new value (press Enter after each entry): ");
		 
		 while(!check){
			 
			 for (int i = 0 ; i < 3; i++) 
				arrValues.set(i, scan.nextLine());
			 
		 
		 int id = Integer.parseInt(arrValues.get(0));
		 String column = arrValues.get(1);
		 if(column.equals("customer")) {
			  String value = arrValues.get(2);
			  update = "UPDATE contract SET "+ column+" = "
				 	   +"\""+value +"\""+" WHERE id = " 
				 	   + id + ";";	
			  check = true;
		 } else if(column.equals("date_start") || column.equals("date_end")) {
			 int value = Integer.parseInt(arrValues.get(2));
			  update = "UPDATE contract SET "+ column+" = "
				 	   +"\""+ value +"\""+" WHERE id = " 
				 	   + id + ";";	
			  check = true;
		 } else if(column.equals("price")) {
			 double value = Double.parseDouble(arrValues.get(2));
			 update = "UPDATE contract SET "+ column+" = "
				 	   +"\""+ value +"\""+" WHERE id = " 
				 	   + id + ";";
			 check = true;
		 } else System.out.println("Please enter a valid column/value");
	   }
	   return update;
	 }
	
//---------------------------------------------------------------------------------------------	
	static String delete() {
//		 ArrayList<String> arrValues = new ArrayList<String>();
		 Scanner scan = new Scanner(System.in);
		 System.out.println("Please enter the number of the row you want to delete: ");
		 
		 int id = scan.nextInt();
		 
		 String delete = "DELETE FROM contract WHERE id  = \"" +id+"\";"; 
		 
		 return delete;
	 }

//private class EditValues {
//	int id = 0;
//	String column = "0";
//	
//	EditValues(int id, String column){
//		this.id = id;
//		this.column = column;
//	}		
//	
//}

}


