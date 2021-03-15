public class Block {

    boolean transparent;
    String type;

    public Block(String type){

        this.type = type;

        transparent = type.equals("air");

    }
}
