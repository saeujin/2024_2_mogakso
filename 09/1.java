class Circle {
    private double rad = 0; // 원의 반지름름
    final double PI = 3.14;

    public Circle(double r) {
        setRad(r); // 아래에 정의된 setRad 메소드 호출을 통한 초기화
    }
    public void setRad(double r) {
        if(r<0) { // 반지름은 0보다 작을 수 없으므로
            rad = 0;
            return; // 이 위치에서 메소드 빠져나감
        }
        rad = r;
    }
    public double getRad() {
        return rad;
    }
    public double getArea() {
        return (rad * rad) * PI; // 원의 넓이 반환
    }
}

class UnsafeCircle {
    public static void main(String args[]) {
        Circle c = new Circle(1.5);
        System.out.println("반지름: " + c.getRad());
        System.out.println("넓 이 : " + c.getArea() + "\n");

        c.setRad(3.4);
        System.out.println("반지름 : " + c.getRad());
        System.out.println("넓 이 : " + c.getArea());
    }
}