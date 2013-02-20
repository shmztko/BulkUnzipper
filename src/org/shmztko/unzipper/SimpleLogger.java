package org.shmztko.unzipper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {

	private String loggerName = "";
	
	private boolean debug = false;
	
	
	public SimpleLogger(Class<?> clazz) {
		this.loggerName = clazz.getName();
	}
	
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public void debug(String message) {
		if (debug) {
			out("DEBUG", message);
		}
	}

	public void info(String message) {
		out("INFO", message);
	}

	private void out(String level, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())).append("]");
		sb.append(" ");
		sb.append("[").append(level).append("]");
		sb.append(" ");
		sb.append("[").append(loggerName).append("]");
		sb.append(" ");

		sb.append(message);
		System.out.println(sb.toString());
	}
}
