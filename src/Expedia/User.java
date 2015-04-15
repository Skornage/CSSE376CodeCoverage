package Expedia;

import java.util.LinkedList;
import java.util.List;

public class User {
	public String Name;
	public List<Booking> Bookings;
	public int bonusFrequentFlierMiles;
	public User(String aName)
	{
		Name = aName;
		Bookings = new LinkedList<Booking>();
		bonusFrequentFlierMiles = 0;
	}
	
	public int FrequentFlierMiles()
	{
			int result = 0;
			for(Booking booking : Bookings)
			{
				if(booking instanceof Flight)
				{
					result += ((Flight)booking).Miles;
				}
			}
			
			return result;
		
	}
	
	private double GetDiscount()
	{
		double result = 1.0;
		for(Discount discount : ServiceLocator.Instance().AvailableDiscounts())
		{	
			if(1.0-discount.ReductionPercent < result && discount.FrequentFlyerMilesCost <= FrequentFlierMiles())
			{
				result = 1.0 - discount.ReductionPercent;
			}
		}
		return result;
	}
	
	public double Price()
	{
		double result = 0.0;
			for(Booking booking : Bookings)
			{
				result += booking.getBasePrice();	
			}
			return result *	GetDiscount();
	}
	
	public void book(Booking[] bookings)
	{
		for(Booking booking : bookings)
		{
			Bookings.add(booking);
			
			if(booking instanceof Flight)
			{
				ServiceLocator.Instance().RemoveFlight((Flight)booking);	
			}
			
			if(booking instanceof Car)
			{
                ServiceLocator.Instance().RemoveCar((Car)booking);	
			}
		}
	}
	
	public void bookWithDoubleMiles(Booking[] bookings)
	{
	    book(bookings);
	    for(Booking booking : bookings)
	    {
	        if (booking.getMiles() > 5000)
	        {
	            // flight rules say you can't earn more than 5000 double miles for each flight
	            bonusFrequentFlierMiles += 5000;
	        } else {
	        	bonusFrequentFlierMiles = booking.getMiles();
	        }
	    }
	}
}
