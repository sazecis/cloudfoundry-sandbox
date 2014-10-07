package hu.evosoft.controller;

import hu.evosoft.logger.MyLogger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MyLoggerController {

	@RequestMapping(value = "/logs", method = RequestMethod.GET)
	public String getLogs(ModelMap model) {
		model.addAttribute("logs", MyLogger.getLogs());
		return "logs";
	}

	@RequestMapping(value = "/logs/clear", method = RequestMethod.GET)
	public View clearLogs(ModelMap model) {
		MyLogger.clearLogs();
		return new RedirectView("/logs");
	}

	
}
