package MovieTheater;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovieTheaterTest {
	
	static Theater test1 = new Theater();
	
	/******************* Test Fulfill Reservation ****************/
	@Test
	public void testFulfillReservation() {
		
		assertEquals(-2, test1.fulfillReservation("R001 0"));
		assertEquals(-1, test1.fulfillReservation("R001 500"));
		assertEquals(0,test1.fulfillReservation("R001 7"));
	}
	
	/******************* Test Arrange Seats ****************/
	@Test
	public void testArrangeSeats() {
		assertEquals(0, test1.arrangeSeats("R002", 4));
		assertEquals(0, test1.arrangeSeats("R002", 4));
	}
}
