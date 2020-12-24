package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadCSV
{
	public static String readCSV(String csvFile) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		StringBuffer sb = new StringBuffer();
		String line ="";
		while ((line = reader.readLine()) != null){
			sb.append(line);
		}
		//System.out.println(sb);
		return sb.toString();
	}

}
