
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientListener implements Runnable {	

	private Socket socket;
	private ObjectOutputStream objWriter; 
	private ObjectInputStream objReader;
	
	public ClientListener(Socket socket, ObjectOutputStream w, ObjectInputStream r) {
		this.socket = socket;					
		try {
			this.objWriter = w; 
			w.flush();
			this.objReader = r;
		} catch (IOException e) {
			closeClientConnection(socket);
		}
	}

	// Waits to read a Message from socketStream and writes in Client console
	// Runs in a separate thread because of its blocking operation
	@Override
	public void run() {
		while (!socket.isClosed()) {
			try {
				Message msg;

				msg = (Message) objReader.readObject();
				if(msg.getRecipientID().equals("public")) {
					if (msg.getUsername() != null) {
						System.out.println(msg.getIndex()+"| "+msg.getTime()+"| "+msg.getUsername() + ": " + msg.getContext());
					} else {
						System.out.println(msg.getContext());
					}
				}else{
					System.out.println(msg.getIndex()+"| "+msg.getTime()+" DM from "+msg.getUsername() + ": " + msg.getContext());
				}
			}catch (IOException e) {
				closeClientConnection(socket);
			}catch (ClassNotFoundException e) {
				closeClientConnection(socket);
			}
		}
	}
	
	//Closes socket, reader, writer when a client disconects
	public void closeClientConnection(Socket socket) {
		try {
			if(socket!= null) {
				socket.close();
			}
			objReader.close();
			objWriter.close();
		}catch (IOException e) {
			e.printStackTrace();
		}		
	}
}