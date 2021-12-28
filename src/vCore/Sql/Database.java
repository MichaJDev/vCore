package vCore.Sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import vCore.Main;
import vCore.Dto.Types.Users.UserEditType;
import vCore.Dto.User.User;
import vCore.Dto.User.Interface.IUser;

public class Database {

	static Main main = Main.getInstance();

	public Database(Main _main) {
		main = _main;
	}

	public static String host = main.getConfig().getString("SQL.host");
	public static String port = main.getConfig().getString("SQL.port");
	public static String database = main.getConfig().getString("SQL.database");
	public static String username = main.getConfig().getString("SQL.username");
	public static String password = main.getConfig().getString("SQL.password");
	public static Connection con;
	static ConsoleCommandSender console = Bukkit.getConsoleSender();

	public static void connect() {

		if (!isConnected()) {
			try {
				con = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database,
						username, password);
				console.sendMessage("MYSQL: connection opened.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// disconnect
	public static void disconnect() {
		if (isConnected()) {
			try {
				con.close();
				console.sendMessage("MYSQL: connection closed.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// isConnected
	public static boolean isConnected() {
		return (con == null ? false : true);
	}

	// getConnection
	public static Connection getConnection() {
		return con;
	}

	public static void createFirstSetup() {
		createDatabase();
		createUserTable();
	}

	public static Boolean exist(String tableName) {
		Connection c = getConnection();
		Boolean exist = false;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = (PreparedStatement) c.prepareStatement(
					"SELECT count(*) " + "FROM information_schema.tables " + "WHERE table_name = ?" + "LIMIT 1;");
			preparedStatement.setString(1, tableName);

			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			exist = resultSet.getInt(1) != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return exist;

	}

	private static void createDatabase() {
		Connection c = getConnection();
		String query = "CREATE DATABASE IF NOT EXISTS VCOREDB";
		try (Statement stmt = c.createStatement()) {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			if (e.getErrorCode() == 1007) {
				main.getLogger().info(e.getMessage());
			} else {
				e.printStackTrace();
			}
		}
	}

	private static void createUserTable() {
		Connection c = getConnection();
		try {
			Statement stmt = c.createStatement();
			String query = "CREATE TABLE `users` (" + "	`id` INT NOT NULL AUTO_INCREMENT,"
					+ "	`uuid` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,"
					+ "	`name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,"
					+ "	`ip` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,"
					+ "	`banned` TINYINT(1) NOT NULL DEFAULT '0',"
					+ "	`banner` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',"
					+ "	`banreason` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',"
					+ "	`warns` INT(255) DEFAULT '0'," + "	PRIMARY KEY (`id`)" + ") ENGINE=InnoDB;";
			stmt.executeQuery(query);
			stmt.close();
			c.close();
			main.getLogger().info("MYSQL: users table created.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createUser(IUser user) {
		String query = "INSERT INTO users (uuid, name, ip,banned,banner,banreason,warns " + "VALUES ('"
				+ user.getUUID().toString() + "','" + user.getName() + "','" + user.getBanned() + "','"
				+ user.getBanReason() + "','" + user.getBanner() + "','" + user.getWarns() + "')";
		String log = "MYSQL: Userrow created for: " + user.getName() + " UUID: " + user.getUUID();
		createStatement(query, log);

	}

	public static void editUser(UserEditType type, IUser user) {
		String query, log;
		switch (type) {
		case BANNED:
			query = "UPDATE users SET banned='" + user.getBanned() + "'";
			log = "MYSQL: Updated 'banned' value from " + user.getName() + " in database to: " + user.getBanned();
			createStatement(query, log);
			break;
		case BANNER:
			query = "UPDATE users SET banner='" + user.getBanner() + "'";
			log = "MYSQL: Updated 'banner' value from " + user.getName() + " in database to: " + user.getBanned();
			createStatement(query, log);
			break;
		case BANREASON:
			query = "UPDATE users SET banreason='" + user.getBanReason() + "'";
			log = "MYSQL: Updated 'banreason' value from " + user.getName() + " in database to: " + user.getBanned();
			createStatement(query, log);
			break;
		case IP:
			query = "UPDATE users SET ip='" + user.getIP() + "'";
			log = "MYSQL: Updated 'ip' value from " + user.getName() + " in database to: " + user.getIP();
			createStatement(query, log);
			break;
		case NAME:
			query = "UPDATE users SET name='" + user.getName() + "'";
			log = "MYSQL: Updated 'name' value from " + user.getName() + " in database to: " + user.getName();
			createStatement(query, log);
			break;
		case WARNS:
			query = "UPDATE users SET warns='" + user.getWarns() + "'";
			log = "MYSQL: Updated 'warns' value from " + user.getName() + " in database to: " + user.getWarns();
			createStatement(query, log);
			break;
		default:
			break;
		}
	}

	public static void deleteUser(IUser user) {
		String query, log;
		query = "DELETE FROM users WHERE uuid='" + user.getUUID().toString() + "'";
		log = "User deleted from table: " + user.getName() + " With uuid: " + user.getUUID().toString();
		createStatement(query, log);
	}

	public static IUser getUser(UUID uuid) {
		Connection c = getConnection();
		IUser user = new User();
		String query;
		query = "SELECT * FROM users WHERE uuid=" + uuid.toString();
		try (Statement stmt = c.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				user.setUUID(rs.getString(1));
				user.setName(rs.getString(2));
				user.setIP(rs.getString(3));
				user.setBanned(rs.getBoolean(4));
				user.setBanner(rs.getString(5));
				user.setBanReason(rs.getString(6));
				user.setWarns(rs.getInt(7));
			}
			main.getLogger().info("MYSQL: retrieved user for uuid: '" + uuid.toString() + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	private static void createStatement(String query, String log) {
		Connection c = getConnection();
		try {
			Statement stmt = (Statement) c.createStatement();
			stmt.executeQuery(query);
			stmt.close();
			c.close();
			main.getLogger().info(log);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Boolean userExist(String uuid) {
		Connection c = getConnection();
		Boolean exist = false;
		String query = "SELECT * FROM users WHERE uuid='" + uuid + "'";
		try (Statement stmt = c.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				exist = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return exist;
	}
	
}
