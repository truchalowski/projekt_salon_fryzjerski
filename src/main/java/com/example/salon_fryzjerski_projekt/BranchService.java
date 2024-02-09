package com.example.salon_fryzjerski_projekt;

import java.sql.*;

public class BranchService {


    public void updateBranchHours(Connection connection, int branchId, String openingTime, String closingTime) {
        String updateBranchHoursSQL = "UPDATE oddzial SET godzina_otwarcia = ?, godzina_zamkniecia = ? WHERE id_oddzialu = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(updateBranchHoursSQL)) {
            // Przekształć ciągi znaków na obiekty Timestamp

            updateStatement.setString(1, openingTime);
            updateStatement.setString(2, closingTime);
            updateStatement.setInt(3, branchId);

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Godziny otwarcia i zamknięcia oddziału zaktualizowane.");
            } else {
                System.out.println("Nie znaleziono oddziału o podanym ID.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public String getBranches(Connection connection) {
        StringBuilder result = new StringBuilder();

        String selectBranchesSQL = "SELECT id_oddzialu, nazwa,ulica FROM oddzial";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectBranchesSQL);
             ResultSet resultSet = selectStatement.executeQuery()) {

            while (resultSet.next()) {
                int branchId = resultSet.getInt("id_oddzialu");
                String branchName = resultSet.getString("nazwa");
                String branchStreet = resultSet.getString("ulica");

                result.append(branchId).append("\n");
                result.append(branchName).append("\n");
                result.append(branchStreet).append("\n");

            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
        return result.toString();
    }

    public String getCity(Connection connection) {
        StringBuilder result = new StringBuilder();

        String selectBranchesSQL = "SELECT id_miejscowosci, nazwa_miejscowosci FROM miejscowosci";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectBranchesSQL);
             ResultSet resultSet = selectStatement.executeQuery()) {

            while (resultSet.next()) {
                int cityId = resultSet.getInt("id_miejscowosci");
                String cityName = resultSet.getString("nazwa_miejscowosci");

                result.append(cityId).append("\n");
                result.append(cityName).append("\n");

            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
        return result.toString();
    }

    public String getBranchesInCity(Connection connection, String cityName) {
        StringBuilder result = new StringBuilder();

        String selectBranchesSQL = "SELECT oddzial.id_oddzialu, oddzial.nazwa, oddzial.ulica" +
                " FROM oddzial" +
                " JOIN miejscowosci ON oddzial.miejscowosci_id_miejscowosci = miejscowosci.id_miejscowosci" +
                " WHERE miejscowosci.nazwa_miejscowosci = ?;";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectBranchesSQL)) {
            selectStatement.setString(1, cityName);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    int branchId = resultSet.getInt("id_oddzialu");
                    String branchName = resultSet.getString("nazwa");
                    String branchStreet = resultSet.getString("ulica");

                    result.append(branchId).append("\n");
                    result.append(branchName).append("\n");
                    result.append(branchStreet).append("\n");

                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return result.toString();
    }


    private void handleSQLException(SQLException e) {
        e.printStackTrace();
        System.out.println("Błąd SQL: " + e.getMessage());
    }

}
