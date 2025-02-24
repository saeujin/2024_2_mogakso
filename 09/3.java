class SinivelCap {
    void take() { // 콧물 처치용 캡슐
        System.out.println("콧물이 싹~ 납니다.");
    }
}

class SneezeCap { // 재채기 처치용 캡슐
    void take() {
        System.out.println("재채기가 멎습니다.");
    }
}

class SnuffleCap { // 코막힘 처치용 캡슐
    void take() {
        System.out.println("코가 뻥 뚫립니다.");
    }
}

class SinusCap {
    SinivelCap siCap = new SinivelCap();
    SneezeCap szCap = new SneezeCap();
    SnuffleCap sfCap = new SnuffleCap();

    void take() {
        siCap.take();
        szCap.take();
        sfCap.take();
    }
}

class ColdPatient {
    void takeSinus(SinusCap cap) {
        cap.take();
    }
}

class BadEncapsulation {
    public static void main(String[] args) {
        ColdPatient suf = new ColdPatient();
        suf.takeSinus(new SinusCap());
    }
}