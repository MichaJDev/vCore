package nl.vCore.Dto;

public class Ban {

    private int Id;
    private User BannedUser;
    private User Banner;
    private String reason;
    private String Date;

    public User getBannedUser() {
        return BannedUser;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setBannedUser(User bannedUser) {
        BannedUser = bannedUser;
    }

    public User getBanner() {
        return Banner;
    }

    public void setBanner(User banner) {
        Banner = banner;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
