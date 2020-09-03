package promoterdata;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import index_category.FnOStocks;
import index_category.Nifty500;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.SendMail;

public class Promoter
{

	public static void main(String[] args) throws IOException
	{
		String output = "C:\\Projects\\Work\\mkt\\output.csv";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime ltoday = LocalDateTime.now();
		LocalDateTime lyesterday = LocalDateTime.now().minusDays(1);
		String yesterday = dtf.format(lyesterday);
		String today = dtf.format(ltoday);

		OkHttpClient client = new OkHttpClient();
		HttpUrl.Builder urlBuilder =
				HttpUrl.parse("https://www.nseindia.com/api/corporates-pit").newBuilder();
		urlBuilder.addQueryParameter("index", "equities");
		urlBuilder.addQueryParameter("from_date", yesterday);
		urlBuilder.addQueryParameter("to_date", today);
		urlBuilder.addQueryParameter("csv", "true");
		String url = urlBuilder.build().toString();
		System.out.println(url);

		Request request =
				new Request.Builder()
						//.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:79.0) Gecko/20100101 Firefox/79.0")
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")
						.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
						.header("Connection", "keep-alive")
						.url(url)
						.build();
		Response response = client.newCall(request).execute();
		//System.out.println(response.body().string());

		String csvFile = "C:\\Projects\\Work\\mkt\\ip.csv";
		CSVReader reader = null;
		Map<String, Long> map = new HashMap<>();
		try {
			//reader = new CSVReader(new FileReader(csvFile));
			reader = new CSVReader(response.body().charStream());
			String[] line;
			//List<String[]> list = reader.readAll();
			while ((line = reader.readNext()) != null) {
				//System.out.println(line[4] + " "+ line[10]);

				if(("Promoters".equalsIgnoreCase(line[4]) || "promoterdata.Promoter Group".equalsIgnoreCase(line[4])) && ("Market Purchase".equalsIgnoreCase(line[18]) || "Market Sale".equalsIgnoreCase(line[18]))){
					Long amount =0L;
					if("Market Purchase".equalsIgnoreCase(line[18])){
						amount = Long.valueOf(line[10]);
					}
					else if("Market Sale".equalsIgnoreCase(line[18])){
						amount = -Long.valueOf(line[10]);
					}
					if(map.containsKey(line[0])){
						map.put(line[0],amount + map.get(line[0]));
					}
					else{
						map.put(line[0],amount);

					}
				}
				}

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			reader.close();
		}
		//Sort the data
		List<Map.Entry<String,Long>> list = new LinkedList<Map.Entry<String, Long>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Long>>()
		{
			public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2)
			{
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		Map<String, Long> keySortedMap = new LinkedHashMap<String, Long>();
		for(Map.Entry<String,Long> l : list){
			keySortedMap.put(l.getKey(),l.getValue());
		}
		CSVWriter writer = new CSVWriter(new FileWriter(output));
		String [] header = {"Stock","Total","NIFTY 500 ?","FnO Stock ?"};
		writer.writeNext(header);
		for (Map.Entry<String,Long> entry : keySortedMap.entrySet()){
			//System.out.println(entry.getKey() + " " +entry.getValue() );
			String [] row = {entry.getKey(),entry.getValue().toString(),
					String.valueOf(Nifty500.listOfNifty500.contains(entry.getKey())),
					String.valueOf(FnOStocks.listOfFnOStocks.contains(entry.getKey()))};
			writer.writeNext(row);
		}
		writer.close();

		SendMail sendMail = new SendMail();
		sendMail.send("promoterdata.Promoter Data " + yesterday + "-to-" + today,output,"promoterdata.Promoter "+ yesterday + "-to-" + today+".csv");

	}
}
