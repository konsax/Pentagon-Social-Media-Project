ENGLISH INSTRUCTIONS

# Pentagon Social Media Project
A social media application that allows users to chat via a java socket stream.

by Constantine Adam Sahinidis , George Papoutsakis

----------------------------------------------------------------------------------------------------------------------------------------------------------------------

##READ ME :

## Unit 1- Compiling instuctions

1. Άνοιγμα cmd.
2. Χρήση του path στο οποίο βρίσκονται αποθηκευμένες οι κλάσεις του προγράμματος.
3. Για την μεταγλώττιση του προγράμματος αρκεί να χρησιμοποιηθεί η εντολή javac ακολουθούμενη από το αρχείο τύπου '.java', πχ. javac Client.java.
4. Η διαδικασία αυτή πρέπει να γίνει για όλες τις κλάσεις της εφαρμογής.

### Notes: -Βεβαιωθείτε ότι το path που χρησιμοποιείται είναι σωστό.

## Unit 2- Execution instuctions

1. Άνοιγμα cmd.
2. Χρήση του path στο οποίο βρίσκονται αποθηκευμένες οι κλάσεις του προγράμματος.
3. Για την εκτέλεση του προγράμματος αρκεί να χρησιμοποιηθεί η εντολή java ακολουθούμενη από το όνομα της κλάσης , πχ. java Client
4. Πρώτα πρέπει να εκτελεστεί το αρχείο Server.java και κατόπιν εκτελείται η Client.

### Notes: <br />
      -Πριν την χρήση του προγράμματος βεβαιωθείτε ότι όλα αρχεία τύπου '.java' έχουν μεταγλωττιστεί και ότι έχει δημιουργηθεί ένα αρχείο τύπου '.class' αντίστοιχα για την κάθε κλάση.
	   -Η Server.java μπορεί να εκτελεστεί μόνο μία φορά.
	   -Η Client.java δεν μπορεί να λειτουργήσει σωστά αν δεν έχει εκτελεστεί προηγουμένως η Server.java
	   -Η Client δύναται να εκτελεστεί πολλαπλές φορές καθώς επιτρέπεται η ταυτόχρονη χρήση του προγράμματος. Αντιθέτως η Server εκτελείται μία μόνο φορά.

## Unit 3- User instructions

1. Αφού εκτελεστούν οι Server και Client στο παράθυρο του Client εμφανίζονται οι ακόλουθες επιλογές:
	a)Login χρήστη: Συνεχίζει στην ροή του προγράμματος
	Παρακάτω παρουσιάζονται οι εντολές και η λειτουργικότητες τους:
		<br /> /help : Prints all commands.
		<br /> /quit : Disconnects client(logout functionality)
		<br /> /priv UserName 'msg' : Send a private message. eg. /priv Kostas Hello World!!.
		<br /> /inbox: Prints all the private messages.
		<br /> /showAll: Prints all messages (both private and public).
		<br /> /online: Prints a list with all the users that are connected to the socket(online users) .
		<br /> /Like 'msgID' : Implements like function.(Depended on the index it's been given) eg. /Like 552 (likes message with index '552')
		<br /> /ShowLikes 'msgID' : Prints a list with all the users who liked the message.
				
	b)Create account: <br />-Αν δωθούν σωστά στοιχεία κάνει εγγραφή και επιστρέφει στο αρχικό μενού.
						<br /> -Αν δωθούν λάθος στοιχεία βγάζει μήνυμα λάθους και επιστρέφει στο αρχικό μενού.
	<br />c)Έξοδος από την εφαρμογή : Κλείνει την σύνδεση του Client.
<br />	d)Αλλαγή κωδικού χρήστη, αλλαγή ονόματος χρήστη, διαγραφή λογαριασμού χρήστη : Υλοποιούν τις αντίστοιχες λειτουργίες και επιστρέφουν στο αρχικό μενού.
	
### Notes: -Οι περισσότερες εντολές είναι CASE SENSITIVE, που σημαίνει ότι πρέπει να γραφούν ακριβώς με τον ίδιο τρόπο που εμφανίζονται στο help guide. Αλλιώς θα βγάζει μήνυμα λάθους.

## Unit 4- Repository structure

Src---<br />
	Client.java <br />
	Server.java <br />
	ClientListener.java <br />
	ClientManager.java <br />
	Profile.java <br />
	Message.java <br />
	Messages.csv <br />
	Profiles.csv <br />

## Unit 5- UML diagram
![UML](https://github.com/8170034/Omada6/blob/main/src/UML-Diagram.png)

## Unit 6- Data structures

-Η εφαρμογή χρησιμοποιεί Arraylist για την αποθήκευση δεδομένων εντός του προγράμματος.<br />
-Επιπλέον κατά την έναρξη και την λήξη της εφαρμογής εισάγονται και εξάγονται αντίστοιχα τα δεδομένα του προγράμματος σε αρχεία τύπου CSV.
