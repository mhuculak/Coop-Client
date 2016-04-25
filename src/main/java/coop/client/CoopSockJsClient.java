package coop.client;

import coop.util.Identity;
import coop.player.PlayerOptions;
import coop.player.PlayerPosition;
import coop.player.PlayerAction;
import coop.map.Position;
import coop.actions.*;

import coop.action.TestObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompFrameHandler;

import org.codehaus.jackson.map.ObjectMapper;

import java.net.URI;
import java.lang.reflect.Type;
import java.io.IOException;
import java.util.*;

public class CoopSockJsClient {

	private SockJsClient sockJsClient;
	private WebSocketStompClient stompClient;
	private WebSocketHttpHeaders headers;
	private URI uri;
	private String url;
	private int port;
	private String userName;
	private String playerName;
	private StompSession stompSession;	

	public CoopSockJsClient(String url) {
		port = 8081;
		this.url = url;
		try {
			uri = new URI(url);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		headers = new WebSocketHttpHeaders();
		List<Transport> transports = new ArrayList<>();
		org.springframework.web.socket.client.WebSocketClient wsClient = new StandardWebSocketClient();
		transports.add(new WebSocketTransport(wsClient));
		RestTemplateXhrTransport xhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		xhrTransport.setRequestHeaders(headers);
		transports.add(xhrTransport);
		sockJsClient = new SockJsClient(transports);
	}

	public void login(Identity i, String loginUrl) {
		userName = i.getUserName();
		StringBuilder password = new StringBuilder(10);
		password.append(i.getPassword());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.execute(loginUrl, HttpMethod.POST, 
				new RequestCallback() {
					@Override
					public void doWithRequest(ClientHttpRequest request) throws IOException {
						MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
						map.add("username", i.getUserName());
						map.add("password", password.toString());
						new FormHttpMessageConverter().write(map, MediaType.APPLICATION_FORM_URLENCODED, request);
					}
				},

				new ResponseExtractor<Object>() {
					@Override
					public Object extractData(ClientHttpResponse response) throws IOException {
						headers.add("Cookie", response.getHeaders().getFirst("Set-Cookie"));
						return null;
					}
				});
	}

	public void connect(GameClient gameClient) {
//		StompSessionHandler handler = new AbstractTestSessionHandler(failure) { // AbstractTestSessionHandler logs some errors...is it important?
		StompSessionHandler handler = new StompSessionHandlerAdapter() {
			@Override
			public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
				stompSession = session;				
				
				session.subscribe("/user/" + userName + "/showOptions", new StompFrameHandler() {
					
					@Override
					public Type getPayloadType(StompHeaders headers) {
						System.out.println("getPayloadType PlayerOptions");
						return PlayerOptions.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						System.out.println("handleFrame PlayerOptions");
						PlayerOptions playerOptions = (PlayerOptions)payload;
						gameClient.showOptions(playerOptions);
					}					
				});

				session.subscribe("/topic/test", new StompFrameHandler() {					
					@Override
					public Type getPayloadType(StompHeaders headers) {
						System.out.println("getPayloadType String");
						return String.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						System.out.println("handleFrame");						
						String testString = (String)payload;
						System.out.println("GOT: " + testString);
						
					}					
				});

				session.subscribe("/user/" + userName + "/test", new StompFrameHandler() {					
					@Override
					public Type getPayloadType(StompHeaders headers) {
						System.out.println("getPayloadType String");
						return String.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						System.out.println("handleFrame");						
						String testString = (String)payload;
						System.out.println("GOT USER SPECIFIC: " + testString);
						
					}					
				});

				session.subscribe("/user/" + userName + "/testObj", new StompFrameHandler() {					
					@Override
					public Type getPayloadType(StompHeaders headers) {
						System.out.println("getPayloadType TestObject");
						return TestObject.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						System.out.println("handleFrame TestObject");					
						TestObject testObject = (TestObject)payload;
						System.out.println("GOT TestObject " + testObject);						
					}					
				});

				session.subscribe("/user/" + userName + "/dest", new StompFrameHandler() {					
					@Override
					public Type getPayloadType(StompHeaders headers) {
						System.out.println("getPayloadType Position");
						return Position.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						System.out.println("handleFrame Position");
//						String json = new String((byte[]) payload);						
						Position dest = (Position)payload;
//						System.out.println("handle frame for TestObject " + json);
						System.out.println("GOT dest " + dest);
						gameClient.setDest(dest);					
					}					
				});

				stompSession.send("/game/startGame", null);
			}
		};
		stompClient = new WebSocketStompClient(  sockJsClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		headers.add("user-name", userName);
		stompClient.connect(url, headers , handler, port);		
	}

	public void disconnect() {
		System.out.println("disconnecting websocket connection");
		stompSession.disconnect();		
	}

	public void quit() {
		System.out.println("sending quit message");
		stompSession.send("/game/quit", playerName);		
	}

	public void sendTest(String testString) {
		stompSession.send("/game/test", testString);
	}

	public void sendUserTest(String testString) {
		stompSession.send("/game/userTest", testString);
	}

	public void sendTestObject(TestObject testObj) {
		System.out.println("sendTestObject" + testObj);
		stompSession.send("/game/testObj", testObj);
	}

	public void sendAction(Action action) {
		String name = (playerName == null) ? userName : playerName;
		PlayerAction playerAction = new PlayerAction(name, action);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
       		 json = mapper.writeValueAsString(playerAction);
        }
        catch(Exception e) {
			e.printStackTrace();
        }
        System.out.println("sendAction: " + json);

        stompSession.send("/game/action", playerAction);
	}

	public void sendMove(PlayerPosition playerPos) {
		stompSession.send("/game/move", playerPos);
	}

	public void sendDestination(PlayerPosition playerPos) {		 
		stompSession.send("/game/dest", playerPos);
	}

	public String getUserName() {
		return userName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}