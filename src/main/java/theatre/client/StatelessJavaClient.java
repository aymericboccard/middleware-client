/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package theatre.client;

import java.math.BigDecimal;
import java.util.Scanner;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import theatre.service.StatelessLocal;

public class StatelessJavaClient {
	
	public static Scanner sc = new Scanner(System.in);

    public static void main(String args[]) {
    	System.out.println("Bonjour! Bienvenue au theatre!\n Que voulez-vous faire?\n 1. Regarder les événements \n"
    			+ "2. Faire une reservation \n 3. Regarder mes reservations \n 4. Partir");
    	String valeur = sc.nextLine();
    	
    	boolean partir = false;
    	boolean unvalid = (!valeur.equals("1") && !valeur.equals("2") && !valeur.equals("3") && !valeur.equals("4"));
    	
    	
    	while (!partir){
    		if(unvalid ^ (!valeur.equals("1") && !valeur.equals("2") && !valeur.equals("3") && !valeur.equals("4"))){
    			System.out.println("Que voulez-vous faire?\n 1. Regarder les événements \n"
	        			+ "2. Faire une reservation \n 3. Regarder mes reservations \n 4. Partir");
	        	valeur = sc.nextLine();
	        	unvalid = (!valeur.equals("1") && !valeur.equals("2") && !valeur.equals("3") && !valeur.equals("4"));
    		}
    		
	    	while (unvalid){
	    		System.out.println("Désolé, action non valide \n Que voulez-vous faire?\n 1. Regarder les événements \n"
	        			+ "2. Faire une reservation \n 3. Regarder mes reservations \n 4. Partir");
	        	valeur = sc.nextLine();
	        	unvalid = !(valeur.equals("1") || valeur.equals("2") || valeur.equals("3") || valeur.equals("4"));
	    	}
	    	
	    	switch(valeur){
			case "1":
				partir = showEvent();
				break;
			case "2":
				//partir = booking();
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
    
    public static boolean booking(){
    	InitialContext ic;
		try {
			ic = new InitialContext();
			StatelessLocal sless = 
		            (StatelessLocal) ic.lookup("theatre.service.StatelessLocal");
			System.out.println(sless.showAllEvents());
		        System.out.println("Écrivez le numéro de l'événement auquel vous souhaitez participer:");
		    	int even = sc.nextInt();
		    	
		    	System.out.println(sless.showPricesByEvent(even));
		        System.out.println("Dans quelle section vous voulez être? :");
		    	String sector = sc.nextLine();
		    
		    	
		    	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	
    	return true;
    }
    
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
