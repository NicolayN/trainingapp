package com.vsu.training;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class DBAdapter {
	private static final String CAT_TABLE_CREATE = "create table catTb (id_cat integer primary key "
			+ "autoincrement, cat text);";
	private static final String EX_TABLE_CREATE = "create table exTb (id_cat integer, name text, pic integer, act integer, foreign key(id_cat) references catTb(id_cat));";
	// private static final String PRO_TABLE_CREATE = "create table ";
	private static final String STAT_TABLE_CREATE = "create table statTb (date text, "
			+ "id_cat integer, done integer, skip integer, act integer, foreign key(id_cat) references catTb(id_cat));";
	private static final String DATABASE_NAME = "training";
	private static final int DATABASE_VERSION = 1;
	private final Context context;
	private DatabaseHelper DBHelper;
	private static SQLiteDatabase db;	
	public static Data.Ex nowEx = new Data.Ex("", 0);

	
	public static class ExFromDb {
		private Cursor ex_cur;
		public ExFromDb() {
			ex_cur = db.rawQuery("select exTb.text, exTb.pic, exTb.id_cat from exTb, pcTb, proTb where proTb.act = 1 and exTb.act = 1 and " +
					"proTb.id_cat = pcTb.id_cat and pcTb.id_cat = exTb.id_cat", null);
		}
		public void insert_into_stat(boolean done) { 
			// statistics update
			// parameters: done/not done, id cat
			
			Cursor cur = db.rawQuery(
					"SELECT strftime('%d.%m.%Y', 'now', 'localtime')", null);
			cur.moveToFirst();
			String td = cur.getString(0);
			int id_c = ex_cur.getInt(2);
			// have us this string in stat table already?
			cur = db.rawQuery("select * from statTb where date = " + td
					+ " and id_cat = " + String.valueOf(id_c), null);
			if (cur.moveToFirst()) {
				if (done) {
					db.execSQL("update statTb set done = done + 1 where id_cat = "
							+ String.valueOf(id_c) + "and date = " + td);
				} else {
					db.execSQL("update statTb set done = done + 1 where id_cat = "
							+ String.valueOf(id_c) + "and date = " + td);
				}
				;
			} else if (done) {
				db.execSQL("insert into statTb values(" + td + ", "
						+ String.valueOf(id_c) + ", 1, 0)");
			} else {
				db.execSQL("insert into statTb values(" + td + ", "
						+ String.valueOf(id_c) + ", 0, 1)");
			}
		}
		public boolean getNextEx() { //nch
			if (ex_cur.moveToNext()) {
				nowEx.text = ex_cur.getString(0);
				nowEx.image_id = ex_cur.getInt(1);
				return true;
			}
			return false;	
		}
	
	}
	// DB Adapter constructor
	public DBAdapter(Context ctx) { // ch
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	// DB helper (for create tables)
	private static class DatabaseHelper extends SQLiteOpenHelper { // need to modify
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CAT_TABLE_CREATE);
			db.execSQL(EX_TABLE_CREATE);
			// db.execSQL(MODE_TABLE_CREATE);
			db.execSQL(STAT_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public DBAdapter open() throws SQLException { // ch
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() { // ch
		DBHelper.close();
	}

	
	public void getAllExStat(TableLayout tl) { 
		Cursor cur = db
				.rawQuery(
						"select s.date, c.cat, s.done, s.skip from statTb s, catTb c where s.id_cat = c.id_cat;",
						null);
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		if (cur.moveToFirst()) {
			// print first, while next - print next
			// first row - header

			tr.setLayoutParams(new TableRow.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			TextView tw_date = new TextView(context);
			tw_date.setLayoutParams(lp);
			TextView tw_cat = new TextView(context);
			tw_cat.setLayoutParams(lp);
			TextView tw_done = new TextView(context);
			tw_done.setLayoutParams(lp);
			TextView tw_skip = new TextView(context);
			tw_skip.setLayoutParams(lp);

			tw_date.setText("Дата");
			tr.addView(tw_date);

			tw_cat.setText("Категория");
			tr.addView(tw_cat);

			tw_done.setText("Выполнено");
			tr.addView(tw_done);

			tw_skip.setText("Пропущено");
			tr.addView(tw_skip);

			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			do {
				tr = new TableRow(context);
				tr.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				tw_date = new TextView(context);
				tw_date.setLayoutParams(lp);
				tw_cat = new TextView(context);
				tw_cat.setLayoutParams(lp);
				tw_done = new TextView(context);
				tw_done.setLayoutParams(lp);
				tw_skip = new TextView(context);
				tw_skip.setLayoutParams(lp);
				tw_date.setText(cur.getString(0));
				tr.addView(tw_date);
				tw_cat.setText(cur.getString(1));
				tr.addView(tw_cat);
				tw_done.setText(cur.getString(2));
				tr.addView(tw_done);
				tw_skip.setText(cur.getString(3));
				tr.addView(tw_skip);

				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			} while (cur.moveToNext());
		} else {
			tr.setLayoutParams(lp);
			TextView tw = new TextView(context);
			tw.setLayoutParams(lp);
			tw.setText("Статистика пока не собрана!");
			tr.addView(tw);
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
		cur.close();
		// else print, that statistic not collected
	}

	public void getSomeExStat(TableLayout tl, String cat) { // ch
		
		Cursor cur = db.rawQuery("select date, done, skip from statTb, catTb where statTb.id_cat = catTb.id_cat and cat = \""
			+ cat + "\";", null);
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		if (cur.moveToFirst()) { // ToLast??
			// print first, while next - print next
			// first row - header

			tr.setLayoutParams(new TableRow.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			TextView tw_date = new TextView(context);
			tw_date.setLayoutParams(lp);
			TextView tw_done = new TextView(context);
			tw_done.setLayoutParams(lp);
			TextView tw_skip = new TextView(context);
			tw_skip.setLayoutParams(lp);

			tw_date.setText("Date");
			tr.addView(tw_date);

			tw_done.setText("Done");
			tr.addView(tw_done);

			tw_skip.setText("Skip");
			tr.addView(tw_skip);

			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// tr.removeAllViews();
			do {
				tr = new TableRow(context);
				tr.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				tw_date = new TextView(context);
				tw_date.setLayoutParams(lp);
				tw_done = new TextView(context);
				tw_done.setLayoutParams(lp);
				tw_skip = new TextView(context);
				tw_skip.setLayoutParams(lp);
				tw_date.setText(cur.getString(0));
				tr.addView(tw_date);
				tw_done.setText(cur.getString(1));
				tr.addView(tw_done);
				tw_skip.setText(cur.getString(2));
				tr.addView(tw_skip);

				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				// tr.removeAllViews();
			} while (cur.moveToNext());
		} else {
			tr.setLayoutParams(lp);
			TextView tw = new TextView(context);
			tw.setLayoutParams(lp);
			tw.setText("Statistics is not collected yet!");
			tr.addView(tw);
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
		cur.close();
		// else print, that statistic not collected
	}

/*	public int[] getExIDsByMode() { // nch
			
		// get random set of ex (their ids) according to current profile
		// parameters: number of ex (db)
		// return set of ids + num of ex
		// ====================
		// get active profile id from db
		Cursor cur = db
				.rawQuery("select id_pro from proTb where act = 1", null);
		cur.moveToFirst();
		int id_pro = cur.getInt(0);
		// get number of cat
		cur = db.rawQuery(
				"select count(*) from pcTb where id_pro = "
						+ String.valueOf(id_pro), null);
		cur.moveToFirst();
		int num_cat = cur.getInt(0);
		// get random exs for every cat
		cur = db.rawQuery("select num_ex from setTb", null);
		cur.moveToFirst();
		int num_ex = cur.getInt(0);
		int m = num_ex / num_cat; // number of ex for every cat
		int i = -1, k = 1, m1 = m - 1;
		int[] exs = new int[num_ex + 1];
		exs[0] = num_ex;
		cur = db.rawQuery("select id_cat from pcTb where id_pro = " + String.valueOf(id_pro), null);
		cur.moveToFirst();
		while (k < num_ex + 1) {
			do {
				i++;
				int j = 0;
				Cursor cur1 = db.rawQuery("select id_ex from exTb where id_cat = " + String.valueOf(cur.getInt(0)), null);
				if (cur1.moveToLast()) {
					int r_count = cur1.getPosition();
					
				}
				if (cur1.moveToFirst()) {
					do {
						exs[k] = cur1.getInt(0);
						k++; j++;
					} while ((j < m1) && cur.moveToNext());
				}
				// if we not have (m) exs in that category, we need to add it from other category
				// (on next step)
				cur1.close();
				m1 = (m1 - j) + m;
			} while ((i < num_cat) && cur.moveToNext());
			i++;
		}
		cur.close();
		return exs;
	}

	public Data.Ex getEx(int id_) { // nch
		// get ex by id
		// return text and picture (Data.Ex)
		Cursor cur = db.rawQuery("select text, pic from exTb where id_ex = " + String.valueOf(id_), null);
		cur.moveToFirst();
		cur.close();
		return new Data.Ex(cur.getString(0), cur.getString(1));		
	} */
	
	
	
	@SuppressWarnings("deprecation")
	public void getExForSettAct(TableLayout tl) {
		// add in row text and checkbox?
		Cursor cur = db.rawQuery("select cat, name, act from exTb, catTb where exTb.id_cat = catTb.id_cat;", null);
		//String[] from = new String[] { "" };
		//SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(context, R.layout.row,   );
		
		RelativeLayout.LayoutParams lp_left = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		RelativeLayout.LayoutParams lp_right = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp_right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		LayoutParams lp1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		cur.moveToFirst();
		do {
			TableRow tr = new TableRow(context);
			tr.setLayoutParams(lp1);
			TextView tw = new TextView(context); // ???
			tw.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
			tw.setText(cur.getString(0) + ": " + cur.getString(1));
			CheckBox cb = new CheckBox(context);
			cb.setLayoutParams(lp1);	
			if (cur.getInt(2) == 1) {
				cb.setChecked(true);
			}
			else cb.setChecked(false);
			cb.setGravity(Gravity.RIGHT);
			tr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TableRow t = (TableRow) v;
					TextView tw = (TextView) t.getChildAt(0);
					CheckBox cb = (CheckBox) t.getChildAt(1);
					String[] s = tw.getText().toString().split(" ");
					ContentValues cv = new ContentValues();		    
					if (cb.isChecked()) { cv.put("act", 0);	}
					else { cv.put("act", 1); }
					db.update("exTb", cv, "name = \"" + s[1] + "\"", null);
					cb.setChecked(!cb.isChecked());
				}
			});
			tr.addView(tw);
			tr.addView(cb);
			tl.addView(tr);
		} while (cur.moveToNext());
		cur.close();
	}
	public void getCatForSettAct() {
		
	}
}

