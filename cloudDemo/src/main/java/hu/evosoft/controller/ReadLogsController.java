package hu.evosoft.controller;

import hu.evosoft.file.Checked;
import hu.evosoft.file.CheckedFile;
import hu.evosoft.logger.MyLogger;
import hu.evosoft.parser.NetStatsParser;
import hu.evosoft.service.CloudRabbitService;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


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
public class ReadLogsController {

	private static String myContent;
	private static final int CHUNK_LIMIT = 250000;
	private static Map<String, CheckedFile> myFileMap = new HashMap<>();

	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;

	@RequestMapping(value = "/read_logs", method = RequestMethod.GET)
	public String getLogs(ModelMap model) {
		if (myContent != null) {
			model.addAttribute("content", myContent);
		}
		try {
			Files.walk(Paths.get("./temp")).forEach(filePath -> {
				MyLogger.appendLog("Files.isRegularFile: ", Boolean.toString(Files.isRegularFile(filePath)));
				MyLogger.appendLog("myFileMap.containsKey: ", filePath.toString(), Boolean.toString(myFileMap.containsKey(filePath.toString())));
			    if (Files.isRegularFile(filePath) && !myFileMap.containsKey(filePath.toString())) {
			        myFileMap.put(filePath.toString(), new CheckedFile(filePath.toString(), Checked.NO));
			    }
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (CheckedFile checked : myFileMap.values()) {
			MyLogger.appendLog(checked.toString());
		}
		model.addAttribute("fileList", myFileMap.values());
		return "read_logs";
	}

	@RequestMapping(value = "/read_logs/mark", method = RequestMethod.GET)
	public View markFile(@ModelAttribute String file, ModelMap model) {
		CheckedFile checked = myFileMap.get(file);
		checked.setChecked((checked.getChecked() == Checked.YES) ? Checked.NO : Checked.YES);
		return new RedirectView("/read_logs");
	}
	
	@RequestMapping(value = "/read_logs/upload", method = RequestMethod.POST)
	public View uploadContent(@RequestParam MultipartFile logFile,
			ModelMap model) {
		if (!logFile.isEmpty()) {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(logFile.getInputStream()))) {
				if (!Files.exists(Paths.get("./temp/"))) {
					Files.createDirectory(Paths.get("./temp/"));
				}
				try (BufferedWriter writer = new BufferedWriter(
						Files.newBufferedWriter(Paths.get("./temp/" + logFile.getOriginalFilename())))){
					String line = null;
					while ((line = reader.readLine()) != null) {
						writer.append(line);
					}
				} catch (IOException x) {
					myContent = x.getMessage();
				}
				myContent = "Successfully uploaded";
			} catch (IOException x) {
				myContent = x.getMessage();
			}
			finally {
				rabbitService.sendEndSignal();
			}
		} else {
			myContent = "The file is empty or no file was provided.";
		}
		return new RedirectView("/read_logs");
	}
	
	@RequestMapping(value = "/read_logs/process", method = RequestMethod.POST)
	public View readLogContent(@RequestParam MultipartFile logFile,
			ModelMap model) {
		if (!logFile.isEmpty()) {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(logFile.getInputStream()))) {
				//Iterator<String> iter = reader.lines().iterator();
				rabbitService.sendBeginSignal();
				int counter = 1;
				//while (iter.hasNext()) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					rabbitService.queueMessage(new NetStatsParser().correctLine(line));
					if (counter++ % CHUNK_LIMIT == 0) {
						rabbitService.sendChunkEndSignal();
					}
				}
				myContent = "Successfully uploaded";
			} catch (IOException x) {
				myContent = x.getMessage();
			}
			finally {
				rabbitService.sendEndSignal();
			}
		} else {
			myContent = "The file is empty or no file was provided.";
		}
		return new RedirectView("/read_logs");
	}
}
