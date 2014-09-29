package hu.evosoft.controller;

import hu.evosoft.model.Person;
import hu.evosoft.service.CloudRabbitService;
import hu.evosoft.transfer.RabbitRedisTransferrer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PersonRabbitController {

	private static Person myPerson = null;
	
	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;

	@Autowired
	private RabbitRedisTransferrer rabbitRedisTransferrer;

	@RequestMapping(value = "/rabbit", method = RequestMethod.GET)
	public String getPerson(ModelMap model) {
		if (myPerson != null) {
			model.addAttribute("person", myPerson.getName());
		}
		return "rabbit";
	}
	
	@RequestMapping(value = "/rabbit/queue", method = RequestMethod.POST)
	public View queuePerson(@ModelAttribute Person person, ModelMap model) {
		myPerson = person;
		rabbitService.queueMessage(person.getName());
		return new RedirectView("/rabbit");
	}

	@RequestMapping(value = "/rabbit/transfer", method = RequestMethod.GET)
	public View transferPerson(ModelMap model) {
		rabbitRedisTransferrer.transferPerson();
		model.addAttribute("personList", rabbitService.retrieveOnePerson());
		return new RedirectView("/redis");
	}

}
