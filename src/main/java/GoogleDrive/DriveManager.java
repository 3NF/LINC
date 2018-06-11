package GoogleDrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class DriveManager {

    public static final Integer DEFAULT_NUMBER_OF_WORKERS = 5;
    private final static String LAST_JOB = "Last Job For Worker";
    private int nWorkers;
    private static DriveWorker[] workers;
    private Drive mainService;


    private final String APPLICATION_NAME = "LINC Google Drive Manager";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String CREDENTIALS_FOLDER = "drive_credentials"; // Directory to store user credentials.

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials/ folder.
     */
    private final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private final String CLIENT_SECRET_DIR = "client_secret_drive.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If there is no client_secret.
     */

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws NullPointerException, IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(new File(CLIENT_SECRET_DIR));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public DriveManager (int nWorkers) throws IOException, GeneralSecurityException {
        if (nWorkers <= 0) {
            throw new RuntimeException("DriveManager was told to create negative/zero workers, don't fuck with my object!");
        }

        this.nWorkers = nWorkers;
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Build a new authorized API client service.
        this.mainService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Drive Manager")
                .build();

        workers = new DriveWorker[nWorkers];
        for (int i = 0; i < nWorkers; i ++) {
            workers[i] = new DriveWorker(APPLICATION_NAME + " Worker " + (i+1), getCredentials(HTTP_TRANSPORT));
        }
    }

    /**
     * Get files in this folder
     * @param folderID
     */
    public void getFolder (String folderID) {
        try {
            FileList result = mainService.files().list().setQ(String.format("'%s' in parents", folderID))
                    .setPageSize(500)
                    .setFields("nextPageToken, files(id, name, parents, capabilities, mimeType)")
                    .execute();
            List <com.google.api.services.drive.model.File> files = result.getFiles();
            for (int i = 0; i < files.size(); i ++) {
                System.out.println(files.get(i).getId() + " Main thread");
                workers[i%nWorkers].addJob(files.get(i).getId());
            }
        } catch (IOException e) {

        }
    }

    public DriveManager () throws  IOException, GeneralSecurityException {
        this(DEFAULT_NUMBER_OF_WORKERS);
    }

    /**
     * Inserts LAST_JOB Strings in
     * workers lists
     */
    public void finishManager () {
        for (DriveWorker worker:workers) {
            worker.addJob(LAST_JOB);
        }
    }

    public class DriveWorker extends Thread {

        //Name for this worker
        private final String name;
        /*
            Google drive service which sends
            requests to Google Drive API
         */
        private Drive service;

        //ArrayList for storing jobs
        private ArrayList <String> jobs;
        /*
            This Semaphore indicates number of
            jobs available in the ArrayList
         */
        private Semaphore semaphore;


        public DriveWorker(String name, Credential appCredentials) throws IOException, GeneralSecurityException {
            this.name = name;

            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, appCredentials)
                    .setApplicationName(name)
                    .build();

            this.service = service;
            semaphore = new Semaphore(0);
            jobs = new ArrayList<>();

            start();
        }

        /**
         * Add new file id in ArrayList
         * @param fileID
         */
        public synchronized void addJob (String fileID) {
            jobs.add(fileID);
            semaphore.release();
        }

        /**
         * Pick file id in ArrayList
         * @return
         */
        public synchronized String pickJob () {
            String lastJob = jobs.get(jobs.size()-1);
            jobs.remove(jobs.size()-1);

            return lastJob;
        }

        /**
         * Following method runs while user inserts
         * LAST_JOB String
         */
        @Override
        public void run() {
            System.out.println("Google Drive Worker with name " + name + " was started!");
            String fileID = "";
            while (!fileID.equals(LAST_JOB)) {
                try {
                    //Wait while anything is inserted into ArrayList
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*
                    Get file id and process it
                 */
                fileID = pickJob();
                System.out.println("||||||||File " + fileID + " " + name);
                processJob (fileID);
            }

            System.out.println(name + " has stopped working!");
        }

        @Override
        public synchronized void start() {
            super.start();
        }

        /**
         *  Gets content of Google Drive file
         * @param fileID id of Google Drive file
         */
        private void processJob (String fileID) {
            if (fileID.equals(LAST_JOB)) {
                return;
            }

            File driveFolder = new File ("Google Drive/");
            if (!driveFolder.exists()) {
                driveFolder.mkdir();
            }

            System.out.println(name + " is Processing the job!");
            try {
                com.google.api.services.drive.model.File file = service.files().get(fileID).execute();
                System.out.println(name +  " has extracted file info!");
                File outputFile = new File("Google Drive/" + file.getName());
                OutputStream outputStream = new FileOutputStream(outputFile);

                service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
