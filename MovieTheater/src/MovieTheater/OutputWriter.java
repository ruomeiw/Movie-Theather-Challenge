package MovieTheater;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OutputWriter {
	public void writeOutput(LinkedHashMap<String, ArrayList<String>> seatsMap) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("SeatsMap.txt"));
			for (Map.Entry<String, ArrayList<String>> entry : seatsMap.entrySet()) {
				ArrayList<String> seatsResult = entry.getValue();
				String seatsLine = "";
				for (int i = 0; i < seatsResult.size() - 1; i++) {
					seatsLine = seatsLine.concat(seatsResult.get(i)).concat(", ");
				}
				seatsLine = seatsLine.concat(seatsResult.get(seatsResult.size() - 1));
				String line = entry.getKey() + " " + seatsLine;
				writer.write(line + "\n");
			}
			Path path = Paths.get("SeatsMap.txt");
			System.out.println("File Output Successful: " + path.toAbsolutePath());
			writer.close();
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		}
	}
}
