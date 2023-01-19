import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profile {

    private String username;
    private	String password;
   
    //Data structure
    //Stores all users profiles
    public static ArrayList<Profile> profiles=new ArrayList<>();
    
    public Profile(){}

    public Profile(String newUsername, String newPassword){
        this.username = newUsername;
        this.password = newPassword;
    }


    //Checks if user's username and password matches in arraylist profiles
    public boolean login(String newUsername, String newPassword) {
        for (Profile profile : profiles) {
            if (Objects.equals(username, profile.username) && Objects.equals(password, profile.password)) {
                System.out.println("Validation was successfull.");
                return true;
            }
        }
        System.out.println("Wrong username or password. Try again or create a new account!");
        return false;
    }

    //creates a new profile in arraylist profiles
    public boolean createAccount(String newUsername, String newPassword) {
        Profile newProfile;
        if (checkPassword(password) && checkUsername(username)) {
            newProfile = new Profile(username, password);
            profiles.add(newProfile);
            return true;
        } else if (!checkUsername(username)) {
            System.out.println("There is already an account with this username. Try something else!!");
            return false;
        } else if (!checkPassword(password) && checkUsername(username)) {
            System.out.println("Invalid password, please input a valid one (password must contain at least one upercase," +
                    " one lowercase letter, one number, one symbol and me at least 8 characters long!");
            return false;
        } else {
            System.out.println("Invalid Username and Password. Try again!!");
            return false;
        }
    }

    //Checks if username exists in profiles arraylist
    public boolean checkUsername(String usern){
        for (Profile profile : profiles) {
            if (Objects.equals(profile.username, usern)) {
                return false;
            }
        }
        return true;
    }

    //Checks if password has UpperCase,LowerCase,Number and Symbol character
    public boolean checkPassword(String password){
        if(password.length()>=8){
            Pattern low_case_letter = Pattern.compile("[a-z]");
            Pattern upper_case_letter=Pattern.compile("[A-Z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLowLetter = low_case_letter.matcher(password);
            Matcher hasUpperLetter = upper_case_letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLowLetter.find() && hasUpperLetter.find() && hasDigit.find() && hasSpecial.find();
        }

        return false;

    }

    @Override
    public String toString() {
        return "Profile{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
    	return this.username;
    }
    
    public String getPassword() {
    	return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //Changes a username in profiles arraylist profiles with a new one
    public boolean changeUsername(String oldUsername, String newUsername) {
    	for (Profile profile : profiles) {
    		if(profile.getUsername().equals(oldUsername)) {
    			if( this.checkUsername(newUsername)) {
    				profile.setUsername(newUsername);
    				return true;
    			} else {
    				break;
    			}    			
    		}
    	}
    	System.out.println("There is already an account with this username. Try something else!!");
    	return false;
    }
    
    //Changes a password in profiles arraylist with a new one
    public boolean changePassword(String oldUsername, String newPassword) {
    	for (Profile profile : profiles) {
    		if(profile.getUsername().equals(oldUsername)) {
    			if( checkPassword(newPassword)) {
    				profile.setPassword(newPassword);
    				return true;
    			} else {
    				break;
    			}    			
    		}
    	}
    	System.out.println("Invalid password, please input a valid one (password must contain at least one upercase," +
                " one lowercase letter, one number, one symbol and me at least 8 characters long!");
    	return false;
    }
    
    //Deletes a profile from arraylist profiles
    public void deleteProfile(Profile profileToRemove) {
    	
    	int index = 0;	
    	for(Profile profile : profiles) {
    		if(profileToRemove.getUsername().equals(profile.getUsername())) {
    			break;
    		} else {
    			index++;
    		}
    	}
       	profiles.remove(index);
    }


}
