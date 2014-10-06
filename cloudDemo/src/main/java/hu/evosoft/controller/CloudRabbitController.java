package hu.evosoft.controller;

import hu.evosoft.service.CloudRabbitListener;
import hu.evosoft.service.CloudRabbitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CloudRabbitController {

	private static String myChecked = "";

	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;

	@RequestMapping(value = "/rabbit", method = RequestMethod.GET)
	public String getData(ModelMap model) {
		model.addAttribute("state", myChecked);
		return "rabbit";
	}

	@RequestMapping(value = "/rabbit/purge", method = RequestMethod.GET)
	public View purgeQueue(ModelMap model) {
		rabbitService.purgeQueue();
		return new RedirectView("/rabbit");
	}
	
	@RequestMapping(value = "/rabbit/nulldev", method = RequestMethod.GET)
	public View turnOnNullDevListener(ModelMap model) {
		myChecked = CloudRabbitListener.changeDevNullState();
		return new RedirectView("/rabbit");
	}
	
	
}
