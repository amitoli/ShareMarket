package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class WriteCSV
{
	static final String OUTPUT_FILE_PATH = "C:\\Projects\\Work\\mkt\\output\\";

	public static void write(String outputFileName,String [] header,String [] row)
	{
		CSVWriter writer = null;
		outputFileName = OUTPUT_FILE_PATH + outputFileName;
		File file = new File(outputFileName);
		boolean headerRequired = !file.exists();
		try
		{

			writer = new CSVWriter(new FileWriter(outputFileName,true));

			if(headerRequired){
				writer.writeNext(header);
			}

			writer.writeNext(row);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}
}
