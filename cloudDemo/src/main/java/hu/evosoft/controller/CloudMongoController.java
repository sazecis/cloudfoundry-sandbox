package hu.evosoft.controller;

import hu.evosoft.model.Data;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.LogEntryDate;
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
	public String getDocumentList(ModelMap model) {
		model.addAttribute("dataList", mongoService.listDocuments(Data.class));
		model.addAttribute("destHostList", mongoService.listDocuments(DestinationHost.class));
		model.addAttribute("logDataList", mongoService.listDocuments(LogEntryDate.class));
		return "mongo";
	}

	@RequestMapping(value = "/mongo/save", method = RequestMethod.POST)
	public View createDocument(@ModelAttribute Data data, ModelMap model) {
		if (StringUtils.hasText(data.getId())) {
			mongoService.updateDocument(data);
		} else {
			mongoService.addDocument(data);
		}
		return new RedirectView("/mongo");
	}

	@RequestMapping(value = "/mongo/clear", method = RequestMethod.GET)
	public View clearDocuments(ModelMap model) {
		mongoService.clearAllDocuments();
		return new RedirectView("/mongo");
	}

}