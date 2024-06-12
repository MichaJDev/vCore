package nl.vCore.Dto;

public class Warn {

    private int id;
    private User warner;
    private User warned;
    private String reason;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
