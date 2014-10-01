package hu.evosoft.controller;

import hu.evosoft.parser.NetStatsParser;
import hu.evosoft.service.CloudRabbitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ReadLogsController {

	private static String myContent;

	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;

	@RequestMapping(value = "/read_logs", method = RequestMethod.GET)
	public String getLogs(ModelMap model) {
		if (myContent != null) {
			model.addAttribute("content", myContent);
		}
		return "read_logs";
	}

	@RequestMapping(value = "/read_logs/process", method = RequestMethod.POST)
	public View readLogContent(@RequestParam MultipartFile logFile,
			ModelMap model) {
		if (!logFile.isEmpty()) {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(logFile.getInputStream()))) {
				Iterator<String> iter = reader.lines().iterator();
				rabbitService.SendBeginSignal();				
				while (iter.hasNext()) {
					rabbitService.queueMessage(new NetStatsParser().correctLine(iter.next()));
				}
				myContent = "success";
			} catch (IOException x) {
				myContent = x.getMessage();
			}
			finally {
				rabbitService.SendEndSignal();
			}
		} else {
			myContent = "file is empty";
		}
		return new RedirectView("/read_logs");
	}
}
