import java.util.*;
import java.io.*;

public class ParkingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParkingData.setting();     // 설정 파일 읽기
        ParkingLot.init();         // 주차장 공간 초기화

        while (true) {
            String cmd = scanner.next();
            if (cmd.equals("q")) break;

            int number = 0, y = 0, m = 0, d = 0, h = 0, min = 0;

            switch (cmd) {
                case "e":
                    number = scanner.nextInt();
                    y = scanner.nextInt(); m = scanner.nextInt(); d = scanner.nextInt();
                    h = scanner.nextInt(); min = scanner.nextInt();
                    if (!TimeValidator.isValid(y, m, d, h, min)) {
                        System.out.println("시간 정보가 바르지 않습니다.");
                        break;
                    }
                    ParkingLot.enter(number, y, m, d, h, min);
                    break;
                case "x":
                    number = scanner.nextInt();
                    y = scanner.nextInt(); m = scanner.nextInt(); d = scanner.nextInt();
                    h = scanner.nextInt(); min = scanner.nextInt();
                    if (!TimeValidator.isValid(y, m, d, h, min)) {
                        System.out.println("시간 정보가 바르지 않습니다.");
                        break;
                    }
                    ParkingLot.exit(number, y, m, d, h, min);
                    break;
                case "v":
                    ParkingLot.view();
                    break;
                case "i":
                    y = scanner.nextInt(); m = scanner.nextInt();
                    if (!TimeValidator.isValidMonth(y, m)) {
                        System.out.println("시간 정보가 바르지 않습니다.");
                        break;
                    }
                    ParkingLot.printIncome(y, m);
                    break;
                default:
                    System.out.println("명령이 유효하지 않습니다.");
            }
        }

        scanner.close();
    }
}

class RegularVehicle {
    int number;
    String id, department, name;
    int year, month, day, hour, minute;

    public static String toString(RegularVehicle r) {
        return r.number + " " + r.year + " " + r.month + " " + r.day + " " +
               r.hour + " " + r.minute + " " + r.id + " " + r.department + " " + r.name;
    }
}

class Vehicle {
    int number;
    int year, month, day, hour, minute;

    public static String toString(Vehicle v) {
        return v.number + " " + v.year + " " + v.month + " " + v.day + " " +
               v.hour + " " + v.minute;
    }
}

class ParkingData {
    static int max, fee, minfee, count;
    static RegularVehicle[] c;

    public static void setting() {
        Scanner in = null;
        try {
            in = new Scanner(new File("parking.txt"));
        } catch (Exception e) {
            // 실제로는 예외 안 씀, 무조건 있다고 가정
        }

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
    static RegularVehicle[] reg;
    static Vehicle[] vis;
    static int regCount = 0, visCount = 0, total = 0;

    static int[][] monthlyVisitIncome = new int[3000][13];

    public static void init() {
        reg = new RegularVehicle[ParkingData.max];
        vis = new Vehicle[ParkingData.max];
    }

    public static void enter(int num, int y, int m, int d, int h, int min) {
        if (total >= ParkingData.max) {
            System.out.println("주차 불가");
            return;
        }
        if (isAlreadyParkedreg(num)) {
            System.out.println("정기주차 차량 " + num + "는(은) 이미 입차한 차량입니다!");
            return;
        }
        if (isAlreadyParkedvis(num)) {
            System.out.println("방문차량 " + num + "는(은) 이미 입차한 차량입니다!");
            return;
        }

        RegularVehicle rv = findRegular(num);
        if (rv != null) {
            RegularVehicle now = new RegularVehicle();
            now.number = rv.number;
            now.id = rv.id;
            now.department = rv.department;
            now.name = rv.name;
            now.year = y; now.month = m; now.day = d; now.hour = h; now.minute = min;
            reg[regCount++] = now;
            total++;
            System.out.println("정기주차 차량 " + num + "가(이) 입차하였습니다!");
        } else {
            Vehicle v = new Vehicle();
            v.number = num;
            v.year = y; v.month = m; v.day = d; v.hour = h; v.minute = min;
            vis[visCount++] = v;
            total++;
            System.out.println("방문주차 차량 " + num + "가(이) 입차하였습니다!");
        }
    }

    public static void exit(int num, int y, int m, int d, int h, int min) {
        for (int i = 0; i < regCount; i++) {
            if (reg[i].number == num) {
                System.out.println("정기주차 차량 " + num + "가(이) 출차하였습니다!");
                removeRegular(i);
                return;
            }
        }

        for (int i = 0; i < visCount; i++) {
            if (vis[i].number == num) {
                int minutes = TimeHelper.calcMinutes(
                    vis[i].year, vis[i].month, vis[i].day, vis[i].hour, vis[i].minute,
                    y, m, d, h, min
                );
                int units = (minutes + 9) / 10;
                int fee = units * ParkingData.minfee;
                monthlyVisitIncome[y][m] += fee;

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
        regCount--; total--;
    }

    public static void removeVisit(int index) {
        for (int i = index; i < visCount - 1; i++) vis[i] = vis[i + 1];
        visCount--; total--;
    }

    public static boolean isAlreadyParkedreg(int num) {
        for (int i = 0; i < regCount; i++) if (reg[i].number == num) return true;
        return false;
    }

    public static boolean isAlreadyParkedvis(int num) {
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
        Arrays.sort(reg, 0, regCount, (a, b) -> Integer.compare(a.number, b.number));
        Arrays.sort(vis, 0, visCount, (a, b) -> Integer.compare(a.number, b.number));
        System.out.println("- 정기주차 차량");
        for (int i = 0; i < regCount; i++) {
            System.out.println("  " + (i + 1) + " " + RegularVehicle.toString(reg[i]));
        }
        System.out.println("- 방문주차 차량");
        for (int i = 0; i < visCount; i++) {
            System.out.println("  " + (i + 1) + " " + Vehicle.toString(vis[i]));
        }
    }

    public static void printIncome(int y, int m) {
        int regularIncome = (ParkingData.fee / 6) * ParkingData.count;
        int visitIncome = monthlyVisitIncome[y][m];
        int total = regularIncome + visitIncome;
        System.out.println("총수입(" + y + "년 " + m + "월): " + total + "원");
        System.out.println("  - 정기주차 차량: " + regularIncome + "원");
        System.out.println("  - 방문주차 차량: " + visitIncome + "원");
    }
}

class TimeValidator {
    public static boolean isValid(int y, int m, int d, int h, int min) {
        if (y < 1 || m < 1 || m > 12 || d < 1 || h < 0 || h > 23 || min < 0 || min > 59)
            return false;
        int[] daysInMonth = { 31, isLeap(y) ? 29 : 28, 31, 30, 31, 30,
                              31, 31, 30, 31, 30, 31 };
        return d <= daysInMonth[m - 1];
    }

    public static boolean isValidMonth(int y, int m) {
        return y > 0 && m >= 1 && m <= 12;
    }

    public static boolean isLeap(int y) {
        return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0);
    }
}

class TimeHelper {
    private static int[] daysInMonth = { 31, 28, 31, 30, 31, 30,
                                         31, 31, 30, 31, 30, 31 };

    public static int toMinutes(int y, int m, int d, int h, int min) {
        int totalDays = 0;

        // 연도별 일수 누적
        for (int i = 1; i < y; i++) totalDays += isLeap(i) ? 366 : 365;

        // 월별 일수 누적
        for (int i = 1; i < m; i++) {
            if (i == 2 && isLeap(y)) totalDays += 29;
            else totalDays += daysInMonth[i - 1];
        }

        totalDays += (d - 1);
        return totalDays * 24 * 60 + h * 60 + min;
    }

    public static int calcMinutes(int y1, int m1, int d1, int h1, int min1,
                                  int y2, int m2, int d2, int h2, int min2) {
        return toMinutes(y2, m2, d2, h2, min2) - toMinutes(y1, m1, d1, h1, min1);
    }

    private static boolean isLeap(int y) {
        return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0);
    }
}
