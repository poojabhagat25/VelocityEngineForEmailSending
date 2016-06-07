package com.example.rest.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.omg.CORBA.UserException;

import com.example.rest.dto.UserDTO;
import com.example.rest.model.ResponseModel;
import com.example.rest.model.UserModel;

public interface UserWebService {
	UserDTO getUser(String authToken);
	ResponseModel signUpUser(HttpServletRequest request, HttpServletResponse response, UserModel userModel)
			throws UserException, com.example.rest.Exception.UserException;
	ResponseModel logIn(HttpServletRequest request, UserModel userModel) throws Exception;
	ResponseModel forgotPassword(HttpServletRequest request, UserModel userModel)
			throws com.example.rest.Exception.UserException;
	ResponseModel getUserList(@Context HttpServletRequest request)throws Exception;

}
