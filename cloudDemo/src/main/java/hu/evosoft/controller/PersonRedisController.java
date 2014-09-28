package hu.evosoft.controller;

import hu.evosoft.model.Person;
import hu.evosoft.service.PersonRedisService;
import hu.evosoft.transfer.RedisMongoTransferrer;

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
public class PersonRedisController {

	@Autowired
	@Qualifier("personRedisService")
	private PersonRedisService redisService;

	@Autowired
	private RedisMongoTransferrer redisMongoTransferrer;

	@RequestMapping(value = "/redis", method = RequestMethod.GET)
	public String getPersonList(ModelMap model) {
		model.addAttribute("personList", redisService.listPerson());
		return "redis";
	}

	@RequestMapping(value = "/redis/save", method = RequestMethod.POST)
	public View createPerson(@ModelAttribute Person person, ModelMap model) {
		redisService.addPerson(person.getName());
		return new RedirectView("/redis");
	}

	@RequestMapping(value = "/redis/transfer", method = RequestMethod.GET)
	public View transferPerson(ModelMap model) {
		redisMongoTransferrer.transferPerson();
		model.addAttribute("personList", redisService.listPerson());
		return new RedirectView("/mongo");
	}

}
