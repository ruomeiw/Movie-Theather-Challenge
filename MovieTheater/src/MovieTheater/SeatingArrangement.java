package MovieTheater;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SeatingArrangement {
	public static void main(String[] args) {
		Theater movieTheater = new Theater();
		Scanner filePathInput = new Scanner(System.in);
		System.out.println("Please enter the file path of the Reservation file: ");
		String inputFilePath = filePathInput.nextLine();
		filePathInput.close();
		try {
			Scanner fileDataScanner = new Scanner(new File(inputFilePath));
			OutputWriter outputWriter = new OutputWriter();
			while (fileDataScanner.hasNext()) {
				String reservationLine = fileDataScanner.nextLine();
				int feedback = movieTheater.fulfillReservation(reservationLine);
				if (feedback == -2) {
					System.out.println("The number of seats requested in this reservation is invalid: " + reservationLine);
				}
				if (feedback == -1) {
					System.out.println("Seats Unavailable for " + reservationLine);
				}
			}
			outputWriter.writeOutput(movieTheater.outputResults());
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found Error: " + e.getMessage());
		}
	}
}
