

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author SnakeGnr
 */
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.lang.System;

public class ClientLoginExemplo {
  public static void main(String[] args) throws IOException {
    // HttpTransport used to send login request.
    HttpTransport transport = new NetHttpTransport();
    try {
      // authenticate with ClientLoginExemplo
      ClientLogin authenticator = new ClientLogin();
      
      authenticator.transport = transport;
      // Google service trying to access, e.g., "cl" for calendar.
      authenticator.authTokenType = "cl";
      authenticator.username = "snakegnr77";
      authenticator.password = "atletico1924";
      authenticator.authenticate();
      System.out.println("Authentication succeeded.");
    } catch (HttpResponseException e) {
      // Likely a "403 Forbidden" error.
      System.err.println(e.getStatusMessage());
      throw e;
    }
  }
}