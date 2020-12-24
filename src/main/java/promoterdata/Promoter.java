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
	static final boolean USE_DOWNLOADED_CSV = false;
	static final String OUTPUT_FILE_PATH = "C:\\Projects\\Work\\mkt\\output.csv";
	public static void main(String[] args) throws IOException
	{

		Response response = null;
		String yesterday = null;
		//String today = dtf.format(ltoday);
		String today = "";
		if(!USE_DOWNLOADED_CSV){
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
			urlBuilder.addQueryParameter("csv", "true");
			String url = urlBuilder.build().toString();
			System.out.println(url);

			Request request =
					new Request.Builder()
							.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")
							//.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
							.header("Accept", "*/*")
							.header("Host","www.nseindia.com")
							.header("Connection", "keep-alive")
							//.header("Accept-Encoding", "gzip, deflate, br")
							.header("Accept-Language", "en-US,en;q=0.5")
							.header("Upgrade-Insecure-Requests", "1")
							//.header("Referer", "https://www.nseindia.com/companies-listing/corporate-filings-insider-trading")
							.header("TE", "Trailers")
							.header("Cookie", "_ga=GA1.2.92378258.1593367993; RT=\"z=1&dm=nseindia.com&si=ca5a3cc8-0942-408b-8b08-c094b96f3743&ss=kj0ttc8h&sl=1&tt=311&bcn=%2F%2F684fc53d.akstat.io%2F&ld=5b5&nu=ed8553192c8d124be55949cc1b2e99dc&cl=a4q\"; ak_bmsc=9C27E6140B1D6CEA7FE5C2A3D2770DB217394A50C2630000DDB1E25FDC7B6C53~pl7O/RNPWFzMuejAuWDsGzkK0+GqzY79+3hp9x0Lr1bOWe8dNbeZ7bpCLL4vjCjYNbc8Z/JRdgwmgRrBGIVb+5MzvZO+OotJ6aFFvxrTJ633FpQfaJQFrg/Idv+aX1PDG86bOqtmKRwGXTb0C+9HTO2wciUSO/vfR864gwVocROb5rn0fsWWb25OWLOKAf4FybgUjzBAEvwk+cJqjV8y5Y+CnpUpQE7mRbUQip8ohDLD5LDpsEDP7sfQjnKKXpdJ0a; bm_sv=A7FA9E9C31AF2D66639CCA7379FE906B~maL8S5BOm/sBwV6DJmDlyGZtEan2BFFMErg1GU27EL2T2gFaHrT+joMOG96xMUemGLJUwY+d49LgySOqUuE3LRoqgs7gJ/EjBIyN3rVDMQKMaiV6Bi1O4aQvnH0cA/UYaZ5j+EvzMQSsfOuQueVx62imFdryqm0nFtBcVc2MmjY=; nsit=tWtmSs8V0ZgWflgmTz60kngt; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTYwODY5MjIyNCwiZXhwIjoxNjA4Njk1ODI0fQ.NYUnCmidHFXg0NJvw1iaZKWAVXIGJyewpPn9XLNXi3g; bm_mi=A9840D1777A2E60B83CE2C266689A4A4~rjHwkL+FaWbjRuCOc1+OGKhwR/OQUVD6/8D6jLsXE3s6fif2RN7nGXfzdUMhZ7K0Lo69AUH2++ctDQlgaZhQBd+4a43t/lYBQgKat9XOVMxd9KQ6ZuJx07FmUePKPqTOdTLNMLOaOMd1/RI8VyIRF5chKMO7m0Jqh50OByc2oNZbMSI3vW0SlOyIexpsntdlmGc5rPcPOgwtpQqVUV9C9bc6z/X5MYc617EIXbchnOwGBOnHNvhbqi9xhptmV5Ekj/hx2lfKkVLr/ba2DoJGo6RdjSGqpYOFk/z3lEZwZXog/Xl0iJXxfHQGBm6wQLnOs06FocE0gh29BPYxrYC8gA==; _gid=GA1.2.1558642155.1608692230")
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

		EmailUtility emailUtility = new EmailUtility();
		if(USE_DOWNLOADED_CSV){
			emailUtility.send("Promoter Data ",OUTPUT_FILE_PATH,"Promoter Data"+".csv");
		}
		else{
			emailUtility.send("Promoter Data " + yesterday + "-to-" + today,OUTPUT_FILE_PATH,"Promoter Data "+ yesterday + "-to-" + today+".csv");
		}


	}
}
