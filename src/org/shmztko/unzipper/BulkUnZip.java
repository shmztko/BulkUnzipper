package org.shmztko.unzipper;

import java.io.File;


public class BulkUnZip {

	private static SimpleLogger LOG = new SimpleLogger(BulkUnZip.class);
	
	private static String HELP_MESSAGE = "Usage\n"
			+ "java -jar unzipper.jar \n"
			+ "arguments 1 : path to bulk unzip target directory \n"
			+ "arguments 2 : destination path to unzip \n";

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			LOG.info(HELP_MESSAGE);
			System.exit(1);
			return;
		}

		File sourceDir = new File(args[0]);
		File destDir = new File(args[1]);
		BulkUnZipper unzipper = new BulkUnZipper(sourceDir, destDir);
		unzipper.unzip();
	}
}
