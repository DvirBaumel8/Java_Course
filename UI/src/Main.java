public class Main {
    public static void main(String[] args) {
        TransPoolManager transPoolManager = TransPoolManager.getTransPoolManagerInstance();
        transPoolManager.run();
    }
}