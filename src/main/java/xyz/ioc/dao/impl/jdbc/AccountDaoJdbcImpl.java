package xyz.ioc.dao.impl.jdbc;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import xyz.ioc.common.ApplicationConstants;

import xyz.ioc.dao.AccountDao;
import xyz.ioc.dao.RoleDao;

import xyz.ioc.model.*;


public class AccountDaoJdbcImpl implements AccountDao {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	public long id() {
		String sql = "select max(id) from accounts";
		long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
		return id;
	}


	public long count() {
		String sql = "select count(*) from accounts";
		long count = jdbcTemplate.queryForObject(sql, new Object[] { }, Long.class);
	 	return count; 
	}


	public Account get(long id) {
		String sql = "select * from accounts where id = ?";
		
		Account account = jdbcTemplate.queryForObject(sql, new Object[] { id }, 
				new BeanPropertyRowMapper<Account>(Account.class));
		
		if(account == null) account = new Account();

		return account;
	}
	

	
	public Account findByUsername(String username) {
		Account account = null;
		try{
			String sql = "select * from accounts where username = '" + username + "'";
			account = jdbcTemplate.queryForObject(sql, new Object[] {}, 
				new BeanPropertyRowMapper<Account>(Account.class));

		}catch(EmptyResultDataAccessException e){
			//TODO:
		}
		return account;	
	}

	
	public List<Account> findAll() {
		String sql = "select * from accounts";
		List<Account> accounts = jdbcTemplate.query(sql, 
				new BeanPropertyRowMapper<Account>(Account.class));
		return accounts;
	}

	
	public List<Account> findAllOffset(int max, int offset) {
		String sql = "select * from accounts limit " + max + " offset " + offset;
		List<Account> accounts = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Account>(Account.class));
		return accounts;
	}
	


	public Account save(Account account) {
		String sql = "insert into accounts (name, username, age, location, image_uri, password) values (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {
				account.getName(), account.getUsername(), account.getAge(), account.getLocation(), account.getImageUri(), account.getPassword()
		});

		long id = id();
		Account savedAccount = get(id);

		checkSaveDefaultAccountRole(id);
		checkSaveDefaultAccountPermission(id);

		return savedAccount;
	}


	public Account saveAdministrator(Account account) {
		String sql = "insert into accounts (name, username, age, location, image_uri, password) values (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {
				account.getName(), account.getUsername(), account.getAge(), account.getLocation(), account.getImageUri(), account.getPassword()
		});

		long id = id();
		Account savedAccount = get(id);

		checkSaveAdministratorRole(id);
		checkSaveDefaultAccountPermission(id);

		return savedAccount;
	}
	
	public void update(Account account) {
		String sql = "update accounts set ( name, age, location, image_uri, uuid ) = ( ?, ?, ?, ?, ? )  where id = ?";
		jdbcTemplate.update(sql, new Object[] { 
			account.getName(), account.getAge(), account.getLocation(), account.getImageUri(), account.getUuid(), account.getId()
		});
	}


	
	public void updatePassword(Account account) {
		String sql = "update accounts set password = ? where id = ?";
		jdbcTemplate.update(sql, new Object[] { 
			account.getPassword(), account.getId()     
		});
	}

	public Account findByUsernameAndUuid(String username, String uuid){
		Account account = null;
		try{
			String sql = "select * from accounts where username = '" + username + "' and uuid = '" + uuid + "'";
			account = jdbcTemplate.queryForObject(sql, new Object[] {},
					new BeanPropertyRowMapper<Account>(Account.class));

		}catch(EmptyResultDataAccessException e){}
		return account;
	}
	
	public void delete(long id) {
		String sql = "delete from accounts where id = ?";
		jdbcTemplate.update(sql, new Object[] {id });
	}

	public String getAccountPassword(String username) {
		Account account = findByUsername(username);
		return account.getPassword();
	}

	public void checkSaveAdministratorRole(long accountId){
		Role role = roleDao.find(ApplicationConstants.ROLE_ADMIN);
		AccountRole existing = getAccountRole(accountId, role.getId());
		if(existing == null){
			saveAccountRole(accountId, role.getId());
		}
	}


	public void checkSaveDefaultAccountRole(long accountId){
		Role role = roleDao.find(ApplicationConstants.ROLE_ACCOUNT);
		AccountRole existing = getAccountRole(accountId, role.getId());
		if(existing == null){
			saveAccountRole(accountId, role.getId());
		}
	}

	public AccountRole getAccountRole(long accountId, long roleId){
		String sql = "select * from account_roles where account_id = ? and role_id = ?";
		try {
			AccountRole  accountRole = jdbcTemplate.queryForObject(sql, new Object[]{accountId, roleId},
					new BeanPropertyRowMapper<AccountRole>(AccountRole.class));
			return accountRole;
		}catch(Exception e){
			return null;
		}
	}


	public void checkSaveDefaultAccountPermission(long accountId){
		String permission = ApplicationConstants.ACCOUNT_MAINTENANCE + accountId;
		AccountPermission existing = getAccountPermission(accountId, permission);
		if(existing == null){
			saveAccountPermission(accountId, permission);
		}
	}



	public AccountPermission getAccountPermission(long accountId, String permission){
		String sql = "select * from account_permissions where account_id = ? and permission = ?";
		try {
			AccountPermission  accountPermission = jdbcTemplate.queryForObject(sql, new Object[]{accountId, permission},
					new BeanPropertyRowMapper<AccountPermission>(AccountPermission.class));
			return accountPermission;
		}catch(Exception e){
			return null;
		}
	}


	public void saveAccountRole(long accountId, long roleId){
		String sql = "insert into account_roles (role_id, account_id) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] { 
			roleId, accountId
		});
	}

	
	public void saveAccountPermission(long accountId, String permission){
		String sql = "insert into account_permissions (account_id, permission) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] { 
			accountId, permission
		});
	}
	
	
	public void deleteAccountRoles(long accountId){
		String sql = "delete from account_roles where account_id = ?";
		jdbcTemplate.update(sql, new Object[] { accountId });
	}
	
	
	public void deleteAccountPermissions(long accountId){
		String sql = "delete from account_permissions where account_id = ?";
		jdbcTemplate.update(sql, new Object[] { accountId });
	}


	public Set<String> getAccountRoles(long id) {	
		String sql = "select r.name from account_roles ur, role r where ur.role_id = r.id and ur.account_id = " + id;
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}

	
	public Set<String> getAccountRoles(String username) {	
		Account account = findByUsername(username);
		String sql = "select r.name from account_roles ur, role r where ur.role_id = r.id and ur.account_id = " + account.getId();
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}


	public Set<String> getAccountPermissions(long id) {	
		String sql = "select permission from account_permissions where account_id = " + id;
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}

	
	public Set<String> getAccountPermissions(String username) {	
		Account account = findByUsername(username);
		String sql = "select permission from account_permissions where account_id = " + account.getId();
		List<String> permissionsList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> permissions = new HashSet<String>(permissionsList);
		return permissions;
	}


	public long countQuery(String query){
		String sql = "select count(*) from accounts where upper(name) like upper('%" + query + "%')";
		long count = jdbcTemplate.queryForObject(sql, new Object[] {}, Long.class);
		return count;
	}



	public List<Account> search(String uncleanedQuery, int offset) {
		String query = uncleanedQuery.replaceAll("([-+.^:,])","");
		String sql = "select distinct * from accounts where upper(name) like upper(:query) order by name";

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("query", "%" + query + "%")
				.addValue("offset", offset);

		List<Account> accountsSearched = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<Account>(Account.class));
		return accountsSearched;
	}

}