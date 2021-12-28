package vCore.Dto.User.Interface;

import java.util.UUID;

public interface IUser {

	public UUID getUUID();

	public void setUUID(String _uuid);

	public String getName();

	public void setName(String _name);

	public String getIP();

	public void setIP(String _ip);

	public Boolean getBanned();

	public void setBanned(Boolean _banned);

	public String getBanner();

	public void setBanner(String _name);

	public String getBanReason();

	public void setBanReason(String _reason);

	public int getWarns();

	public void setWarns(int _warns);

}
