package vCore.Data.SQL.Interface;

import java.util.UUID;

import vCore.Dto.Types.Users.UserEditType;
import vCore.Dto.User.Interface.IUser;

public interface IDatabase {

	public void setConnectionInfo(String host, String port, String database, String username, String password);

	public IUser getUser(UUID uuid);

	public IUser getUserByName(String name);

	public void createNewUser(IUser user);

	public void editUser(UserEditType type, IUser user);

	public void deleteUser(IUser user);

	public void setupDB();

//	public boolean tableExists(String tableName);

	public boolean userExists(IUser user);
	
	public void disableConnection();

	boolean tableExists(String tableName);

}
