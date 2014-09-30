package hu.evosoft.controller;

import hu.evosoft.model.Data;
import hu.evosoft.service.CloudMongoService;

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
public class CloudMongoController {

	@Autowired
	@Qualifier("cloudMongoService")
	private CloudMongoService mongoService;
	
	@RequestMapping(value = "/mongo", method = RequestMethod.GET)
	public String getDataList(ModelMap model) {
		model.addAttribute("dataList", mongoService.listData());
		return "mongo";
	}

	@RequestMapping(value = "/mongo/save", method = RequestMethod.POST)
	public View createData(@ModelAttribute Data data, ModelMap model) {
		if (StringUtils.hasText(data.getId())) {
			mongoService.updateData(data);
		} else {
			mongoService.addData(data);
		}
		return new RedirectView("/mongo");
	}

	@RequestMapping(value = "/mongo/delete", method = RequestMethod.GET)
	public View deleteData(@ModelAttribute Data data, ModelMap model) {
		mongoService.deleteData(data);
		return new RedirectView("/mongo");
	}
	
}