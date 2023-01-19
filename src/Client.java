/*
Copyright 2023

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Client {

	private Socket socket;
	private String username;
	private ObjectOutputStream objWriter;
	private ObjectInputStream objReader;

	public Client(Socket socket, String username, ObjectOutputStream w, ObjectInputStream r) {
		this.socket = socket;
		this.username = username;
		this.objWriter = w;
		this.objReader = r;
	}

	// Depending on user's input, sends the appropriate Message to socketStream
	public void sendMessage() throws IOException {
		Scanner scanner = new Scanner(System.in);

		// Single Message with only client's username to be stored in clientManagers arrayList(happens in clientManager constuctor)
		Message messageToSend;
		messageToSend = new Message(username, null);
		writeToSocketStream(messageToSend);

		System.out.println("Welcome to the group chat. Type /help if you need help");

		while (!socket.isClosed()) {
			System.out.println("Type in the chat....");
			String userInput = scanner.nextLine();
			messageToSend = new Message(username, userInput);
			//Menu with the available commands
			if (messageToSend.getContext().startsWith("/")) {
				if (messageToSend.getContext().equalsIgnoreCase("/help")) {
					printHelpGuide();
				} else if (messageToSend.getContext().equalsIgnoreCase("/quit")) {
					writeToSocketStream(messageToSend);
					System.out.println("Successfull log out");
					closeClientConnection(socket);
				} else if (messageToSend.getContext().startsWith("/priv ")) {
					writeToSocketStream(messageToSend);
				} else if (messageToSend.getContext().startsWith("/online")) {
					writeToSocketStream(messageToSend);
				} else if (messageToSend.getContext().startsWith("/inbox")) {
					writeToSocketStream(messageToSend);
				}else if (messageToSend.getContext().startsWith("/showAll")) {
					writeToSocketStream(messageToSend);
				}else if (messageToSend.getContext().startsWith("/Like")) {
					writeToSocketStream(messageToSend);
				}else if (messageToSend.getContext().startsWith("/ShowLikes")) {
					writeToSocketStream(messageToSend);
				}else {
					System.out.println("This is not a valid command");
				}
			} else {
				writeToSocketStream(messageToSend);
			}
		}
		scanner.close();
	}

	//Prints available commands
	public void printHelpGuide() {
		System.out.println("/help : Print help guide.");
		System.out.println("/quit : Log out.");
		System.out.println("/priv UserName 'msg' : Send a private message.");
		System.out.println("/inbox: Print all private messages you have got.");
		System.out.println("/showAll: Prints all messages from the start of the server running");
		System.out.println("/online: Print all online users.");
		System.out.println("/Like 'msgID' : Like a specific message.");
		System.out.println("/ShowLikes 'msgID' : Print all users who liked a specific message.");
		System.out.println();
	}

	//Sends a message to socketStream
	public void writeToSocketStream(Message mess) {
		try {
			objWriter.writeObject(mess);
			objWriter.flush();
		} catch (IOException e) {
			closeClientConnection(socket);
		}
	}

	//Closes socket, reader, writer when a client disconects
	public void closeClientConnection(Socket socket) {
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


	// Reads the profiles from a csv file and adds them in Arraylist arraylist
	public static void readProfileCsv(ArrayList<Profile> profiles, File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] line_ar = line.split(";", 2);
			Profile profile = new Profile(line_ar[0], line_ar[1]);
			profiles.add(profile);
		}
	}

	// Stores the profiles in a csv file
	public static void writeProfileCsv(ArrayList<Profile> profiles, File file) throws IOException {
		BufferedWriter buffWriter = null;
		try {
			buffWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.ISO_8859_1));
			for (Profile profile : profiles) {
				buffWriter.write(profile.getUsername() + ";" + profile.getPassword());
				buffWriter.newLine();
				buffWriter.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			buffWriter.close();
		}
	}

		
	//The menu before user enters group chat
	public static void client_login_menu(Profile userProfile) throws IOException{
		Scanner sc=new Scanner(System.in);
		while(true) {
			System.out.println();
		    System.out.println("Type /login to login to your account");
		    System.out.println("Type /sign up to create a new account");
		    System.out.println("Type /changeName to change your username");
		    System.out.println("Type /changePass to change your password");
		    System.out.println("Type /deleteMyAccount to delete your account");
		    System.out.println("Type /quit to close the application");

		    String answer=sc.nextLine();

		    if (answer.equalsIgnoreCase("/login")) {
	        	System.out.println("Enter your username.");
	            String username = sc.nextLine();
	            if (Objects.equals(username, "back")) {
	            	client_login_menu(userProfile);
	            }
	            System.out.println("Enter your password.");
	            String password = sc.nextLine();
	            if (new Profile(username, password).login(username, password)) {
		            userProfile.setUsername(username);
		            userProfile.setPassword(password);
		            break;
	            }   
		   
		    } else if (answer.equalsIgnoreCase("/sign up")) {
		    	System.out.println("Enter a username!!");
	            String username = sc.nextLine();
	            if (Objects.equals(username, "back")) {
	            	client_login_menu(userProfile);
	            }
	            System.out.println("Enter a password!!");
	            String password = sc.nextLine();
	            if (new Profile(username, password).createAccount(username, password)) {
	            	writeProfileCsv(Profile.profiles, new File("Profiles.csv").getAbsoluteFile());
	                System.out.println("Successfull registaration!");
	            }
		   
		    } else if (answer.equalsIgnoreCase("/changeName")) {
		    	System.out.println("Enter your username");
		        String oldUsername = sc.nextLine();
		        System.out.println("Enter your password");
		        String oldPassword = sc.nextLine();
		        Profile myProfile = new Profile(oldUsername,oldPassword);
		        //VALIDATION 
		       	if(myProfile.login(oldUsername, oldPassword)) { //xrisimopoiw tin log in gia na tsekarw oti ta username kai password einai egkira -> SUCCESSFUL VALIDATION
		        	System.out.println("Enter new username");
		        	String newUsername = sc.nextLine();
		        	if( myProfile.changeUsername(oldUsername, newUsername) ) {
		       			writeProfileCsv(Profile.profiles, new File("Profiles.csv").getAbsoluteFile());
		       			System.out.println("Your new username is "+newUsername);
		       		}
		       	}

		    } else if (answer.equalsIgnoreCase("/changePass")) {	
		        System.out.println("Enter your username");
		       	String oldUsername = sc.nextLine();
		       	System.out.println("Enter your password");
		       	String oldPassword = sc.nextLine();
		       	Profile myProfile = new Profile(oldUsername,oldPassword);
		       	//VALIDATION 
		       	if( myProfile.login(oldUsername, oldPassword)) {
		       		System.out.println("Enter new password");
		       		String newPassword = sc.nextLine();
	        		if( myProfile.changePassword(oldUsername, newPassword) ) {
	        			writeProfileCsv(Profile.profiles, new File("Profiles.csv").getAbsoluteFile());
	        			System.out.println("Your new Password is "+newPassword);
	        		}
	        	}
		        
		    } else if (answer.equalsIgnoreCase("/deleteMyAccount")) {	
		       	System.out.println("Enter your username");
		       	String oldUsername = sc.nextLine();
		       	System.out.println("Enter your password");
		       	String oldPassword = sc.nextLine();
	        	Profile myProfile = new Profile(oldUsername,oldPassword);
	        	
	        	System.out.println("Are you sure you want to delete your account?(YES/NO)");
	        	String confirmation = sc.nextLine();	        	
		       	if(confirmation.equalsIgnoreCase("YES")) {
		       		myProfile.deleteProfile(myProfile);
		       		writeProfileCsv(Profile.profiles, new File("Profiles.csv").getAbsoluteFile());
		       		System.out.println("Your account has been deleted successfully");
		       	} else {
					System.out.println("Your account is still active.");
				}
		        
		    	} else if (answer.equalsIgnoreCase("/quit")) {
		    		System.exit(0);
		    	}else{
		    		System.out.println("Invalid command. Try again");
		    	}
			}
	    }

	public static void main(String[] args) throws IOException {
		try {
			Profile profile=new Profile();
		    readProfileCsv(Profile.profiles, new File("Profiles.csv"));
		    System.out.println("Welcome to our chat!!");
		    client_login_menu(profile);
		
		    Socket socket = new Socket("localHost", 4100);
		
		    ObjectOutputStream w = new ObjectOutputStream((socket.getOutputStream()));
		    ObjectInputStream r = new ObjectInputStream(socket.getInputStream());
		    Client client = new Client(socket, profile.getUsername(), w, r);
		    ClientListener clientListener = new ClientListener(socket, w, r);
		
		    Thread listenThread = new Thread(clientListener);
		    listenThread.start();
	        client.sendMessage();
    	} catch(SocketException e) {
		    System.out.println("SERVER IS DOWN. TRY AGAIN LATER.");
		}
	}
}
	

