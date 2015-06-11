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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity implements OnClickListener {
	private WebView webView;
	private static final String API = "http://xudafeng.com/feedit/api?type=get&unread=true";
	private static final String TAG = "feedit";
	private Button rightButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		getRemoteDate();
	}

	public void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		rightButton = (Button) findViewById(R.id.button_refresh);
		rightButton.setOnClickListener(this);
		// init webview
		webView = (WebView) findViewById(R.id.webview);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
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

	public void getRemoteDate() {
		// start sync task
		Task task = new Task();
		task.execute(100);
	}

	class Task extends AsyncTask<Integer, Integer, String> {
		ProgressBar processbar = (ProgressBar) findViewById(R.id.loading);

		public Task() {
		}

		@Override
		protected String doInBackground(Integer... params) {
			HttpClient client = new DefaultHttpClient();
			StringBuilder builder = new StringBuilder();
			HttpGet myget = new HttpGet(API);
			try {
				HttpResponse response = client.execute(myget);
				int statusCode = response.getStatusLine().getStatusCode();
				Log.i(TAG, "status code: " + statusCode);
				if (statusCode == 200) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));

					for (String s = reader.readLine(); s != null; s = reader
							.readLine()) {
						builder.append(s);
					}
					JSONObject jsonObject = new JSONObject(builder.toString());
					Log.i(TAG, jsonObject.toString());
					webView.addJavascriptInterface(jsonObject,
							"dataFromInterface");
					webView.loadUrl("file:///android_asset/index.html");
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

		@Override
		protected void onPostExecute(String result) {
			processbar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_refresh:
			getRemoteDate();
			break;
		}
	}
}
