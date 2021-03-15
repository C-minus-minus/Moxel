import processing.core.PApplet;
import processing.core.PVector;

public class Moxel extends PApplet {

    //  our world
    private World world;

    public static void main(String[] args) {
        PApplet.main("Moxel");
    }

    public void settings(){
        //fullScreen(P3D);
        size(800,800,P3D);
    }

    public void setup(){
        world = new World(this);
        //frameRate(500);
    }

    public void draw(){
        background(0,150,200);
        world.draw();
        pushMatrix();
        PVector p = world.player.position;
        translate(p.x,p.y,p.z);
        noFill();
        stroke(0);
        //sphere(100*world.chunk.render);
        popMatrix();
    }
}
