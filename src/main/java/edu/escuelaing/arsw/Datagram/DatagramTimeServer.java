package edu.escuelaing.arsw.Datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class This class establishes a server that gives the current time to the client every 5 seconds.
 * @author Miguel Angel Rodriguez Siachoque
 * @author Luis Daniel Benavides Navarro
 */
public class DatagramTimeServer 
{
    DatagramSocket socket;
    /**
     * This constructor method where the socket for the server.
     */
    public DatagramTimeServer() 
    {
        try {
            socket = new DatagramSocket(4445);
        } catch (SocketException ex) {
            Logger.getLogger(DatagramTimeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This method is used to start the server service.
     */
    public void startServer() 
    {
        byte[] buf = new byte[256];
        Thread thread = new Thread();
        while (true) {
            try {
                thread.sleep(5000);
                packetDatagram(buf);
            } catch (InterruptedException e) {
		e.printStackTrace();
            }
        }
    }
    /**
     * This method receives and sends the current date and time in diagram.
     * @param buf A string bytes to convert the datagram
     */
    public void packetDatagram (byte[] buf)
    {
        try {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String dString = new Date().toString();
            buf = dString.getBytes();
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(DatagramTimeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This main method that creates the server and gives the instruction to start.
     * @param args Customer indication.
     */
    public static void main(String[] args) {
        DatagramTimeServer ds = new DatagramTimeServer();
        ds.startServer();
    }   
}
