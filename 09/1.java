class Circle {
    double rad = 0; // 원의 반지름름
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
    public double getArea() {
        return (rad * rad) * PI; // 원의 넓이 반환
    }
}

class UnsafeCircle {
    public static void main(String args[]) {
        Circle c = new Circle(1.5);
        System.out.println(c.getArea());

        c.setRad(2.5);
        System.out.println(c.getArea());
        c.setRad(-3.3);
        System.out.println(c.getArea());
        c.rad = -4.5; // 옳지 않은 접근 방법, 그리고 문제가 되는 부분
        System.out.println(c.getArea());
    }
}