package com.example.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.rest.Exception.UserException;
import com.example.rest.dao.UserDao;
import com.example.rest.dto.EmailDTO;
import com.example.rest.dto.UserDTO;
import com.example.rest.model.UserModel;
import com.example.rest.util.ApplicationBeanUtil;
import com.example.rest.util.EmailSender;
import com.example.rest.util.LocaleConverter;
import com.example.rest.util.ResourceManager;
import com.example.rest.util.TokenGenerator;

import static com.example.rest.constants.RestConstants.NOT_FOUND;
import static com.example.rest.constants.RestConstants.REGISTER_FAILED_EXCEPTION;
import static com.example.rest.constants.RestConstants.USER_ALREADY_EXIST_EXCEPTION;
import static com.example.rest.constants.RestConstants.PASSWORD_MISMATCH;
import static com.example.rest.constants.RestConstants.USER_NOT_REGISTERED;
import static com.example.rest.constants.RestConstants.LOG_IN_FAILED;

public class UserServiceImpl implements UserService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private Mapper dozerMapper;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserDTO getUserByAuthToken(String authToken) {
		UserDTO dto = null;
		List<UserDTO> userDtos = userDao.getUserByAuthToken(authToken);
		if (!userDtos.isEmpty()) {
			dto = (UserDTO) userDtos.get(0);
			return dto;
		}
		return dto;
	}

	// @Override
	public UserModel saveUser(UserModel userModel, HttpServletRequest request) throws UserException {
		Locale locale = LocaleConverter.getLocaleFromRequest(request);
		UserDTO userDTO = userDao.checkUser(userModel.getEmailId());
		if (userDTO != null) {
			// already exist
			throw new UserException(ResourceManager.getMessage(USER_ALREADY_EXIST_EXCEPTION, null, NOT_FOUND, locale));

		}
		userDTO = dozerMapper.map(userModel, UserDTO.class);

		try {
			String authToken = TokenGenerator.generateToken(userModel.getEmailId() + new Date());
			userDTO.setAuthToken(authToken);
			userDTO = userDao.saveUser(userDTO);
			
			// To send an email after successful registration.
			EmailDTO emailDTO = new EmailDTO();
			emailDTO.setTo(userDTO.getEmailId());
			emailDTO.setUsername(userDTO.getFirstName());
			emailDTO.setPassword(userDTO.getPassword());
			emailDTO.setFrom("Draft");
			try {
				EmailSender emailSender = (EmailSender) ApplicationBeanUtil.getApplicationContext().getBean("mail");
				emailSender.sendRegistrationConfirmationMail(emailDTO, userDTO);
			} catch (Exception ex) {
				logger.error(ex.getStackTrace(), ex);
			}
			userModel = dozerMapper.map(userDTO, UserModel.class);
		} catch (Exception ex) {
			logger.error(ex.getStackTrace(), ex);
			throw new UserException(ResourceManager.getMessage(REGISTER_FAILED_EXCEPTION, null, NOT_FOUND, locale));
		}
		return userModel;
	}

	// @Override
	public UserModel logIn(UserModel userModel, HttpServletRequest request) throws Exception {
		Locale locale = LocaleConverter.getLocaleFromRequest(request);

		UserDTO user = userDao.checkUser(userModel.getEmailId());
		if (user == null) {
			throw new UserException(ResourceManager.getMessage(USER_NOT_REGISTERED, null, "not.found", locale));
		}
		if (user != null && !user.getPassword().equals(userModel.getPassword())) {
			throw new UserException(ResourceManager.getProperty(PASSWORD_MISMATCH));
		}

		try {
			userModel = dozerMapper.map(user, UserModel.class);
		} catch (Exception ex) {
			throw new UserException(ResourceManager.getMessage(LOG_IN_FAILED, null, "not.found", locale));
		}
		return userModel;
	}
	
//	@Override
	public void forgotPassword(HttpServletRequest request, String email) throws UserException {
		Locale locale = LocaleConverter.getLocaleFromRequest(request);
		EmailDTO emailDTO = new EmailDTO();

		UserDTO retrievedUser = userDao.checkUser(email);
		if (retrievedUser == null) {
			logger.error(ResourceManager.getMessage(USER_NOT_REGISTERED, null, NOT_FOUND, locale));
			throw new UserException(ResourceManager.getMessage(USER_NOT_REGISTERED, null, NOT_FOUND, locale));
		}


		emailDTO.setFrom("Rest API Demo");
		emailDTO.setTo(retrievedUser.getEmailId());
		emailDTO.setUsername(retrievedUser.getFirstName());
		emailDTO.setPassword(retrievedUser.getPassword());
		try {
			// Working code using velocity dependancies
			EmailSender emailSender = (EmailSender) ApplicationBeanUtil.getApplicationContext().getBean("mail");
			emailSender.sendForgotPasswordMail(emailDTO);
		} catch (Exception e) {
			logger.error(e.getMessage() + e.getStackTrace());
		}

	}
	
//	@Override
	public List<UserModel> getUserList()throws Exception{
		List<UserModel> userModels = new ArrayList<UserModel>() ;
		try{
			List<UserDTO> userDTOs = userDao.getUserList();
			for(UserDTO userDTO:userDTOs){
				UserModel userModel = dozerMapper.map(userDTO, UserModel.class);
				userModels.add(userModel);
			}
		}catch(Exception ex){
			throw ex;
		}
		return userModels;
		
	}

}
