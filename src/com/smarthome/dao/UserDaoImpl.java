package com.smarthome.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.smarthome.domain.User;
import com.smarthome.utils.DaoUtils;

public class UserDaoImpl implements UserDao{
	public void addUser(User user) {
		String sql = "insert into users values (null,?,?,?,?,?)";
		try{
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			runner.update(sql,user.getName(),user.getPassword(),user.getMobilePhone(),user.getEmail(),user.getAddress());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public User findUserByName(String name) {
		String sql = "select * from users where name = ?";
		try{
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			return runner.query(sql, new BeanHandler<User>(User.class),name);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<User> getAllUser() {
		String sql = "select * from users";
		try{
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			return runner.query(sql, new BeanListHandler<User>(User.class));
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public User findUserById(String id) {
		String sql = "select * from users where id = ?";
		try{
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			return runner.query(sql, new BeanHandler<User>(User.class),id);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public void updateUser(User user) {
		String sql = "update users set name=? ,password=?,mobilePhone=?,email=?,address=? where id=?";
		try{
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			runner.update(sql,user.getName(),user.getPassword(),user.getMobilePhone(),user.getEmail(),user.getAddress(),user.getId());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void delUserByID(String id) {
		String sql = "delete from users where id = ?";
		try{
			QueryRunner runner = new QueryRunner(DaoUtils.getSource());
			runner.update(sql,id);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void delUserByIDWithTrans(Connection conn, String id) throws SQLException {
		String sql = "delete from users where id = ?";
		QueryRunner runner = new QueryRunner();
		runner.update(conn,sql,id);
	}
}
