package hu.evosoft.controller;

import hu.evosoft.service.CloudRedisService;
import hu.evosoft.service.RedisMongoTransferrer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CloudRedisController {

	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;

	@Autowired
	private RedisMongoTransferrer redisMongoTransferrer;

	@RequestMapping(value = "/redis", method = RequestMethod.GET)
	public String getDataList(ModelMap model) {
		model.addAttribute("netStatsList", redisService.listNetStatInfo());
		return "redis";
	}

	@RequestMapping(value = "/redis/clear", method = RequestMethod.GET)
	public View clearData(ModelMap model) {
		redisService.clearData();
		return new RedirectView("/redis");
	}
	
}
