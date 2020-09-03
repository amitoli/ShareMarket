package intraday;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class DailyCPR
{
	public static void main(String[] args) throws IOException
	{
		String csvFile = "C:\\Projects\\Work\\mkt\\daily.csv";

		CSVReader reader = null;
		String csv = "C:\\Projects\\Work\\mkt\\cpr.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(csv));
		Map<String, DayData> map = new HashMap<>();
		try {
			reader = new CSVReader(new FileReader(csvFile));
			String[] line;
			line = reader.readNext();
			while ((line = reader.readNext()) != null) {
				float high = Float.parseFloat(line[3].replaceAll(",", ""));
				float low = Float.parseFloat(line[4].replaceAll(",", ""));
				float close = Float.parseFloat(line[5].replaceAll(",", ""));
				DayData dayData = new DayData();
				float pivot = (high + low + close )/3;
				float bc = (high + low)/2;
				float tc = (pivot - bc) + pivot;
				float width = (Math.abs(tc - bc) / pivot) * 100;
				String [] row = {line[0],String.valueOf(width)};
				writer.writeNext(row);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			writer.close();
			reader.close();
		}
	}
}
