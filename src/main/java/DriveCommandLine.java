/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class DriveCommandLine {
//adicione aqui o CLIENT_ID que nós criamos 

    private static String CLIENT_ID = "544454124567-7514gcpfs1eipk7ed8kq5c0jasa7puie.apps.googleusercontent.com";
//adicione aqui o CLIENT_SECRET que nós criamos
    private static String CLIENT_SECRET = "client_secret.json";
//a REDIRECT_URI vai ser a mesma sempre (provavelmente) 
    private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    public static void main(String[] args) throws IOException {
      
        
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

//gera um código de autorização 
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online").setApprovalPrompt("auto").build();

        String urlAuthorization = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();

// abrir a uri automaticamente 
        try {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI(urlAuthorization);
            desktop.browse(uri);
        } catch (Exception ex) {
            System.err.println("Erro ao abrir página");
            ex.printStackTrace();
            
        } //aqui ele lê o código que retorna do site
        BufferedReader br = new BufferedReader( new InputStreamReader(System.in ));
        String code = br.readLine();
        
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response); 

        //Cria um nova autorização do API client
        Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
        
         // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
             .setPageSize(10)
             .setFields("nextPageToken, files(id, name)")
             .execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
        
        
        
    }
}//fim da classe DriveCommandLine
