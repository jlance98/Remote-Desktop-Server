package com.amilhaj.RemoteDesktopServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	public static void main(String[] args) throws IOException {
        Server server=new Server();
        while(true) {
        	server.run();
        }
    }
	
	public void run() throws IOException {
		this.open(6666);
		this.accept();
		String input;
		while ((input = this.getReceivedMessage()) != null) {
			this.sendMessage(input);
		}
		this.close();
	}

	public void open(int port) {
		try {
			this.setServerSocket(new ServerSocket(port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void accept() {	
			try {
				System.out.println("[SERVER] Awaiting connection.");
				this.setSocket(serverSocket.accept());
				this.setPrintWriter(new PrintWriter(clientSocket.getOutputStream(), true));
				this.setBufferedReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
				System.out.println("[SERVER] Connection established.");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public String getReceivedMessage() {
		try {
			String input = in.readLine();
			System.out.println("[SERVER] Message received: " + input);
			return input;
		} catch (IOException e) {
			e.printStackTrace();
			return "Exception occured.";
		}
	}
	
	public boolean sendMessage(String message) {
		out.println(message);
		System.out.println("[SERVER] Message sent: " + message);
		return true;
	}
	
	public void close() throws IOException {
		if (serverSocket != null) {
			serverSocket.close();
		}
		if (clientSocket != null) {
			clientSocket.close();
		}
	}
	
	public boolean isClosed() {
		return clientSocket.isClosed();
	}

	//----------GETTERS & SETTERS-------------
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}
	
	public void setSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public Socket getSocket() {
		return this.clientSocket;
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.out = printWriter;
	}
	
	public PrintWriter getPrintWriter() {
		return this.out;
	}
	
	public void setBufferedReader(BufferedReader bufferedReader) {
		this.in = bufferedReader;
	}
	
	public BufferedReader getBufferedReader() {
		return this.in;
	}

}
