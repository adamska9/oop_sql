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
		Scanner scan = new Scanner(System.in);
	     
		 LinkedHashMap<String, String> rows = new LinkedHashMap<String, String>(4);
		 String column;
		 boolean check = false;
	     rows.put("customer", " ");
	     rows.put("date_start", "0"); 
	     rows.put("date_end", "0");
	     rows.put("price", "0");
	     Set set = rows.entrySet();
	     
	     
	    while(!check) { 
	    	Iterator i = set.iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			column = (String)me.getKey();
			System.out.print("New "+column + ": ");
			rows.put(column, scan.nextLine());
		}
		check = check(rows);
	    }
		
		String values = "\""+rows.get("customer")+"\""+ ", " + rows.get("date_start")+ ", " + rows.get("date_end")+", " + rows.get("price");
		String add = "INSERT INTO contract (customer, date_start, date_end, price)"
				+ "VALUES" + "(" + values + ")" +";";
		return add;
	}
//---------------------------------------------------------------------------------------------	 
	static String edit() {
		 LinkedHashMap<String, String> rows = new LinkedHashMap<String, String>(4);
		 Scanner scan = new Scanner(System.in);
		 String update = "";
		 int id = 0;
		 System.out.println("Please enter the number of the row, column you want to change and the new value (press Enter after each entry): ");
		
		 String column = "", value;
		 boolean check = false;
	     rows.put("customer", " ");
	     rows.put("date_start", "0"); 
	     rows.put("date_end", "0");
	     rows.put("price", "0");
	     Set set = rows.entrySet();
		 
		 while(!check){
			 
			 System.out.println("# of the row to edit: ");
			 id = scan.nextInt();
			 System.out.println("Column to edit: ");
			 scan.nextLine();
			 column = scan.nextLine();
			 System.out.println("New value: ");
			 value = scan.nextLine();
			 
			 rows.put(column, value);
			 
			 check = check(rows);
	   }
		 update = "UPDATE contract SET "+ column +" = "
			 	   +"\""+ rows.get(column) +"\""+" WHERE id = " 
			 	   + id + ";";	
	   return update;
	 }
	
//---------------------------------------------------------------------------------------------	
	static String delete() {
		 Scanner scan = new Scanner(System.in);
		 System.out.println("Please enter the number of the row you want to delete: ");
		 
		 int id = scan.nextInt();
		 
		 String delete = "DELETE FROM contract WHERE id  = \"" +id+"\";"; 
		 
		 return delete;
	 }
//---------------------------------------------------------------------------------------------	
	static boolean check(LinkedHashMap<String,String> lhm) {
		boolean check = true;
		for(String key : lhm.keySet()) {
			switch(key) {
			case "date_start":
				try {
					int i = Integer.parseInt(lhm.get(key));
				} catch(NumberFormatException e) {
					System.out.println("Please enter a valid start date value.");
					check = false;
				}
				break;
			case "date_end":
				try {
					int i = Integer.parseInt(lhm.get(key));
				} catch(NumberFormatException e) {
					System.out.println("Please enter a valid end date value.");
					check = false;
				}
				break;
			case "price":
				try {
					double d = Double.parseDouble(lhm.get(key));
				} catch(NumberFormatException e) {
					System.out.println("Please enter a valid price value.");
					check = false;
				}
				break;
			}
		}
		return check;
	}
	
	
}

