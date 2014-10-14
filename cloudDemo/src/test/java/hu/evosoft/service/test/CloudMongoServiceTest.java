package hu.evosoft.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterEntity;
import hu.evosoft.logger.CounterType;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.model.LogEntryDate;
import hu.evosoft.model.MongoModelList;
import hu.evosoft.service.CloudMongoService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;

import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class CloudMongoServiceTest {

	MongoTemplate mockMongoTemplate;
	CloudMongoService service;

	@Before
	public void setUp() throws Exception {
		mockMongoTemplate = mock(MongoTemplate.class);
		service = new CloudMongoService(mockMongoTemplate);
	}

	@Test
	public void testAddDocumentWithDestinationHost() {
		when(
				mockMongoTemplate.collectionExists(DestinationHost.class
						.getSimpleName())).thenReturn(true);
		DestinationHost ds = new DestinationHost("abc", 1234);
		String id = ds.getId();
		service.addDocument(ds);
		verify(mockMongoTemplate).collectionExists(
				DestinationHost.class.getSimpleName());
		verify(mockMongoTemplate, never()).createCollection(
				DestinationHost.class.getSimpleName());
		verify(mockMongoTemplate).insert(ds, ds.collectionName());
		assertNotEquals(id, ds.getId());
	}

	@Test
	public void testAddDocumentWithLogEntryData() {
		when(
				mockMongoTemplate.collectionExists(LogEntryDate.class
						.getSimpleName())).thenReturn(false);
		LogEntryDate led = new LogEntryDate(123L, 1234);
		String id = led.getId();
		service.addDocument(led);
		verify(mockMongoTemplate).collectionExists(
				LogEntryDate.class.getSimpleName());
		verify(mockMongoTemplate).createCollection(
				LogEntryDate.class.getSimpleName());
		verify(mockMongoTemplate).insert(led, led.collectionName());
		assertNotEquals(id, led.getId());
	}

	@Test
	public void testAddCounter() {
		when(
				mockMongoTemplate.collectionExists(CounterEntity.class
						.getSimpleName())).thenReturn(false);
		CounterEntity ce = new CounterEntity();
		service.addCounter(ce);
		verify(mockMongoTemplate).collectionExists(ce.getClass());
		verify(mockMongoTemplate).createCollection(ce.getClass());
		verify(mockMongoTemplate).insert(ce);
	}

	@Test
	public void testClearAllDocuments() {
		service.clearAllDocuments();
		for (IMongoModel type : MongoModelList.getModelSet()) {
			verify(mockMongoTemplate).dropCollection(type.collectionName());
		}
	}

	@Test
	public void testClearDocuments() {
		for (IMongoModel type : MongoModelList.getModelSet()) {
			service.clearDocuments(type.getClass());
			verify(mockMongoTemplate).dropCollection(type.getClass());
		}
	}

	@Test
	public void testDeleteDocument() {
		DestinationHost dh = new DestinationHost();
		service.deleteDocument(dh);
		verify(mockMongoTemplate).remove(dh, dh.collectionName());
	}

	@Test
	public void testListDocuments() {
		List<DestinationHost> list = new ArrayList<>();
		list.add(new DestinationHost("abcd", 1234));
		list.add(new DestinationHost("wxyz", 5678));
		Class<DestinationHost> type = DestinationHost.class;
		when(mockMongoTemplate.findAll(type, type.getSimpleName())).thenReturn(
				list);
		assertEquals(list.get(0), service.listDocuments(type).get(0));
		verify(mockMongoTemplate).findAll(type, type.getSimpleName());
		assertEquals(list.size(), service.listDocuments(type).size());
	}

	@Test
	public void testPerformanceCounters() {
		List<CounterEntity> list = new ArrayList<>();
		list.add(new CounterEntity(CounterCategory.MONGO_MR, CounterType.START,
				"abc", 123L));
		list.add(new CounterEntity(CounterCategory.MONGO_MR, CounterType.END,
				"abc", 123L));
		Class<CounterEntity> type = CounterEntity.class;
		when(mockMongoTemplate.findAll(type)).thenReturn(list);
		assertEquals(list.get(0), service.listPerformanceCounters().get(0));
		verify(mockMongoTemplate).findAll(type);
		assertEquals(list.size(), service.listPerformanceCounters().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public <T> void testMapReduce() {
		List<IMongoModel> list = new ArrayList<>();
		list.add((IMongoModel) new DestinationHost("abcd", 1234));
		list.add((IMongoModel) new DestinationHost("wxyz", 5678));

		IMongoModel model = MongoModelList.getModelSet().get(0);
		MapReduceResults<IMongoModel> mockMapMongoResults = new MapReduceResults<IMongoModel>(
				list, mock(DBObject.class));
		when((MapReduceResults<IMongoModel>) mockMongoTemplate.mapReduce(
						model.collectionName(), model.mapper(),
						"classpath:js/reduce.js", model.getClass()))
				.thenReturn(mockMapMongoResults);
		verify(mockMongoTemplate, never()).insert(list.get(0),
				list.get(0).collectionName());
		service.mapReduce(model.getClass(), model.mapper());
		verify(mockMongoTemplate).insert(list.get(0),
				list.get(0).collectionName());
	}

}
