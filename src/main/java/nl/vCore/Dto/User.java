package nl.vCore.Dto;
import java.util.UUID;
public class User {
    private UUID id;
    private String name;
    private String displayName;
    private String IP;
    private Boolean banned;
    private int warnTimes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public int getWarnTimes() {
        return warnTimes;
    }

    public void setWarnTimes(int warnTimes) {
        this.warnTimes = warnTimes;
    }
}
