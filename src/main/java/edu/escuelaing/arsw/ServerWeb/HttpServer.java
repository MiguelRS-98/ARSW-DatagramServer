package edu.escuelaing.arsw.ServerWeb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class runs an HTPP Server for datagram execution.
 * @author Miguel Angel Rodriguez Siachoque
 * @author Luis Daniel Benavides Navarro
 */
public class HttpServer implements Runnable
{
    /**
     * This method perform the server request process and displays its header, received and request.
     * @param clientSocket Client that wants to run the HTTP server.
     * @throws IOException Exception in the malfunction of some request.
     */
    public void proccessRequest (Socket clientSocket) throws IOException
    {
        BufferedReader in;
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            String method = "";
            String path = "";
            String version = "";
            List<String> headers = new ArrayList<String>();
            while ((inputLine = in.readLine()) != null) {
                if (method.isEmpty()){
                    String[] requestStrings = inputLine.split(" ");
                    method = requestStrings[0];
                    path = requestStrings[1];
                    version = requestStrings[2];
                    System.out.println("Request: " + method + " " + path + " " + version);
                }
                else {
                    System.out.println("Header: " + inputLine);
                    headers.add(inputLine);
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }   
            String responseMsg = createTextResponse(path);
            out.println(responseMsg);
            out.close();
            in.close();
            clientSocket.close();
        }
    }
    /**
     * This method is in charge of reading the content of the files for the HTTP server.
     * @param path The file path to identify and read.
     * @return File content.
     */
    public String createTextResponse (String path)
    {
        String type = "txt/html";
        if (path.endsWith(".css")) {
            type = "text/css";
        }
        else if (path.endsWith(".js")) {
            type = "text/js";
        }
        Path file = Paths.get("./TestHttpServer" + path);
        Charset charset = Charset.forName("UTF-8");
        String outmsg = "";
        try (BufferedReader reader = Files.newBufferedReader(file,charset)) {
            String line = null;
            while ((line = reader.readLine())!= null) {
                System.out.println(line);
                outmsg = outmsg + "\r\n" + line;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            x.printStackTrace();
        } 
        return "HTTP/1.1 200 OK\r\n" + "Content-Type_ text/html\r\n" + "\r\n" + outmsg;
    }
    /**
     * This method Starts the service where the threads can establish their connection with port.
     */
    @Override
    public void run() 
    {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean run = true;
        while (run) {
            try {
                System.out.println("Listo para recibir.");
                clientSocket = serverSocket.accept();
                proccessRequest(clientSocket);
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
                e.printStackTrace();
            }
        }
        try {    
            serverSocket.close();            
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
