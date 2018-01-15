import java.io.*;
import java.net.*;
/**
 * Server - recive UDP packets. - host(dns)
 * @author Aviv
 *
 */
class Server
{
   public static void main(String args[]) throws Exception
      {
         DatagramSocket serverSocket = new DatagramSocket(80);
            byte[] receiveData = new byte[4096];
            byte[] sendData = new byte[4096];
            while(true)
               {
            	System.out.println("Waiting for packet...");
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  write(receiveData);
               }
      }
   public static void write(byte[] data) throws IOException {
	   
	   File downloadFile = new File("D:/Fromftp.txt");
	     OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile));
	     try {
			outputStream2.write(data, 0, data.length);
			outputStream2.flush();
			System.out.println("File from FTP created - "+downloadFile.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	     outputStream2.close();
   }
}