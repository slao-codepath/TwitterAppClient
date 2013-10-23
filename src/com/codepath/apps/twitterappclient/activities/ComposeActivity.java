package com.codepath.apps.twitterappclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.twitterappclient.R;

public class ComposeActivity extends Activity {
	private static final int MAX_NUM_CHARS = 140;
	private EditText etMessage;
	private TextView tvCharsLeft;
	
	private int charsLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		etMessage = (EditText) findViewById(R.id.etMessage);
		tvCharsLeft = (TextView) findViewById(R.id.tvCharsLeft);
		charsLeft = MAX_NUM_CHARS;
		tvCharsLeft.setText(String.valueOf(charsLeft));
		etMessage.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				charsLeft += before - count;
				String newText = String.valueOf(charsLeft);
				tvCharsLeft.setText(newText);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	public void onTweet(View v) {
		Intent i = new Intent();
		i.putExtra("message", etMessage.getText().toString());
		setResult(0, i);
		finish();
	}
}
