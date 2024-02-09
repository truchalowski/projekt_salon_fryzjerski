package com.example.salon_fryzjerski_projekt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoginService {

    private static String dbUrl = "jdbc:mysql://localhost:3306/projekt1";
    private static String dbUser = "root";
    private static String dbPassword = "";

    public static boolean authenticateUser(Connection connection, String enteredUsername, String enteredPassword) {
        String sql = "SELECT * FROM dane_logowania WHERE login = ? AND haslo = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, enteredUsername);
            preparedStatement.setString(2, enteredPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public boolean isUidUnique(Connection connection, String uid) {
        String query = "SELECT COUNT(*) FROM dane_logowania WHERE uid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0;  // Jeśli count == 0, to uid jest unikalne
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return false;  // W przypadku błędu, zakładamy, że uid nie jest unikalne
    }

    public void deleteEmployeeByID(Connection connection, String employeeID) {
        String deletePracownikSQL = "DELETE FROM pracownik WHERE id_pracownika = ?";
        String deleteDaneLogowaniaSQL = "DELETE FROM dane_logowania WHERE uid = (SELECT pracownik.dane_logowania_uid FROM pracownik WHERE id_pracownika = ?)";

        try (PreparedStatement deletePracownikStatement = connection.prepareStatement(deletePracownikSQL);
             PreparedStatement deleteDaneLogowaniaStatement = connection.prepareStatement(deleteDaneLogowaniaSQL)) {

            // Usuń dane logowania
            deleteDaneLogowaniaStatement.setString(1, employeeID);
            int rowsAffectedDaneLogowania = deleteDaneLogowaniaStatement.executeUpdate();
            System.out.println("Usunięto dane logowania. Liczba usuniętych wierszy z tabeli 'dane_logowania': " + rowsAffectedDaneLogowania);

            // Usuń pracownika
            deletePracownikStatement.setString(1, employeeID);
            int rowsAffectedPracownik = deletePracownikStatement.executeUpdate();
            System.out.println("Usunięto pracownika. Liczba usuniętych wierszy z tabeli 'pracownik': " + rowsAffectedPracownik);


            if (rowsAffectedPracownik > 0 || rowsAffectedDaneLogowania > 0) {
                System.out.println("Pracownik usunięty wraz z danymi logowania.");
            } else {
                System.out.println("Nie znaleziono pracownika o podanym ID.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void deleteClientByLogin(Connection connection, String clientLogin) {
        String deleteClientSQL = "DELETE FROM klient WHERE dane_logowania_uid IN (SELECT uid FROM dane_logowania WHERE login = ?)";
        String deleteDaneLogowaniaSQL = "DELETE FROM dane_logowania WHERE login = ?";

        try (PreparedStatement deleteClientStatement = connection.prepareStatement(deleteClientSQL);
             PreparedStatement deleteDaneLogowaniaStatement = connection.prepareStatement(deleteDaneLogowaniaSQL)) {

            // Usuń klienta
            deleteClientStatement.setString(1, clientLogin);
            int rowsAffectedClient = deleteClientStatement.executeUpdate();
            System.out.println("Usunięto klienta. Liczba usuniętych wierszy z tabeli 'klient': " + rowsAffectedClient);

            // Usuń dane logowania
            deleteDaneLogowaniaStatement.setString(1, clientLogin);
            int rowsAffectedDaneLogowania = deleteDaneLogowaniaStatement.executeUpdate();
            System.out.println("Usunięto dane logowania. Liczba usuniętych wierszy z tabeli 'dane_logowania': " + rowsAffectedDaneLogowania);

            if (rowsAffectedClient > 0 || rowsAffectedDaneLogowania > 0) {
                System.out.println("Klient usunięty wraz z danymi logowania.");
            } else {
                System.out.println("Nie znaleziono klienta o podanym loginie.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }


    public void addNewEmployeeWithLogin(Connection connection, String imie, String nazwisko, String accountType , String pesel, String adres, String phoneNumber, int dochod, int branchId) {
        String uid = UUID.randomUUID().toString();

        if (isUidUnique(connection, uid)) {
            String insertDaneLogowaniaQuery = "INSERT INTO dane_logowania (uid, login, haslo, rodzaj_konta) VALUES (?, ?, ?, ?)";
            String insertPracownikQuery = "INSERT INTO pracownik (imie,nazwisko,pesel,adres,nr_tel,dochod,oddzial_id_oddzialu,dane_logowania_uid,status) VALUES (?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement insertDaneLogowaniaStatement = connection.prepareStatement(insertDaneLogowaniaQuery);
                 PreparedStatement insertPracownikStatement = connection.prepareStatement(insertPracownikQuery)) {

                connection.setAutoCommit(false);
                String login = imie + "_" + nazwisko;
                String password = imie + "_" + nazwisko + "_" + uid.substring(0,5);

                insertDaneLogowaniaStatement.setString(1, uid);
                insertDaneLogowaniaStatement.setString(2,login );
                insertDaneLogowaniaStatement.setString(3, password);
                insertDaneLogowaniaStatement.setString(4, accountType);
                insertDaneLogowaniaStatement.executeUpdate();

                String daneLogowaniaUid = getDaneLogowaniaUid(connection, login, password);

                insertPracownikStatement.setString(1,imie);
                insertPracownikStatement.setString(2, nazwisko);
                insertPracownikStatement.setString(3,pesel);
                insertPracownikStatement.setString(4,adres);
                insertPracownikStatement.setString(5,phoneNumber);
                insertPracownikStatement.setInt(6,dochod);
                insertPracownikStatement.setInt(7,branchId);
                insertPracownikStatement.setString(8, daneLogowaniaUid);
                insertPracownikStatement.setString(9,"pracownik");
                insertPracownikStatement.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println("UID is not unique. Choose a different UID.");
        }
    }

    private static boolean isLoginExists(Connection connection, String login) {
        String sql = "SELECT COUNT(*) FROM dane_logowania WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;

        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public String getDaneLogowaniaUid(Connection connection, String enteredUsername, String enteredPassword) throws SQLException {
        String selectDaneLogowaniaUidSQL = "SELECT uid FROM dane_logowania WHERE login = ? AND haslo = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectDaneLogowaniaUidSQL)) {
            preparedStatement.setString(1, enteredUsername);
            preparedStatement.setString(2, enteredPassword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Zwróć UID jako identyfikator danych logowania
                    return resultSet.getString("uid");
                } else {
                    throw new SQLException("Nie udało się pobrać UID danych logowania.");
                }
            }
        }
    }


    private static List<String> executeSqlQueryForList(Connection connection, String sql, String columnName) {
        List<String> resultList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                resultList.add(resultSet.getString(columnName));
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return resultList;
    }

    private static void handleSQLException(SQLException e) {
        e.printStackTrace();
        System.out.println("Błąd SQL: " + e.getMessage());
    }

    public void addNewClientWithLogin(Connection connection, String login, String password, String name, String surname, String phoneNumber) {
        String uid = UUID.randomUUID().toString();

        if (isUidUnique(connection, uid)) {
            String insertDaneLogowaniaQuery = "INSERT INTO dane_logowania (uid, login, haslo, rodzaj_konta) VALUES (?, ?, ?, ?)";
            String insertKlientQuery = "INSERT INTO klient (dane_logowania_uid,imie,nazwisko,nr_tel) VALUES (?,?,?,?)";

            try (PreparedStatement insertDaneLogowaniaStatement = connection.prepareStatement(insertDaneLogowaniaQuery);
                 PreparedStatement insertKlientStatement = connection.prepareStatement(insertKlientQuery)) {

                connection.setAutoCommit(false);

                insertDaneLogowaniaStatement.setString(1, uid);
                insertDaneLogowaniaStatement.setString(2, login);
                insertDaneLogowaniaStatement.setString(3, password);
                insertDaneLogowaniaStatement.setString(4, "klient");
                insertDaneLogowaniaStatement.executeUpdate();

                String daneLogowaniaUid = getDaneLogowaniaUid(connection, login, password);

                insertKlientStatement.setString(1, daneLogowaniaUid);
                insertKlientStatement.setString(2,name);
                insertKlientStatement.setString(3,surname);
                insertKlientStatement.setString(4,phoneNumber);

                insertKlientStatement.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println("UID is not unique. Choose a different UID.");
        }
    }

    public String getClientDataByLoginAndPassword(Connection connection, String enteredUsername, String enteredPassword) {

        StringBuilder result = new StringBuilder();
        String sql = "SELECT klient.id_klienta, klient.imie, klient.nazwisko,klient.nr_tel FROM klient " +
                "JOIN dane_logowania ON klient.dane_logowania_uid = dane_logowania.uid " +
                "WHERE dane_logowania.login = ? AND dane_logowania.haslo = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, enteredUsername);
            preparedStatement.setString(2, enteredPassword);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int clientId = resultSet.getInt("id_klienta");
                String name = resultSet.getString("imie");
                String surname = resultSet.getString("nazwisko");
                String nr_tel = resultSet.getString("nr_tel");

                result.append(clientId).append("\n");
                result.append(name).append("\n");
                result.append(surname).append("\n");
                result.append(nr_tel).append("\n");
            }


        } catch (SQLException e) {
            handleSQLException(e);
        }
        return result.toString();
    }

    public  String getAccountDetails(Connection connection, String enteredUsername, String enteredPassword) {

        StringBuilder result = new StringBuilder();
        String sql = "SELECT dane_logowania.rodzaj_konta, " +
                "CASE WHEN klient.id_klienta IS NOT NULL THEN klient.id_klienta " +
                "     WHEN pracownik.id_pracownika IS NOT NULL THEN pracownik.id_pracownika END AS id, " +
                "CASE WHEN klient.id_klienta IS NOT NULL THEN klient.imie " +
                "     WHEN pracownik.id_pracownika IS NOT NULL THEN pracownik.imie END AS imie, " +
                "CASE WHEN klient.id_klienta IS NOT NULL THEN klient.nazwisko " +
                "     WHEN pracownik.id_pracownika IS NOT NULL THEN pracownik.nazwisko END AS nazwisko, " +
                "CASE WHEN klient.id_klienta IS NOT NULL THEN klient.nr_tel " +
                "     WHEN pracownik.id_pracownika IS NOT NULL THEN pracownik.nr_tel  END AS nr_tel " +
                "FROM dane_logowania " +
                "LEFT JOIN klient ON dane_logowania.uid = klient.dane_logowania_uid " +
                "LEFT JOIN pracownik ON dane_logowania.uid = pracownik.dane_logowania_uid " +
                "WHERE dane_logowania.login = ? AND dane_logowania.haslo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, enteredUsername);
            preparedStatement.setString(2, enteredPassword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String accountType = resultSet.getString("rodzaj_konta");
                    String accountId = resultSet.getString("id");
                    String firstName = resultSet.getString("imie");
                    String lastName = resultSet.getString("nazwisko");
                    String phoneNumber = resultSet.getString("nr_tel");

                    result.append(accountId).append("\n");
                    result.append(firstName).append("\n");
                    result.append(lastName).append("\n");
                    result.append(accountType).append("\n");
                    result.append(phoneNumber).append("\n");

                } else {
                    return "Unknown";
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return "Error";
        }
        return result.toString();
    }

}
