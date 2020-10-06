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
import util.SendMail;

public class Promoter
{
	static boolean USE_DOWNLOADED_CSV = true;
	public static void main(String[] args) throws IOException
	{
		String output = "C:\\Projects\\Work\\mkt\\output.csv";
		Response response = null;
		String yesterday = "";
		//String today = dtf.format(ltoday);
		String today = "";
		if(!USE_DOWNLOADED_CSV){
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDateTime ltoday = LocalDateTime.now();
			LocalDateTime lyesterday = LocalDateTime.now().minusDays(1);
			//String yesterday = dtf.format(lyesterday);
			yesterday = "21-09-2020";
			//String today = dtf.format(ltoday);
			today = "27-09-2020";

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
							//.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")
							.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")
							//.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
							.header("Accept", "*/*")
							.header("Connection", "keep-alive")
							//.header("Accept-Encoding", "gzip, deflate, br")
							.header("Accept-Language", "en-US,en;q=0.5")
							.header("Referer", "https://www.nseindia.com/companies-listing/corporate-filings-insider-trading")
							.header("TE", "Trailers")
							.header("Cookie", "_ga=GA1.2.92378258.1593367993; RT=\\\"z=1&dm=nseindia.com&si=cdb302fe-650d-4b53-8058-ff57ebcb7f63&ss=kfbwqgsh&sl=0&tt=0&bcn=%2F%2F684fc53f.akstat.io%2F&ld=4bfhrc&nu=ed8553192c8d124be55949cc1b2e99dc&cl=d0u\\\"; ak_bmsc=9C6367419FDFBD958C4F886940B1E9C0173F6D0460370000620E685F94772C33~plv6W4geajFJePSnZPDli8ba6itEWMWkohVYC6Nj0v307DF67a8SPXH5OjYxlfpKMMEiSWf9o8/ueUnr8qYskT6VQjy1qOTXYza5MQf25Dtmez+htGwbw277V/gtoe65+0DwRmYLKG4mzSRp3gMxD55aWSRO9I49HW9yZW+93v4FwIRJUThUEWYlHDYELSleEG+D53Y3ifz0WVCMvSW/ChVupTyarPEBym+GfvkFc0PH9NOo24B+7ElV9DJtSiB0Om; bm_sv=CEC96D6E28B01C9F75F48780D3F0DB9B~9e6zv9JVooxx7/fTUoHg4Vjhbhq+M48l+CPnqQyZvEE7jvM4HhxsgikSmchWaB4/LP5QQmowN1g7ENO0MrceA1New97+NgLvUaLTpqxC7lHVKqG7Aa6/fblDQb6YbFKXH0U2Y4VR4aXwWP7NghWaBgFyyuB+kR7qPWCLbA2ji9U=; nsit=FCzLlI4c-Go7DrPMSmmFc3Ho; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTYwMDY1NTEyNCwiZXhwIjoxNjAwNjU4NzI0fQ.kl9MWmmk3Awvv4-bUNtleaNX_8hNLnGRnha01CUN1UY; bm_mi=B1030A75B350C117AE86A3E5F4621A9D~hLueSp5xyQ4TiUABpZmgxXX0FZb3Ur9dusyqyNfTfyekVQUm83XVvybV6f7z9RK4XHQTkn9HX08XMoQ0NRPGs4S/Xn0YywAfaFLtgB6qxVVFEKghWAV/jCB5SlKQ5ejt/oASRhdB6SV3+D+7wX7lQs/kLy0WZH+0c7uL5RF5M7lXSe+fXjFTN6aJfbeaa1WKJGOa7ajtt22IkJbu3tcb1PWJC199iUKtD2+PK8cDd8AXHsEDjhj5hDi4McPoa30NkKTyFsGSsa4EDNXWZIm6/IcdXbfgUpssO4/tEVNX1KyBS4OwQP8rE0yWmaTnu5LKUOiTXKhUlQPHmXXK7hywKQ==; _gid=GA1.2.1633423947.1600655131; _gat_UA-143761337-1=1")
							.url(url)
							.build();
			response = client.newCall(request).execute();
			System.out.println(response.body().string());
		}

		String csvFile = "C:\\Projects\\Work\\mkt\\ip.csv";
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

		SendMail sendMail = new SendMail();
		if(USE_DOWNLOADED_CSV){
			sendMail.send("Promoter Data ",output,"Promoter Data"+".csv");
		}
		else{
			sendMail.send("Promoter Data " + yesterday + "-to-" + today,output,"Promoter Data "+ yesterday + "-to-" + today+".csv");
		}


	}
}
