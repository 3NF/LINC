package HelperClasses;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Models.DownloadedAssignment;


/**
 * This class contains static methods as helper functions for various operations.
 */
public final class Utilities {

	public static void unzipInputStream(ByteArrayInputStream inStream, DownloadedAssignment downloadedAssignment, String actorUserID) throws IOException {
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
			downloadedAssignment.addAssignmentFile(new Models.File(entry.getName(), result, actorUserID));
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
		return firstChar.toUpperCase() + source.substring(1);
	}

	public static void sendError (HttpServletRequest request, HttpServletResponse response, int code, String message) throws IOException {
        request.setAttribute("code", code);
        request.setAttribute("message", message);
        response.sendError(code);
    }
}



