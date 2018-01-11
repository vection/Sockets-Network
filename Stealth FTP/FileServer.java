/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author annaf
 */

import java.net.*;
import java.io.*;

public class FileServer {
    
    
  public static void main(String[] args) throws IOException {
        
    int serverPort = 80; // The port that this server is listening on
    ServerSocket serverSocket = null;  // Server sock that will listen for incoming connections
    Socket clientSocket = null; // socket created by accept
    boolean continueFlag= true; //Boolean that controls whether or not this server is listening
        
    try { 
         //create socket    
         serverSocket = new ServerSocket(serverPort); 
         
             while (continueFlag) {
                 try {
                     System.out.println("Waiting for client ");
                     
                     clientSocket = serverSocket.accept(); // wait for client to connect
                     System.out.println("Accepted connection: " + clientSocket);
                     
                     //send file
                     //File myFile = new File ("c:\\original.txt");
                     File myFile = new File ("original.txt");
                     byte [] mybytearray = new byte [(int)myFile.length()];
                     
                     FileInputStream fis = new FileInputStream(myFile);
                     BufferedInputStream bis = new BufferedInputStream(fis);
                     
                     bis.read(mybytearray, 0, mybytearray.length);
                     
                     OutputStream os = clientSocket.getOutputStream();
                     System.out.println("Sending file");
                     
                     os.write(mybytearray, 0, mybytearray.length);
                     os.flush();
                     
                     clientSocket.close();
                     
                                  
                 } catch (IOException e) {
                        System.err.println("Could not listen this port.");
                        System.exit(1);
                 }
                 
                 
               continueFlag = false;  
             }
             
             serverSocket.close();                
             System.out.println("Server Closed");
             
         } catch (Throwable e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
         }	
    
   }   
}

