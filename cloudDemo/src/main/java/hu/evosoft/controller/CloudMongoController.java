package hu.evosoft.controller;

import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterEntity;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.LogEntryDate;
import hu.evosoft.service.CloudMongoService;
import hu.evosoft.service.PerformanceCounterService;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Spring Controller used to handle the requests from the mongo.jsp
 * 
 * @author Csaba.Szegedi
 *
 */
@Controller
public class CloudMongoController {

	@Autowired
	@Qualifier("cloudMongoService")
	private CloudMongoService mongoService;
	@Autowired
	private PerformanceCounterService performanceCounterService;
	
	/**
	 * Prepares and sets the information to the mongo.jsp via the ModelMap:
	 *  - list of the DestinationHost from the MongoDB
	 *  - list of the LogEntryDate from the MongoDB
	 *  - performance counters (RabbitSend, RabbitReceive, MongoAdd, MongoMR)
	 * 
	 * @param model contains information for the mongo.jsp
	 * @return the name of the jsp where to return to
	 */
	@RequestMapping(value = "/mongo", method = RequestMethod.GET)
	public String getDocumentList(ModelMap model) {
		List<DestinationHost> destHostList = mongoService.listDocuments(DestinationHost.class);
		Collections.sort(destHostList);
		model.addAttribute("destHostList", destHostList);
		List<LogEntryDate> logEntryList = mongoService.listDocuments(LogEntryDate.class);
		Collections.sort(logEntryList);
		model.addAttribute("logDataList", logEntryList);
		model.addAttribute("perfCountRabbitSend", 
				performanceCounterService.getGlobalTimeSpentFor(CounterCategory.RABBIT_SEND));
		model.addAttribute("perfCountRabbitReceive", 
				performanceCounterService.getGlobalTimeSpentFor(CounterCategory.RABBIT_RECEIVE));
		model.addAttribute("perfCountMongoAdd", 
				performanceCounterService.getGlobalTimeSpentFor(CounterCategory.MONGO_ADD));
		model.addAttribute("perfCountMongoMr", 
				performanceCounterService.getGlobalTimeSpentFor(CounterCategory.MONGO_MR));
		return "mongo";
	}

	/**
	 * Triggers a full clear for IMongoModels in the MongoDB. Clears all collections. 
	 * 
	 * @param model contains information for the mongo.jsp
	 * @return the view name where the dispatcher will navigate
	 */
	@RequestMapping(value = "/mongo/clear", method = RequestMethod.GET)
	public View clearDocuments(ModelMap model) {
		mongoService.clearAllDocuments();
		return new RedirectView("/mongo");
	}

	/**
	 * Triggers the clearing of the performance counters.
	 * 
	 * @param model contains information for the mongo.jsp
	 * @return the view name where the dispatcher will navigate
	 */
	@RequestMapping(value = "/mongo/clearCounters", method = RequestMethod.GET)
	public View clearCounters(ModelMap model) {
		mongoService.clearDocuments(CounterEntity.class);
		return new RedirectView("/mongo");
	}

}