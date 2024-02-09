package com.example.salon_fryzjerski_projekt;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private static String dbUrl = "jdbc:mysql://localhost:3306/projekt1";
    private static String dbUser = "root";
    private static String dbPassword = "";

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            RequestType requestType = RequestType.valueOf(in.readLine());

            String employeeId, login,password,name,surname,clientId,branchId,phoneNumber;
            LoginService loginService = new LoginService();
            BranchService branchService = new BranchService();
            BookingTypeService hairdressingServiceService = new BookingTypeService();
            ReservationService reservationService = new ReservationService();
            EmployeeService employeeAndDepartmentService = new EmployeeService();

            try (Connection connection = getConnection()) {
                switch (requestType) {
                    case UserData:
                        login = in.readLine();
                        password = in.readLine();
                        name = in.readLine();
                        surname = in.readLine();
                        String phoneNubmer = in.readLine();
                        loginService.addNewClientWithLogin(connection, login, password, name, surname, phoneNubmer);
                        break;
                    case ChangeBranchTime:
                        String brancId = in.readLine();
                        String openingTime = in.readLine();
                        String closeingTime = in.readLine();
                        branchService.updateBranchHours(connection, Integer.parseInt(brancId), openingTime, closeingTime);
                        break;
                    case DeleteEmployee:
                        employeeId = in.readLine();
                        loginService.deleteEmployeeByID(connection,employeeId);
                        break;
                    case GetBranch:
                       out.println((branchService.getBranches(connection)));
                       break;
                    case GetEmployeeByBranch:
                        String branchName = in.readLine();
                        out.println(employeeAndDepartmentService.getEmployeesByDepartment(connection,branchName));
                        break;
                    case GetCity:
                        out.println(branchService.getCity(connection));
                        break;
                    case GetBranchesInCity:
                        String cityName = in.readLine();
                        out.println(branchService.getBranchesInCity(connection,cityName));
                        break;
                    case GetHairdressingService:
                        out.println(hairdressingServiceService.getAllAvailableServices(connection));
                        break;
                    case GetOneEmployeeByBranch:
                        String branchName1 = in.readLine();
                        out.println(employeeAndDepartmentService.getOneEmployeesByDepartment(connection,branchName1));
                        break;
                    case DeleteReservation:
                        String reservationId = in.readLine();
                        reservationService.deleteReservation(connection,Integer.parseInt(reservationId));
                        break;
                    case GetAvailableTimeSlotsForEmployee:
                        employeeId = in.readLine();
                        String date = in.readLine();
                        out.println(reservationService.getAvailableTimeSlotsForEmployee(connection,Integer.parseInt(employeeId),date));
                        break;
                    case GetUserData:
                        login = in.readLine();
                        password = in.readLine();
                        out.println(loginService.getAccountDetails(connection, login, password));
                        break;
                    case AddEmployee:
                        login = in.readLine();
                        password = in.readLine();
                        String status = in.readLine();
                        String pesel = in.readLine();
                        String adres = in.readLine();
                        phoneNumber = in.readLine();
                        String dochod = in.readLine();
                        branchId = in.readLine();
                        loginService.addNewEmployeeWithLogin(connection,login,password,status,pesel,adres,phoneNumber,Integer.parseInt(dochod),Integer.parseInt(branchId));
                        break;
                    case AddReservation:
                        try {
                            String data = in.readLine();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date utilDate = sdf.parse(data);
                            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                            String godzinaPoczatkowa = in.readLine();
                            String godzinaKoncowa = in.readLine();
                            name = in.readLine();
                            surname = in.readLine();
                            String phone = in.readLine();
                            clientId = in.readLine();
                            employeeId = in.readLine();
                            String serviceId = in.readLine();
                            branchId = in.readLine();
                            int employeeIdInt = employeeId.isEmpty() ? 0 : Integer.parseInt(employeeId);
                            int serviceIdInt = serviceId.isEmpty() ? 0 : Integer.parseInt(serviceId);
                            int branchIdInt = branchId.isEmpty() ? 0 : Integer.parseInt(branchId);
                            reservationService.createReservation(connection, sqlDate, godzinaPoczatkowa, godzinaKoncowa, name, surname, phone, clientId, employeeIdInt, serviceIdInt, branchIdInt);
                        } catch (NumberFormatException e) {
                            System.out.println("NumberFormatException: One of the inputs is not a valid integer.");
                            // Handle the exception, maybe log it or inform the user
                        } catch (IOException e) {
                            System.out.println("IOException occurred: " + e.getMessage());
                            // Handle the IOException
                        }
                        break;
                    case GetClientReservation:
                        clientId = in.readLine();
                        out.println(reservationService.getClientReservation(connection,Integer.parseInt(clientId)));
                        break;
                    case GetEmployeeReservation:
                        employeeId = in.readLine();
                        out.println(reservationService.getEmployeeReservation(connection,Integer.parseInt(employeeId)));
                        break;
                    case GetBookingDetails:
                        reservationId = in.readLine();
                        out.println(reservationService.getBookingDetails(connection,Integer.parseInt(reservationId)));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

}