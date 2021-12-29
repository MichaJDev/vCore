package vCore.Sql.interfaces;

import java.sql.Connection;
import java.util.UUID;

import vCore.Dto.Types.Users.UserEditType;
import vCore.Dto.User.Interface.IUser;

public interface IDataBase {

	public void connect();

	public void disconnect();

	public boolean isConnected();

	public Connection getConnection();

	public void createFirstSetup();

	public Boolean exist(String tableName);

	public void createUser(IUser user);

	public void editUser(UserEditType type, IUser user);

	public void deleteUser(IUser user);

	public IUser getUser(UUID uuid);

	public Boolean userExist(String uuid);
}
