package C10;

class InstCnt {
    static int instNum = 0;

    InstCnt() {     // 생성자
        instNum++;      // static으로 선언된 변수의 값 증가
        System.out.println("인스턴스 생성: "+instNum);
    }
}

public class study10 {
    public static void main(String[] args) {
        InstCnt cnt1 = new InstCnt();
        InstCnt cnt2 = new InstCnt();
        InstCnt cnt3 = new InstCnt();
    }
}
