package org.example;

import org.jdbi.v3.core.Jdbi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3307/ecenaz_tuzelturk";
        String user = "root";
        String password = "xxxxxxxxxxx";

        //PreparedStatement
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
            String createQuery = "INSERT INTO personel (name, position, salary) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(createQuery)) {
                stmt.setString(1, "Han Solo");
                stmt.setString(2, "Smuggler");
                stmt.setBigDecimal(3, new BigDecimal("75000"));
                stmt.executeUpdate();

                stmt.setString(1, "Luke Skywalker");
                stmt.setString(2, "Jedi Knight");
                stmt.setBigDecimal(3, new BigDecimal("90000"));
                stmt.executeUpdate();

                stmt.setString(1, "Leia Organa");
                stmt.setString(2, "Princess");
                stmt.setBigDecimal(3, new BigDecimal("85000"));
                stmt.executeUpdate();
            }

            String selectQuery = "SELECT * FROM personel";
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + ", " + rs.getString("name") + ", " + rs.getString("position") + ", " + rs.getBigDecimal("salary"));
                }
            }

            String updateQuery = "UPDATE personel SET salary = ? WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setBigDecimal(1, new BigDecimal("80000"));
                stmt.setString(2, "Han Solo");
                stmt.executeUpdate();
            }

            String deleteQuery = "DELETE FROM personel WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.setString(1, "Leia Organa");
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        //JDBI
        String url2 = "jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7720981";
        String user2 = "sql7720981";
        String password2 = "LZplxCvMdh";

        Jdbi jdbi = Jdbi.create(url2, user2, password2);
        jdbi.useHandle(handle -> {
            handle.execute("INSERT INTO personel (name, position, salary) VALUES (?, ?, ?)", "Yoda", "Jedi Master", new BigDecimal("85000"));
        });


        jdbi.useHandle(handle -> {
            List<Map<String, Object>> personnel = handle.createQuery("SELECT * FROM personel").mapToMap().list();
            personnel.forEach(System.out::println);
        });

        jdbi.useHandle(handle -> {
            handle.execute("UPDATE personel SET salary = ? WHERE name = ?", new BigDecimal("90000"), "Boba Fett");
        });

        jdbi.useHandle(handle -> {
            handle.execute("DELETE FROM personel WHERE name = ?", "Boba Fett");
        });

        jdbi.useHandle(handle -> {
            List<Map<String, Object>> personnel = handle.createQuery("SELECT * FROM personel").mapToMap().list();
            personnel.forEach(System.out::println);
        });
    }
}
