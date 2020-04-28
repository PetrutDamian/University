package Repositories;

import Domain.Entity;

import java.sql.*;

public interface SqlRepo {
    default void writeToSql(String s){
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "postgres", "sharpshooter22@a");
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(s);
            stmt.close();
            con.commit();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
    default ResultSet getResultSet(String table) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "postgres", "sharpshooter22@a");
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM "+ table);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet;
        } catch (ClassNotFoundException | SQLException ex){ex.printStackTrace();}
        return null;
    }
     void loadFromSql();
}
