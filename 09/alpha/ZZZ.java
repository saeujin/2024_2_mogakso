package alpha;

class AAA {
    protected int num;
}   

public class ZZZ extends alpha.AAA { // 클래스 AAA가 alpha 패키지로 묶였으므로 alpha.AAA가 됨.
    public void init(int n) {
        num = n;
    }
}