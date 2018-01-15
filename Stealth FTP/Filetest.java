import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
/**
 * Packet UDP creator with data.
 * @author Aviv
 *
 */
public class Filetest {
    /*
     * This method converts an IPv4 address from a string representation
     * to a byte array.
     * ipAddress: The string representation of an IPv4 address.
     */
    public static byte[] ip4StringToByte(String ipAddress)
    {       
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        byte[] ipByte = ip.getAddress();

        return ipByte;
    }
    /**
     * Lanch packet with data inside
     * @param dnsAddress - IP destination
     * @param port - port destination
     * @param bytesArray - data
     * @throws IOException
     */
    public static void launch(byte[] dnsAddress, int port, byte[] bytesArray) throws IOException
    {
        DatagramSocket socket = null; // Open UDP socket.
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Failed to grab socket for port.");
            System.out.println(e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Port out of range");
            System.out.println(e.getMessage());
        }

        DatagramPacket dPacket = new DatagramPacket(bytesArray, bytesArray.length); // Applying data inside
        try {
            dPacket.setAddress(InetAddress.getByAddress(dnsAddress)); // Setting ip destination
            dPacket.setPort(port); // setting port
        } catch (UnknownHostException e) {
            e.printStackTrace();
            socket.close();
            return;
        }
        
        try {
            socket.send(dPacket); // Send packet.
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
            return;
        }
        socket.close();
    }
}