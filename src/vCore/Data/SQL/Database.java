package vCore.Data.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import vCore.Data.SQL.Interface.IDatabase;
import vCore.Dto.Types.Users.UserEditType;
import vCore.Dto.User.User;
import vCore.Dto.User.Interface.IUser;

public class Database implements IDatabase {

	private String host, port, database, username, password;
	private ConsoleCommandSender c = Bukkit.getConsoleSender();

	@Override
	public void setConnectionInfo(String _host, String _port, String _database, String _username, String _password) {
		host = _host;
		port = _port;
		database = _database;
		username = _username;
		password = _password;
	}
	/**
	 * Returns a User by looking up by UUID
	 * 
	 * @param uuid UUID of User
	 * @return user IUser Object filled with db stored data.
	 */
	@Override
	public IUser getUser(UUID uuid) {
		Connection con = getConnection();
		String query = "SELECT * FROM USERS WHERE uuid=?";
		IUser user = new User();
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			c.sendMessage("[vCore] MYSQL: getting user with UUID: " + uuid);
			stmt.setString(1, user.getUUID().toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				user.setUUID(rs.getString(0));
				user.setName(rs.getString(1));
				user.setIP(rs.getString(2));
				user.setBanned(rs.getBoolean(3));
				user.setBanner(rs.getString(4));
				user.setBanReason(rs.getString(5));
				user.setWarns(rs.getInt(6));
				rs.close();
				stmt.close();
				con.close();
			}

		} catch (SQLException e) {
		}
		return user;
	}
	/**
	 * Returns a User when looking up by name
	 * 
	 * @param name Name of User
	 * @return user IUser Object filled with db stored data.
	 */
	@Override
	public IUser getUserByName(String name) {
		c.sendMessage("[vCore] MYSQL: getting user by name: " + name);
		Connection con = getConnection();
		String query = "SELECT * FROM USERS WHERE name = " + name;
		IUser user = new User();
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				user.setUUID(rs.getString(0).toString());
				user.setName(rs.getString(1));
				user.setIP(rs.getString(2));
				user.setBanned(rs.getBoolean(3));
				user.setBanner(rs.getString(4));
				user.setBanReason(rs.getString(5));
				user.setWarns(rs.getInt(6));
				stmt.close();
				con.close();
			}

		} catch (SQLException e) {
		}
		return user;
	}
	/**
	 * Creates new user in database
	 * 
	 * @param user IUser object filled with data
	 * 
	 */
	@Override
	public void createNewUser(IUser user) {
		if (getConnection() != null) {
			c.sendMessage("Creating user for: " + user.getName());
			Connection con = getConnection();
			String query = "INSERT INTO users (uuid, name, ip, banned, warns) VALUES (?,?,?,?,?)";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(1, user.getUUID().toString());
				stmt.setString(2, user.getName());
				stmt.setString(3, user.getIP());
				stmt.setBoolean(4, user.getBanned());
				stmt.setInt(5, user.getWarns());
				stmt.execute();
				c.sendMessage("[vCore] MYSQL: User created for: " + user.getName());
				stmt.close();
				con.close();
			} catch (SQLException e) {
				c.sendMessage("[vCore] MYSQL: Unable to create user Error: " + e.getMessage());
			}
		}

	}
	/**
	 * Returns a boolean if a parsed user exists or not
	 * 
	 * @param type UserEditType.BANNED/BANNER/BANREASON/IP/NAME/WARNS
	 * @param user IUSer Object filled with data
	 * @return boolean returns true or false if the user does exist in the database
	 */
	@Override
	public void editUser(UserEditType type, IUser user) {
		switch (type) {
		case BANNED:
			if (getConnection() != null) {
				Connection con = getConnection();
				String query = "UPDATE users SET banned=?  WHERE uuid=?";
				try (PreparedStatement stmt = con.prepareStatement(query)) {
					stmt.setBoolean(1, user.getBanned());
					stmt.setString(2, user.getUUID().toString());
					stmt.execute();
					c.sendMessage("[vCore] MYSQL: User edited for: " + user.getName() + " Banned: " + user.getBanned());
					stmt.close();
					con.close();

				} catch (SQLException e) {
					c.sendMessage("[vCore] MYSQL: Unable to create user Error: " + e.getMessage());
				}
			}
			break;
		case BANNER:
			if (getConnection() != null) {
				Connection con = getConnection();
				String query = "UPDATE users SET banner=? WHERE uuid=?";
				try (PreparedStatement stmt = con.prepareStatement(query)) {
					stmt.setString(1, user.getBanner());
					stmt.setString(2, user.getUUID().toString());
					stmt.execute();
					c.sendMessage("[vCore] MYSQL: User edited for: " + user.getName() + " Banner: " + user.getBanned());
					stmt.close();
					con.close();

				} catch (SQLException e) {
					c.sendMessage("[vCore] MYSQL: Unable to create user Error: " + e.getMessage());
				}
			}
			break;
		case BANREASON:
			if (getConnection() != null) {
				Connection con = getConnection();
				String query = "UPDATE users SET banreason=? WHERE uuid=? ";
				try (PreparedStatement stmt = con.prepareStatement(query)) {
					stmt.setString(1, user.getBanReason());
					stmt.setString(2, user.getUUID().toString());
					stmt.execute();
					c.sendMessage("[vCore] MYSQL: User edited for: " + user.getBanReason() + " BanReason: "
							+ user.getBanned());
					stmt.close();
					con.close();

				} catch (SQLException e) {
					c.sendMessage("[vCore] MYSQL: Unable to create user Error: " + e.getMessage());

				}
			}
			break;
		case IP:
			if (getConnection() != null) {
				Connection con = getConnection();
				String query = "UPDATE users SET ip=? WHERE uuid=?";
				;
				try (PreparedStatement stmt = con.prepareStatement(query)) {
					stmt.setString(1, user.getIP());
					stmt.setString(2, user.getUUID().toString());
					stmt.execute();
					c.sendMessage("[vCore] MYSQL: User edited for: " + user.getIP() + " IP: " + user.getBanned());
					stmt.close();
					con.close();
				} catch (SQLException e) {
					c.sendMessage("[vCore] MYSQL: Unable to create user Error: " + e.getMessage());
				}
			}
			break;
		case NAME:
			if (getConnection() != null) {
				Connection con = getConnection();
				String query = "UPDATE users SET name=? WHERE uuid=?";
				;
				try (PreparedStatement stmt = con.prepareStatement(query)) {
					stmt.setString(1, user.getName());
					stmt.setString(2, user.getUUID().toString());
					stmt.execute();
					c.sendMessage("[vCore] MYSQL: User edited for: " + user.getName() + " Name: " + user.getName());
					stmt.close();
					con.close();

				} catch (SQLException e) {
					c.sendMessage("[vCore] MYSQL: Unable to create user Error: " + e.getMessage());
				}
			}
			break;
		case WARNS:
			if (getConnection() != null) {
				Connection con = getConnection();
				String query = "UPDATE users SET warns=? WHERE uuid=?";
				try (PreparedStatement stmt = con.prepareStatement(query)) {
					stmt.setInt(1, user.getWarns());
					stmt.setString(2, user.getUUID().toString());
					stmt.execute();
					c.sendMessage("[vCore] MYSQL: User edited for: " + user.getName() + " Warns: " + user.getWarns());
					stmt.close();
					con.close();

				} catch (SQLException e) {
					c.sendMessage("[vCore] MYSQL: Unable to edit user Error: " + e.getMessage());
				}
			}
			break;
		}

	}
	/**
	 * Deletes User in database
	 * @param user IUser object filled with data
	 */
	@Override
	public void deleteUser(IUser user) {
		if (getConnection() != null) {
			Connection con = getConnection();
			String query = "DELETE FROM USERS IF EXISTS WHERE uuid=?";
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setString(2, user.getUUID().toString());
				stmt.execute();
				c.sendMessage("[vCore] MYSQL: User deleted for: " + user.getName() + " Warns: " + user.getWarns());
				stmt.close();
				con.close();
			} catch (SQLException e) {
				c.sendMessage("[vCore] MYSQL: Unable to delete user Error: " + e.getMessage());
			}
		}

	}
	/**
	 * [DONOTUSE]: Sets up full database environment: DATABASE,USERTABLE 
	 */
	@Override
	public void setupDB() {
		createDatabase();
		createTable();

	}

	private void createDatabase() {
		Connection con = getConnection();
		String query = "CREATE DATABASE IF NOT EXISTS " + database;
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.execute(query);
			c.sendMessage("[vCore] MYSQL: Creating Database if doesnt exist: " + database);
			stmt.close();
			con.close();

		} catch (SQLException e) {
			if (databaseExists()) {
				c.sendMessage("[vCore] MYSQL: Database found... skipping");
			} else {

				if (e.getErrorCode() == 1007) {
					c.sendMessage(e.getMessage());
				} else {
					c.sendMessage(
							"[vCore] MYSQL: Unable to created database: " + database + "Error: " + e.getMessage());

				}
			}
		}
	}

	private boolean databaseExists() {
		boolean exists = false;
		Connection con = getConnection();
		String query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + database + "'";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				exists = true;
			}
			rs.close();
			con.close();
		} catch (SQLException e) {
		}
		return exists;
	}

	private void createTable() {
		Connection con = getConnection();
		String query = "CREATE TABLE IF NOT EXISTS `Users` (" + "	`id` INT NOT NULL AUTO_INCREMENT,"
				+ "	`uuid` CHAR(36) NOT NULL," + "	`name` VARCHAR(255) NOT NULL," + "	`ip` VARCHAR(255) NOT NULL,"
				+ "	`banned` TINYINT DEFAULT '0'," + "	`banner` VARCHAR(255)," + "	`banreason` TEXT,"
				+ "	`warns` INT NOT NULL DEFAULT '0'," + "	PRIMARY KEY (`id`)" + ");";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.execute();
			c.sendMessage("[vCore] MYSQL: Creating user table if not exist");
			stmt.close();
			con.close();

		} catch (SQLException e) {
			c.sendMessage("[vCore] MYSQL: Unable to create Users table Error:" + e.getMessage());
		}
	}

//	private List<String> buildColumnList(HashMap<String, String> column) {
//		List<String> list = new ArrayList<String>();
//		for (String key : column.keySet()) {
//			list.add(key);
//		}
//		return list;
//	}
//
//	private String buildColumnString(HashMap<String, String> column) {
//		StringBuilder sb = new StringBuilder();
//		Set<String> keySet = column.keySet();
//		for (String key : keySet) {
//			sb.append("`" + key + "`" + column.get(key) + ", ");
//		}
//		return sb.toString();
//	}
	/**
	 * Returns a boolean if a parsed table exists or not
	 * 
	 * @param tableName Table name to search for
	 * @return boolean returns true or false if the Table does exist in the database
	 */
	@Override
	public boolean tableExists(String tableName) {
		c.sendMessage("[vCore] MYSQL: checking if " + tableName + " exists");
		String query = "SELECT count(*) FROM information_schema.TABLES WHERE (TABLE_SCHEMA = '" + database
				+ "') AND (TABLE_NAME = '" + tableName + "')";
		boolean exists = false;
		if (getConnection() != null) {
			Connection con = getConnection();
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				ResultSet rs = stmt.executeQuery();
				exists = rs.getInt(1) != 0;
				stmt.close();
				con.close();
			} catch (SQLException e) {
			}

		}
		return exists;
	}

	/**
	 * Returns a boolean if a parsed user exists or not
	 * 
	 * @param user IUser object filled with data
	 * @return boolean returns true or false if the user does exist in the database
	 */
	@Override
	public boolean userExists(IUser user) {
		Connection con = getConnection();
		Boolean exist = false;
		String query = "SELECT * FROM users WHERE uuid= ?";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, user.getUUID().toString());
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				exist = true;
			}
		} catch (SQLException e) {
		}

		return exist;
	}

	private Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			c.sendMessage("[vCore] MYSQL: Connecting to Database");
			return con;
		} catch (SQLException e) {
			throw new IllegalStateException("[vCore] MYSQL: Couldnt connect to database", e);
		}
	}

	@Override
	public void disableConnection() {
		Connection con = getConnection();
		c.sendMessage("[vCore] MYSQL: Closing connection");
		try {
			con.close();
			c.sendMessage("[vCore] MYSQL: Connection closed succesfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
