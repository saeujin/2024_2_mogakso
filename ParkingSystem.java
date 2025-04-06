import java.util.*;
import java.io.*;

public class ParkingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParkingData.setting();     // parking.txt 파일에서 주차장 설정값과 정기 차량 정보 불러오기
        ParkingLot.init();         // parking.txt 파일의 주차장 공간에 맞춰 배열 초기화

        while (true) {
            String cmd = scanner.next(); // e x i v q 명령어 입력받음
            if (cmd.equals("q")) break; // q가 입력되면 종료

            int number = 0, y = 0, m = 0, d = 0, h = 0, min = 0; // 차량번호, 날짜, 시간 변수 선언/초기화

            switch (cmd) {
                case "e": //  e가 입력되었을 때 : 입차
                    number = scanner.nextInt();
                    y = scanner.nextInt();
                    m = scanner.nextInt();
                    d = scanner.nextInt();
                    h = scanner.nextInt();
                    min = scanner.nextInt();
                    // 차량 번호와 입차한 시간 입력받음
                    if (!TimeValidator.isValid(y, m, d, h, min)) { // 시간 유효성 검사
                        System.out.println("시간 정보가 바르지 않습니다."); //  유효하지 않다면 오류 메시지 출력하고 break
                        break;
                    }
                    ParkingLot.enter(number, y, m, d, h, min); // 입차처리
                    break;
                case "x": // x가 입력되었을 때 : 출차
                    number = scanner.nextInt();
                    y = scanner.nextInt();
                    m = scanner.nextInt();
                    d = scanner.nextInt();
                    h = scanner.nextInt();
                    min = scanner.nextInt();
                    // 차량 번호, 입차 시간 입력받음
                    if (!TimeValidator.isValid(y, m, d, h, min)) { // 시간 유효성 검사
                        System.out.println("시간 정보가 바르지 않습니다."); // 유효하지 않다면 오류메시지 출력하고 break
                        break;
                    }
                    ParkingLot.exit(number, y, m, d, h, min); // 출차
                    break;
                case "v": // v 입력 : 현재 주차된 차량 정보 보기    
                    ParkingLot.view(); // 차량 목록 출력
                    break;
                case "i": // i 입력 : 월별 수입 출력
                    y = scanner.nextInt(); m = scanner.nextInt(); // 년도와 월 입력
                    if (!TimeValidator.isValidMonth(y, m)) { // 년,월 유효성 검사
                        System.out.println("시간 정보가 바르지 않습니다."); // 유효하지 않다면 오류메시지 출력
                        break;
                    }
                    ParkingLot.printIncome(y, m); // 수입 출력
                    break;
                default:
                    System.out.println("명령이 유효하지 않습니다."); // 위 명령어에 모두 해당하지 않으면 유효하지 않은 명령어이므로 오류메시지 출력하고 while문 반복
            }
        }

        scanner.close();
    }
}

// 정기주차 차량 정보를 저장할 클래스
class RegularVehicle {
    int number; // 차량 번호 변수
    String id, department, name; // 차량 소유자의 id와 학과, 이름을 저장할 변수
    int year, month, day, hour, minute; // 입차한 연도,월,일,시,분

    // 정기주차 차량의 정보를 문자열로 반환하는 메소드
    public static String toString(RegularVehicle r) {
        return r.number + " " + r.year + " " + r.month + " " + r.day + " " +
               r.hour + " " + r.minute + " " + r.id + " " + r.department + " " + r.name;
    }
}

// 방문 차량 정보를 저장할 클래스
class VisitVehicle {
    int number; // 차량 번호
    int year, month, day, hour, minute; // 입차 년,월,일,시,분

    // 방문차량 정보를 문자열 형태로 변환하는 메소드
    public static String toString(VisitVehicle v) {
        return v.number + " " + v.year + " " + v.month + " " + v.day + " " +
               v.hour + " " + v.minute;
    }
}

// parking.txt 파일에서 주차장 설정값과 정기 차량 정보를 불러오는 클래스
class ParkingData {
    static int max, fee, minfee, count; // 주차장 전체 수용 가능 차량수, 정기주차 차량 6개월 요금, 방문차량 10분당 요금, 정기차량의 수
    static RegularVehicle[] c; // 정기차량 정보를 저장할 배열

    // 파일을 읽고 설정 정보를 저장할 메소드
    public static void setting() {
        Scanner in = null;
        try {
            in = new Scanner(new File("parking.txt"));   // parking.txt 파일을 읽을 scanner 선언언
        } catch (Exception e) {
            // 파일이 존재하지 않을 경우는 고려하지 않음
        }

        max = in.nextInt(); // 주차장 최대 가능 수용수
        fee = in.nextInt(); // 정기주차 요금
        minfee = in.nextInt(); // 방문주차 10분당 요금
        count = in.nextInt(); // 등록된 정기주차 차량의 수

        c = new RegularVehicle[count]; // count에 따라 정기주차 차량 배열 선언
        for (int i = 0; i < count; i++) {
            c[i] = new RegularVehicle(); // 새로운 RegularVehicle 생성
            c[i].number = in.nextInt(); // 차량 번호
            c[i].id = in.next(); // id
            c[i].department = in.next(); // 학과
            c[i].name = in.next(); // 이름
        }

        in.close();
    }
}

class ParkingLot {
    static RegularVehicle[] reg; // 정기주차 차량 배열
    static VisitVehicle[] vis; // 방문주차 차량 배열
    static int regCount = 0, visCount = 0, total = 0; // 정기주차 방문주차 차량 수, 현재 주차중인 차량 수

    static int[][] monthlyVisitIncome = new int[3000][13]; // 월별 방문주차 수입을 저장할 배열[연도][월].

    public static void init() {
        reg = new RegularVehicle[ParkingData.max]; // 주차장 최대 크기만큼 정기주차 배열 선언
        vis = new VisitVehicle[ParkingData.max]; // 주차장 최대 크기만큼 방문주차 배열 선언
    }

    public static void enter(int num, int y, int m, int d, int h, int min) {
        if (total >= ParkingData.max) { // 현재 차량 수가 최대이면
            System.out.println("주차 불가"); // 주차불가 출력하고 종료
            return;
        }
        if (isAlreadyParkedreg(num)) { // 만약 이미 입차된 정기주차 차량일 경우우
            System.out.println("정기주차 차량 " + num + "는(은) 이미 입차한 차량입니다!");
            return;
        }
        if (isAlreadyParkedvis(num)) { // 이미 입차된 방문주차 차량일 경우우
            System.out.println("방문차량 " + num + "는(은) 이미 입차한 차량입니다!");
            return;
        }

        RegularVehicle rv = findRegular(num); // 정기주차 차량인지 확인
        if (rv != null) { // 정기주차 차량인 경우
            RegularVehicle now = new RegularVehicle(); // 새로운 정기주차 차량 객체 생성
            now.number = rv.number;
            now.id = rv.id;
            now.department = rv.department;
            now.name = rv.name;
            // 차량 정보 복사
            now.year = y; now.month = m; now.day = d; now.hour = h; now.minute = min; // 입차 시각 설정
            reg[regCount++] = now; // 배열에 저장 후 정기주차 차량 수 카운트 증가
            total++; // 전체 주차 차량 수 증가
            System.out.println("정기주차 차량 " + num + "가(이) 입차하였습니다!"); // 입차 완료 메시지
        } else { // 정기주차 차량이 아닐 경우 = 방문주차 차량일 경우
            VisitVehicle v = new VisitVehicle(); // 방문주차 차량 객체 생성
            v.number = num; // 차량 번호 설정정
            v.year = y; v.month = m; v.day = d; v.hour = h; v.minute = min; // 입차 시각 설정
            vis[visCount++] = v; // 배열에 저장한 후 방문차량 수 카운트 증가
            total++; // 전체 주차 차량 수 카운트 증가가
            System.out.println("방문주차 차량 " + num + "가(이) 입차하였습니다!"); // 입차 완료 메시지 출력
        }
    }

    public static void exit(int num, int y, int m, int d, int h, int min) { // 출차
        for (int i = 0; i < regCount; i++) { // 정기주차 배열 탐색
            if (reg[i].number == num) { // 해당 차량 번호가 있는 경우
                System.out.println("정기주차 차량 " + num + "가(이) 출차하였습니다!"); // 정기주차 차량 출차 메시지 출력
                removeRegular(i); // 배열에서 제거
                return; // 종료
            }
        }

        for (int i = 0; i < visCount; i++) { // 방문주차 배열 탐색
            if (vis[i].number == num) { // 탐색 중 차량 번호가 일치하면
                int minutes = TimeHelper.calcMinutes( // 주차된 시간 계산
                    vis[i].year, vis[i].month, vis[i].day, vis[i].hour, vis[i].minute, // 입차시간
                    y, m, d, h, min // 출차 시간
                );
                int units = (minutes + 9) / 10; // 10분 단위로 요금을 부과해야 함.
                int fee = units * ParkingData.minfee; // 이후에 분당 요금을 곱해줌
                monthlyVisitIncome[y][m] += fee; // 헤당 월의 수입에 더해줌

                System.out.println("방문주차 차량 " + num + "가(이) 출차하였습니다!"); // 출차 메시지 출력
                System.out.println("주차시간: " + minutes + "분"); // 계산한 주차 시간
                System.out.println("주차요금: " + fee + "원"); // 곱해서 나온 주차 요금
                removeVisit(i); // 배열에서 차량 정보 제거
                return;
            }
        }

        System.out.println("입차하지 않은 차량입니다!"); // 둘다 아닐 경우 = 입차되지 않은 경우
    }
    
    // 정기주차 차량 배열에서 차량 제거하는 메소드
    public static void removeRegular(int index) {
        for (int i = index; i < regCount - 1; i++) reg[i] = reg[i + 1]; // 뒤 차량들을 하나씩 앞으로 당긴다.
        regCount--; total--; // 정기주차 차량 수와 전체 차량 수를 감소시킨다.
    }

    // 방문주차 차량 배열에서 차량 제거하는 메소드
    public static void removeVisit(int index) {
        for (int i = index; i < visCount - 1; i++) vis[i] = vis[i + 1]; // 뒤 차량들을 하나씩 앞으로 당긴다.
        visCount--; total--; // 방문주차 차량 수와 전체 차량 수 감소
    }

    public static boolean isAlreadyParkedreg(int num) { // 입차처리에서 이미 입차된 정기주차 차량인지 확인하는 메소드
        for (int i = 0; i < regCount; i++) if (reg[i].number == num) return true; // 동일한 번호가 존재한다면 true
        return false; // 아니면 false
    }

    public static boolean isAlreadyParkedvis(int num) { // 이미 입차된 방문주차 차량인지 확인
        for (int i = 0; i < visCount; i++) if (vis[i].number == num) return true; // 동일한 번호가 존재한다면 true
        return false; // 아니면 false
    }

    // 정기주차 차량인지 탐색하는 메소드
    public static RegularVehicle findRegular(int num) {
        for (int i = 0; i < ParkingData.count; i++) { // 정기주차 차량 개수만큼 반복
            if (ParkingData.c[i].number == num) return ParkingData.c[i]; // 정기주차 차량 리스트에서 탐색하여 발견되면 객체를 return
        }
        return null; // 없으면 null 반환
    }

    public static void view() { // 주차된 차량 정보 확인
        Arrays.sort(reg, 0, regCount, (a, b) -> Integer.compare(a.number, b.number)); // 정기주차 차량 오름차순 정렬
        Arrays.sort(vis, 0, visCount, (a, b) -> Integer.compare(a.number, b.number)); // 방문주차 차량 오름차순 정렬
        System.out.println("- 정기주차 차량목록");
        for (int i = 0; i < regCount; i++) {
            System.out.println("  " + (i + 1) + " " + RegularVehicle.toString(reg[i])); // 정기주차 차량 정보 출력
        }
        System.out.println("- 방문주차 차량목록");
        for (int i = 0; i < visCount; i++) {
            System.out.println("  " + (i + 1) + " " + VisitVehicle.toString(vis[i])); // 방문주차 차량 정보 출력
        }
    }

    public static void printIncome(int y, int m) { // 수입을 출력할 메소드
        int regularIncome = (ParkingData.fee / 6) * ParkingData.count; // 정기차량 수입 계산
        int visitIncome = monthlyVisitIncome[y][m]; // 해당 월의 방문차량 수입 계산
        int total = regularIncome + visitIncome; // 둘을 더해 총 수입을 계산
        // 수입 출력
        System.out.println("총수입(" + y + "년 " + m + "월): " + total + "원");
        System.out.println("  - 정기주차 차량: " + regularIncome + "원");
        System.out.println("  - 방문주차 차량: " + visitIncome + "원");
    }
}

// 시간 유효성 검사를 위한 클래스
class TimeValidator {
    // 전체 날짜와 시간이 유효한지 검사
    public static boolean isValid(int y, int m, int d, int h, int min) {
        if (y < 1 || m < 1 || m > 12 || d < 1 || h < 0 || h > 23 || min < 0 || min > 59)
            return false; // 연도:1이상 / 월:1~12 사이 / 일 : 1 이상 / 시 : 0~23 사이(24면 0시) / 분 : 0~59 사이
        int[] daysInMonth = { 31, isLeap(y) ? 29 : 28, 31, 30, 31, 30, // 각 월의 최대 일수를 담은 배열(2월은 윤년 여부에 따라 처리)
                              31, 31, 30, 31, 30, 31 };
        return d <= daysInMonth[m - 1]; // 입력된 일이 해당 월의 최대 일 수 이하인지 확인한다.
    }

    public static boolean isValidMonth(int y, int m) { // 년,월이 제대로 입력되었는지 확인할 메소드
        return y > 0 && m >= 1 && m <= 12; // 연도 : 1이상 / 월 : 1~12 사이
    }

    public static boolean isLeap(int y) { // 윤년인지 확인하는 메소드드
        return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0); // 4의 배수이면서 100의 배수가 아니면 : 윤년 / 400의 배수 : 윤년
    }
}

// 시간 계산을 위한 클래스
class TimeHelper {
    private static int[] daysInMonth = { 31, 28, 31, 30, 31, 30,
                                         31, 31, 30, 31, 30, 31 }; //  각 월의 최대 일수 저장한 배열(2월은 윤년에 따라 나중에 달라짐)

    public static int toMinutes(int y, int m, int d, int h, int min) { // 특정 날짜, 시간을 분 단위로 변환하는 메소드
        int totalDays = 0; // 전체 일수 저장할할 변수

        // 1년부터 y-1년까지 총 일 수 저장
        for (int i = 1; i < y; i++) totalDays += isLeap(i) ? 366 : 365; // 윤년이면 366, 아니면 365일

        // 1월부터 m-1월까지 총 일 수 저장
        for (int i = 1; i < m; i++) {
            if (i == 2 && isLeap(y)) totalDays += 29; // 윤년이면 2월에 29일
            else totalDays += daysInMonth[i - 1]; // 나머지는 배열에서 가져옴
        }

        // 해당 월의 일(d-1) 추가
        totalDays += (d - 1); // 당일은 아직 지나지 않아서 -1 해줌
        return totalDays * 24 * 60 + h * 60 + min; // 일수를 분으로 변환하여 더하기
    }

    // 두 날짜 / 시간의 차이를 분 단위로 계산할 메소드
    public static int calcMinutes(int y1, int m1, int d1, int h1, int min1,
                                  int y2, int m2, int d2, int h2, int min2) {
        return toMinutes(y2, m2, d2, h2, min2) - toMinutes(y1, m1, d1, h1, min1); // 출차할 때 분 수 - 입차할 때 분 수
    }

    private static boolean isLeap(int y) { // 윤년여부를 판단하는 메소드
        return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0); // 4의 배수이면서 100의 배수가 아니면 : 윤년 / 400의 배수이면 : 윤년년
    }
}
