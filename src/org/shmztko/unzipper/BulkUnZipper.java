package org.shmztko.unzipper;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class BulkUnZipper {
	/** 一括解凍対象のZIPファイルがあるディレクトリ */
	private File baseDir;

	/** 一括解凍後のZIPファイル・解凍後ファイルを配置するディレクトリ */
	private File destDir;
	
	private SimpleLogger LOG = new SimpleLogger(getClass());

	private String encoding = "MS932";
	
	public BulkUnZipper(File baseDir, File destDir) {
		this.baseDir = baseDir;
		this.destDir = destDir;
	}

	class ZipFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			return file.getName().endsWith(".zip");
		}
	}

	public void unzip() {
		long start = System.currentTimeMillis();
		LOG.info("Unzip start");
		try {
			for (File targetFile : baseDir.listFiles(new ZipFileFilter())) {
				if (!targetFile.getName().endsWith("zip")) {
					throw new RuntimeException("Target is not zip file.");
				}

				LOG.info("Unzipping file -> " + targetFile.getName());
				File unzipped = unzip(targetFile, destDir);
				LOG.info("Unzipping file -> " + targetFile.getName() + " done.");
				
				LOG.info("Remaming file to -> " + unzipped.getName() + ".zip");
				targetFile.renameTo(new File(unzipped.getPath() + ".zip"));
			}
		} finally {
			LOG.info("Unzip done -> " + (System.currentTimeMillis() - start) + "[ms]");
		}
	}

	private File unzip(File sourceZip, File destDir) {
		ZipArchiveInputStream archiveInput = null;
		try {
			File unzippedDir = null;
			archiveInput = new ZipArchiveInputStream(new FileInputStream(sourceZip), encoding, true);
			ZipArchiveEntry zipEntry = null;
			while ((zipEntry = archiveInput.getNextZipEntry()) != null) {
				LOG.debug("Unzipping...");
				File zipDestFile = new File(destDir.getPath() + "/" + zipEntry.getName());
				LOG.debug("FROM -> " + sourceZip.getPath() + " TO -> " + zipDestFile.getPath());

				if (unzippedDir == null && !destDir.equals(zipDestFile.getParentFile())) {
					unzippedDir = zipDestFile.getParentFile();
					LOG.debug("unzipped directroy is -> " + zipDestFile.getParentFile());
				}

				if (zipEntry.isDirectory()) {
					// zipEntry でディレクトリかどうか判断しないと正確にわからない＞＜
					LOG.debug("Directory created.");
					zipDestFile.mkdirs();
				} else {
					if (!zipDestFile.getParentFile().exists()) {
						zipDestFile.getParentFile().mkdirs();
						LOG.debug("Parent doesn't exist. auto created. ->" + zipDestFile.getParentFile().getName());
					}

					OutputStream out = null;
					try {
						out = new FileOutputStream(zipDestFile);
						IOUtils.copy(archiveInput, out);
					} finally {
						org.apache.commons.io.IOUtils.closeQuietly(out);
					}
				}
			}
			return unzippedDir;
		} catch (IOException e) {
			throw new RuntimeException("Failed to unzip -> " + sourceZip.getName(), e);
		} finally {
			org.apache.commons.io.IOUtils.closeQuietly(archiveInput);
		}
	}
}
