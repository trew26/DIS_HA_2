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

                //testing
                //String sttaticSQL = "INSERT INTO estate(id, zip, number, city, street, area, fk_agent_login) VALUES (6, 12345, 42, 'TEST querry', 'blah', 'blah', 'Finn123')";
                //PreparedStatement test = con.prepareStatement(sttaticSQL, Statement.RETURN_GENERATED_KEYS);
                //test.executeUpdate();
                //test.close();

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
