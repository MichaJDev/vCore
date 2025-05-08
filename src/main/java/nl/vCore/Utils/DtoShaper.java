package nl.vCore.Utils;

import nl.vCore.Dto.Ban;
import nl.vCore.Dto.User;
import nl.vCore.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utility class for creating DTO objects from Bukkit entities
 * 
 * @version 1.1.0
 */
public class DtoShaper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final MessageUtils msgUtils = new MessageUtils(Main.getInstance());

    /**
     * Creates a User object from a Player
     * 
     * @param p The online player
     * @return User object with player data or null if player is null
     */
    public static User userShaper(Player p) {
        if (p == null) {
            return null;
        }
        
        try {
            User u = new User();
            u.setId(p.getUniqueId());
            u.setName(p.getName());
            
            // Use player.getDisplayName() which is the modern way to get the display name
            u.setDisplayName(p.getDisplayName());
            
            // Safer IP address handling
            String ipAddress = "unknown";
            InetSocketAddress socketAddress = p.getAddress();
            if (socketAddress != null && socketAddress.getAddress() != null) {
                ipAddress = socketAddress.getAddress().getHostAddress();
            }
            u.setIP(ipAddress);
            
            // Set default values
            u.setBanned(p.isBanned());
            u.setWarnTimes(0);
            
            return u;
        } catch (Exception e) {
            msgUtils.severe("Error creating User from Player: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a User object from an OfflinePlayer
     * This is safer than casting OfflinePlayer to Player
     * 
     * @param p The offline player
     * @return User object with basic player data or null if player is null
     */
    public static User userShaper(OfflinePlayer p) {
        if (p == null) {
            return null;
        }
        
        try {
            // If player is online, use the online player method
            if (p.isOnline() && p.getPlayer() != null) {
                return userShaper(p.getPlayer());
            }
            
            // Otherwise create a basic user with available offline data
            User u = new User();
            u.setId(p.getUniqueId());
            
            // Get name safely - OfflinePlayer.getName() might return null
            String playerName = p.getName();
            if (playerName == null) {
                playerName = "Unknown-" + p.getUniqueId().toString().substring(0, 8);
            }
            
            u.setName(playerName);
            u.setDisplayName(playerName);
            u.setIP("offline");
            u.setBanned(p.isBanned());
            u.setWarnTimes(0);
            
            return u;
        } catch (Exception e) {
            msgUtils.severe("Error creating User from OfflinePlayer: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a User object from a UUID string
     * 
     * @param uuid The player's UUID as string
     * @return User object with basic data or null if invalid UUID
     */
    public static User userShaperFromUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return null;
        }
        
        try {
            UUID parsedUUID = UUID.fromString(uuid);
            OfflinePlayer player = Bukkit.getOfflinePlayer(parsedUUID);
            return userShaper(player);
        } catch (IllegalArgumentException e) {
            msgUtils.severe("Invalid UUID format: " + uuid);
            return null;
        } catch (Exception e) {
            msgUtils.severe("Error creating User from UUID: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a Ban object from online players
     * 
     * @param banned The banned player
     * @param reason The ban reason
     * @param banner The player who issued the ban
     * @return Ban object or null if either player is null
     */
    public static Ban banShaper(Player banned, String reason, Player banner) {
        if (banned == null || banner == null) {
            return null;
        }
        
        try {
            Ban ban = new Ban();
            ban.setBannedUser(userShaper(banned));
            ban.setBanner(userShaper(banner));
            ban.setReason(sanitizeReason(reason));
            ban.setDate(getCurrentTimestamp());
            
            return ban;
        } catch (Exception e) {
            msgUtils.severe("Error creating Ban object: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a Ban object using OfflinePlayer objects
     * 
     * @param banned The banned player (can be offline)
     * @param reason The ban reason
     * @param banner The player who issued the ban (can be offline)
     * @return Ban object or null if either player is null
     */
    public static Ban banShaper(OfflinePlayer banned, String reason, OfflinePlayer banner) {
        if (banned == null || banner == null) {
            return null;
        }
        
        try {
            Ban ban = new Ban();
            ban.setBannedUser(userShaper(banned));
            ban.setBanner(userShaper(banner));
            ban.setReason(sanitizeReason(reason));
            ban.setDate(getCurrentTimestamp());
            
            return ban;
        } catch (Exception e) {
            msgUtils.severe("Error creating Ban object from OfflinePlayers: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a Ban object using UUID strings
     * 
     * @param bannedUuid UUID of the banned player
     * @param reason The ban reason
     * @param bannerUuid UUID of the player who issued the ban
     * @return Ban object or null if either UUID is invalid
     */
    public static Ban banShaperFromUUIDs(String bannedUuid, String reason, String bannerUuid) {
        try {
            User bannedUser = userShaperFromUUID(bannedUuid);
            User bannerUser = userShaperFromUUID(bannerUuid);
            
            if (bannedUser == null || bannerUser == null) {
                return null;
            }
            
            Ban ban = new Ban();
            ban.setBannedUser(bannedUser);
            ban.setBanner(bannerUser);
            ban.setReason(sanitizeReason(reason));
            ban.setDate(getCurrentTimestamp());
            
            return ban;
        } catch (Exception e) {
            msgUtils.severe("Error creating Ban from UUIDs: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Sanitizes and normalizes the ban reason
     * 
     * @param reason The raw reason string
     * @return Sanitized reason string
     */
    private static String sanitizeReason(String reason) {
        return reason != null && !reason.trim().isEmpty() ? reason.trim() : "No reason provided";
    }
    
    /**
     * Gets a standardized timestamp for the current time
     * 
     * @return Formatted timestamp string
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DATE_FORMATTER);
    }
}
