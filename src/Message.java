import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Message implements Serializable {
    private String username;
    private String time;
    private String context;
    private String recipientID;
    private Integer index; //primary key

    //Data structure
    //Stores all users who liked a message
    private ArrayList<String> likes;

    public Message(){}
    //constructor for "public" messages
    public Message(String userID,String context){
        this.context=context;
        this.username=userID;
        this.recipientID="public";
        this.time=LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")).toString();
        this.likes = new ArrayList<String>(); //initializes the arraylist
    }

    //constructor for "private" messages
    public Message(String userID,String context,String recipientID){
        this.username=userID;
        this.context=context;
        this.recipientID=recipientID;
        this.time=LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")).toString();
        this.likes = new ArrayList<String>(); 
    }

    //Getters
    public String getUsername() {
        return username;
    }

    public String getContext() {
        return context;
    }

    public String getTime() {
        return time;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public Integer getIndex() {
        return index;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void Like(String User_who_liked) {
        likes.add(User_who_liked);
    }
    
    //Setters

    public void setTime(String time) {
        this.time = time;
    }
    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setIndex(Integer index){
        this.index=index;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", time='" + time + '\'' +
                ", context='" + context + '\'' +
                ", recipientID='" + recipientID + '\'' +
                ", index=" + index +
                ", likes=" + likes +
                '}';
    }
}
