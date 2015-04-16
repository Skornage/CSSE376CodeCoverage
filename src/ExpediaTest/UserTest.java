package ExpediaTest;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Expedia.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class UserTest
{	
	private User target;
	private final Date StartDate = new Date(2009, 11, 1);
	private final Date EndDate = new Date(2009, 11, 30);
	

	@Before
	public void TestInitialize()
	{
		target = new User("Bob Dole");
	}
	
	@Test
	public void TestThatUserInitializes()
	{
		Assert.assertEquals("Bob Dole", target.Name);
	}
	
	@Test
	public void TestThatUserHasZeroFrequentFlierMilesOnInit()
	{
		Assert.assertEquals(0, target.FrequentFlierMiles());
	}
	
	@Test
	public void TestThatUserCanBookEverything()
	{
		target.book(new Booking[]{new Flight(StartDate, EndDate, 0), new Hotel(5), new Car(3)});
		Assert.assertEquals(3, target.Bookings.size());
	}
	
	@Test
	public void TestThatUserHasFrequentFlierMilesAfterBooking()
	{
		target.book(new Booking[]{new Flight(StartDate, EndDate, 1), new Hotel(5), new Car(3)});
		Assert.assertTrue(0 < target.FrequentFlierMiles());
		Assert.assertEquals(3, target.Bookings.size());
	}
	
	@Test
	public void TestThatUserCanBookAOnlyFlight()
	{
		target.book(new Booking[]{new Flight(StartDate, EndDate, 0)});
		Assert.assertEquals(1, target.Bookings.size());
	}
	
	@Test
	public void TestThatUserCanBookAHotalAndACar()
	{
		target.book(new Booking[]{new Car(5), new Hotel(5)});
		Assert.assertEquals(2, target.Bookings.size());
	}
	
	@Test
	public void TestThatUserHasCorrectNumberOfFrequentFlyerMilesAfterOneFlight()
	{
		target.book(new Booking[]{new Flight(StartDate, EndDate, 500)});
		Assert.assertEquals(500, target.FrequentFlierMiles());
	}
	
	@Test
	public void TestThatUserTotalCostIsCorrect()
	{
		Flight flight = new Flight(StartDate, EndDate, 500);
		target.book(new Booking[]{flight});	
		Assert.assertEquals(flight.getBasePrice(), target.Price(), 0.01);
	}
	
	@Test
	public void TestThatUserTotalCostIsCorrectWhenMoreThanFlights()
	{
		Car car = new Car(5);
		Flight flight = new Flight(StartDate, EndDate, 500);
		target.book(new Booking[]{flight});	
		target.book(new Booking[]{car});
		Assert.assertEquals(flight.getBasePrice() + car.getBasePrice(), target.Price(), 0.01);
	}
	
	@Test
	public void TestDoubleMiles()
	{
		Car car = new Car(5);
		Flight flight = new Flight(StartDate, EndDate, 10000);
		target.bookWithDoubleMiles(new Booking[]{flight});
		Assert.assertEquals(5000, target.bonusFrequentFlierMiles, 0.01);
	}
	
	@Test
	public void TestDoubleMiles2()
	{
		Car car = new Car(5);
		Flight flight = new Flight(StartDate, EndDate, 1000);
		target.bookWithDoubleMiles(new Booking[]{flight});
		Assert.assertEquals(1000, target.bonusFrequentFlierMiles, 0.01);
	}
	
	@Test
	public void TestThatDiscountInitializes() 
	{
		Discount target = new Discount(0.01, 1);
		ServiceLocator.Instance().AddDiscount(target);
		this.target.book(new Booking[]{new Flight(StartDate, EndDate, 100), new Hotel(5), new Car(3)});
		assertEquals(1024.65,this.target.Price(), 0.01);
	}
	
	@Test
	public void TestGetDiscount1() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Flight aFlight = new Flight(StartDate, EndDate, 100);
		Car aCar = new Car(1);
		Discount d = new Discount(0, 50);
		ServiceLocator.Instance().AddDiscount(d);
		this.target.book(new Booking[]{aFlight, new Hotel(1), aCar});
		assertEquals(835, this.target.Price(), 0.01);
		
		Field discounts = ServiceLocator.class.getDeclaredField("discounts");
		discounts.setAccessible(true);
		discounts.set(ServiceLocator.Instance(), new LinkedList<Discount>());
	}
	
	@Test
	public void TestGetDiscount2() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Discount target = new Discount(0.01, 200);
		ServiceLocator.Instance().AddDiscount(target);
		this.target.book(new Booking[]{new Flight(StartDate, EndDate, 100), new Hotel(5), new Car(3)});
		assertEquals(1035.0,this.target.Price(), 0.01);
		
		Field discounts = ServiceLocator.class.getDeclaredField("discounts");
		discounts.setAccessible(true);
		discounts.set(ServiceLocator.Instance(), new LinkedList<Discount>());
	}
	
	@Test
	public void TestGetDiscount3() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Discount target = new Discount(0.1, 1);
		ServiceLocator.Instance().AddDiscount(target);
		this.target.book(new Booking[]{new Flight(StartDate, EndDate, 100), new Hotel(5), new Car(3)});
		assertEquals(931.5,this.target.Price(), 0.01);
		
		Field discounts = ServiceLocator.class.getDeclaredField("discounts");
		discounts.setAccessible(true);
		discounts.set(ServiceLocator.Instance(), new LinkedList<Discount>());
	}

	
	@After
	public void TearDown()
	{
		target = null; // this is entirely unnecessary.. but I'm just showing a usage of the TearDown method here
	}
}
