package com.taufeeq.web.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomLogger {

	private static final Logger accessLogger = Logger.getLogger("access-logger");
	private static final Logger appLogger = Logger.getLogger("application-logger");

	public enum LogLevel {
		DEBUG(Level.FINEST),
		INFO(Level.INFO),
		WARN(Level.WARNING),
		ERROR(Level.SEVERE);

		private final Level julLevel;

		LogLevel(Level julLevel) {
			this.julLevel = julLevel;
		}

		public Level getJulLevel() {
			return julLevel;
		}
	}

	public enum LogType {
		ACCESS, APPLICATION
	}

	private static CustomLogger instance;
	private static boolean isInitialized = false;

	private CustomLogger() {
		initializeLoggers();
	}

	public static CustomLogger getInstance() {
		if (instance == null) {
			instance = new CustomLogger();
		}
		return instance;
	}

	private void initializeLoggers() {
		if (isInitialized) {
			return;
		}
		isInitialized = true;

		String logDirPath = "/home/umar-pt7695/taufeeq/applications/apache-tomcat-9.0.94/logs-MyContacts/";

		try {
			Files.createDirectories(Paths.get(logDirPath));

			String accessLogPattern = Paths.get(logDirPath, "access%g.log").toString();
			int accessLogLimitBytes = 10 * 1024 * 1024; // 10mb
			int accessLogRotationCount = 10;
			FileHandler accessFileHandler = new FileHandler(accessLogPattern, accessLogLimitBytes,
					accessLogRotationCount, true);
			accessFileHandler.setFormatter(new SimpleFormatter());
			accessLogger.addHandler(accessFileHandler);
			accessLogger.setLevel(Level.INFO);

			String appLogPattern = Paths.get(logDirPath, "application%g.log").toString();
			int appLogLimitBytes = 10 * 1024 * 1024; // 10mb
			int appLogRotationCount = 10;
			FileHandler appFileHandler = new FileHandler(appLogPattern, appLogLimitBytes, appLogRotationCount, true);
			appFileHandler.setFormatter(new SimpleFormatter());
			appLogger.addHandler(appFileHandler);
			appLogger.setLevel(Level.INFO);

		} catch (IOException e) {
			System.err.println("Error initializing loggers: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void log(LogLevel level, LogType type, String message) {
		Logger logger = getLoggerByType(type);
		if (logger != null && logger.isLoggable(level.getJulLevel())) {
			logger.log(level.getJulLevel(), "[" + type.name() + "] " + message);
		}
	}

	public void debugAccess(String message) {
		log(LogLevel.DEBUG, LogType.ACCESS, message);
	}

	public void infoAccess(String message) {
		log(LogLevel.INFO, LogType.ACCESS, message);
	}

	public void warnAccess(String message) {
		log(LogLevel.WARN, LogType.ACCESS, message);
	}

	public void errorAccess(String message) {
		log(LogLevel.ERROR, LogType.ACCESS, message);
	}

	public void debugApp(String message) {
		log(LogLevel.DEBUG, LogType.APPLICATION, message);
	}

	public void infoApp(String message) {
		log(LogLevel.INFO, LogType.APPLICATION, message);
	}

	public void warnApp(String message) {
		log(LogLevel.WARN, LogType.APPLICATION, message);
	}

	public void errorApp(String message) {
		log(LogLevel.ERROR, LogType.APPLICATION, message);
	}

	private Logger getLoggerByType(LogType type) {
		if (type == LogType.ACCESS) {
			return accessLogger;
		} else if (type == LogType.APPLICATION) {
			return appLogger;
		}
		return null;
	}

	public void setAccessLogLevel(LogLevel level) {
		accessLogger.setLevel(level.getJulLevel());
	}

	public void setAppLogLevel(LogLevel level) {
		appLogger.setLevel(level.getJulLevel());
	}

}