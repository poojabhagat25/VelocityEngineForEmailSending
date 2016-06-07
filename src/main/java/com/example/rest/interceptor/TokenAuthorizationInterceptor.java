package com.example.rest.interceptor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.rest.dto.UserDTO;
import com.example.rest.model.ErrorModel;
import com.example.rest.model.ResponseModel;
import com.example.rest.service.UserService;

import static com.example.rest.constants.RestConstants.INVALID_USER_TOKEN;
import static com.example.rest.constants.RestConstants.AUTHTOKEN;
import static com.example.rest.constants.RestConstants.AUTHTOKEN_KEYWORD;
import static com.example.rest.constants.RestConstants.USER;
import static com.example.rest.constants.RestConstants.LOGIN_URL;
import static com.example.rest.constants.RestConstants.FORGOT_PASSWORD;
import static com.example.rest.constants.RestConstants.SIGN_UP_USER_URL;
import static com.example.rest.constants.RestConstants.FAIL;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TokenAuthorizationInterceptor extends
		AbstractPhaseInterceptor<Message> {
	private Logger logger = Logger
			.getLogger(TokenAuthorizationInterceptor.class);
	@Autowired
	private UserService userService;

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public TokenAuthorizationInterceptor() {
		super(Phase.PRE_INVOKE); // Put this interceptor in this phase
	}

	public void handleMessage(Message message) throws RuntimeException {
		HttpServletRequest httpRequest = (HttpServletRequest) message
				.get(AbstractHTTPDestination.HTTP_REQUEST);

		String pathInfo = httpRequest.getPathInfo();
		System.out.println("Method Type "+httpRequest.getMethod());
		
		logger.info("PATH httpRequest" + pathInfo);
		if(!pathInfo.equals(SIGN_UP_USER_URL) && !pathInfo.contains(LOGIN_URL)
				&& !pathInfo.contains(FORGOT_PASSWORD)) 
		{
			// get the authToken value from header
			String authToken = httpRequest.getHeader(AUTHTOKEN);
			logger.info(AUTHTOKEN_KEYWORD+ authToken);
			UserDTO user=null; 
			try{
				user = userService.getUserByAuthToken(authToken);
			}
			catch(Exception ex)
			{
				logger.error(ex.getStackTrace(),ex);
			}
			if (user == null) {
				String errorMessage=INVALID_USER_TOKEN;						
				 logger.info(AUTHTOKEN_KEYWORD+ errorMessage);				
				ResponseModel responseModel = ResponseModel.getInstance();
				ErrorModel error = new ErrorModel(errorMessage);
				responseModel.setError(error);
				responseModel.setStatus(FAIL);
				String errorResponse = null;
				ObjectMapper mapper = new ObjectMapper();
				try {
					errorResponse = mapper.writeValueAsString(responseModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
				HttpServletResponse response = (HttpServletResponse) message
						.get(AbstractHTTPDestination.HTTP_RESPONSE);
				response.setStatus(500);
				try {
					response.getWriter().write(errorResponse);
				} catch (IOException e) {
					e.printStackTrace();
				}
				throw new org.apache.cxf.interceptor.security.AccessDeniedException(
					INVALID_USER_TOKEN);

			}
			httpRequest.setAttribute(USER, user);
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
