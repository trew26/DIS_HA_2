package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Contract {
    private int id = -1;
    private int person;
    private int duration;
    private int additional_cost;
    private int appartment;

    private String first_name;
    private String last_name;
    private String address;
    private String place;
    private String startdate;
    private String contractdate;

    private int getId() {return id;}
    private int getPerson() {return person;}
    private int getDuration() {return duration;}
    private int getAdditionalCost() {return additional_cost;}
    private int getAppartment() {return appartment;}
    private String getFirst_name() {return first_name;}
    private String getLast_name() {return last_name;}
    private String getAddress() {return address;}
    private String getPlace() {return place;}
    private String getStartdate() {return startdate;}
    private String getContractdate() {return contractdate;}

    public void setId(int id) {this.id = id; }
    public void setPerson(int person) {
        this.person = person;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setAdditionalCost(int additional_cost) {
        this.additional_cost = additional_cost;
    }
    public void setAppartment(int appartment) {
        this.appartment = appartment;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
    public void setContractdate(String contract) {
        this.contractdate = contractdate;
    }

    public void savePerson() {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
            if (getId() == -1) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                String insertSQL = "INSERT INTO person(first_name, last_name, address) VALUES (?, ?, ?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setString(1, getFirst_name());
                pstmt.setString(2, getLast_name());
                pstmt.setString(3, getAddress());
                pstmt.executeUpdate();

                // Hole die Id des engefC<gten Datensatzes
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }

                rs.close();
                pstmt.close();
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "UPDATE person SET first_name = ?, last_name = ?, address = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setString(1, getFirst_name());
                pstmt.setString(2, getLast_name());
                pstmt.setString(3, getAddress());
                pstmt.setInt(4, getId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveContract() {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
            if (getId() == -1) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                String insertSQL = "INSERT INTO contract(date, place, fk_person_id) VALUES (?, ?, ?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setString(1, getContractdate());
                pstmt.setString(2, getPlace());
                pstmt.setInt(3, getPerson());
                pstmt.executeUpdate();

                // Hole die Id des engefC<gten Datensatzes
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }

                rs.close();
                pstmt.close();
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "UPDATE contract SET date = ?, place = ?, fk_person_id = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setString(1, getContractdate());
                pstmt.setString(2, getPlace());
                pstmt.setInt(3, getPerson());
                pstmt.setInt(4, getId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
