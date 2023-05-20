package com.amilhaj.RemoteDesktopServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientRunnables {

}

class ClientConnect implements Runnable {

	@SuppressWarnings("resource")
	@Override
	public void run() {
		try {
			new Socket("127.0.0.1", 6666);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

class ClientReceivedMessage implements Runnable {
	
	Socket clientSocket;
	String receivedMessage = "";

	@Override
	public void run() {
		try {
			clientSocket = new Socket("127.0.0.1", 6666);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getReceivedMessage() {
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			receivedMessage = in.readLine();
			return receivedMessage;
		} catch (IOException e) {
			e.printStackTrace();
			return "Exception occured.";
		}
	}
	
}

class ClientSendMessage implements Runnable {
	
	Socket clientSocket;

	@Override
	public void run() {
		try {
			clientSocket = new Socket("127.0.0.1", 6666);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		PrintWriter out;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

class ClientLoseConnection implements Runnable {
	
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;

	@Override
	public void run() {
		try {
			clientSocket = new Socket("127.0.0.1", 6666);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			out.println("Connection confirmed.");
			Thread.sleep(100);
			clientSocket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}