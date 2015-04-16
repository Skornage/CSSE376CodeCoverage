package ExpediaTest;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Expedia.*;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class FlightTest {
	private Flight targetFlight;
	private final Date StartDate = new Date(2009, 11, 1);
	private final Date EndDate = new Date(2009, 11, 30);
	//private MockRepository mocks;
	
	@Before
	public void TestInitialize()
	{
		//mocks = new MockRepository();
		targetFlight = new Flight(StartDate, EndDate, 0);
	}
	
	@Test
	public void TestThatFlightInitializes()
	{
		Assert.assertNotNull(targetFlight);
	}
	
	@Test
	public void TestThatFlightHasCorrectMilesAfterConstruction()
	{
		Flight target = new Flight(StartDate, EndDate, 500);
		Assert.assertEquals(500, target.Miles);
	}
	
	@Test(expected=RuntimeException.class)
	//[ExpectedException(typeof(InvalidOperationException))]
	public void TestThatFlightThrowsOnInvertedDates()
	{
		new Flight(EndDate, StartDate, 500);
	}
	
	@Test(expected=RuntimeException.class)
	//[ExpectedException(typeof(ArgumentOutOfRangeException))]
	public void TestThatFlightThrowsOnBadMiles()
	{
		new Flight(StartDate, EndDate, -500);
	}
	
	@Test
	public void TestThatFlightHasCorrectBasePriceForSameDayFlight()
	{
		Flight target = new Flight(new Date(), new Date(), 0);
		Assert.assertEquals(200, target.getBasePrice(), 0.001);
	}
	
	@Test
	public void TestThatFlightHasCorrectBasePriceForNextDayFlight()
	{
		Flight target = new Flight(new Date(2015,1,1), new Date(2015,1,2), 0);
		Assert.assertEquals(220, target.getBasePrice(), 0.001);
	}
	
	@Test
	public void TestThatFlightHasCorrectBasePriceForFiveDayFlight()
	{
		Flight target = new Flight(new Date(2015,1,1), new Date(2015,1,6), 0);
		Assert.assertEquals(300, target.getBasePrice(), 0.0001);
	}
	
	@Test
	public void TestThatFlightDoesGetNumberOfPassengers()
	{
		IDatabase mockDB = EasyMock.createMock(IDatabase.class);
		
		List<String> values = new LinkedList<String>();
		for(int i = 0; i < 50; i++)
			values.add("Bob");
		mockDB.Passengers = values;
		
		EasyMock.replay(mockDB);
		
		Flight target = new Flight(new Date(2015,1,1), new Date(2015,1,6), 0);
		
		target.Database = mockDB;
		Assert.assertEquals(50, target.NumberOfPassengers());
	}
	
	@After
	public void TearDown()
	{
		//mocks.VerifyAll();	
	}
}
