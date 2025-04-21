public class study {
    public static void main(String[] args) {
        HybridWaterCar c = new HybridWaterCar(1,2,3);
        c.showCurrentGauge();
    }
}

class Car {
    int gasolineGauge;
    public Car(int i) {
        this.gasolineGauge = i;
    }
}

class HybridCar extends Car {
    int electricGauge;
    public HybridCar(int i,int j) {
        super(i);
        this.electricGauge = j;
    }
}

class HybridWaterCar extends HybridCar {
    int waterGauge;
    public HybridWaterCar(int i, int j, int k) {
        super(i,j);
        this.waterGauge = k;
    }
    public void showCurrentGauge() {
        System.out.println("잔여 가솔린: "+gasolineGauge);
        System.out.println("잔여 전기량: " +electricGauge);
        System.out.println("잔여 워터랑: "+waterGauge);
    }
}
