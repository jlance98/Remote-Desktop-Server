package com.amilhaj.RemoteDesktopServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppTest 
{
	
	private Server testServer;
	private ServerSocket mockServerSocket;
	private Socket mockSocket;
	private PipedInputStream inputStream;
	private PipedOutputStream outputStream;
	private BufferedReader in;
	private PrintWriter out;

	@Before
	public void setup() throws IOException {
		testServer = new Server();
		outputStream = new PipedOutputStream();
		inputStream = new PipedInputStream(outputStream);
		
		mockServerSocket = mock(ServerSocket.class);
		mockSocket = mock(Socket.class);
		in = mock(BufferedReader.class);
		out = mock(PrintWriter.class);
	}
	
	@After
	public void tearDown() throws IOException {
		testServer.close();
	}
	
	@Test
	public void serverShouldSetServerSocket() throws IOException {
		System.out.println("- [TEST] serverShouldSetServerSocket()");
		
		testServer.open(6666);
		assertNotNull(testServer.getServerSocket());
	}
	
	@Test
	public void serverShouldSetSocketAndStreams() throws InterruptedException, IOException {
		System.out.println("- [TEST] serverShouldSetSocketAndStreams()");
		testServer.setServerSocket(mockServerSocket);
		when(mockServerSocket.accept()).thenReturn(mockSocket);
		when(mockSocket.getOutputStream()).thenReturn(outputStream);
		when(mockSocket.getInputStream()).thenReturn(inputStream);
		
		testServer.accept();
		assertNotNull(testServer.getSocket());
		assertNotNull(testServer.getPrintWriter());
		assertNotNull(testServer.getBufferedReader());
	}
	
	@Test
	public void serverShouldReceiveMessages() throws InterruptedException, IOException {
		System.out.println("- [TEST] serverShouldReceiveMessages()");
		testServer.setBufferedReader(in);
		String sentMessage = "Successfully received message.";
		when(in.readLine()).thenReturn(sentMessage);
		
		String receivedMessage = testServer.getReceivedMessage();
		assertEquals(sentMessage, receivedMessage);
		verify(in).readLine();
	}
	
	@Test
	public void serverShouldSendMessage() throws InterruptedException {
		System.out.println("- [TEST] serverShouldSendMessage()");
		testServer.setPrintWriter(out);
		String messageToSend = "Successfully sent message.";
		
		testServer.sendMessage(messageToSend);
		verify(out).println(messageToSend);
	}
	
	@Test
	public void serverShouldCloseAfterConnectionLoss() throws InterruptedException, IOException {
		System.out.println("- [TEST] serverShouldCloseAfterConnectionLoss()");
		testServer.setServerSocket(mockServerSocket);
		testServer.setSocket(mockSocket);
		
		testServer.close();
		verify(mockServerSocket).close();
		verify(mockSocket).close();
	}
	
	@Test
	public void serverRunFunctionTest() throws IOException {
		System.out.println("- [TEST] serverRunFunctionTest()");
		Server spyServer = spy(testServer);
		doCallRealMethod().when(spyServer).run();
		
		doAnswer(setServerSocket -> {
	        spyServer.setServerSocket(mockServerSocket);
	        return null;
	    }).when(spyServer).open(6666);
		
		doAnswer(setSocketAndStreams -> {
	        spyServer.setSocket(mockSocket);
	        spyServer.setPrintWriter(out);
	        spyServer.setBufferedReader(in);
	        return null;
	    }).when(spyServer).accept();
		
		when(in.readLine()).thenReturn("Hello", "World", null);
		
		
		spyServer.run();
		verify(spyServer).open(6666);
		verify(spyServer).accept();
		verify(spyServer, times(3)).getReceivedMessage();
		verify(spyServer).sendMessage("Hello");
		verify(spyServer).sendMessage("World");
		verify(spyServer).close();
		
	}
	
}
