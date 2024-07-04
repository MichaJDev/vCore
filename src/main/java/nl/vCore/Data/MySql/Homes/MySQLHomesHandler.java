package nl.vCore.Data.MySql.Homes;

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
import java.util.Objects;
import java.util.UUID;

public class MySQLHomesHandler {

    private final ConfigHandler configHandler;
    private final String url;
    private final String user;
    private final String password;
    private final MessageUtils msgUtils;

    public MySQLHomesHandler(Main main) {
        this.configHandler = new ConfigHandler(main);
        this.url = buildJdbcUrl();
        this.user = configHandler.getConfig().getString("MySQL.username");
        this.password = configHandler.getConfig().getString("MySQL.password");
        this.msgUtils = new MessageUtils(main);
    }

    private String buildJdbcUrl() {
        String ip = configHandler.getConfig().getString("MySQL.ip");
        int port = configHandler.getConfig().getInt("MySQL.port");
        String dbName = configHandler.getConfig().getString("MySQL.database_name");
        return "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
    }

    public void createHomesTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS homes (" +
                "id int PRIMARY KEY AUTO_INCREMENT, " +
                "owner VARCHAR(255), " +
                "name VARCHAR(255), " +
                "x INT, " +
                "y INT, " +
                "z INT," +
                "world VARCHAR(255));";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void create(Home home) {
        String sql = "INSERT INTO homes (owner, name, x, y, z, world) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(0, home.getOwner().getId().toString());
            stmt.setString(1, home.getName());
            stmt.setInt(2, home.getLocation().getBlockX());
            stmt.setInt(3, home.getLocation().getBlockY());
            stmt.setInt(4, home.getLocation().getBlockZ());
            stmt.setString(5, home.getLocation().getWorld().getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public Home read(UUID id) {

        String sql = "SELECT * FROM homes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(0, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                Location loc = new Location(Main.getInstance().getServer().getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                Home h = new Home();
                h.setId(UUID.fromString(rs.getString("id")));
                h.setName(rs.getString("name"));
                h.setLocation(loc);
                h.setOwner(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(UUID.fromString(rs.getString("owner"))))));
                return h;
            }
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
        return null;
    }

    public void update(Home home) {
        String sql = "UPDATE homes SET x = ?, y = ?, z = ?, world = ? WHERE id = ? AND name = ?;";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(0, home.getLocation().getBlockX());
            stmt.setInt(1, home.getLocation().getBlockY());
            stmt.setInt(2, home.getLocation().getBlockZ());
            stmt.setString(3, home.getLocation().getWorld().getName());
            stmt.setString(4, home.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public void delete(Home home) {
        String sql = "DELETE FROM homes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(0, home.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            msgUtils.severe(e.getMessage());
        }
    }

    public boolean checkIfHomeExist(Home h) {
        String query = "SELECT COUNT(*) FROM homes WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
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

    public List<Home> getListFromUser(User u) {
        List<Home> homes = new ArrayList<>();
        String sql = "SELECT * FROM homes WHERE owner = ?;";
        try(Connection conn = DriverManager.getConnection(url,user,password);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            stmt.setString(0, u.getId().toString());
            try(ResultSet rs = stmt.executeQuery()){
                Home h = new Home();
                h.setId(UUID.fromString(rs.getString("id")));
                h.setOwner(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(rs.getString("owner")))));
                h.setName(rs.getString("name"));
                Location loc = new Location(Main.getInstance().getServer().getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                h.setLocation(loc);
                homes.add(h);
            }
        }catch (SQLException e){
            msgUtils.severe(e.getMessage());
        }
        return homes;
    }

    public List<Home> getAll() {
        List<Home> homes = new ArrayList<>();
        String sql = "SELECT * FROM home;";
        try(Connection conn = DriverManager.getConnection(url,user,password);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            try(ResultSet rs = stmt.executeQuery()){
                Home h = new Home();
                h.setId(UUID.fromString(rs.getString("id")));
                h.setOwner(DtoShaper.userShaper(Objects.requireNonNull(Main.getInstance().getServer().getPlayer(rs.getString("owner")))));
                h.setName(rs.getString("name"));
                Location loc = new Location(Main.getInstance().getServer().getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                h.setLocation(loc);
                homes.add(h);
            }
        }catch (SQLException e){
            msgUtils.severe(e.getMessage());
        }
        return homes;
    }
}

