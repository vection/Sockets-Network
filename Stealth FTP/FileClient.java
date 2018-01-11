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

public class FileClient {
     public static void main(String[] args)throws IOException{
     int filesize = 6022386; //filesize temporary hardcoded 
     
     long start = System.currentTimeMillis();
     int bytesRead;
     int current = 0;
     
     Socket socket = null;       //Socket object for communicating
     
     try {
            socket = new Socket("127.0.0.1", 80);   //establish the socket connection between the client and the server
            System.out.println("Connecting... ");
            
            //receive file
            byte [] mybytearray = new byte [filesize];
            InputStream is = socket.getInputStream();
            
            FileOutputStream fos = new FileOutputStream ("copy_original.txt");
            
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;
            
            do{
              bytesRead =  is.read(mybytearray, current, (mybytearray.length-current)); 
              if (bytesRead >=0) current +=bytesRead;
            }while (bytesRead > -1);
            
            bos.write(mybytearray, 0, current);
            bos.flush();
            
            long end = System.currentTimeMillis();
            System.out.println("Time for copping file: " + (end-start) + " ms");
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host" + "127.0.0.1");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to host" + "127.0.0.1");
            //System.exit(1);
        }
      socket.close();
     }   
}
