package hu.evosoft.model.test;

import static org.junit.Assert.*;

import java.util.UUID;

import hu.evosoft.model.DestinationHost;

import org.junit.Test;

public class DestinationHostTest {

	private String myName = "www.siemens.com";
	private int myValue = Integer.MAX_VALUE;

	@Test
	public void testDefaultConstructor() {
		DestinationHost ds = new DestinationHost();
		assertEquals(UUID.fromString(ds.getId()).toString(), ds.getId());
		assertEquals(null, ds.getName());
		assertEquals(1, ds.getValue());
	}

	@Test
	public void testConstructorStringInt() {
		DestinationHost ds = new DestinationHost(myName, myValue);
		assertEquals(UUID.fromString(ds.getId()).toString(), ds.getId());
		assertNotSame(myName + "@", ds.getName());
		assertNotSame(myValue - 1, ds.getValue());		
		assertEquals(myName, ds.getName());
		assertEquals(myValue, ds.getValue());		
	}
	
	@Test
	public void testEquals() {
		DestinationHost ds = new DestinationHost(myName, myValue);
		assertFalse("ds.equals(new DestinationHost()) returned false", 
				ds.equals(new DestinationHost()));
		assertTrue("ds.equals(new DestinationHost()) returned true", 
				ds.equals(new DestinationHost(myName, myValue)));
	}
	
	@Test
	public void testMoveIdToContent() {
		DestinationHost ds = new DestinationHost(myName, myValue);
		String id = ds.getId();
		ds.moveIdToContent();
		String newId = ds.getId();
		assertNotSame(id, newId);
		assertEquals(id, ds.getName());		
	}
	
	@Test
	public void testGetSetName() {
		DestinationHost ds = new DestinationHost();
		ds.setName(myName);
		ds.setValue(myValue);
		assertEquals(myName, ds.getName());
		assertEquals(myValue, ds.getValue());				
	}
	
	@Test
	public void testHashCode() {
		DestinationHost ds = new DestinationHost(myName, myValue);
		assertEquals(myName.hashCode(), ds.hashCode());
	}

	@Test
	public void testMapper() {
		assertEquals("classpath:js/mapDestinationHost.js", new DestinationHost().mapper());
		assertEquals(new DestinationHost().mapper(), new DestinationHost(myName, myValue).mapper());		
	}
	
	@Test
	public void testToString() {
		DestinationHost ds1 = new DestinationHost();
		ds1.setName(myName);
		ds1.setValue(myValue);
		DestinationHost ds2 = new DestinationHost(myName, myValue);
		assertNotSame(ds1.toString(), ds2.toString());
		ds2.setId(ds1.getId());
		assertEquals(ds1.toString(), ds2.toString());		
		
	}
	
	
}
