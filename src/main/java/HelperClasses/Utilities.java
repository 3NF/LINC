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
		return new ByteArrayInputStream(outStream.toByteArray());
	}


	public static String capitalizeString(String source)
	{
		String firstChar = source.substring(0,1);
		if(!firstChar.matches("^[a-zA-Z]+$")) return source;
		return firstChar.toUpperCase() + source.substring(1,source.length());
	}
}



