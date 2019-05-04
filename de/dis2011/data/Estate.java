package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Estate {

    private int id = -1;
    private int zip;
    private int number;

    private String city;
    private String street;
    private String area;
    private String fk_agent;

    public int getId() {
        return id;
    }

    public int getzip() {
        return zip;
    }

    public int getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getArea() {
        return area;
    }

    public String getFk_agent() {
        return fk_agent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setFk_agent(String fk_agent) {
        this.fk_agent = fk_agent;
    }

    public void save() {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
            if (getId() == -1) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                String insertSQL = "INSERT INTO estate(id, zip, number, city, street, area, fk_agent_login) VALUES (?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement insert_pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // get the estate id's to find the next highest that is available
                String get_id_SQL = "SELECT id from estate";
                PreparedStatement id_pstmt = con.prepareStatement(get_id_SQL);
                ResultSet id_rs = id_pstmt.executeQuery();

                if (id_rs.next()){
                    while (id_rs.next()) {
                        setId(id_rs.getInt("id"));
                    }
                    setId(getId()+1);
                } else {
                    // keine estate in Tabelle
                    setId(0);
                }

                // Setze Anfrageparameter und fC<hre Anfrage aus
                insert_pstmt.setInt(1, getId());
                insert_pstmt.setInt(2, getzip());
                insert_pstmt.setInt(3, getNumber());

                insert_pstmt.setString(4, getCity());
                insert_pstmt.setString(5, getStreet());
                insert_pstmt.setString(6, getArea());
                insert_pstmt.setString(7, getFk_agent());

                insert_pstmt.executeUpdate();
                insert_pstmt.close();
            }
            else {
                // wenn ID nicht initial ist wird die Safe methode an einem Objekt aufgerufen, d.h. es wird ein Update durchgeführt
                // prüfe ob makler Estate bearbeiten darf
                String testSQL = "SELECT * FROM estate WHERE id = ?";
                PreparedStatement test_pstmt = con.prepareStatement(testSQL);

                test_pstmt.setInt(1, getId());

                ResultSet test_rs = test_pstmt.executeQuery();
                if(test_rs.next()) {
                    if(!(test_rs.getString("fk_agent_login").equals(getFk_agent()))) {
                        System.out.println("Makler " + getFk_agent() + " ist nicht berechtigt die Estate mit ID " + getId() +
                                " zu bearbeiten, da diese von Makler " + test_rs.getString("fk_agent_login") + " verwaltet wird.");
                        test_pstmt.close();
                        test_rs.close();
                    }
                    else {
                        // wenn makler bearbeiten darf
                        String updateSQL = "Update estate SET zip = ?, number = ?, city = ?, street = ?, area = ?, fk_agent_login = ? WHERE id = ?";
                        PreparedStatement update_pstmt = con.prepareStatement(updateSQL);

                        update_pstmt.setInt(1, getzip());
                        update_pstmt.setInt(2, getNumber());
                        update_pstmt.setString(3, getCity());
                        update_pstmt.setString(4, getStreet());
                        update_pstmt.setString(5, getArea());
                        update_pstmt.setString(6, getFk_agent());
                        update_pstmt.setInt(7, getId());

                        update_pstmt.executeUpdate();
                        update_pstmt.close();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showEstates() {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();
        try {
            String selectSQL = "SELECT id, street, fk_agent_login FROM estate";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                System.out.println("ID: " + rs.getString("id") +
                        ", Street: " + rs.getString("street") +
                        ", Agent: " + rs.getString("fk_agent_login"));
            }

            pstmt.close();
            rs.close();
            System.out.println();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEstate(int id, String makler_login) {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // Teste ob Estate mit id existiert
            String test_SQL = "SELECT * FROM estate WHERE id = ?";
            PreparedStatement test_pstmt = con.prepareStatement(test_SQL);
            test_pstmt.setInt(1, id);
            ResultSet test_rs = test_pstmt.executeQuery();
            if (!test_rs.next()){
                System.out.println("Kein Makler mit der ID " + id + " vorhanden.");
                return;
            }

            else if (test_rs.getString("fk_agent_login") != makler_login) {
                System.out.println("Makler " + makler_login + " ist nicht berechtigt die Estate mit ID " + id +
                        " zu löschen, da diese von Makler " + test_rs.getString("fk_agent_login") + " verwaltet wird.");

                test_pstmt.close();
                test_rs.close();
            }
            else {
                // Tatsächlies löschen
                // Erzeuge Querry
                String selectSQL = "DELETE FROM estate WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(selectSQL);
                pstmt.setInt(1, id);

                // Führe Querry aus
                pstmt.executeUpdate();
                pstmt.close();
                System.out.println("Estate mit ID " + id + " wurde gelöscht.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
