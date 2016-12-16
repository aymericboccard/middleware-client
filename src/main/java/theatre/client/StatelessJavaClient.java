/* interface client */

package theatre.client;
import java.util.HashMap;
import java.util.Scanner;
import javax.naming.InitialContext;
import theatre.service.StatelessLocal;

public class StatelessJavaClient {
	
	public static Scanner sc = new Scanner(System.in);

    public static void main(String args[]) {
    	//menu
    	System.out.println("Bonjour! Bienvenue au theatre!\n Que voulez-vous faire?\n 1. Regarder les événements \n"
    			+ " 2. Faire une reservation \n 3. Regarder mes reservations \n 4. Partir\n\n");
    	String valeur = sc.nextLine();
    	
    	boolean partir = false; //commande la sortie de l'interface
    	boolean unvalid = (!valeur.equals("1") && !valeur.equals("2") && !valeur.equals("3") && !valeur.equals("4"));
    	
    	
    	while (!partir){
    		//on ne doit pas afficher la première fois, d'où la condition bizarre
    		if(unvalid ^ (!valeur.equals("1") && !valeur.equals("2") && !valeur.equals("3") && !valeur.equals("4"))){
    			System.out.println("Que voulez-vous faire?\n 1. Regarder les événements \n"
	        			+ " 2. Faire une reservation \n 3. Regarder mes reservations \n 4. Partir\n\n");
	        	valeur = sc.nextLine(); // doit être doubler certaine foit pour fonctionner correctement
	        	valeur = sc.nextLine();
	        	unvalid = (!valeur.equals("1") && !valeur.equals("2") && !valeur.equals("3") && !valeur.equals("4"));
    		}
    		
    		//permet de foltrer les commandes invalides
	    	while (unvalid){
	    		System.out.println("Désolé, action non valide \n Que voulez-vous faire?\n 1. Regarder les événements \n"
	        			+ " 2. Faire une reservation \n 3. Regarder mes reservations \n 4. Partir\n\n");
	        	valeur = sc.nextLine();
	        	unvalid = !(valeur.equals("1") || valeur.equals("2") || valeur.equals("3") || valeur.equals("4"));
	    	}
	    	
	    	switch(valeur){
			case "1":
				partir = showEvent();
				break;
			case "2":
				partir = booking();
				break;
			case "3":
				partir = showMyEvent();
				break;
			case "4":
				partir = true;
				break;
	    	}
	    	
	    	unvalid = true;
	    	
	    	if(partir){
	    		System.out.println("Au revoir!!");
	    	}
    	}
        sc.close();
    }
    
    //montre la liste des événements disponibles
    public static boolean showEvent(){
        try {

            InitialContext ic = new InitialContext();
            StatelessLocal sless = 
                (StatelessLocal) ic.lookup("theatre.service.StatelessLocal");
			System.out.println(sless.showAllEvents());

        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return askContinue();
    }
    
    //réservation
    public static boolean booking(){
    	InitialContext ic;
		try {
			ic = new InitialContext();
			StatelessLocal sless = 
            (StatelessLocal) ic.lookup("theatre.service.StatelessLocal");
			System.out.println(sless.showAllEvents());
	        System.out.println("\n\nÉcrivez le numéro de l'événement auquel vous souhaitez participer:");
	    	int even = sc.nextInt();
	    	
	    	while(!sless.checkAvailability(even)){
	    		System.out.println("\n\nEvenement "+even+" complet !");
	    		System.out.println("Choisissez un autre événement en saisissant son numéro ou revenez en arrière en saisissant -1\n\n");
	    		even = sc.nextInt();
		    	if(even == -1){
		    		return false;
		    	}
	    	}
	    	
	    	System.out.println(sless.showPricesByEvent(even));
	        System.out.println("Dans quelle section vous voulez être? (A, B, C ou D) :\n");
	    	String sector = sc.nextLine();
	    	sector = sc.nextLine();//doit etre doublé pour bien fonctionner
	    	
	    	while(!(sector.equals("A") || sector.equals("B") || sector.equals("C") || sector.equals("D"))){
	    		System.out.println("Saisissez A, B, C ou D ou revenez au menu précédent en saisissant Q");
		    	sector = sc.nextLine();
		    	if(!sless.checkAvailabilityBySection(even, sector) && (sector.equals("A") || sector.equals("B") || sector.equals("C") || sector.equals("D"))){
		    		System.out.println("Secteur "+sector+" complet");
		    	}
		    	if(sector.equals("Q")){
		    		return false;
		    	}
	    	}
	    	
	    	float price;
	    	//pour éviter d'écrire 4 fois le même code
	    	HashMap<String, String> rangeseat = new HashMap<String,String>();
	    	rangeseat.put("A", "1-25");
	    	rangeseat.put("B", "1-45");
	    	rangeseat.put("C", "1-100");
	    	rangeseat.put("D", "1-500");
	    	
	    	System.out.println("Voici la liste des sièges déjà réservés :");
	    	System.out.println(sless.showBookedSeatsByEventInSection(even, sector));
	    		 
    		 System.out.println("Choissisez le numéro du siège à réserver ("+rangeseat.get(sector)+"):");
    		 int numseat = sc.nextInt();
    		 while(!sless.checkReservation(even, sector+""+numseat)){
    			 System.out.println("Choissisez le numéro du siège à réserver ("+rangeseat.get(sector)+") ou revenez au menu précédent en saisissant -1 :");
	    		 numseat = sc.nextInt();
	    		 if(numseat == -1){
			    		return false;
			     }
    		 }
    		 price = sless.getPriceForSeat(even, sector);
    		 System.out.println("Le prix du siège "+sector+""+numseat+" vous sera facturé "+price+" €");
    		 System.out.println("Veuillez renseigner votre prénom suivi de votre nom :");
    		 String username = sc.nextLine();
    		 username = sc.nextLine();
    		 //simulation de paiement
    		 System.out.println("Veuillez renseigner votre numéro de carte bleue :");
    		 String numcard = sc.nextLine();
    		 
    		 System.out.println("Veuillez renseigner le nom du propriétaire de la carte :");
    		 String holdername = sc.nextLine();
    		 
    		 if(sless.addBooking(even, sector+""+numseat, username, numcard, holdername)){
				 System.out.println("Votre réservation a été effectué.");
				 System.out.println("Souhaitez-vous partir ? (o/n)");
		    	 String valeur = sc.nextLine();
		    	 
		    	 while (!valeur.equals("o") && !valeur.equals("n")){
		    	 	 System.out.println("Désolé, réponse non valide");
		         	 valeur = sc.nextLine();
		    	 }
		    	 return valeur.equals("o");
		    	 
    		 }else{
    			 System.out.println("Une erreur est survenue lors du paiement. La réservation n'a pas été prise en compte.");
    			 return false;
    		 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return true;
    }
    
    //affiche les événements auxquels on participe
    public static boolean showMyEvent(){
    	try {

            InitialContext ic = new InitialContext();
            StatelessLocal sless = 
                (StatelessLocal) ic.lookup("theatre.service.StatelessLocal");
            System.out.println("Écrivez votre nom:");
        	String nom = sc.nextLine();
        	
        	System.out.println(sless.showBookedEvent(nom));
        	
        	return askContinue();
        	
        	

        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return askContinue();
    	
    }
    
    public static boolean askContinue(){
    	System.out.println("Voulez-vous faire autre chose? (o/n)");
    	
    	String valeur = sc.nextLine();
    	while (!valeur.equals("o") && !valeur.equals("n")){
    		System.out.println("Désolé, action non valide");
        	valeur = sc.nextLine();
    	}
    	return valeur.equals("n");
    }
}
