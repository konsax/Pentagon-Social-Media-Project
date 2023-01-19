
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class ClientManager implements Runnable {

	private Socket socket;
	private String username;

	private ObjectOutputStream objWriter;
	private ObjectInputStream objReader;
	
	//Data Structures
	//Stores all ClientManager instances to easily broadcasts messages(main purpose)
	private static ArrayList<ClientManager> clientManagers = new ArrayList<>();
	//Stores all messages 
	public static ArrayList<Message> messageslist = new ArrayList<>();

	public static ArrayList<Message> getMessageslist() {
		return messageslist;
	}

	public ClientManager(Socket socket) {
		try {
			this.socket = socket;
			this.objWriter = new ObjectOutputStream(socket.getOutputStream());
			this.objReader = new ObjectInputStream(socket.getInputStream());

			// First Message from socketStream is always client's username
			Message msg = (Message) objReader.readObject();
			this.username = msg.getUsername();
			clientManagers.add(this);
			System.out.println(username + " has connected to the server!");

			msg = new Message(null, "SERVER: " + username + " just entered the chat!");
			post(msg);
		} catch (IOException e) {
			closeClientConnection(socket);
		} catch (ClassNotFoundException e) {
			closeClientConnection(socket);
		}
	}

	// Waits to read a Message from the socketStream and depending its context, calls the appropriate method to send a Message to the socketStream.
	// Runs in a separate thread because of its blocking operation
	@Override
	public void run() {
		Message messagesFromClient;

		while (!socket.isClosed()) {
			try {
				messagesFromClient = (Message) objReader.readObject();

				if (messagesFromClient.getContext().startsWith("/")) {
					if (messagesFromClient.getContext().equalsIgnoreCase("/quit")) {
						closeClientConnection(socket);
					} else if (messagesFromClient.getContext().startsWith("/priv ")) {
						String[] splitter = messagesFromClient.getContext().split(" ", 3);
						if(splitter.length == 3) {
							messagesFromClient.setContext(splitter[2]);
							messagesFromClient.setRecipientID(splitter[1]);
							messagesFromClient.setIndex(messageslist.size());
							messageslist.add(messagesFromClient);
							writeMessagesCsv(messageslist, new File("Messages.csv"));
							privateMsg(messagesFromClient);
						}
					} else if (messagesFromClient.getContext().startsWith("/online")) {
						objWriter.writeObject(new Message(null, "Online Users:"));
						for (ClientManager clientManager : clientManagers) {
							showOnlineUsers(clientManager, username);
						}
					} else if (messagesFromClient.getContext().startsWith("/inbox")) {
						showPrivateMessages(messageslist);
					}else if(messagesFromClient.getContext().startsWith("/showAll")){
						showAllMessages(messageslist);
					}else if(messagesFromClient.getContext().startsWith("/Like ")){
						String[] splitr = messagesFromClient.getContext().split(" ", 2);
						int index= Integer.parseInt(splitr[1]);
						like(messageslist,index);
					}else if(messagesFromClient.getContext().startsWith("/ShowLikes ")){
						String[] splitr = messagesFromClient.getContext().split(" ", 2);
						int index= Integer.parseInt(splitr[1]);
						showLikes(messageslist,index);
					}
				} else {
					messagesFromClient.setIndex(messageslist.size());
					messageslist.add(messagesFromClient);
					writeMessagesCsv(messageslist, new File("Messages.csv"));
					post(messagesFromClient);
				}
			} catch (ClassNotFoundException e) {
				closeClientConnection(socket);
			} catch (IOException e) {
				closeClientConnection(socket);
			}
		}
	}

	// Broadcasts a Message to every other Client
	public void post(Message msg) {
		for (ClientManager manager : clientManagers) {
			try {
				if (!manager.username.equals(this.username)) {
					manager.objWriter.writeObject(msg);
					manager.objWriter.flush();
				}
			} catch (IOException e) {
				closeClientConnection(socket);
			}
		}
	}

	// Broadcasts a Message to a single Client
	public void privateMsg(Message msgPrivate) {
		for (ClientManager clientManager : clientManagers) {
			if (clientManager.username.equals(msgPrivate.getRecipientID())) {
				try {
					clientManager.objWriter.writeObject(msgPrivate);
					clientManager.objWriter.flush();
				} catch (IOException e) {
					closeClientConnection(socket);
				}
			}
		}
	}
	
	//Shows which users are online
	public void showOnlineUsers(ClientManager onlineUsername, String client) {
		for (ClientManager clientManager : clientManagers) {
			if (clientManager.username.equals(client)) {
				try {
					clientManager.objWriter.writeObject(new Message(null, onlineUsername.username));
					clientManager.objWriter.flush();
				} catch (IOException e) {
					closeClientConnection(socket);
				}
			}
		}
	}
	
	//Shows all private messages
	public void showPrivateMessages(ArrayList<Message> messageslist) {
		for (Message message : messageslist) {
			if (message.getRecipientID().equals(this.username)) {
				try {
					//System.out.println(message);
					objWriter.writeObject(message);
				} catch (IOException e) {
					closeClientConnection(socket);
				}
			}
		}
	}
	
	//Shows all messages that have been sent
	public void showAllMessages(ArrayList<Message> messageslist) {
		for (Message message : messageslist) {
			if (message.getRecipientID().equals(this.username) || message.getRecipientID().equals("public")) {
				try {
					//System.out.println(message);
					objWriter.writeObject(message);
				} catch (IOException e) {
					closeClientConnection(socket);
				}
			}
		}
	}

	//Likes a message
	public void like(ArrayList<Message> messageslist,int index){
		for (Message message : messageslist) {
			if (message.getIndex().equals(index)) {
				if(!message.getLikes().contains(username)){
				message.Like(username);}
			}
		}
	}

	//Shows the total amount of likes and users who liked on a message
	public void showLikes(ArrayList<Message> messageslist,int index) throws IOException {
		for (Message message : messageslist) {
			if (message.getIndex().equals(index)) {
				Message mess=new Message(null,"The "+
						message.getLikes().size()+" following users liked this message:" +
						" \n "+ message.getLikes().toString().replace("[","").
						replace("]","").replace(",","\n"));
				objWriter.writeObject(mess);
			}
		}
	}

	//Diavazei ta xaraktiristika twn minimatwn apo to csv kai ta topothetei se ena minima
	//to opoio meta prostithetai sto arraylist me ta minimata tou programmatos
	public static void readMessageCsv(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] line_ar = line.split(";", 6);
			Message message = new Message(line_ar[0], line_ar[3]);
			if(line_ar[2]!=""){message.setRecipientID(line_ar[2]);}
			message.setTime(line_ar[1]);
			message.setIndex(Integer.valueOf(line_ar[4]));
			ArrayList<String> likes=new ArrayList<>();
			String[] like_ar=line_ar[5].split(",");
			String s=like_ar[0].replace("[","@");
			like_ar[0]=s;
			String s1=like_ar[like_ar.length-1].replace("]","");
			like_ar[like_ar.length-1]=s1;
			Collections.addAll(likes, like_ar);
			if(likes.get(0).equals("@")) {
				likes.remove(0);
			} else {
				likes.set(0, likes.get(0).replace("@",""));
			}
			message.setLikes(likes);
			messageslist.add(message);
		}
	}

	//Grafei ola ta minimata tou arraylist me structured morfi se ena arxeio typou csv.
	public static void writeMessagesCsv(ArrayList<Message> messages, File file) throws IOException {
		BufferedWriter buffWriter = null;
		try {
			buffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.ISO_8859_1));
			for (Message message : messages) {
				buffWriter.write(message.getUsername()+";"+message.getTime()+";"+message.getRecipientID()
						+";"+message.getContext()+";"+message.getIndex()+";"+message.getLikes().toString());
				buffWriter.newLine();
				buffWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			buffWriter.close();
		}
	}

	//Broadcasts a message when a client disconnects
	//Closes socket, reader, writer when a client disconects
	public void closeClientConnection(Socket socket) {
		clientManagers.remove(this);
		Message serverMsg = new Message( null,"SERVER: " + username + " has left the chat");
		post(serverMsg);
		System.out.println("SERVER: " + username + " has disconected");
		try {
			if (socket != null) {
				socket.close();
			}
			objReader.close();
			objWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
