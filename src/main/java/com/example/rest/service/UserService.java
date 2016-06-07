package com.example.rest.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.rest.Exception.UserException;
import com.example.rest.dto.UserDTO;
import com.example.rest.model.UserModel;

public interface UserService {
	public UserDTO getUserByAuthToken(String authToken);

	UserModel saveUser(UserModel userModel, HttpServletRequest request) throws UserException;

	UserModel logIn(UserModel userModel, HttpServletRequest request) throws Exception;

	void forgotPassword(HttpServletRequest request, String email) throws UserException;

	List<UserModel> getUserList()throws Exception;
}
