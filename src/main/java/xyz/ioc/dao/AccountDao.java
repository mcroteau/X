package xyz.ioc.dao;

import java.util.List;
import java.util.Set;

import xyz.ioc.model.Account;

public interface AccountDao {

	public long id();

	public long count();
	
	public Account get(long id);
	
	public Account findByUsername(String username);
	
	public List<Account> findAll();
	
	public List<Account> findAllOffset(int max, int offset);

	public Account save(Account account);

	public Account saveAdministrator(Account account);

	public void update(Account account);

	public void updatePassword(Account account);

	public Account findByUsernameAndUuid(String username, String uuid);
	
	public void delete(long id);
	
	public String getAccountPassword(String username);

	public void saveAccountRole(long accountId, long roleId);
	
	public void saveAccountPermission(long accountId, String permission);
	
	public void deleteAccountRoles(long accountId);
	
	public void deleteAccountPermissions(long accountId);

	public Set<String> getAccountRoles(long id);
	
	public Set<String> getAccountRoles(String username);

	public Set<String> getAccountPermissions(long id);

	public Set<String> getAccountPermissions(String username);

	public long countQuery(String query);

	public List<Account> search(String query, int offset);

}