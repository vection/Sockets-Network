
import java.io.*;

public class Main {
	
	
	public static void main(String[] args) {
		String ip = "127.0.0.1";
		int timeout = 100;
		int port = 0;
		int min = 1;
		int max = 1000;
		String line = null;
		 String password = null;
		String path = PortScanner.CustomScan(ip, 20);
		System.out.println("Searching for open ports...");
		try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                port = Integer.parseInt(line);
                break;
            }
		}
		catch(Exception e1) {
			System.out.println("There is no open ports.");
		}
		
		if(port > 1) {
		      try {
			         password = SSH.Findpassword(ip, port, "aviv");
		          } catch (FileNotFoundException e) {
			          e.printStackTrace();
		          }
		      
		      if(password != null) {
		    	  System.err.println("Password found: "+password );
		      }
		      else {
		    	  System.out.println("Password wasn't found");
		      }
		      
		}
		else {
			System.out.println("Port is not good");
		}
		
		
		
		
	}

}
