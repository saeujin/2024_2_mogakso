interface Printable {
    public void print(String doc);
}

class Printer implements Printable {
    @Override
    public void print(String doc) {
        System.out.println(doc);
    }
}

public class study {
    public static void main(String[] args) {

    }
}