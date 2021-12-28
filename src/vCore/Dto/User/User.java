package vCore.Dto.User;

import java.util.UUID;

import vCore.Dto.User.Interface.IUser;

public class User implements IUser {

	private UUID uuid;
	private String name;
	private String ip;
	private Boolean banned;
	private String banner;
	private String banReason;
	private int warns;

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void setUUID(String _uuid) {
		uuid = UUID.fromString(_uuid);

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String _name) {
		name = _name;
	}

	@Override
	public String getIP() {
		return ip;
	}

	@Override
	public void setIP(String _ip) {
		ip = _ip;
	}

	@Override
	public Boolean getBanned() {
		return banned;
	}

	@Override
	public void setBanned(Boolean _banned) {
		banned = _banned;
	}

	@Override
	public String getBanner() {
		return banner;
	}

	@Override
	public void setBanner(String _name) {
		banner = _name;

	}

	@Override
	public String getBanReason() {
		return banReason;
	}

	@Override
	public void setBanReason(String _reason) {
		banReason = _reason;
	}

	@Override
	public int getWarns() {
		return warns;
	}

	@Override
	public void setWarns(int _warns) {
		warns = _warns;

	}

}
