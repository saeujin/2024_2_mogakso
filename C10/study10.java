package C10;

class Car {
    void myCar() {
        System.out.println("this is my car");
    }
    public static void main(String[] args) {
		Car c = new Car();
		c.myCar();
		Boat t = new Boat();
		t.myBoat();
    }   
}

class Boat {
    void myBoat() {
        System.out.println("This is my boat");
    }
}