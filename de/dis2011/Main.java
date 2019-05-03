package de.dis2011;

import de.dis2011.data.Makler;
import de.dis2011.data.Estate;

import java.text.Normalizer;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int QUIT = 1;
		final int MENU_ESTATES = 2;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Estate-Verwaltung", MENU_ESTATES);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_MAKLER:
					String password = "temppw123";
					String user_pw = FormUtil.readString("Bitte geben Sie das Passwort ein");
					if (!password.equals(user_pw)) {
						System.out.println("Falsches Passwort!");
						break;
					} else {
						showMaklerMenu();
						break;
					}
				case MENU_ESTATES:
					showEstateMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int BACK = 1;
		final int EDIT_MAKLER = 2;
		final int DELETE_MAKLER = 3;
		final int SHOW_MAKLERS = 4;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
		maklerMenu.addEntry("Zeige alle Makler", SHOW_MAKLERS);
		maklerMenu.addEntry("Makler bearbeiten", EDIT_MAKLER);
		maklerMenu.addEntry("Makler löschen", DELETE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			switch(response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case BACK:
					return;
				case EDIT_MAKLER:
					editMakler();
					break;
				case DELETE_MAKLER:
					deleteMakler();
					break;
				case SHOW_MAKLERS:
					showAllMaklers();
					break;
			}
		}
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		Makler m = new Makler();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
	}

	/**
	 * Löschen einen existierenden Makler, nachdem der Benutzer die ID eingegeben hat
	 */
	public static void deleteMakler() {
		// just testing here...
		int makler_id = FormUtil.readInt("ID des zu löschenden Maklers");
		Makler m =  new Makler();
		m = m.load(makler_id);

		Makler direkt_m = Makler.load(makler_id);

		// actual delete call
		Makler.delete(makler_id);
	}

	public static void editMakler() {
		int makler_id = FormUtil.readInt("ID des zu bearbeitenden Maklers");
		Makler m = Makler.load(makler_id);
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();

		System.out.println("Makler mit der ID "+m.getId()+" wurde gespeichert.");
	}

	public static void showAllMaklers(){
		Makler.showAll();
	}

	public static void showEstateMenu() {
		// check login from makler
		String agent_login = FormUtil.readString("Bitte geben Sie den Makler Login ein");
		String agent_pw = FormUtil.readString("Bitte geben Sie das Makler Passwort ein");

		Boolean correct_login = Makler.check_login(agent_login, agent_pw);

		if (correct_login){
			System.out.println("Login erfolgreich!");
			System.out.println();

		} else {
			System.out.println("Login war nicht erfolgreich!");
			System.out.println();

		}

		//Menüoptionen
		final int NEW_ESTATE = 0;
		final int BACK = 1;
		final int EDIT_ESTATE = 2;
		final int DELETE_ESTATE = 3;
		final int SHOW_ESTATES = 4;

		//Estateverwaltungsmenü
		Menu estateMenu = new Menu("Estate-Verwaltung");
		estateMenu.addEntry("Estate anlegen", NEW_ESTATE);
		estateMenu.addEntry("Zeige alle Estates", EDIT_ESTATE);
		estateMenu.addEntry("Estate bearbeiten", DELETE_ESTATE);
		estateMenu.addEntry("Estate löschen", SHOW_ESTATES);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			switch(response) {
				case NEW_ESTATE:
					createEstate(agent_login);
					break;
				case BACK:
					return;
				case EDIT_ESTATE:
					System.out.println("Not yet implemented");
					break;
				case DELETE_ESTATE:
					System.out.println("Not yet implemented");
					break;
				case SHOW_ESTATES:
					System.out.println("Not yet implemented");
					break;
			}
		}

	}

	public static void createEstate(String makler_login) {
		Estate estate = new Estate();
		//Estate attribute abfragen
		estate.setZip(FormUtil.readInt("ZIP (int)"));
		estate.setNumber(FormUtil.readInt("Number (int)"));
		estate.setCity(FormUtil.readString("City (string)"));
		estate.setStreet(FormUtil.readString("Street (string)"));
		estate.setArea(FormUtil.readString("Area (string)"));
		//den makler benutzen welcher sich eingeloggt hat
		estate.setFk_agent(makler_login);

		estate.save();
	}
}
