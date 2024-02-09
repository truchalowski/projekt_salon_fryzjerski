package com.example.salon_fryzjerski_projekt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingTypeService {

    public String getAllAvailableServices(Connection connection) {
        String selectBranchesSQL = "SELECT id_uslugi, nazwa_uslugi, opis_uslugi, czas_uslugi, cena_uslugi FROM uslugi";

        StringBuilder result = new StringBuilder();

        try (PreparedStatement selectStatement = connection.prepareStatement(selectBranchesSQL)) {

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("id_uslugi");
                    String productName = resultSet.getString("nazwa_uslugi");
                    String productTime = resultSet.getString("czas_uslugi");
                    double productPrice = resultSet.getDouble("cena_uslugi");
                    String desscription = resultSet.getString("opis_uslugi");

                    result.append(productId).append("\n");
                    result.append(productName).append("\n");
                    result.append(desscription).append("\n");
                    result.append(productTime).append("\n");
                    result.append(productPrice).append("\n");


                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return result.toString();
    }

    private static void handleSQLException(SQLException e) {
        e.printStackTrace();
        System.out.println("Błąd SQL: " + e.getMessage());
    }


}
