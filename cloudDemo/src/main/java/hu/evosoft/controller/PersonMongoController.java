package hu.evosoft.controller;

import hu.evosoft.model.Person;
import hu.evosoft.service.PersonMongoService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PersonMongoController {

	private static final Logger logger = Logger
			.getLogger(PersonMongoController.class);

	@Autowired
	@Qualifier("personMongoService")
	private PersonMongoService mongoService;
	
	@RequestMapping(value = "/mongo", method = RequestMethod.GET)
	public String getPersonList(ModelMap model) {
		logger.info("getPersonList 123");
		model.addAttribute("personList", mongoService.listPerson());
		return "mongo";
	}

	@RequestMapping(value = "/mongo/save", method = RequestMethod.POST)
	public View createPerson(@ModelAttribute Person person, ModelMap model) {
		if (StringUtils.hasText(person.getId())) {
			mongoService.updatePerson(person);
		} else {
			mongoService.addPerson(person);
		}
		return new RedirectView("/mongo");
	}

	@RequestMapping(value = "/mongo/delete", method = RequestMethod.GET)
	public View deletePerson(@ModelAttribute Person person, ModelMap model) {
		mongoService.deletePerson(person);
		return new RedirectView("/mongo");
	}
	
}