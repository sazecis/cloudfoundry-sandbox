package hu.evosoft.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CloudMapper {

	private static List<String> mapper = new ArrayList<>();
	
	public static void addItem(String item) {
		mapper.add(item);
	}

	public static void addItems(String... items) {
		mapper.addAll(Arrays.asList(items));
	}
	
	public static List<String> getList() {
		return mapper;
	}
	
}
