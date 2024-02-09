package com.example.salon_fryzjerski_projekt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeService {

    public  String getEmployeesByDepartment(Connection connection, String departmentName) {

        StringBuilder result = new StringBuilder();

        String selectBranchesSQL = "SELECT pracownik.id_pracownika, pracownik.imie, pracownik.nazwisko" +
                " FROM pracownik" +
                " JOIN oddzial ON pracownik.oddzial_id_oddzialu = oddzial.id_oddzialu" +
                " WHERE oddzial.nazwa = ?";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectBranchesSQL);
             ) {
            selectStatement.setString(1, departmentName);

            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("id_pracownika");
                String employeeName = resultSet.getString("imie");
                String employeeSurname = resultSet.getString("nazwisko");

                result.append(employeeId).append("\n");
                result.append(employeeName).append("\n");
                result.append(employeeSurname).append("\n");


            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
        System.out.println(result.toString());

        return result.toString();
    }

    public String getOneEmployeesByDepartment(Connection connection, String departmentName){
        StringBuilder result = new StringBuilder();

        String selectBranchesSQL = "SELECT pracownik.id_pracownika, pracownik.imie, pracownik.nazwisko" +
                " FROM pracownik" +
                " JOIN oddzial ON pracownik.oddzial_id_oddzialu = oddzial.id_oddzialu" +
                " WHERE oddzial.nazwa = ?";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectBranchesSQL);
        ) {
            selectStatement.setString(1, departmentName);

            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int employeeId = resultSet.getInt("id_pracownika");
            String employeeName = resultSet.getString("imie");
            String employeeSurname = resultSet.getString("nazwisko");

            result.append(employeeId).append("\n");
            result.append(employeeName).append("\n");
            result.append(employeeSurname).append("\n");


        } catch (SQLException e) {
            handleSQLException(e);
        }
        System.out.println(result.toString());

        return result.toString();
    }

    private static void handleSQLException(SQLException e) {
        e.printStackTrace();
        System.out.println("Błąd SQL: " + e.getMessage());
    }

}
