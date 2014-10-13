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

/**
 * 
 * Spring Controller used to handle the requests from the rabbit.jsp
 * 
 * @author Csaba.Szegedi
 *
 */
@Controller
public class CloudRabbitController {

	private static String myChecked = "";

	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;

	/**
	 * Handles the default Rabbit page.  
	 * 
	 * @param model model contains information for the handling View
	 * @return returns the location where the dispatcher will navigate
	 */
	@RequestMapping(value = "/rabbit", method = RequestMethod.GET)
	public String getData(ModelMap model) {
		model.addAttribute("state", myChecked);
		return "rabbit";
	}

	/**
	 * Triggers a purge command for the RabbitMQ. Will delete all the messages fromt he queue.
	 * 
	 * @param model model contains information for the handling View
	 * @return returns the location where the dispatcher will navigate
	 */
	@RequestMapping(value = "/rabbit/purge", method = RequestMethod.GET)
	public View purgeQueue(ModelMap model) {
		rabbitService.purgeQueue();
		return new RedirectView("/rabbit");
	}
	
	/**
	 * Handles the request from the DevNull checkbox from rabbit.jsp. If DevNull is on then the messages from the
	 * queue will not be processed further to Redis.
	 * 
	 * @param model model contains information for the handling View
	 * @return returns the location where the dispatcher will navigate
	 */
	@RequestMapping(value = "/rabbit/nulldev", method = RequestMethod.GET)
	public View turnOnNullDevListener(ModelMap model) {
		myChecked = CloudRabbitListener.changeDevNullState();
		return new RedirectView("/rabbit");
	}
	
	
}
