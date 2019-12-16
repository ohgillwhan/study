public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        CloneClass cloneClass = new CloneClass();
        cloneClass.setName("Soora");

        System.out.println(cloneClass.clone().getName());
    }
}
