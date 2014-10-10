package hu.evosoft.model.test;

import static org.junit.Assert.assertEquals;
import hu.evosoft.model.AbstractMongoModel;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.LogEntryDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AbstractMongoModelTest {

	@Test
	public void testGetSetValue() {
		AbstractMongoModel model = new DestinationHost();
		model.setValue(100);
		assertEquals(100, model.getValue());
		model = new LogEntryDate();
		model.setValue(200);
		assertEquals(200, model.getValue());
	}

	@Test
	public void testCollectionName() {
		AbstractMongoModel model = new DestinationHost();
		assertEquals(DestinationHost.class.getSimpleName(), model.collectionName());
		model = new LogEntryDate();
		assertEquals(LogEntryDate.class.getSimpleName(), model.collectionName());
	}

	@Test
	public void testCompareTo() {
		AbstractMongoModel dh1 = new DestinationHost("www.alabama.org", 1);
		AbstractMongoModel dh2 = new DestinationHost("www.alabama.org", 3);
		assertEquals(2, dh1.compareTo(dh2));
		assertEquals(-2, dh2.compareTo(dh1));
		AbstractMongoModel led1 = new LogEntryDate(123L, 1);
		AbstractMongoModel led2 = new LogEntryDate(234L, 5);
		assertEquals(4, led1.compareTo(led2));
		assertEquals(-4, led2.compareTo(led1));
	}
	
	@Test
	public void testCompareToWhenSorting() {
		DestinationHost dh1 = new DestinationHost("www.alabama.org", 1111);
		DestinationHost dh2 = new DestinationHost("www.wisconsin.org", 2222);
		DestinationHost dh3 = new DestinationHost("www.wyoming.org", 3333);
		List<DestinationHost> dsList = new ArrayList<>();
		dsList.add(dh3);
		dsList.add(dh1);
		dsList.add(dh2);
		Collections.sort(dsList);
		assertEquals(dh3, dsList.get(0));
		assertEquals(dh1, dsList.get(2));
		Collections.sort(dsList, DestinationHost.MongoModelComparator);
		assertEquals(dh1, dsList.get(0));
		assertEquals(dh2, dsList.get(1));
		LogEntryDate led1 = new LogEntryDate(123L, 1111);
		LogEntryDate led2 = new LogEntryDate(234L, 2222);
		LogEntryDate led3 = new LogEntryDate(345L, 3333);
		List<LogEntryDate> ledList = new ArrayList<>();
		ledList.add(led3);
		ledList.add(led1);
		ledList.add(led2);
		Collections.sort(ledList);
		assertEquals(led3, ledList.get(0));
		assertEquals(led1, ledList.get(2));
		Collections.sort(ledList, LogEntryDate.MongoModelComparator);
		assertEquals(led1, ledList.get(0));
		assertEquals(led2, ledList.get(1));
	}
	
}
