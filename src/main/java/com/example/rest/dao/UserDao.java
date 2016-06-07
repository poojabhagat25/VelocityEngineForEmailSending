package com.example.rest.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.example.rest.dto.UserDTO;

public interface UserDao {

	public List<UserDTO> getUserByAuthToken(String authToken);

	UserDTO checkUser(String emailId);

	UserDTO saveUser(UserDTO userDTO);

	List<UserDTO> getUserList() throws HibernateException;

}
