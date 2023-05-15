package MovieTheater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Theater {
	
	// Initiate global variables that could be accessed throughout the class
	static final int ROWS = 10;
	static final int SEATS_PER_ROW = 20;
	static int seatsTotal = ROWS * SEATS_PER_ROW;
	static final int BUFFER_SEATS = 3;
	static final int BUFFER_ROW = 1;
	static int[] seatsCount = new int[ROWS];
	static boolean[][] seatsAvailability = new boolean[ROWS][SEATS_PER_ROW];
	LinkedHashMap<String, ArrayList<String>> seatsMap = new LinkedHashMap<>();
	
	
	// Fill the seatsCount array with the seats count per row
	// Initialize the boolean array seatsCount
	// Marking all the seats as available for now
	public Theater() {
		for (int i = 0; i < ROWS; i++) {
			seatsCount[i] = SEATS_PER_ROW;
			for (int j = 0; j < SEATS_PER_ROW; j++) {
				seatsAvailability[i][j] = true;
			}
		}
	}
	
	public int fulfillReservation(String reservation) {
		String[] reservationData = reservation.split(" ");
		String reservationID = reservationData[0];
		int numberOfSeats = Integer.valueOf(reservationData[1]);
		// If the reservation has requested a valid number of seats
		if (numberOfSeats > 0) {
			// If we have empty seats still
			if (numberOfSeats <= seatsTotal) {
				// If the requested seats has more than 20 people
				if (numberOfSeats > SEATS_PER_ROW) {
					int validIndicator = 0;
					while (numberOfSeats > SEATS_PER_ROW) {
						validIndicator = arrangeSeats(reservationID, numberOfSeats);
						numberOfSeats -= 20;
					}
					return validIndicator;
				}
				return arrangeSeats(reservationID, numberOfSeats);
			} else
				return -1;
		}
		return -2;
	}

	public int arrangeSeats(String reservationID, int numberOfSeats) {
		// Create a new int variable to store numberOfSeats
		// To avoid modifying the original value
		int seatsToArrange = numberOfSeats;
		List<String> confirmedSeats = new ArrayList<>();
		// If we still have empty seats
		if (numberOfSeats <= seatsTotal) {
			for (int row = ROWS - 1; row > 0; row--) {
				if (seatsCount[row] >= numberOfSeats) {
					int[] openSeats = arrangeNextSeat(numberOfSeats, row);
					// openSeats[0] would be the row number
					if (openSeats[0] != -1) {
						int endSeat = openSeats[1] + numberOfSeats - 1;
						// Mark the seats from the start to the end seat as taken
						Arrays.fill(seatsAvailability[openSeats[0]], openSeats[1], endSeat + 1, false);
						markBuffer(openSeats[0], openSeats[1], numberOfSeats, endSeat);
						// Update variables
						seatsCount[openSeats[0]] = seatsCount[openSeats[0]] - numberOfSeats;
						seatsTotal -= numberOfSeats;
						
						if (!seatsMap.containsKey(reservationID)) {
							seatsMap.put(reservationID, new ArrayList<>());
						//	seatsMap.get(reservationID).add((char) (openSeats[0] + 65) + String.valueOf(counter + openSeats[1] + 1));
						}
						
						int counter = 0;
						while (counter < seatsToArrange) {
							// Have the row number + 65 - convert the number to match the alphabetic value according to ASCII
							seatsMap.get(reservationID).add((char) (openSeats[0] + 65) + String.valueOf(counter + openSeats[1] + 1));
							counter++;
						}
					// return 0 as the validIndicator - if it is 0 then it means the current reservation is valid when 
					// the this method is called in the Driver code
					return 0;
					
					} else {
						// If the openSeats[0] is not -1, it means the guests cannot sit next to each other
						int otherRow = ROWS - 1;
						
						while (seatsToArrange > 0 && otherRow >= 0) {
							// If this row has more than 1 empty seat
							if (seatsCount[otherRow] > 1) {
								for (int seatPointer = 0; seatPointer < SEATS_PER_ROW; seatPointer++) {
									// If this seat pointed at is available, then we select it and change the value to false
									if (seatsAvailability[otherRow][seatPointer]) {
										seatsAvailability[otherRow][seatPointer] = false;
										confirmedSeats.add(otherRow + " " + seatPointer);
										if (!seatsMap.containsKey(reservationID)) {
											seatsMap.put(reservationID, new ArrayList<>());
											seatsMap.get(reservationID).add((char) (otherRow + 65) + String.valueOf(seatPointer + 1));
										} else {
											seatsMap.get(reservationID).add((char) (otherRow + 65) + String.valueOf(seatPointer + 1));
										}
										seatsTotal--;
										seatsCount[otherRow]--;
										seatsToArrange--;
									}
								}
							}
							otherRow--;
						}
						enforceBuffer(confirmedSeats);
					}
				}
			}
			return 0;
		}
		return -1;
	}

	public void markBuffer(int row, int col, int numberOfSeats, int endSeat) {
		for (int i = 1; i <= BUFFER_ROW; i++) {
			if (row - i > 0) {
				for (int socialDistance = 0; socialDistance < numberOfSeats; socialDistance++) {
					// Mark the seat as taken
					seatsAvailability[row - i][col + socialDistance] = false;
					seatsCount[row - i]--;
					seatsTotal--;
				}
			}
			
			if (i + row < ROWS - 1) {
				for (int socialDistance = 0; socialDistance < numberOfSeats; socialDistance++) {
					// Mark the seat as taken
					seatsAvailability[row + i][col + socialDistance] = false;
					// Update the empty seats in this row
					seatsCount[row + i]--;
					// Update the empty seats total count
					seatsTotal--;
				}
			}
		}
		
		for (int j = 1; j <= BUFFER_SEATS; j++) {
			if (j + endSeat <= SEATS_PER_ROW - 1) {
				// Mark the seat as taken
				seatsAvailability[row][endSeat + j] = false;
				// Update the empty seats in this row
				seatsCount[row]--;
				// Update the empty seats total count
				seatsTotal--;
			}
			
			if (col - j >= 0 && seatsAvailability[row][col - j]) {
				// Mark the seat as taken
				seatsAvailability[row][col - j] = false;
				// Update the empty seats in this row
				seatsCount[row]--;
				// Update the empty seats total count
				seatsTotal--;
			}
			
		}
		
	}

	public void enforceBuffer(List<String> confirmedSeats) {
		for (String s : confirmedSeats) {
			String[] seats = s.split(" ");
			int seatRow = Integer.valueOf(seats[0]);
			int seatCol = Integer.valueOf(seats[1]);
			
			int rowCounter = 1;
			while (rowCounter < BUFFER_ROW && seatRow - rowCounter < ROWS) {
				int upperRow = seatRow + rowCounter;
				int lowerRow = seatRow - rowCounter;
				if (upperRow < ROWS - 1 && seatsAvailability[upperRow][seatCol])
					// Mark the seat as taken
					seatsAvailability[upperRow][seatCol] = false;
				if (lowerRow > 0 && seatsAvailability[lowerRow][seatCol])
					// Mark the seat as taken
					seatsAvailability[lowerRow][seatCol] = false;
				
				rowCounter++;
			}
			
			int colCounter = 1;
			while (colCounter < BUFFER_SEATS && seatCol - colCounter < SEATS_PER_ROW) {
				int leftSeat = seatCol + colCounter;
				int rightSeat = seatCol - colCounter;
				if (rightSeat < SEATS_PER_ROW && !seatsAvailability[seatRow][rightSeat]) 
					// Mark the seat as taken
					seatsAvailability[seatRow][rightSeat] = false;
				if (leftSeat < SEATS_PER_ROW && !seatsAvailability[seatRow][leftSeat])
					// Mark the seat as taken
					seatsAvailability[seatRow][leftSeat] = false;
				colCounter++;
			}
		}
		
	}

	public int[] arrangeNextSeat(int numberOfSeats, int row) {
		int[] res = {-1, -1};
		
		for (int i = row; i >= 0; i--) {
			// See if the current row has enough empty seats for the current request
			if (seatsCount[i] > numberOfSeats) {
				int seatsToArrange = numberOfSeats;
				int counter = 0;
				
				while (seatsToArrange > 0) {
					// If someone has claimed the seat, change the unclaimedSeats back to its initial value
					if (!seatsAvailability[i][counter]) {
						seatsToArrange = numberOfSeats;
					} else {
						seatsToArrange--;
					}
					if (counter != SEATS_PER_ROW) {
						counter++;
					} else {
						break;
					}
				}
				// If all seats requested have been arranged successfully
				// Store the row number at the first index
				// Store the seat number at the second index
				if (seatsToArrange == 0) {
					res[0] = i;
					// If the counter is 20 and the number of seats is 4
					// Then the seat number will be 16 and number of seats will be 3
					// Then the next seat number will be 17
					res[1] = counter - numberOfSeats;
					return res;
				}
			}
		}
		// If no seat is available
		return res;
	}
	
	public LinkedHashMap<String, ArrayList<String>> outputResults() {
		return seatsMap;
	}
	
	public int getSeatsTotal() {
		return seatsTotal;
	}
}
 