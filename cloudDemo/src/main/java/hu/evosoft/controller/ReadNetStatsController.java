package hu.evosoft.controller;

import hu.evosoft.file.UploadedFile;
import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterType;
import hu.evosoft.logger.MyLogger;
import hu.evosoft.parser.NetStatsParser;
import hu.evosoft.service.CloudRabbitService;
import hu.evosoft.service.PerformanceCounterService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ReadNetStatsController {

	private static String myContent;
	private static final int CHUNK_LIMIT = 250000;
	private static final String UPLOAD_LOCATION = "./temp/";
	private static List<UploadedFile> myFileSet = new ArrayList<>();

	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;
	@Autowired
	private PerformanceCounterService performanceCounterpervice;

	@RequestMapping(value = "/read_netstat", method = RequestMethod.GET)
	public String readNetStat(ModelMap model) {
		if (myContent != null) {
			model.addAttribute("content", myContent);
		}
		try {
			myFileSet = new ArrayList<>();
			Files.walk(Paths.get(UPLOAD_LOCATION)).forEach(
					filePath -> {
						if (Files.isRegularFile(filePath)) {
							myFileSet.add(new UploadedFile(filePath.toString(),
									Long.toString(filePath.toFile().length())));
						}
					});
		} catch (IOException e) {
			MyLogger.appendLog(e.getMessage());			
		}
		model.addAttribute("fileList", myFileSet);
		return "read_netstat";
	}

	@RequestMapping(value = "/read_netstat/process_selected", method = RequestMethod.GET)
	public View processSelectedFile(@ModelAttribute UploadedFile uploadedFile,
			ModelMap model) {
		MyLogger.appendLog("processSelectedFile file.getName()=", uploadedFile.getName());
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(uploadedFile.getName()))) {
			readAndProcessFile(reader);
		} catch (IOException x) {
			myContent = x.getMessage();
		} 
		return new RedirectView("/read_netstat");
	}

	@RequestMapping(value = "/read_netstat/upload", method = RequestMethod.POST)
	public View uploadContent(@RequestParam MultipartFile logFile,
			ModelMap model) {
		if (!logFile.isEmpty()) {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(logFile.getInputStream()))) {
				if (!Files.exists(Paths.get(UPLOAD_LOCATION))) {
					Files.createDirectory(Paths.get(UPLOAD_LOCATION));
				}
				try (BufferedWriter writer = new BufferedWriter(
						Files.newBufferedWriter(Paths.get(UPLOAD_LOCATION
								+ logFile.getOriginalFilename())))) {
					String line = null;
					while ((line = reader.readLine()) != null) {
						writer.append(line);
						writer.newLine();
					}
				} catch (IOException x) {
					myContent = x.getMessage();
				}
				MyLogger.appendLog(
						"Does the file exist? ",
						Boolean.toString(Files.exists(Paths.get(UPLOAD_LOCATION
								+ logFile.getOriginalFilename()))));
				myContent = "Successfully uploaded";
			} catch (IOException x) {
				myContent = x.getMessage();
			}
		} else {
			myContent = "The file is empty or no file was provided.";
		}
		return new RedirectView("/read_netstat");
	}

	@RequestMapping(value = "/read_netstat/live_process", method = RequestMethod.POST)
	public View readLogContent(@RequestParam MultipartFile logFile,
			ModelMap model) {
		if (!logFile.isEmpty()) {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(logFile.getInputStream()))) {
				readAndProcessFile(reader);
			} catch (IOException x) {
				myContent = x.getMessage();
			} 
		} else {
			myContent = "The file is empty or no file was provided.";
		}
		return new RedirectView("/read_netstat");
	}

	private void readAndProcessFile(BufferedReader reader) throws IOException {
		int counter = 1;
		String line = null;

		performanceCounterpervice.clearCounters();
		rabbitService.sendBeginSignal();	
		performanceCounterpervice.addNewCounterEntry(
				CounterCategory.RABBIT_SEND, CounterType.START, 
				this.getClass().getSimpleName(), 
				System.currentTimeMillis());		
		while ((line = reader.readLine()) != null) {
			rabbitService.queueMessage(new NetStatsParser().correctLine(line));
			if (counter++ % CHUNK_LIMIT == 0) {
				rabbitService.sendChunkEndSignal();
			}
		}
		performanceCounterpervice.addNewCounterEntry(
				CounterCategory.RABBIT_SEND, CounterType.END, 
				this.getClass().getSimpleName(), 
				System.currentTimeMillis());
		rabbitService.sendEndSignal();		
	}

	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public String gwtStatistics(ModelMap model) {
		return "stats";
	}

}
