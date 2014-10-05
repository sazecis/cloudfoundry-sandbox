package hu.evosoft.controller;

import hu.evosoft.model.Data;
import hu.evosoft.service.CloudRedisService;
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
public class CloudRedisController {

	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;

	@Autowired
	private RedisMongoTransferrer redisMongoTransferrer;

	@RequestMapping(value = "/redis", method = RequestMethod.GET)
	public String getDataList(ModelMap model) {
		model.addAttribute("dataList", redisService.listData());
		model.addAttribute("destHostList", redisService.listNetStatInfo());
		return "redis";
	}

	@RequestMapping(value = "/redis/save", method = RequestMethod.POST)
	public View createData(@ModelAttribute Data data, ModelMap model) {
		redisService.addData(data.getData());
		return new RedirectView("/redis");
	}

	@RequestMapping(value = "/redis/transfer", method = RequestMethod.GET)
	public View transferData(ModelMap model) {
		redisMongoTransferrer.transferData();
		model.addAttribute("dataList", redisService.listData());
		return new RedirectView("/mongo");
	}

	@RequestMapping(value = "/redis/clear", method = RequestMethod.GET)
	public View clearData(ModelMap model) {
		redisService.clearData();
		model.addAttribute("dataList", redisService.listData());
		return new RedirectView("/redis");
	}
	
}
