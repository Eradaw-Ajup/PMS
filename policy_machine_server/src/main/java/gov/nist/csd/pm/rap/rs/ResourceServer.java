package gov.nist.csd.pm.rap.rs;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class ResourceServer {

	private static Drive drive;

	private static final String APPLICATION_NAME = "Google Drive as resource server";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String CREDENTIAL_PATH = "D:\\NCSC\\policy_machine_server\\src\\main\\resources";

	// Directory to store user credentials for this application.
	private static final java.io.File CREDENTIALS_FOLDER = new java.io.File(CREDENTIAL_PATH, "credentials");

	private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";

	/**
	 *  Global instance of the scopes required by this quickstart.
	 * If modifying these scopes, delete your previously saved credentials/ folder.
	*/
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);

		if (!clientSecretFilePath.exists()) {
			throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME //
					+ " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
		}

		// Load client secrets.
		InputStream in = new FileInputStream(clientSecretFilePath);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
											.Builder(HTTP_TRANSPORT, JSON_FACTORY,clientSecrets, SCOPES)
											.setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
											.setAccessType("offline").build();

		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	public static Drive getDriveInstance() throws GeneralSecurityException, IOException {
		if (null == drive) {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

			// 3: Read client_secret.json file & create Credential object.
			Credential credential = getCredentials(HTTP_TRANSPORT);

			// 5: Create Google Drive Service.
			drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
					.setApplicationName(APPLICATION_NAME).build();
		}
		return drive;
	}

	public static List<File> getFiles() throws IOException, GeneralSecurityException {
		drive = getDriveInstance();
		FileList result = drive.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
		List<File> files = result.getFiles();
		if (files == null || files.isEmpty()) {
			System.out.println("No files found.");
		} else {
			System.out.println("Files:");
			for (File file : files) {
				System.out.printf("%s (%s)\n", file.getName(), file.getId());
			}
		}
		return files;
	}

	public static String upload(java.io.File fData) throws IOException, GeneralSecurityException {

		// Upload file photo.jpg on drive.
		File fileMetadata = new File();
		fileMetadata.setName("class_diagram.jpg");

		// File's content.
		// Specify media type and file-path for file.
		FileContent mediaContent = new FileContent("image/jpeg", fData);
		try {
			drive = getDriveInstance();
			File file = drive.files().create(fileMetadata, mediaContent).setFields("id").execute();
			String fileID = file.getId();
			System.out.println("File ID: " + fileID);

			return fileID;
		} catch (GoogleJsonResponseException e) {
			System.err.println("Unable to upload file: " + e.getDetails());
			throw e;
		}
	}

	public static ByteArrayOutputStream downloadFile(String realFileId) throws IOException, GeneralSecurityException {
		drive = getDriveInstance();
		try {
			OutputStream outputStream = new ByteArrayOutputStream();

			drive.files().get(realFileId).executeMediaAndDownloadTo(outputStream);
			return (ByteArrayOutputStream) outputStream;
		} catch (GoogleJsonResponseException e) {
			System.err.println("Unable to move file: " + e.getDetails());
			throw e;
		}
	}

}
