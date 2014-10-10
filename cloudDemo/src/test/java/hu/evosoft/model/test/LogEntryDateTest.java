package hu.evosoft.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import hu.evosoft.model.LogEntryDate;

import java.util.UUID;

import org.junit.Test;

public class LogEntryDateTest {

	private long myTimeStamp = Long.MAX_VALUE;
	private int myValue = Integer.MAX_VALUE;
	
	@Test
	public void testDefaultConstructor() {
		LogEntryDate led = new LogEntryDate();
		assertEquals(UUID.fromString(led.getId()).toString(), led.getId());
		assertEquals(null, led.getTimeStamp());
		assertEquals(1, led.getValue());
	}

	@Test
	public void testConstructorStringInt() {
		LogEntryDate led = new LogEntryDate(myTimeStamp, myValue);
		assertEquals(UUID.fromString(led.getId()).toString(), led.getId());
		assertNotSame(myTimeStamp - 1, (long) led.getTimeStamp());
		assertNotSame(myValue - 1, led.getValue());		
		assertEquals(myTimeStamp, (long) led.getTimeStamp());
		assertEquals(myValue, led.getValue());		
	}

	@Test
	public void testEquals() {
		LogEntryDate led = new LogEntryDate(myTimeStamp, myValue);
		assertFalse("led.equals(new LogEntryDate()) returned false", 
				led.equals(new LogEntryDate()));
		assertTrue("led.equals(new LogEntryDate()) returned true", 
				led.equals(new LogEntryDate(myTimeStamp, myValue)));
	}

	@Test
	public void testMoveIdToContent() {
		long testValue = Long.MAX_VALUE / 2;
		LogEntryDate led = new LogEntryDate(myTimeStamp, myValue);
		led.setId(Long.toString(testValue));
		String id = led.getId();
		led.moveIdToContent();
		String newId = led.getId();
		assertNotSame(id, newId);
		assertEquals(id, Long.toString(led.getTimeStamp()));		
	}
	
	@Test
	public void testGetSetTimeStamp() {
		LogEntryDate led = new LogEntryDate();
		led.setTimeStamp(myTimeStamp);
		led.setValue(myValue);
		assertEquals(myTimeStamp, (long) led.getTimeStamp());
		assertEquals(myValue, led.getValue());				
	}

	@Test
	public void testHashCode() {
		LogEntryDate led = new LogEntryDate(myTimeStamp, myValue);
		assertEquals(Long.valueOf(myTimeStamp).hashCode(), led.hashCode());
	}
	
	@Test
	public void testMapper() {
		assertEquals("classpath:js/mapLogEntryDate.js", new LogEntryDate().mapper());
		assertEquals(new LogEntryDate().mapper(), new LogEntryDate(myTimeStamp, myValue).mapper());		
	}
	
	@Test
	public void testToString() {
		LogEntryDate led1 = new LogEntryDate();
		led1.setTimeStamp(myTimeStamp);
		led1.setValue(myValue);
		LogEntryDate led2 = new LogEntryDate(myTimeStamp, myValue);
		assertNotSame(led1.toString(), led2.toString());
		led2.setId(led1.getId());
		assertEquals(led1.toString(), led2.toString());				
	}

	@Test
	public void testGetTimeStampAsString() {
		/*long timeStamp = System.currentTimeMillis();
		LogEntryDate led = new LogEntryDate(myTimeStamp, myValue);*/
		// TODO
	}

}
