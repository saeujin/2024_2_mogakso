import java.util.*;
import java.io.*;


public class ParkingSystem2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParkingData.setting();
        while(1) {
            switch () {
                case value:
                    
                    break;
            
                default:
                    break;
            }
        }
        Car c = new Car();
    }
}
class RegularVehicle {
    static int number;
    static String id;
    static String department;
    static String name;
}
class Vehicle {
    private static int year, month, day, hour, minute;
    public static void setCar() {
        Scanner in = new Scanner(System.in);
        year = in.nextInt();
        month = in.nextInt();
        day = in.nextInt();
        hour = in.nextInt();
        minute = in.nextInt();
    }
}

class ParkingData {
    static int max, fee, minfee, count;
    static RegularVehicle c;
    static int number;
    static String id, department, name;
    public static void setting() {
        Scanner in = new Scanner(new File("src/parking.txt"));
        max = in.nextInt();
        fee = in.nextInt();
        minfee = in.nextInt();
        count = in.nextInt();
        RegularVehicle[] c = new RegularVehicle[count];
        for(int i = 0; i<count; i++) {
            number = in.nextInt();
            id = in.next();
            department = in.next();
            name = in.next();
            c[i] = RegularVehicle(number,id,department,name);
        }
    }       
}