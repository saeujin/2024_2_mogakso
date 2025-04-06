// ParkingSystem.java

import java.util.*;
import java.io.*;

public class ParkingSystem {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ParkingData.setting();
    
        int number = 0, y = 0, m = 0, d = 0, h = 0, min = 0; // ✅ 여기 추가!
    
        while (true) {
            String cmd = scanner.next();
            if (cmd.equals("q")) break;
    
            switch (cmd) {
                case "e":
                    number = scanner.nextInt();
                    y = scanner.nextInt();
                    m = scanner.nextInt();
                    d = scanner.nextInt();
                    h = scanner.nextInt();
                    min = scanner.nextInt();
                    ParkingLot.enter(number, y, m, d, h, min);
                    break;
                case "x":
                    number = scanner.nextInt();
                    y = scanner.nextInt();
                    m = scanner.nextInt();
                    d = scanner.nextInt();
                    h = scanner.nextInt();
                    min = scanner.nextInt();
                    ParkingLot.exit(number, y, m, d, h, min);
                    break;
                case "v":
                    ParkingLot.view();
                    break;
                case "i":
                    y = scanner.nextInt();
                    m = scanner.nextInt();
                    ParkingLot.printIncome(y, m);
                    break;
                default:
                    System.out.println("알 수 없는 명령입니다.");
            }
        }
    
        scanner.close();
    }
    
}

class RegularVehicle {
    int number;
    String id;
    String department;
    String name;
    int year, month, day, hour, minute;

    public static String toString(RegularVehicle r) {
        return r.number + " " + r.year + " " + r.month + " " + r.day + " " + r.hour + " " + r.minute + " " + r.id + " " + r.department + " " + r.name;
    }
}

class Vehicle {
    int number;
    int year, month, day, hour, minute;

    public static String toString(Vehicle v) {
        return v.number + " " + v.year + " " + v.month + " " + v.day + " " + v.hour + " " + v.minute;
    }
}

class ParkingData {
    static int max, fee, minfee, count;
    static RegularVehicle[] c;

    public static void setting() throws FileNotFoundException {
        Scanner in = new Scanner(new File("src/parking.txt"));
        max = in.nextInt();
        fee = in.nextInt();
        minfee = in.nextInt();
        count = in.nextInt();
        c = new RegularVehicle[count];
        for (int i = 0; i < count; i++) {
            c[i] = new RegularVehicle();
            c[i].number = in.nextInt();
            c[i].id = in.next();
            c[i].department = in.next();
            c[i].name = in.next();
        }
        in.close();
    }
}

class ParkingLot {
    static int total = 0;
    static RegularVehicle[] reg = new RegularVehicle[100];
    static int regCount = 0;
    static Vehicle[] vis = new Vehicle[100];
    static int visCount = 0;
    static int visitIncome = 0;

    public static void enter(int num, int y, int m, int d, int h, int min) {
        if (total >= ParkingData.max) {
            System.out.println("공간 부족으로 입차하지 못하였습니다!");
            return;
        }

        if (isAlreadyParked(num)) {
            System.out.println("차량 " + num + "는(은) 이미 입차한 차량입니다!");
            return;
        }

        RegularVehicle rv = findRegular(num);
        if (rv != null) {
            RegularVehicle now = new RegularVehicle();
            now.number = rv.number;
            now.id = rv.id;
            now.department = rv.department;
            now.name = rv.name;
            now.year = y;
            now.month = m;
            now.day = d;
            now.hour = h;
            now.minute = min;
            reg[regCount++] = now;
            total++;
            System.out.println("정기주차 차량 " + num + "가(이) 입차하였습니다!");
        } else {
            Vehicle v = new Vehicle();
            v.number = num;
            v.year = y;
            v.month = m;
            v.day = d;
            v.hour = h;
            v.minute = min;
            vis[visCount++] = v;
            total++;
            System.out.println("방문주차 차량 " + num + "가(이) 입차하였습니다!");
        }
    }

    public static void exit(int num, int y, int m, int d, int h, int min) {
        for (int i = 0; i < regCount; i++) {
            if (reg[i].number == num) {
                System.out.println("정기주차 차량  " + num + "가(이) 출차하였습니다!");
                removeRegular(i);
                return;
            }
        }
        for (int i = 0; i < visCount; i++) {
            if (vis[i].number == num) {
                int minutes = calcMinutes(vis[i], y, m, d, h, min);
                int units = (int) Math.ceil(minutes / 10.0);
                int fee = units * ParkingData.minfee;
                visitIncome += fee;
                System.out.println("방문주차 차량 " + num + "가(이) 출차하였습니다!");
                System.out.println("주차시간: " + minutes + "분");
                System.out.println("주차요금: " + fee + "원");
                removeVisit(i);
                return;
            }
        }
        System.out.println("입차하지 않은 차량입니다!");
    }

    public static void removeRegular(int index) {
        for (int i = index; i < regCount - 1; i++) reg[i] = reg[i + 1];
        regCount--;
        total--;
    }

    public static void removeVisit(int index) {
        for (int i = index; i < visCount - 1; i++) vis[i] = vis[i + 1];
        visCount--;
        total--;
    }

    public static int calcMinutes(Vehicle v, int y, int m, int d, int h, int min) {
        Calendar in = Calendar.getInstance();
        in.set(v.year, v.month - 1, v.day, v.hour, v.minute);
        Calendar out = Calendar.getInstance();
        out.set(y, m - 1, d, h, min);
        long diff = out.getTimeInMillis() - in.getTimeInMillis();
        return (int) (diff / 60000);
    }

    public static boolean isAlreadyParked(int num) {
        for (int i = 0; i < regCount; i++) if (reg[i].number == num) return true;
        for (int i = 0; i < visCount; i++) if (vis[i].number == num) return true;
        return false;
    }

    public static RegularVehicle findRegular(int num) {
        for (int i = 0; i < ParkingData.count; i++) {
            if (ParkingData.c[i].number == num) return ParkingData.c[i];
        }
        return null;
    }

    public static void view() {
        System.out.println("정기주차 차량목록");
        for (int i = 0; i < regCount; i++) {
            System.out.println((i + 1) + " " + RegularVehicle.toString(reg[i]));
        }
        System.out.println("방문주차 차량목록");
        for (int i = 0; i < visCount; i++) {
            System.out.println((i + 1) + " " + Vehicle.toString(vis[i]));
        }
    }

    public static void printIncome(int y, int m) {
        int regularIncome = (ParkingData.fee / 6) * ParkingData.count;
        int total = regularIncome + visitIncome;
        System.out.println("총수입(" + y + "년 " + m + "월): " + total + "원");
        System.out.println("- 정기주차 차량: " + regularIncome + "원");
        System.out.println("- 방문주차 차량: " + visitIncome + "원");
    }
}
