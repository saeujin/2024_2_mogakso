package C10;

public class Study10 {
    static int a = 0;
    static void add(int n) {
        a += n;
    }
    static void showResult() {
        System.out.println("sum = " + a);
    }
    public static void main(String[] args) {
        for(int i = 0; i < 10; i++) 
            Study10.add(i);
        Study10.showResult();
    }   
}