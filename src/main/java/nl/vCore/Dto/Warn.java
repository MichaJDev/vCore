package nl.vCore.Dto;

import java.util.UUID;

public class Warn {

    private UUID id;
    private User warner;
    private User warned;
    private String reason;

    private String date;

    public Warn(){
        id = UUID.randomUUID();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getWarner() {
        return warner;
    }

    public void setWarner(User warner) {
        this.warner = warner;
    }

    public User getWarned() {
        return warned;
    }

    public void setWarned(User warned) {
        this.warned = warned;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
