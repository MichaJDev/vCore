package nl.vCore.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import nl.vCore.Main;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {
    private final Main main;
    
    // Pattern for hex color codes like #RRGGBB
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");
    
    // Legacy component serializer that translates & color codes
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = 
            LegacyComponentSerializer.builder()
                    .character('&')
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();
    
    public MessageUtils(Main _main) {
        this.main = _main;
    }

    /**
     * Log an info message to the console
     */
    public void log(String msg) {
        main.getLogger().log(Level.INFO, msg);
    }

    /**
     * Log a warning message to the console
     */
    public void warn(String msg) {
        main.getLogger().log(Level.WARNING, msg);
    }
    
    /**
     * Log a severe error message to the console
     */
    public void severe(String msg) {
        main.getLogger().log(Level.SEVERE, msg);
    }
    
    /**
     * Send a colorized message to a player
     */
    public void msg(Player p, String msg) {
        if (p == null) return;
        
        // Use Adventure API (Paper 1.16.5+)
        p.sendMessage(colorize(msg));
    }

    /**
     * Colorize a string with both legacy & codes and #RRGGBB hex codes
     * 
     * @param message Text with color codes
     * @return Component with colors applied
     */
    public Component colorize(String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }
        
        try {
            // This uses the Adventure API to handle both legacy & color codes and #RRGGBB hex codes
            return LEGACY_SERIALIZER.deserialize(message);
        } catch (Exception e) {
            // Fallback method for older versions
            return legacyColorize(message);
        }
    }
    
    /**
     * Returns the colorized string (for methods that need String instead of Component)
     * 
     * @param message Text with color codes
     * @return String with legacy color codes
     */
    public String colorizeToString(String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }
        
        // Process hex colors first
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {
            String hexCode = matcher.group(1);
            // Convert #RRGGBB to &x&R&R&G&G&B&B format that legacy systems understand
            StringBuilder replacement = new StringBuilder("&x");
            for (char c : hexCode.toCharArray()) {
                replacement.append("&").append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);
        
        // Now process regular color codes
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
    
    /**
     * Legacy fallback for servers without Adventure API
     */
    private Component legacyColorize(String message) {
        // Convert hex colors to their closest named color equivalent
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {
            String hexCode = matcher.group(1);
            // Just remove the hex codes in legacy mode, as they'll be translated in next step
            matcher.appendReplacement(buffer, "&7"); // Default to gray
        }
        matcher.appendTail(buffer);
        
        // Use legacy named colors
        String legacyText = org.bukkit.ChatColor.translateAlternateColorCodes('&', buffer.toString());
        return Component.text(legacyText);
    }
}
