package syntaxtree;
public class Type {

 
    private String name;

    public Type(String name) {
        if(name != null) {
            this.name = name;
        }
    }

    public String retName() {
        return name;
    }



}