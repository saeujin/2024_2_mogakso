class BankAccount {
    String accNumber; // 계좌번호
    String ssNumber; // 주민번호
    int balance = 0; // 예금 잔액
    public BankAccount(String acc, String ss, int bal) { //  생성자자
        accNumber = acc;
        ssNumber = ss;
        balance = bal;
    }
    public int deposit(int amount) {
        balance += amount;
        return balance;
    }
    public int withdraw(int amount) {
        balance -= amount;
        return balance;
    }
    public int checkMyBalance() {
        System.out.println("계좌번호 : " + accNumber);
        System.out.println("주민번호 : " + ssNumber);
        System.out.println("잔    액 : " + balance + "\n");
        return balance;
    }
}
class BankAccountUniID {
    public static void main(String[] args) {
        BankAccount yoon = new BankAccount("12-34-89", "990-990-9090990", 10000);
        BankAccount park = new BankAccount("33-55-09", "77088-5959007", 10000);

        yoon.deposit(5000);
        park.deposit(3000);
        yoon.withdraw(2000);
        park.withdraw(2000);
        yoon.checkMyBalance();
        park.checkMyBalance();
    }
}