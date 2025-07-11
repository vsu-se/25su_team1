public class Employee {
    private String name;
    private String position;
    private int id;


    
    public Employee(String name, String position) {
        if (name.length() == 0)
            throw new RuntimeException("name must have length >0");
        if (position.length() == 0)
            throw new RuntimeException("position must have length >0");

        this.name = name;
        this.position = position;
    }


    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }
}
