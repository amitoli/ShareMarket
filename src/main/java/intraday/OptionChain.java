package intraday;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.EmailUtility;
import util.ReadCSV;
import util.WriteCSV;

public class OptionChain
{
	private static final int NO_OF_STRIKE_PRICES_TO_CONSIDER=5;
	private static final String OUTPUT_FILE_NAME = "Option Chain Data.csv";
	private static final String UNDERLYING_INDEX = "BANKNIFTY";
	private static final String INPUT_JSON_FILE_ABSOLUTE_PATH = "C:\\Projects\\Work\\mkt\\options\\temp.json";
	private static final boolean CALL_REST_API = true;


	public void buildOptionChainData() throws IOException
	{
		JSONObject responseBody = null;
		if(CALL_REST_API){
			Response response = null;
			Request request = buildHttpRequest();
			response = httpCall(request);
			try
			{
				responseBody = new JSONObject(response.body().string());
			}

			catch (JSONException e)
			{
				response = httpCall(request);
				System.out.println(response.body().string());
				responseBody = new JSONObject(response.body().string());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		else{
			System.out.println("hello");
			String input = ReadCSV.readCSV(INPUT_JSON_FILE_ABSOLUTE_PATH);
			//System.out.println(input);
			responseBody = new JSONObject(input);
			//System.out.println(responseBody);
		}

		try
		{
			JSONArray records = (JSONArray) responseBody.getJSONObject("filtered").get("data");

			List<JSONObject> callsFromJson = new ArrayList<>();
			List<JSONObject> putsFromJson = new ArrayList<>();

			for (int i = 0; i < records.length() ; i++)
			{
				callsFromJson.add(records.getJSONObject(i).getJSONObject("CE"));
				putsFromJson.add(records.getJSONObject(i).getJSONObject("PE"));
			}
			//System.out.println(putsFromJson);

			List<Options> putOptions = new ArrayList<>();
			List<Options> callOptions = new ArrayList<>();

			callsFromJson.stream().forEach(p->{
				Options options = new Options();
				options.setOptionType(Options.OptionType.CALL);
				options.setChangeInOpenInterest((Integer) p.get("changeinOpenInterest"));
				options.setStrikePrice((Integer) p.get("strikePrice"));
				callOptions.add(options);
			});

			putsFromJson.stream().forEach(p->{
				Options options = new Options();
				options.setOptionType(Options.OptionType.PUT);
				options.setChangeInOpenInterest((Integer) p.get("changeinOpenInterest"));
				options.setStrikePrice((Integer) p.get("strikePrice"));
				putOptions.add(options);
			});

			Double underlyingValue = (Double) putsFromJson.get(0).get("underlyingValue");

			Collections.sort(callOptions,(sp1,sp2)->sp2.getStrikePrice()-sp1.getStrikePrice());
			//Collections.sort(putOptions,(sp1,sp2)->sp1.getStrikePrice()-sp2.getStrikePrice());

			System.out.println("callOptions - "+ callOptions );


			List<Options> filteredCallOptions = Stream
					.concat(
							callOptions.stream()
									.filter(call->call.getStrikePrice()>underlyingValue)
									.sorted((sp1,sp2)->sp1.getStrikePrice()-sp2.getStrikePrice())
									.limit(NO_OF_STRIKE_PRICES_TO_CONSIDER),
							callOptions.stream()
									.filter(call->call.getStrikePrice()<underlyingValue)
									.sorted((sp1,sp2)->sp2.getStrikePrice()-sp1.getStrikePrice())
									.limit(NO_OF_STRIKE_PRICES_TO_CONSIDER)
					)
					.collect(Collectors.toList());

			List<Options> filteredPutOptions = Stream
					.concat(
							putOptions.stream()
									.filter(put->put.getStrikePrice()>underlyingValue)
									.sorted((sp1,sp2)->sp1.getStrikePrice()-sp2.getStrikePrice())
									.limit(NO_OF_STRIKE_PRICES_TO_CONSIDER),
							putOptions.stream()
									.filter(put->put.getStrikePrice()<underlyingValue)
									.sorted((sp1,sp2)->sp2.getStrikePrice()-sp1.getStrikePrice())
									.limit(NO_OF_STRIKE_PRICES_TO_CONSIDER)
					)
					.collect(Collectors.toList());

			Collections.sort(filteredCallOptions,(sp1,sp2)->sp1.getStrikePrice()-sp2.getStrikePrice());
			Collections.sort(filteredPutOptions,(sp1,sp2)->sp1.getStrikePrice()-sp2.getStrikePrice());

			System.out.println(" filteredCallOptions - "+filteredCallOptions);
			System.out.println(" filteredPutOptions - "+filteredPutOptions);

			/*List<Options> filteredCallOptions = callOptions
					.stream()
					.filter(coi->coi.getStrikePrice()<underlyingValue)
					.collect(Collectors.toList());
			List<Options> filteredPutOptions = putOptions
					.stream()
					.filter(poi->poi.getStrikePrice()>underlyingValue)
					.collect(Collectors.toList());*/


			Integer sumOfCE= filteredCallOptions
					.stream()
					.mapToInt(value -> value.getChangeInOpenInterest()).sum();
			Integer sumOfPE= filteredPutOptions
					.stream()
					.mapToInt(value -> value.getChangeInOpenInterest()).sum();

			System.out.println("sumCE - "+sumOfCE);
			System.out.println("sumPE - "+sumOfPE);

			writeInCSV(sumOfCE,sumOfPE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException
	{
		OptionChain optionChain = new OptionChain();
		optionChain.buildOptionChainData();
		//optionChain.sendEmail();
	}

	private Request buildHttpRequest(){
		HttpUrl.Builder urlBuilder =
				HttpUrl.parse("https://www.nseindia.com/api/option-chain-indices").newBuilder();
		urlBuilder.addQueryParameter("symbol", UNDERLYING_INDEX);

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
						//.header("Host","www.nseindia.com")
						//.header("Cookie", "_ga=GA1.2.92378258.1593367993; ak_bmsc=E837F6A7373AA6CEDA10F79A37534358ACE82CB7A2310000E374E05FE3C61459~pldeqU0C7HNoDy0WBtacHQkDr37oH/MZYseYpGUVZgpTdkWV1jxw/Y5AmrgTlwVph1rNNNoghijvkKJjWZ2t5VzGlp/OqG5s2bqKNyjo3VRNCl7XjN2h1qolyF1/FBCdZTDeHU4DyXVNltN+JXEj0XxYXtVMY4updBrBJJ3xQLJ2t3hOMT91WKwA3/kNDyGyJKohkRzk0DoPak8FPimFPR1OYN8S+PzIZUUZoSCFSYV2uGG+0TtzMlpLqbZY7RPpJK; bm_sv=25EC1B7465E33423F7D37785A91539EA~md8ENnCf+jyVqwbUUf/ZNt96Fm8zQVDMfFAgRZCw7ulESCAYsGJ3CGxrs41/DLMi8ME2yZKa7vlPpzs1j2oiZwjXfmWZLM0amR5agCO8vDJaFRbcfQNSYZdZL8zxntsoK87Nf5Z0gvWAE+3lCiLRJg0xwlAiilSkq8cJqAlts+o=; nsit=yV8cNn6pNHM8_4BPHh6VSeJY; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTYwODU0NTYwOSwiZXhwIjoxNjA4NTQ5MjA5fQ.yBFSkEHo6-_j6XHX1rG0lNrZCh7omSSJ9gN7A8vchhU; bm_mi=28860EFE12D0A95AD540F37405EAEE5C~KvOs0G+6SJGxgkNByC7IQak5S32698PSNzHhBBEhnIEV+4xIRT/XbNsO48DQayBQ1s2Ff72GKumnXTNLKcW/cbBcThzn+5weFn4qfQhSHZH3WbQ66JfVqICaMYt4s+IqEZgkVZ9pbriLTAu7fs0RWj9cIfOySt2TDL+kgoAXyZRXMyUgxJSMwSrGxxdvhBusJ3HdyVI0A5G0ROHUKGzj7CRH2oflJysuoGFzE9WXcfdq9nGZgoN8LXqeW3y2sl4tt+uGSay0NjQqXBkQfZ9TUWqKwQO5XfkLFAIPKkyAEn8=; RT=\"z=1&dm=nseindia.com&si=ca5a3cc8-0942-408b-8b08-c094b96f3743&ss=kiyeivq4&sl=1&tt=43k&bcn=%2F%2F173e255d.akstat.io%2F&ld=dpk\"; _gid=GA1.2.645798177.1608545619")
						.url(url)
						.build();
		return request;
	}

	private  Response httpCall(Request request) throws IOException
	{
		OkHttpClient client = new OkHttpClient();
		Response  response = client.newCall(request).execute();
		return response;
	}

	private void writeInCSV(Integer sumOfCE,Integer sumOfPE){
		Integer diff = sumOfPE - sumOfCE;
		String indication;
		if(diff>0){
			indication = "BULLS COMING";
			System.out.println("BULLS COMING");
		}
		else{
			indication = "BEAR CARTEL";
			System.out.println("BEAR CARTEL");
		}
		String [] headers = {"SumOfCE","SumOfPE","Difference","Indication","Timestamp"};
		String [] row = {sumOfCE.toString(),sumOfPE.toString(),diff.toString(),indication,LocalDateTime.now().toString()};
		WriteCSV.write(OUTPUT_FILE_NAME,headers,row);
	}

	private void sendEmail(){
		EmailUtility emailUtility = new EmailUtility();
		emailUtility.send("Option Chain Data "+LocalDateTime.now().toString(),OUTPUT_FILE_NAME,OUTPUT_FILE_NAME);
	}
}
