package nl.vCore.Data.MSSQL.Homes;

import nl.vCore.Config.ConfigHandler;
import nl.vCore.Dto.Home;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import nl.vCore.Utils.DtoShaper;
import nl.vCore.Utils.MessageUtils;
import org.bukkit.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MSSQLHomesHandler {
    private Main main = Main.getInstance();
    MessageUtils msgUtils = new MessageUtils(main);
    private ConfigHandler cfg;
    private final String JDBC_URL = "jdbc:sqlserver://" + cfg.getConfig().getString("MSSQL.ip") + "\\" + cfg.getConfig().getString("MSSQL.database_name") + ":" + cfg.getConfig().getString("MSSQL.port");
    private final String USERNAME = cfg.getConfig().getString("MSSQL.username");
    private final String PASSWORD = cfg.getConfig().getString("MSSQL.password");

    public MSSQLHomesHandler(Main _main) {
        main = _main;
    }

    public void createHomesTableIfNotExists() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String tableName = "homes";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT PRIMARY KEY IDENTITY(1,1)," +
                "owner VARCHAR(50)," +
                "name VARCHAR(50)," +
                "x INT," +
                "y INT," +
                "z INT," +
                "world VARCHAR(50)" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            msgUtils.log("Table '" + tableName + "' created (if not already exists)...");
        }
    }

    public void create(Home home) {
        String insertQuery = "INSERT INTO homes(id, owner,name,x,y,z,world) VALUES (?,?,?,?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(0, home.getOwner().getId().toString());
            insertStatement.setString(1, home.getName());
            insertStatement.setInt(2, home.getLocation().getBlockX());
            insertStatement.setInt(3, home.getLocation().getBlockY());
            insertStatement.setInt(4, home.getLocation().getBlockZ());
            insertStatement.setString(5, home.getLocation().getWorld().getName());
            insertStatement.executeUpdate();
            msgUtils.log("Home record inserted successfully");
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }

    }

    public Home read(Home home) {
        String selectQuery = "SELECT * FROM users WHERE id = ? AND name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setInt(0, home.getId());
            selectStatement.setString(1, home.getName());
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    Location location = new Location(main.getServer().getWorld(resultSet.getString("world")), resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z"));
                    Home h = new Home();
                    h.setId(resultSet.getInt("id"));
                    h.setName(resultSet.getString("name"));
                    h.setLocation(location);
                    h.setOwner(DtoShaper.userShaper(main.getServer().getPlayer(UUID.fromString(resultSet.getString("owner")))));
                    return h;
                }

            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        msgUtils.warn("Home not found...");
        return null;
    }

    public void update(Home h) {
        String updateQuery = "UPDATE homes SET x = ?, y = ?, z = ?, world =? WHERE owner = ? AND name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setInt(0, h.getLocation().getBlockX());
            updateStatement.setInt(1, h.getLocation().getBlockX());
            updateStatement.setInt(2, h.getLocation().getBlockX());
            updateStatement.setString(3, h.getOwner().getId().toString());
            updateStatement.setString(4, h.getName());
            updateStatement.executeUpdate();
            msgUtils.log("Home record updated successfully!");
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void delete(Home h) {
        String deleteQuery = "DELETE FROM homes WHERE owner = ? AND name = ? AND id = ?";
        try(Connection connection = DriverManager.getConnection(JDBC_URL,USERNAME,PASSWORD);
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)){
            deleteStatement.setString(0, h.getOwner().getId().toString());
            deleteStatement.setString(1, h.getName());
            deleteStatement.setInt(2, h.getId());
            deleteStatement.executeUpdate();
        }catch(SQLException e){
            msgUtils.severe(e.getMessage());
        }
    }

    public boolean checkIfHomeExist(Home h){
        String query = "SELECT COUNT(*) FROM homes WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(0, h.getName());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return false;
    }
    public List<Home> getHomesFromUserUuid(String userUuid) {
        List<Home> homes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM homes WHERE owner = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(0, userUuid);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Home h = new Home();
                        h.setId(rs.getInt("id"));
                        h.setName(rs.getString("name"));
                        h.setOwner(DtoShaper.userShaper(main.getServer().getPlayer(UUID.fromString("owner"))));
                        Location loc = new Location(main.getServer().getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                        h.setLocation(loc);
                        homes.add(h);
                    }
                }
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return homes;
    }
    public List<Home> getAll() {
        List<Home> homes = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement selectAllStatement = connection.prepareStatement(selectAllQuery);
             ResultSet resultSet = selectAllStatement.executeQuery()) {
            while (resultSet.next()) {
                Home h = new Home();
                h.setId(resultSet.getInt("id"));
                h.setName(resultSet.getString("name"));
                h.setOwner(DtoShaper.userShaper(main.getServer().getPlayer(UUID.fromString(resultSet.getString("owner")))));
                Location loc = new Location(main.getServer().getWorld(resultSet.getString("world")), resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z"));
                h.setLocation(loc);
                homes.add(h);

            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return homes;
    }
}

