package hu.evosoft.controller;

import hu.evosoft.model.Data;
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
public class CloudRabbitController {

	private static Data myData = null;

	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;

	@Autowired
	private RabbitRedisTransferrer rabbitRedisTransferrer;

	@RequestMapping(value = "/rabbit", method = RequestMethod.GET)
	public String getData(ModelMap model) {
		if (myData != null) {
			model.addAttribute("data", myData.getName());
		}
		return "rabbit";
	}

	@RequestMapping(value = "/rabbit/queue", method = RequestMethod.POST)
	public View queueData(@ModelAttribute Data data, ModelMap model) {
		myData = data;
		rabbitService.queueMessage(data.getName());
		return new RedirectView("/rabbit");
	}

	@RequestMapping(value = "/rabbit/transfer", method = RequestMethod.GET)
	public View transferData(ModelMap model) {
		rabbitRedisTransferrer.transferData();
		model.addAttribute("dataList", rabbitService.retrieveOneData());
		return new RedirectView("/redis");
	}

}
