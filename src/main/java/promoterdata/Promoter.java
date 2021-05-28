package promoterdata;

import java.io.FileReader;
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
import util.EmailUtility;

public class Promoter
{
	static final boolean USE_DOWNLOADED_CSV = true;
	static final boolean SEND_MAIL = false;
	static String csvFile = "C:\\Projects\\Work\\mkt\\ip.csv";
	static final String OUTPUT_FILE_PATH = "C:\\Projects\\Work\\mkt\\output.csv";
	public static void main(String[] args) throws IOException
	{

		Response response = null;
		String yesterday = null;
		//String today = dtf.format(ltoday);
		String today = "";
		if(!USE_DOWNLOADED_CSV){
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-MM-yyyy");
			LocalDateTime ltoday = LocalDateTime.now();
			LocalDateTime lyesterday = LocalDateTime.now().minusDays(1);
			yesterday = dtf.format(lyesterday);
			//yesterday = "20-10-2020";
			today = dtf.format(ltoday);
			//today = "27-10-2020";

			OkHttpClient client = new OkHttpClient();
			HttpUrl.Builder urlBuilder =
					HttpUrl.parse("https://www.nseindia.com/api/corporates-pit").newBuilder();
			urlBuilder.addQueryParameter("index", "equities");
			urlBuilder.addQueryParameter("from_date", yesterday);
			urlBuilder.addQueryParameter("to_date", today);
			//urlBuilder.addQueryParameter("csv", "true");
			String url = urlBuilder.build().toString();
			System.out.println(url);

			Request request =
					new Request.Builder()
							.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")
							.header("Accept", "*/*")
							.header("Connection", "keep-alive")
							//.header("Accept-Encoding", "gzip, deflate, br")
							.header("Accept-Language", "en-US,en;q=0.5")
							.header("TE", "Trailers")
							.header("Upgrade-Insecure-Requests", "1")
							.header("Cache-Control", "max-age=0")
							.header("Host","www.nseindia.com")
							//.header("Cookie", "_ga=GA1.2.92378258.1593367993; ak_bmsc=E837F6A7373AA6CEDA10F79A37534358ACE82CB7A2310000E374E05FE3C61459~pldeqU0C7HNoDy0WBtacHQkDr37oH/MZYseYpGUVZgpTdkWV1jxw/Y5AmrgTlwVph1rNNNoghijvkKJjWZ2t5VzGlp/OqG5s2bqKNyjo3VRNCl7XjN2h1qolyF1/FBCdZTDeHU4DyXVNltN+JXEj0XxYXtVMY4updBrBJJ3xQLJ2t3hOMT91WKwA3/kNDyGyJKohkRzk0DoPak8FPimFPR1OYN8S+PzIZUUZoSCFSYV2uGG+0TtzMlpLqbZY7RPpJK; bm_sv=25EC1B7465E33423F7D37785A91539EA~md8ENnCf+jyVqwbUUf/ZNt96Fm8zQVDMfFAgRZCw7ulESCAYsGJ3CGxrs41/DLMi8ME2yZKa7vlPpzs1j2oiZwjXfmWZLM0amR5agCO8vDJaFRbcfQNSYZdZL8zxntsoK87Nf5Z0gvWAE+3lCiLRJg0xwlAiilSkq8cJqAlts+o=; nsit=yV8cNn6pNHM8_4BPHh6VSeJY; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTYwODU0NTYwOSwiZXhwIjoxNjA4NTQ5MjA5fQ.yBFSkEHo6-_j6XHX1rG0lNrZCh7omSSJ9gN7A8vchhU; bm_mi=28860EFE12D0A95AD540F37405EAEE5C~KvOs0G+6SJGxgkNByC7IQak5S32698PSNzHhBBEhnIEV+4xIRT/XbNsO48DQayBQ1s2Ff72GKumnXTNLKcW/cbBcThzn+5weFn4qfQhSHZH3WbQ66JfVqICaMYt4s+IqEZgkVZ9pbriLTAu7fs0RWj9cIfOySt2TDL+kgoAXyZRXMyUgxJSMwSrGxxdvhBusJ3HdyVI0A5G0ROHUKGzj7CRH2oflJysuoGFzE9WXcfdq9nGZgoN8LXqeW3y2sl4tt+uGSay0NjQqXBkQfZ9TUWqKwQO5XfkLFAIPKkyAEn8=; RT=\"z=1&dm=nseindia.com&si=ca5a3cc8-0942-408b-8b08-c094b96f3743&ss=kiyeivq4&sl=1&tt=43k&bcn=%2F%2F173e255d.akstat.io%2F&ld=dpk\"; _gid=GA1.2.645798177.1608545619")
							.url(url)
							.build();
			response = client.newCall(request).execute();
			System.out.println(response.body().string());
		}


		CSVReader reader = null;
		Map<String, Long> map = new HashMap<>();
		try {
			if(USE_DOWNLOADED_CSV){
				reader = new CSVReader(new FileReader(csvFile));
			}
			else{
				reader = new CSVReader(response.body().charStream());
			}

			String[] line;
			//List<String[]> list = reader.readAll();
			while ((line = reader.readNext()) != null) {
				//System.out.println(line[4] + " "+ line[10]);

				if(("Promoters".equalsIgnoreCase(line[4]) || "Promoter Group".equalsIgnoreCase(line[4])) && ("Market Purchase".equalsIgnoreCase(line[18]) || "Market Sale".equalsIgnoreCase(line[18]))){
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
		List<Map.Entry<String,Long>> list = new LinkedList<>(map.entrySet());
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
		CSVWriter writer = new CSVWriter(new FileWriter(OUTPUT_FILE_PATH));
		String [] header = {"Stock","Total","Total(in lakhs)","NIFTY 500 ?","FnO Stock ?"};
		writer.writeNext(header);
		for (Map.Entry<String,Long> entry : keySortedMap.entrySet()){
			//System.out.println(entry.getKey() + " " +entry.getValue() );
			String [] row = {entry.getKey(),entry.getValue().toString(),
					String.valueOf(entry.getValue()/100000.0),
					String.valueOf(Nifty500.listOfNifty500.contains(entry.getKey())),
					String.valueOf(FnOStocks.listOfFnOStocks.contains(entry.getKey()))};
			writer.writeNext(row);
		}
		writer.close();

		if(SEND_MAIL){
			EmailUtility emailUtility = new EmailUtility();
			if(USE_DOWNLOADED_CSV){
				emailUtility.send("Promoter Data ",OUTPUT_FILE_PATH,"Promoter Data"+".csv");
			}
			else{
				emailUtility.send("Promoter Data " + yesterday + "-to-" + today,OUTPUT_FILE_PATH,"Promoter Data "+ yesterday + "-to-" + today+".csv");
			}
		}



	}
}
