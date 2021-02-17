package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import game.player_info;

public class SaveServer extends JFrame {

	private JTextArea wordsBox;
	private ObjectOutputStream outputToClient;
	private ObjectInputStream inputFromClient;
	private ServerSocket serverSocket;
	PreparedStatement insertStatement, updateStatement;
	player_info player = new player_info();
	ResultSet rs ;
	Connection con;
	
	public SaveServer() {
		createMainPanel();
		buildConnect();
		wordsBox.append("Ready to Accept Connections\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		setVisible(true);
		startServ();
	}
	
	public void createMainPanel() {
		wordsBox = new JTextArea(35,10);

		JScrollPane listScroller = new JScrollPane(wordsBox);
		this.add(listScroller, BorderLayout.CENTER);
		listScroller.setPreferredSize(new Dimension(250, 80));
	}
	public void startServ() {
		try {
			serverSocket = new ServerSocket(8000);
			wordsBox.append("Server started.\n");
			while (true) {
				Socket socket = serverSocket.accept();
				inputFromClient = new ObjectInputStream(socket.getInputStream());
				outputToClient = new ObjectOutputStream(socket.getOutputStream());
				
				Object object = inputFromClient.readObject();
				if (object instanceof player_info) {
					player = (player_info)object;
					System.out.println(checkPlayerExist(player.getName()));
					if (checkPlayerExist(player.getName())) {
						updateData(player);
						wordsBox.append("database update.\n");
					}
					else {
						insertData(player);
						wordsBox.append("datebase save.\n");
					}
				}
				else {
					String fromClient = (String)object;
					if (fromClient.equals("Load the game")) {
						String rs = getData();
						outputToClient.writeObject((Object)rs);
						outputToClient.flush();
						wordsBox.append("output all data in database to client.\n");
						Thread.sleep(1000);
					}
					else {
						if (checkPlayerExist(fromClient)) {
							player_info outputPlayer = new player_info();
							outputPlayer = outputData(fromClient);
							outputToClient.writeObject(outputPlayer);
							outputToClient.flush();
							wordsBox.append("Output to Client.\n");
							Thread.sleep(1000);
						}
						else {
							String warning = "Database doesn't have this player!";
							outputToClient.writeObject((Object)warning);
							outputToClient.flush();
							wordsBox.append("No such Player!\n");
						}
					}
				}
				
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean checkPlayerExist(String playerName) {
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:yahtzee.db");
			Statement stmt = con.createStatement();
			String sql = "Select count(*) from Players Where PName='" + playerName + "'";
			//ResultSet rs = con.getMetaData().getColumns(null, null, "Players", playerName);
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			int count = rs.getInt(1);
			if (count>0) {
				rs.close();
				con.close();
				return true;
			}
			else {
				rs.close();
				con.close();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rootPaneCheckingEnabled;
	}
	private void updateData(player_info player) {
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:sqlite:yahtzee.db");
			String sql  ="Update Players Set "
					+ "Aces='" + player.getacesTxt()+"',"
					+ "Twos='" + player.gettwosTxt() +"',"
					+ "Threes='" +player.getthreesTxt() + "',"
					+ "Fours='" + player.getfoursTxt() + "',"
					+ "Fives='" + player.getfivesTxt() + "',"
					+ "Sixes='" + player.getsixsTxt() + "',"			
					+ "ThreeOfAKind='" + player.getthreekTxt() + "',"
					+ "FourOfAKind='" + player.getfourkTxt() + "',"
					+ "FullHouse='" + player.getfullTxt() + "',"
					+ "SmallStraight='" + player.getsmallTxt()+ "',"
					+ "LargeStraight='" + player.getlargeTxt() + "',"
					+ "Chance='" + player.getchanceTxt() + "',"
					+ "Yahtzee='" + player.getyhzTxt() + "',"
					+ "YahtzeeBonus='" + player.getyhzbnsTxt() + "',"
					+ "RollCount='" + Integer.toString(player.getRollNum()) + "',"
					+ "Round='" + Integer.toString(player.getTurnNum()) + "'"
					+ "Where PName='" + player.getName() + "'";
					
			Statement statement = con.createStatement();
			statement.execute(sql);
			statement.close();
			con.close();
			createMessageBox("Updated Successfully");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void createMessageBox(String msg) {
		JFrame frame = new JFrame("Result");
		JLabel lbl = new JLabel(msg);
		frame.add(lbl);
		frame.setSize(200,200);
		frame.setVisible(true);
	}
public void insertData(player_info player) {
		
		try {
			this.con = DriverManager.getConnection("jdbc:sqlite:yahtzee.db");
			insertStatement.setString(1, player.getName());
			insertStatement.setString(2, Integer.toString(player.getacesTxt()));
			insertStatement.setString(3, Integer.toString(player.gettwosTxt()));
			insertStatement.setString(4, Integer.toString(player.getthreesTxt()));
			insertStatement.setString(5, Integer.toString(player.getfoursTxt()));
			insertStatement.setString(6, Integer.toString(player.getfivesTxt()));
			insertStatement.setString(7, Integer.toString(player.getsixsTxt()));
			insertStatement.setString(8,Integer.toString(player.getthreekTxt()));
			insertStatement.setString(9, Integer.toString(player.getfourkTxt()));
			insertStatement.setString(10,Integer.toString( player.getfullTxt()));
			insertStatement.setString(11, Integer.toString(player.getsmallTxt()));
			insertStatement.setString(12,Integer.toString( player.getlargeTxt()));
			insertStatement.setString(13, Integer.toString(player.getchanceTxt()));		
			insertStatement.setString(14, Integer.toString(player.getyhzTxt()));
			insertStatement.setString(15, Integer.toString(player.getyhzbnsTxt()));
			insertStatement.setString(16, Integer.toString(player.getRollNum()));
			insertStatement.setString(17, Integer.toString(player.getTurnNum()));
			
			insertStatement.execute();
			
			createMessageBox("Inserted Successfully");
			this.con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
public String getData() {
	ResultSet resultSet = null;
	Connection con;
	String allData = null;
	try {
		con = DriverManager.getConnection
			      ("jdbc:sqlite:yahtzee.db");
		
		String sql = "SELECT * FROM Players";
		Statement stmt = con.createStatement();
		resultSet = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int numColumns = rsmd.getColumnCount();
		String rowString = "";
		while(resultSet.next()) {
			for (int i=1;i<=numColumns;i++) {
				Object o = resultSet.getObject(i);
				rowString += o.toString() + "\t";
			}
			rowString += "\n";
			allData += rowString;
			rowString = "";
		}
		con.close();
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return allData;
}
	
	public player_info outputData(String PName) {
		player_info player = new player_info();
		
		Connection con;
		try {
			con = DriverManager.getConnection
				      ("jdbc:sqlite:yahtzee.db");
			
			String sql = "SELECT * FROM Players WHERE PName='" + PName + "'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			
			player.setName(PName);
			player.setacesTxt(Integer.parseInt(rs.getString("Aces")));
			player.settwosTxt(Integer.parseInt(rs.getString(3)));
			player.setthreesTxt(Integer.parseInt(rs.getString(4)));
			player.setfoursTxt(Integer.parseInt(rs.getString(5)));
			player.setfivesTxt(Integer.parseInt(rs.getString(6)));
			player.setsixsTxt(Integer.parseInt(rs.getString(7)));
			player.setthreekTxt(Integer.parseInt(rs.getString(8)));
			player.setfourkTxt(Integer.parseInt(rs.getString(9)));
			player.setfullTxt(Integer.parseInt(rs.getString(10)));
			player.setsmallTxt(Integer.parseInt(rs.getString(11)));
			player.setlargeTxt(Integer.parseInt(rs.getString(12)));
			player.setchanceTxt(Integer.parseInt(rs.getString(13)));		
			player.setyhzTxt(Integer.parseInt(rs.getString(14)));
			player.setyhzBns(Integer.parseInt(rs.getString(15)));
			player.setrollNum(Integer.parseInt(rs.getString(16)));
			player.setturnNum(Integer.parseInt(rs.getString(17)));
			
			con.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return player;
	}
	public void buildConnect() {
		try {
			this.con = DriverManager.getConnection("jdbc:sqlite:yahtzee.db");
			wordsBox.append("Connect to Database.\n");
			if (!checkTable("Players")) {
				String CREATE_TABLE_SQL = "CREATE TABLE Players ("
						+ "PName VARCHAR(255) NOT NULL,"
						+ "Aces INTEGER ,"
						+ "Twos INTEGER, "
						+ "Threes INTEGER, "
						+ "Fours INTEGER, "
						+ "Fives INTEGER, "
						+ "Sixes INTERGER, "
						+ "ThreeOfAKind INTEGER, "
						+ "FourOfAKind INTEGER, "
						+ "FullHouse INTEGER, "
						+ "SmallStraight INTEGER, "
						+ "LargeStraight INTEGER, "
						+ "Chance INTEGER, "
						+ "Yahtzee INTEGER, "
						+ "YahtzeeBonus INTERER, "
						+ "RollCount INTEGER, "
						+ "Round INTEGER, "
						+ "PRIMARY KEY(PName))";
						
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate(CREATE_TABLE_SQL);
				System.out.println("Table created");
				//this.con.close();
			}
			
			String insertSQL = "Insert Into Players (PName,Aces,Twos,Threes,Fours,Fives,Sixes,"
					+ "ThreeOfAKind,FourOfAKind,FullHouse,SmallStraight,LargeStraight,"
					+ "Chance,Yahtzee,YahtzeeBonus,RollCount,Round)" + "Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			try {
				insertStatement = con.prepareStatement(insertSQL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean checkTable(String tableName) {
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:yahtzee.db");
			ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);
			if (rs.next()) {
				con.close();
				return true;
			}
			else {
				con.close();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rootPaneCheckingEnabled;
		
	}



	public static void main(String[] main) {
		SaveServer saveServer = new SaveServer();
	}
}
