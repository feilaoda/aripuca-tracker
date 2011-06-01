package com.aripuca.tracker.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;


public class WaypointGpxExportTask extends TrackExportTask {
	
	public WaypointGpxExportTask(Context c) {
		super(c);

		extension = "gpx";

	}
	
	protected void prepareWriter() throws IOException {

		String todayDate = (new SimpleDateFormat("yyyy-MM-dd_HH-mm")).format((new Date()).getTime());
		
		// create file named as track title on sd card
		File outputFolder = new File(myApp.getAppDir() + "/waypoints");

		file = new File(outputFolder, "wp_" + todayDate + "."+extension);

		if (!file.exists()) {
			file.createNewFile();
		}

		// overwrite existing file
		pw = new PrintWriter(new FileWriter(file, false));

	}
	
	protected void prepareCursors() {

		// track cursor
		String sql = "SELECT * FROM waypoints;";
		tpCursor = myApp.getDatabase().rawQuery(sql, null);
		tpCursor.moveToFirst();

	}
	
	protected void writeHeader() {

		String todayDate = (new SimpleDateFormat("yyyy-MM-dd")).format((new Date()).getTime());
		
		// write gpx header
		pw.format("<?xml version=\"1.0\" encoding=\"%s\" standalone=\"yes\"?>\n", Charset.defaultCharset()
				.name());
		pw.println("<gpx");
		pw.println(" version=\"1.1\"");
		pw.println(" creator=\"AripucaTracker for Android\"");
		pw.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		pw.println(" xmlns=\"http://www.topografix.com/GPX/1/1\"");
		pw.print(" xmlns:topografix=\"http://www.topografix.com/GPX/Private/TopoGrafix/0/1\"");
		pw.print(" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 ");
		pw.print("http://www.topografix.com/GPX/1/1/gpx.xsd ");
		pw.print("http://www.topografix.com/GPX/Private/TopoGrafix/0/1 ");
		pw.println("http://www.topografix.com/GPX/Private/TopoGrafix/0/1/topografix.xsd\">");

		pw.println("<time>" + todayDate + "</time>");

	}

	protected void writeTrackPoint() {

		String wpTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(tpCursor.getLong(tpCursor
				.getColumnIndex("time")));

		pw.println("<wpt lat=\"" + tpCursor.getFloat(tpCursor.getColumnIndex("lat")) + "\" lon=\""
				+ tpCursor.getFloat(tpCursor.getColumnIndex("lng")) + "\">");
		pw.println("<ele>" + tpCursor.getFloat(tpCursor.getColumnIndex("elevation")) + "</ele>");
		pw.println("<time>" + wpTime + "</time>");
		pw.println("<name>" + tpCursor.getString(tpCursor.getColumnIndex("title")) + "</name>");
		pw.println("<desc>" + tpCursor.getString(tpCursor.getColumnIndex("descr")) + "</desc>");
		//					pw.println("<type>" +  + "</type>");
		pw.println("</wpt>");
		
	}

	protected void writeFooter() {

		// footer
		pw.println("</gpx>");

	}

}