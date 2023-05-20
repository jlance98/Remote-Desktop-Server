package com.amilhaj.RemoteDesktopServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppTest 
{
	
	private Server testServer;
	private Socket testClient;
	private ServerSocket testServerSocket;
	private Socket testSocket;
	private ServerSocket mockServerSocket;
	private Socket mockSocket;

	@Before
	public void setup() throws IOException {
		testServer = new Server();
		mockServerSocket = mock(ServerSocket.class);
		mockSocket = mock(Socket.class);
		
	}
	
	@After
	public void tearDown() throws IOException {
		testServer.close();
	}
	
	@Test
	public void serverSocketsShouldBeOpened() throws IOException {
		System.out.println("[TEST] serverSocketShouldBeOpened()");
		boolean result = testServer.open(6666);
		assertTrue(result);
	}
	
	@Test
	public void serverSocketsShouldAcceptConnections() throws InterruptedException {
		System.out.println("[TEST] serverSocketsShouldAcceptConnections()");
		testServer.open(6666);
		ClientConnect client = new ClientConnect();
		Thread clientThread = new Thread(client);
		clientThread.start();
		clientThread.join();
		boolean result = testServer.accept();
		assertTrue(result);
	}
	
	@Test
	public void serverShouldSendMessage() throws InterruptedException {
		System.out.println("[TEST] serverSocketsShouldAcceptConnections()");
		testServer.open(6666);
		ClientReceivedMessage client = new ClientReceivedMessage();
		Thread clientThread = new Thread(client);
		clientThread.start();
		testServer.accept();
		clientThread.join();
		testServer.sendMessage("Message successfully sent.");
		String clientReceivedMessage = client.getReceivedMessage();
		assertEquals("Message successfully sent.", clientReceivedMessage);
	}
	
	@Test
	public void serverShouldReceiveMessages() throws InterruptedException, IOException {
		System.out.println("[TEST] serverShouldReceiveMessages()");
		testServer.open(6666);
		ClientSendMessage client = new ClientSendMessage();
		Thread clientThread = new Thread(client);
		clientThread.start();
		testServer.accept();
		clientThread.join();
		client.sendMessage("Successfully received message.");
		String receivedMessage = testServer.getReceivedMessage();
		assertEquals("Successfully received message.", receivedMessage);
	}
	
	@Test
	public void serverShouldCloseAfterConnectionLoss() throws InterruptedException, IOException {
		System.out.println("[TEST] serverShouldCloseAfterConnectionLoss()");
		ClientLoseConnection client = new ClientLoseConnection();
		Thread clientThread = new Thread(client);
		clientThread.start();
		testServer.run();
		clientThread.join();
		boolean result = testServer.isClosed();
		assertTrue(result);
	}
	
}
