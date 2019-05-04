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

	/**
	 * Bearbeitet eines existierenden Maklers andhand der angegebenen ID
	 */
	public static void editMakler() {
		int makler_id = FormUtil.readInt("ID des zu bearbeitenden Maklers");
		Makler m = Makler.load(makler_id);
		m.setName(FormUtil.readString("Neuer Name"));
		m.setAddress(FormUtil.readString("Neue Adresse"));
		m.setLogin(FormUtil.readString("Neuer Login"));
		m.setPassword(FormUtil.readString("Neues Passwort"));
		m.save();

		System.out.println("Makler mit der ID "+m.getId()+" wurde gespeichert.");
	}

	/**
	 * Gibt alle existierenden Makler aus
	 */
	public static void showAllMaklers(){
		Makler.showAll();
	}

	/**
	 * Öffnet die Estate Verwaltung. Dazu muss sich ein Makler mit seinem Passwort und Login anmelden.
	 */
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
			return;
		}

		//Menüoptionen
		final int NEW_HOUSE = 0;
		final int NEW_APARTMENT = 1;
		final int BACK = 2;
		final int EDIT_ESTATE = 3;
		final int DELETE_ESTATE = 4;
		final int SHOW_ESTATES = 5;

		//Estateverwaltungsmenü
		Menu estateMenu = new Menu("Estate-Verwaltung");
		estateMenu.addEntry("Haus anlegen", NEW_HOUSE);
		estateMenu.addEntry("Apartment anlegen", NEW_APARTMENT);
		estateMenu.addEntry("Zeige alle Estates", SHOW_ESTATES);
		estateMenu.addEntry("Estate bearbeiten", EDIT_ESTATE);
		estateMenu.addEntry("Estate löschen", DELETE_ESTATE);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();
			switch(response) {
				case NEW_HOUSE:
					createHouse(agent_login);
					break;
				case NEW_APARTMENT:
					createApartment(agent_login);
					break;
				case BACK:
					return;
				case EDIT_ESTATE:
					editEstate(agent_login);
					break;
				case DELETE_ESTATE:
					deleteEstate(agent_login);
					break;
				case SHOW_ESTATES:
					showEstates(agent_login);
					break;
			}
		}

	}

	/**
	 * Erzeugt ein neues Estate Objekt in der Datenbank
	 * @param makler_login der angemeldete Makler
	 */
	public static void createHouse(String makler_login) {
		Estate estate = new Estate();
		//Estate attribute abfragen
		estate.setZip(FormUtil.readInt("ZIP (int)"));
		estate.setNumber(FormUtil.readInt("Number (int)"));
		estate.setCity(FormUtil.readString("City (string)"));
		estate.setStreet(FormUtil.readString("Street (string)"));
		estate.setArea(FormUtil.readString("Area (Stadtteil)"));
		estate.setFloors(FormUtil.readInt("Floors"));
		estate.setPrice(FormUtil.readInt("Price"));
		estate.setGarden(FormUtil.readInt("Garden"));
		//den makler benutzen welcher sich eingeloggt hat
		estate.setFk_agent(makler_login);

		estate.save();
	}

	/**
	 * Erzeugt ein neues Estate Objekt in der Datenbank
	 * @param makler_login der angemeldete Makler
	 */
	public static void createApartment(String makler_login) {
		Estate estate = new Estate();
		//Estate attribute abfragen
		estate.setZip(FormUtil.readInt("ZIP (int)"));
		estate.setNumber(FormUtil.readInt("Number (int)"));
		estate.setCity(FormUtil.readString("City (string)"));
		estate.setStreet(FormUtil.readString("Street (string)"));
		estate.setArea(FormUtil.readString("Area (Stadtteil)"));
		estate.setFloor(FormUtil.readInt("Floor"));
		estate.setRent(FormUtil.readInt("Rent"));
		estate.setRooms(FormUtil.readInt("Rooms"));
		estate.setBalcony(FormUtil.readInt("Balcony"));
		estate.setKitchen(FormUtil.readInt("Kitchen"));
		//den makler benutzen welcher sich eingeloggt hat
		estate.setFk_agent(makler_login);

		estate.save();
		estate.saveApartment();

	}

	/**
	 * Gibt alle Estates aus vom eingeloggten Makler
	 * @param makler_login der angemeldete Makler
	 */
	public static void showEstates(String makler_login) {
		Estate.showEstates(makler_login);
	}

	/**
	 * löscht eine Estate. Der angemeldete Makler kann nur Estates löschen, welche von Ihm verwaltet werden
	 * @param agent_login der angemeldete Makler
	 */
	public static void deleteEstate(String agent_login) {
		int estate_id = FormUtil.readInt("ID der zu löschenden Estate");
		Estate.deleteEstate(estate_id, agent_login);
	}

	/**
	 * Bearbeiten einer Estate. Der Angemeldete Makler kann nur Estate bearbeiten, welche von Ihm verwaltet werden
	 * @param agent_login der angemeldete Makler
	 */
	public static void editEstate(String agent_login) {
		int estate_id = FormUtil.readInt("ID der zu bearbeitenden Estate");

		System.out.println("Geben Sie nun die neuen Werte für die Estate an");

		Estate estate = new Estate();
		//Estate attribute abfragen
		estate.setId(estate_id);
		estate.setZip(FormUtil.readInt("Neuer ZIP-Code"));
		estate.setNumber(FormUtil.readInt("Neue Nummer"));
		estate.setCity(FormUtil.readString("Neue City"));
		estate.setStreet(FormUtil.readString("Neue Straße"));
		estate.setArea(FormUtil.readString("Neue Area (Stadtteil)"));
		estate.setFk_agent(agent_login);

		estate.save();
	}
}
