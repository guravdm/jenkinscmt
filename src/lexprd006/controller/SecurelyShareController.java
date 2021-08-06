package lexprd006.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;

@Controller
@RequestMapping(value = "/*")
public class SecurelyShareController {

	// please add the UserToken to perform operations
	private String userToken = "31dbf11e-fa9f-3f1c-8576-53687d5afecb";
	// change https://dev.securelyshare.com to your respective URl
	private static final String CreateShareUrl = "https://dev.securelyshare.com/v2/orgs/1/shares";
	private static final MediaType JSON = MediaType.parse("multipart/form-data");
	ObjectMapper mapper = new ObjectMapper();
	// please add your Identity Id you want to add to your headers
	private String identityId = "testuser@securelyshare.com";
	// please change the path to where the files exist
	private String filePath = "src/main/java/";

}
