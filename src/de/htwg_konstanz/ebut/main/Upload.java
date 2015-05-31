package de.htwg_konstanz.ebut.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/**
 * Uploader
 * 
 * @author tim
 *
 */
public class Upload {

	/**
	 * Receives an XML File and generates an InputStream.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return InputStream Stream of the uploaded file
	 * @throws FileUploadException
	 *             Exception Handling for File Upload
	 * @throws IOException
	 *             Exception Handling for IO Exceptions
	 */
	public InputStream upload(final HttpServletRequest request)
			throws FileUploadException, IOException {
		// Check Variable to check if it is a File Uploade
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			// List of all uploaded Files
			List files = upload.parseRequest(request);
			// Returns the uploaded File
			Iterator iter = files.iterator();
			FileItem element = (FileItem) iter.next();
			String fileName = element.getName();
			String extension = FilenameUtils.getExtension(element.getName());
			// check if file extension is xml is it not it will be aborted
			extension = extension.toLowerCase();
			if (extension.equals("xml")) {
				System.out.println("Filename: " + fileName);
				// Converts the File into an Input Stream
				InputStream is;
				is = element.getInputStream();
				return is;
			}
		}
		return null;
	}
}
