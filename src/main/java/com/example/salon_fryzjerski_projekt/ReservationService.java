package com.example.salon_fryzjerski_projekt;

import java.sql.*;
import java.util.Date;

public class ReservationService {

    public void deleteReservation(Connection connection, int reservationId) {
        String deleteReservationSQL = "DELETE FROM rezerwacje WHERE id_rezerwacji = ?";

        try (PreparedStatement deleteReservationStatement = connection.prepareStatement(deleteReservationSQL)) {
            deleteReservationStatement.setInt(1, reservationId);

            int rowsAffected = deleteReservationStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Rezerwacja o ID " + reservationId + " została usunięta.");
            } else {
                System.out.println("Nie znaleziono rezerwacji o podanym ID: " + reservationId);
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    public String getAvailableTimeSlotsForEmployee(Connection connection, int employeeId, String date) {
        StringBuilder result = new StringBuilder();
        String sql = "SELECT rezerwacje_terminy.* " +
                "FROM rezerwacje_terminy " +
                "WHERE rezerwacje_terminy.pracownik_id_pracownika = ? " +
                "AND rezerwacje_terminy.godzina_poczatkowa NOT IN (" +
                "    SELECT rezerwacje_terminy.godzina_poczatkowa " +
                "    FROM rezerwacje_terminy " +
                "    RIGHT JOIN rezerwacje ON rezerwacje_terminy.godzina_poczatkowa = rezerwacje.godzina_poczatkowa " +
                "    WHERE rezerwacje_terminy.pracownik_id_pracownika = ? AND rezerwacje.data = ?" +
                ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, employeeId);
            preparedStatement.setString(3, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String startTime = resultSet.getString("godzina_poczatkowa");
                    result.append(startTime).append('\n');
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        System.out.println(result.toString());
        return result.toString();
    }
    public void createReservation(Connection connection,Date data, String godzinaPoczatkowa, String godzinaKoncowa , String name, String surname, String phone, String clientId,int employeeId,int serviceId,int branchId) {
        Integer clientIdInteger;
        try {
            clientIdInteger = Integer.parseInt(clientId);
        } catch (NumberFormatException e) {
            clientIdInteger = null;
        }

        String insertReservationQuery = "INSERT INTO rezerwacje (data, godzina_poczatkowa, godzina_koncowa,imie, nazwisko,telefon,klient_id_klienta,pracownik_id_pracownika,uslugi_id_uslugi ,oddzial_id_oddzialu) VALUES (?, ?, ?, ?,?,?, ?, ?, ?,?)";

        try (PreparedStatement insertReservationStatement = connection.prepareStatement(insertReservationQuery)) {


            insertReservationStatement.setDate(1, new java.sql.Date(data.getTime()));
            insertReservationStatement.setString(2, godzinaPoczatkowa);
            insertReservationStatement.setString(3, godzinaKoncowa);
            insertReservationStatement.setString(4, name);
            insertReservationStatement.setString(5, surname);
            insertReservationStatement.setString(6, phone);
            if (clientIdInteger != null) {
                insertReservationStatement.setInt(7, clientIdInteger);
            } else {
                insertReservationStatement.setObject(7, null);
            }
            insertReservationStatement.setInt(8, employeeId);
            insertReservationStatement.setInt(9, serviceId);
            insertReservationStatement.setInt(10, branchId);

            int rowsAffected = insertReservationStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Rezerwacja została pomyślnie utworzona.");
            } else {
                System.out.println("Nie udało się utworzyć rezerwacji.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public String getClientReservation(Connection connection, int clientId){
        StringBuilder result = new StringBuilder();
        String sql = "SELECT rezerwacje.id_rezerwacji, rezerwacje.data, rezerwacje.godzina_poczatkowa, uslugi.nazwa_uslugi " +
                "FROM rezerwacje JOIN uslugi on rezerwacje.uslugi_id_uslugi = uslugi.id_uslugi " +
                "WHERE rezerwacje.klient_id_klienta = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, clientId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id_rezerwacji");
                    Date date = resultSet.getDate("data");
                    String stringDate = date.toString();
                    String startTime = resultSet.getString("godzina_poczatkowa");
                    String name = resultSet.getString("nazwa_uslugi");

                    result.append(id).append("\n");
                    result.append(stringDate).append("\n");
                    result.append(startTime).append("\n");
                    result.append(name).append("\n");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public String getEmployeeReservation(Connection connection, int employeeId){
        StringBuilder result = new StringBuilder();
        String sql = "SELECT rezerwacje.id_rezerwacji, rezerwacje.data, rezerwacje.godzina_poczatkowa, uslugi.nazwa_uslugi " +
                "FROM rezerwacje JOIN uslugi on rezerwacje.uslugi_id_uslugi = uslugi.id_uslugi " +
                "WHERE rezerwacje.pracownik_id_pracownika = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id_rezerwacji");
                    Date date = resultSet.getDate("data");
                    String stringDate = date.toString();
                    String startTime = resultSet.getString("godzina_poczatkowa");
                    String name = resultSet.getString("nazwa_uslugi");

                    result.append(id).append("\n");
                    result.append(stringDate).append("\n");
                    result.append(startTime).append("\n");
                    result.append(name).append("\n");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public String getBookingDetails(Connection connection, int reservationId){
        StringBuilder result = new StringBuilder();
        String sql = "SELECT oddzial.nazwa, oddzial.ulica, miejscowosci.nazwa_miejscowosci, uslugi.nazwa_uslugi, uslugi.opis_uslugi, " +
                "uslugi.czas_uslugi, pracownik.imie as 'imie_pracownika', pracownik.nazwisko as 'nazwisko_pracownika', " +
                "rezerwacje.imie as 'imie_klienta', rezerwacje.nazwisko as 'nazwisko_klienta', " +
                "rezerwacje.telefon, rezerwacje.data, rezerwacje.godzina_poczatkowa, uslugi.cena_uslugi " +
                "FROM rezerwacje Join uslugi on uslugi.id_uslugi = rezerwacje.uslugi_id_uslugi J" +
                "oin oddzial on rezerwacje.oddzial_id_oddzialu = oddzial.id_oddzialu " +
                "JOIN pracownik on rezerwacje.pracownik_id_pracownika = pracownik.id_pracownika " +
                "Join miejscowosci on oddzial.miejscowosci_id_miejscowosci = miejscowosci.id_miejscowosci " +
                "WHERE rezerwacje.id_rezerwacji = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, reservationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String branchName = resultSet.getString("nazwa");
                    String branchStreet = resultSet.getString("ulica");
                    String cityName = resultSet.getString("nazwa_miejscowosci");
                    String bookingType = resultSet.getString("nazwa_uslugi");
                    String bookingDesc = resultSet.getString("opis_uslugi");
                    String bookingDuration = resultSet.getString("czas_uslugi");
                    String employeeName = resultSet.getString("imie_pracownika");
                    String employeeSurname = resultSet.getString("nazwisko_pracownika");
                    String clientName = resultSet.getString("imie_klienta");
                    String clientSurname = resultSet.getString("nazwisko_klienta");
                    String phoneNumber = resultSet.getString("telefon");
                    Date date = resultSet.getDate("data");
                    String stringDate = date.toString();
                    String startTime = resultSet.getString("godzina_poczatkowa");
                    String bookingPrice = resultSet.getString("cena_uslugi");

                    result.append(branchName).append("\n");
                    result.append(branchStreet).append("\n");
                    result.append(cityName).append("\n");
                    result.append(bookingType).append("\n");
                    result.append(bookingDesc).append("\n");
                    result.append(bookingDuration).append("\n");
                    result.append(employeeName).append("\n");
                    result.append(employeeSurname).append("\n");
                    result.append(clientName).append("\n");
                    result.append(clientSurname).append("\n");
                    result.append(phoneNumber).append("\n");
                    result.append(stringDate).append("\n");
                    result.append(startTime).append("\n");
                    result.append(bookingPrice).append("\n");

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
