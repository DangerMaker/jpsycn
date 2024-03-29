package com.jpsycn.jixiao.ui;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jpsycn.jixiao.R;
import com.jpsycn.jixiao.adapter.YYAdapter;
import com.jpsycn.jixiao.utils.SysUtils;

public class DetailActivity extends BaseListActivity {

	private List<String> list;
	private Map<String, String> cookies;
	private ProgressDialog progressDialog;
	public YYAdapter mAdapter;
	private String id;
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		preferences=getSharedPreferences("userinfo",Context.MODE_PRIVATE);
		XXAsyncTask task = new XXAsyncTask();
		task.execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			XXAsyncTask task = new XXAsyncTask();
			task.execute();
			return true;
		case R.id.menu_add:
			Intent intent=new Intent(this,AddActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.refresh, menu);
		getSupportMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	private class XXAsyncTask extends AsyncTask<Void, Void, Void> {

		

		@Override
		protected Void doInBackground(Void... params) {
			try {

				cookies = SysUtils.getCookies(preferences.getString("username", ""), preferences.getString("password", ""));
				id = getIntent().getStringExtra("detailId");

				list = SysUtils.getBBSDetail(cookies, "999710", id);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(DetailActivity.this);
			progressDialog.setMessage("正在请求数据,请稍候...");
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			mAdapter = new YYAdapter(DetailActivity.this, list);
			setListAdapter(mAdapter);
		}
	}
}
