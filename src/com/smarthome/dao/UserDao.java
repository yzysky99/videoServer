package com.smarthome.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.smarthome.domain.User;

public interface UserDao {

	/**
	 * 添加用户
	 * @param user
	 */
	void addUser(User user);
	
	/**
	 * 根据用户名查找用户
	 * @param name 用户名
	 * @return 找到的用户,如果找不到返回null
	 */
	User findUserByName(String name);
	
	/**
	 * 查询所有客户信息组成的集合
	 * @return 封装了所有客户信息的集合,如果没有任何客户,返回的集合中内容为空
	 */
	List<User> getAllUser();

	/**
	 * 根据id查找客户
	 * @param id 客户id
	 * @return 客户bean
	 */
	User findUserById(String id);

	/**
	 * 修改客户信息
	 * @param user 最新信息bean
	 */
	void updateUser(User user);

	/**
	 * 根据id删除客户
	 * @param id
	 */
	void delUserByID(String id);

	/**
	 * 根据Id删除客户,并管理事务
	 * @param id
	 * @throws SQLException 
	 */
	void delUserByIDWithTrans(Connection conn, String id) throws SQLException;

}
