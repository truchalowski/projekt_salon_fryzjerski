package com.example.salon_fryzjerski_projekt;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private static Socket socket;

    public static void main(String[] args) {

        try {

            socket = new Socket("localhost", 12346);

        } catch (IOException e) {
            e.printStackTrace();
        }
         HelloApplication.main(args);
    }

    private static void CreateSocket() {
        try {
            if(socket.isClosed())
                socket = new Socket("localhost", 12346);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendRequest(RequestType requestType, String... params) {
        CreateSocket();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            StringBuilder request = new StringBuilder(requestType.toString() + "\n");
            for (String param : params) {
                request.append(param).append("\n");
            }
            out.println(request.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Branch> getBranches(RequestType requestType){

        List<Branch> branchList = new ArrayList<>();
        CreateSocket();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            String id, name, street;
            while((id = in.readLine()) != null && (name = in.readLine()) != null && (street = in.readLine()) != null){
                    branchList.add(new Branch(Integer.parseInt(id),name,street));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return branchList;
    }

    public static List<Booking> getBookingInfo(RequestType requestType, int userId){
        CreateSocket();

        List<Booking> bookingList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            out.println(userId);
            String id,date,time,type;
            while((id = in.readLine()) != null && (date = in.readLine()) != null
                    && (time = in.readLine()) != null && (type = in.readLine()) != null){
                bookingList.add(new Booking(Integer.parseInt(id),date,time,type));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    public static BookingDetails getBookingDetalis(RequestType requestType, int reservationId){
        CreateSocket();

        BookingDetails booking = new BookingDetails("Krakow1","Zablocie1","Krakow","Strzyżenie Męskie","Strzyżenie Męskie","00:30:00","Krzysztof"," Nowak", "Ola", "Nowak","123456789","2024-01-17","10:00","85" + " zł");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            out.println(reservationId);
            String branchName ,branchStreet,cityName,bookingType,bookingDesc,bookingDuration,employeeName,employeeSurname,clientName, clientSurname, phoneNumber,stringDate,startTime,bookingPrice;
            while((branchName = in.readLine()) != null && (branchStreet = in.readLine()) != null && (cityName = in.readLine()) != null
                    && (bookingType = in.readLine()) != null && (bookingDesc = in.readLine()) != null && (bookingDuration = in.readLine()) != null
                    && (employeeName = in.readLine()) != null && (employeeSurname = in.readLine()) != null && (clientName = in.readLine()) != null
                    && (clientSurname = in.readLine()) != null && (phoneNumber = in.readLine()) != null && (stringDate = in.readLine()) != null
                && (startTime = in.readLine()) != null && (bookingPrice = in.readLine()) != null){

                booking = new BookingDetails(branchName,branchStreet,cityName,bookingType,bookingDesc,bookingDuration,employeeName,employeeSurname,clientName,clientSurname,phoneNumber,stringDate,startTime,bookingPrice);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return booking;
    }

    public static List<Employee> getEmployee(RequestType requestType, String branchName){

        List<Employee> branchList = new ArrayList<>();
        CreateSocket();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            out.println(branchName);
            String id, name, surname;
            while((id = in.readLine()) != null && (name = in.readLine()) != null &&  (surname = in.readLine()) != null){
                branchList.add(new Employee(Integer.parseInt(id),name,surname," "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return branchList;
    }

    public static List<City> getCity(RequestType requestType){

        List<City> cityList = new ArrayList<>();
        CreateSocket();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            String id, name;
            while((id = in.readLine()) != null && (name = in.readLine()) != null){
                cityList.add(new City(Integer.parseInt(id),name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    public static List<Branch> getBranchesInCity(RequestType requestType, String cityName){

        List<Branch> branchList = new ArrayList<>();
        CreateSocket();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            out.println(cityName);
            String id, name, street;
            while((id = in.readLine()) != null && (name = in.readLine()) != null && (street = in.readLine()) != null){
                branchList.add(new Branch(Integer.parseInt(id),name,street));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return branchList;
    }

    public static List<BookingType> getService(RequestType requestType){

        List<BookingType> serviceList = new ArrayList<>();
        CreateSocket();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            String id, name,desc, time,price;
            while((id = in.readLine()) != null && (name = in.readLine()) != null && (desc = in.readLine()) != null && (time = in.readLine()) != null && (price = in.readLine()) != null){
                serviceList.add(new BookingType(Integer.parseInt(id),name,desc,price,time));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serviceList;
    }

    public static List<String> getTimeSlots(RequestType requestType, int employeeId , String date){
        List<String> serviceList = new ArrayList<>();
        CreateSocket();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            out.println(employeeId);
            out.println(date);
            String line;
            while((line = in.readLine()) != null){
                System.out.println(line);
                serviceList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serviceList;
    }

    public static User getUserData(RequestType requestType, String login, String passoword){
        CreateSocket();

        User user = new User(100,"","","","");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(requestType.toString());
            out.println(login);
            out.println(passoword);
            String id, name, surname, type, phoneNumber;
            while((id = in.readLine()) != null && (name = in.readLine()) != null &&  (surname = in.readLine()) != null
                    &&  (type = in.readLine()) != null  &&  (phoneNumber = in.readLine()) != null) {
                user = new User(Integer.parseInt(id),name,surname,type,phoneNumber);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

}
