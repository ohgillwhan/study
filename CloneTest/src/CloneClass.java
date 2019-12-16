public class CloneClass implements  Cloneable{
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CloneClass clone() throws CloneNotSupportedException {
        return (CloneClass) super.clone();
    }
}
