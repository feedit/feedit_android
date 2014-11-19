package com.github.feedit.feedit_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// init webview
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsAlert(view, url, message, result);
			}

		});
		// start sync task
		Task task = new Task(webView);
		task.execute(100);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class Task extends AsyncTask<Integer, Integer, String> {
		private WebView webview;
		private String API = "http://xudafeng.com/feedit";
		private String TAG = "feedit";

		public Task(WebView webView) {
			webview = webView;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			StringBuilder builder = new StringBuilder();
			HttpGet myget = new HttpGet(API);
			try {
				HttpResponse response = client.execute(myget);
				int statusCode = response.getStatusLine().getStatusCode();
				Log.i(TAG, "status code: " + statusCode);
				if (statusCode == 200) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent()));

					for (String s = reader.readLine(); s != null; s = reader
							.readLine()) {
						builder.append(s);
					}
					JSONObject jsonObject = new JSONObject(builder.toString());
					Log.i(TAG, jsonObject.toString());
					webview.addJavascriptInterface(jsonObject, "dataFromInterface");
					webview.loadUrl("file:///android_asset/index.html");
				} else {
					Log.i(TAG, "failed");
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}
