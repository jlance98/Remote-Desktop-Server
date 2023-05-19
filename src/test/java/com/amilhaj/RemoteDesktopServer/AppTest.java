package com.amilhaj.RemoteDesktopServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppTest 
{
	
	private Server testServer;

	@Before
	public void setup() {
		testServer = new Server();
	}
	
	@After
	public void tearDown() throws IOException {
		testServer.close();
	}
	
	@Test
	public void serverSocketsShouldBeOpened() throws IOException {
		boolean result = testServer.open(6666);
		assertTrue(result);
	}
	
	@Test
	public void serverSocketsShouldAcceptConnections() throws InterruptedException {
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
	public void serverShouldReceiveMessages() throws InterruptedException {
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
		ClientLoseConnection client = new ClientLoseConnection();
		Thread clientThread = new Thread(client);
		clientThread.start();
		testServer.run();
		clientThread.join();
		boolean result = testServer.isClosed();
		assertTrue(result);
	}
	
}
