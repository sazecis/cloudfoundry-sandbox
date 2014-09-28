package hu.evosoft.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {
	private static final Logger logger = Logger.getLogger(WelcomeController.class);

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String getWelcome(ModelMap model) {
		logger.info("haho - welcome");
		model.addAttribute("msg", "WelcomeController");
		return "welcome";
	}

}