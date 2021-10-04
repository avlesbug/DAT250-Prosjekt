package no.hvl.dat250.jpa.basicexample.Poll;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PutRequest {

	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	
	public static void main(String[] args) {

		PollUser pollUser = new PollUser("Ola Normann", "Ola@gmail.com", "passord123");
		
		OkHttpClient client = new OkHttpClient();

		RequestBody body = RequestBody.create(JSON, pollUser.toJson());
		
		Request request = new Request.Builder().url("http://localhost:8080/counters").put(body).build();

		System.out.println(request.toString());

		try (Response response = client.newCall(request).execute()) {
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
