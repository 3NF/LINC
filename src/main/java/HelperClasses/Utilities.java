package HelperClasses;


import Models.UploadedAssignment;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * This class contains static methods as helper functions for various operations.
 */
public final class Utilities {

	public static void main(String args[]) {
		unZip("/home/bakuri/Downloads/kanfetebi.zip");
	}


	private static final String subFolderName = "unzipedFiles";

	/**
	 *
	 * This method extracts zip files
	 *
	 * @param fileZip zips location
	 *
	 * Method is written from
	 * <href a="https://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/"
	 */
	public static void unZip(String fileZip) {
		byte[] buffer = new byte[1024];
		String dist = subFolderName + File.separator + new File(fileZip).getName();
		try {

			//create output directory is not exists
			File folder = new File(dist);
			if (!folder.exists()) {
				folder.mkdir();
			}

			//get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {
				String fileName = ze.getName();
				if (!(fileName.endsWith(".txt") || fileName.endsWith(".cpp") || fileName.endsWith(".h"))) {
					ze = zis.getNextEntry();
					continue;
				}

				File newFile = new File(dist + File.separator + fileName);

				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	public static void unzipInputStream(ByteArrayInputStream inStream, UploadedAssignment uploadedAssignment, String actorUserID) throws IOException {
		ZipInputStream zis = new ZipInputStream(inStream);
		ZipEntry entry;
		// while there are entries I process them
		while ((entry = zis.getNextEntry()) != null) {
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// consume all the data from this entry
			int read;
			while ((read = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, read);
			}
			String result = fos.toString("UTF-8");
			uploadedAssignment.addAssignmentFile(new Models.File(entry.getName(), result, actorUserID));
			fos.close();
		}
	}


	public static ByteArrayInputStream convertOutputIntoInputStream(OutputStream outputStream) {
		ByteArrayOutputStream outStream = ((ByteArrayOutputStream) outputStream);
		ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
		return inStream;
	}


	public static String capitalizeString(String source)
	{
		String firstChar = source.substring(0,1);
		if(!firstChar.matches("^[a-zA-Z]+$")) return source;
		return firstChar.toUpperCase() + source.substring(1,source.length());
	}
}



