package hu.evosoft.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class NetStatsReader {

	public void formatFile(Path in, Path out) {
		try (BufferedWriter writer = Files.newBufferedWriter(out)) {
			writer.append(readFileContent(in));
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}
	
	public String readFileContent(Path path) {
		try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
			return bufferedRead(reader);
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		return null;
	}

	public String readFromInputStream(InputStream stream) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			return bufferedRead(reader);
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		return null;
	}
	
	private String bufferedRead(BufferedReader reader) throws IOException{
		StringBuilder strBuilder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			strBuilder.append(replaceUnwantedSemicolonsWithComa(line));
			strBuilder.append("\n");
		}
		return strBuilder.toString();		
	}
	
	public String replaceUnwantedSemicolonsWithComa(String line) {
		Matcher matcher = Pattern.compile("\\(([^\\)]+)").matcher(line);
		int pos = -1;
		while (matcher.find(pos + 1)) {
			pos = matcher.start();
			String match = matcher.group(0) + ")";
			if (match.contains(";")) {
				String replacer = match.replace(";", ",");
				line = line.replace(match, replacer);
			}
		}

		return line;
	}

}
