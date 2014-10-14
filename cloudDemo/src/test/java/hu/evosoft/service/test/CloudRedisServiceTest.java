package hu.evosoft.service.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.service.CloudRedisService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

@RunWith(MockitoJUnitRunner.class)
public class CloudRedisServiceTest {

    private static final String MY_KEYS = "MY_KEYS";

	RedisTemplate<String, String> mockRedisTemplate;
    ListOperations<String, String> mockListOps;
    SetOperations<String, String> mockSetOps;
    ValueOperations<String, String> mockValOps;
	CloudRedisService service;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		mockRedisTemplate = mock(RedisTemplate.class);
		mockListOps = mock(ListOperations.class);
		mockSetOps = mock(SetOperations.class);
		mockValOps = mock(ValueOperations.class);
		service = new CloudRedisService(mockRedisTemplate, mockListOps, mockSetOps, mockValOps);
	}
	
	@Test
	public void testAddNetStatInfo() {
		String[] keys = {"abc", "123"};
		service.addNetStatInfo(keys);
		verify(mockSetOps).add(MY_KEYS, "abc", "123");
    	for (String key : keys) {
    		verify(mockListOps).rightPush(key, "1");
    	}
	}
	
	@Test
	public void testClearData() {
		Set<String> keySet = new HashSet<>();
		keySet.add("abc");
		keySet.add("123");
		when(mockSetOps.members(MY_KEYS)).thenReturn(keySet);
		service.clearData();
		for (String key : keySet) {
			verify(mockRedisTemplate).delete(key);
		}
		verify(mockRedisTemplate).delete(MY_KEYS);
	}

	@Test
	public void testListNetStatInfo() {
		Set<String> keySet = new HashSet<>();
		keySet.add("abc");
		keySet.add("123");
		when(mockSetOps.members(MY_KEYS)).thenReturn(keySet);
		String key = "abc";
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("1");
		list.add("1");
		when(mockListOps.size(key)).thenReturn(3L);
		when(mockListOps.range(key, 0, mockListOps.size(key))).thenReturn(list);
		for(String entry : service.listNetStatInfo()) {
			if (entry.startsWith("key")) {
				assertTrue("Key does not contain the corretly formated text", entry.contains("[1, 1, 1]"));
			}
		}
	}
	
	@Test
	public void testPopAllMongoCompatibleVales() {
		long currentTime = System.currentTimeMillis();
		Set<String> keySet = new HashSet<>();
		keySet.add("www.test.org");
		keySet.add(Long.toString(currentTime));
		when(mockSetOps.members(MY_KEYS)).thenReturn(keySet);
		when(mockListOps.size("www.test.org")).thenReturn(3L);
		when(mockListOps.size("2014-10-12 12:23:45.123")).thenReturn(5L);
		for (IMongoModel model : service.popAllMongoCompatibleValues()) {
			System.out.println(model);
			// TODO assert
		}
	}
	
}
