package com.example.rest.webservice;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.rest.Exception.UserException;
import com.example.rest.dto.UserDTO;
import com.example.rest.model.ResponseModel;
import com.example.rest.model.UserModel;
import com.example.rest.service.UserService;
import com.example.rest.util.LocaleConverter;
import com.example.rest.util.ResourceManager;

import static com.example.rest.constants.RestConstants.NOT_FOUND;
import static com.example.rest.constants.RestConstants.USER_REGISTERED_SUCCESSFULLY_MESSAGE;
import static com.example.rest.constants.RestConstants.USER_LOGGED_IN_SUCCESSFULLY;
import static com.example.rest.constants.RestConstants.PASSWORD_SENT_SUCCESSFULLY;
import static com.example.rest.constants.RestConstants.USER_LIST_SENT_SUCCESSFULLY;

@Component
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class UserWebServiceImpl implements UserWebService {
	private static Logger logger = Logger.getLogger(UserWebServiceImpl.class);

	@Autowired
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@GET
	@Path("/user")
	public UserDTO getUser(@QueryParam("token") String authToken) {
		UserDTO userDto = userService.getUserByAuthToken(authToken);
		return userDto;

	}

	@POST
	// @Override
	public ResponseModel signUpUser(@Context HttpServletRequest request, @Context HttpServletResponse response,
			UserModel userModel) throws UserException {
		logger.info("<------inside signUpUser start------>");
		Locale locale = LocaleConverter.getLocaleFromRequest(request);
		ResponseModel responseModel = null;
		userModel = userService.saveUser(userModel, request);
		responseModel = ResponseModel.getInstance();
		responseModel.setObject(userModel);
		responseModel
				.setMessage(ResourceManager.getMessage(USER_REGISTERED_SUCCESSFULLY_MESSAGE, null, NOT_FOUND, locale));
		return responseModel;
	}

	@POST
	@Path("/logIn")
	// @Override
	public ResponseModel logIn(@Context HttpServletRequest request, UserModel userModel) throws Exception {
		Locale locale = LocaleConverter.getLocaleFromRequest(request);
		ResponseModel responseModel = null;
		userModel = userService.logIn(userModel, request);

		responseModel = ResponseModel.getInstance();
		responseModel.setObject(userModel);
		responseModel.setMessage(ResourceManager.getMessage(USER_LOGGED_IN_SUCCESSFULLY, null, NOT_FOUND, locale));
		return responseModel;
	}

	@POST
	@Path("/forgot_password")
	// @Override
	public ResponseModel forgotPassword(@Context HttpServletRequest request, UserModel userModel) throws UserException {
		logger.info("<------Forgot Password start------>");
		Locale locale = LocaleConverter.getLocaleFromRequest(request);
		userService.forgotPassword(request, userModel.getEmailId());
		ResponseModel responseModel = ResponseModel.getInstance();
		responseModel.setMessage(ResourceManager.getMessage(PASSWORD_SENT_SUCCESSFULLY, null, NOT_FOUND, locale));
		return responseModel;
	}

	@GET
	@Path("/getUserList")
	public ResponseModel getUserList(@Context HttpServletRequest request) throws Exception {
		logger.info("<------User List------>");
		Locale locale = LocaleConverter.getLocaleFromRequest(request);
		List<UserModel> userModels = userService.getUserList();
		ResponseModel responseModel = ResponseModel.getInstance();
		responseModel.setObject(userModels);
		responseModel.setMessage(ResourceManager.getMessage(USER_LIST_SENT_SUCCESSFULLY, null, NOT_FOUND, locale));
		return responseModel;
	}

}
