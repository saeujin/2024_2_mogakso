class Point {
    int xPos, yPos;
    public Point(int x, int y) {
        xPos = x;
        yPos = y;
    }
    public void showPointInfo() {
        System.out.println("[" + xPos + ", " + yPos + "]");
    }
}

class Circle {
    void setCircle() {
        
    }
}

class Main {
    public static void main(String[] args) {
        Circle c = new Circle(2,2,4);
    }
}