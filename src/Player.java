
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;
import queasycam.QueasyCam;

import java.lang.reflect.Field;

import static processing.core.PConstants.*;

public class Player extends QueasyCam {

    PApplet app;
    World world;

    private int selX,selY,selZ;
    private int pSelX,pSelY,pSelZ;

    PVector center;

    public Player(PApplet app,World world){
        super(app);
        center = null;


        this.app = app;
        app.noCursor();
        app.perspective(PApplet.PI/3,app.width/(float)app.height,0.01f,100000);
        this.world = world;
        app.registerMethod("mouseEvent",this);



        speed*=2;
    }


    public void mouseEvent(MouseEvent e){

        if(e.getAction()==MouseEvent.PRESS){
            if(app.mouseButton==app.LEFT){
                if(selX!=-1){
                    world.setBlock(selX/100,selY/100,selZ/100,"air");
                }
            }
            if(app.mouseButton==app.RIGHT){
                if(selX!=-1){
                    world.setBlock(pSelX/100,pSelY/100,pSelZ/100,"stone");
                }
            }
        }
    }

    private void castRay(){
        try {
            Field field = getClass().getSuperclass().getDeclaredField("center");
            field.setAccessible(true);
            center = (PVector)field.get(this);

        }catch (Exception e){
            e.printStackTrace();
        }

        int tselX = (int)(position.x+(center.x-position.x));
        int tselY = (int)(position.y+(center.y-position.y));
        int tselZ = (int)(position.z+(center.z-position.z));

        boolean tmp = false;
        for(int i=0;i<5000;i++){
            int x = (int)(position.x+(this.center.x-position.x)*i);
            int y = (int)(position.y+(this.center.y-position.y)*i);
            int z = (int)(position.z+(this.center.z-position.z)*i);
            if(x!=tselX||y!=tselY||z!=tselZ){
                pSelX = tselX;
                pSelY = tselY;
                pSelZ = tselZ;
                tselX = x;
                tselY = y;
                tselZ = z;
                if(!world.getBlockType(new PVector((x/100)/16,(z/100)/16),(x/100)%16,(y/100),(z/100)%16).equals("air")){
                    tmp = true;
                    break;
                }
            }
        }
        if(tmp){
            selX = tselX;
            selY = tselY;
            selZ = tselZ;
        }else{
            selX = -1;
        }
    }

    public void draw(){
        super.draw();

        castRay();

        speed = 3f * (1f/ (app.frameRate/60f));


        if(selX != -1){
            app.pushMatrix();
            app.translate(100*(((int)selX)/100)+52,100*(((int)selY)/100)+52,100*(((int)selZ)/100)+52);
            app.noFill();
            app.strokeWeight(10);
            app.stroke(0);
            app.box(104);
            app.popMatrix();
        }



        //  draw HUD
        app.pushMatrix();
        app.camera();
        app.hint(DISABLE_DEPTH_TEST);
        app.textSize(50);
        app.fill(255,0,0);
        app.text("FPS: "+(int)app.frameRate,0,50);
        app.text("X: "+(int)position.x,0,100,0);
        app.text("Y: "+(int)position.y,0,150,0);
        app.text("Z: "+(int)position.z,0,200,0);
        //app.text("Xc: "+x,0,250,0);
        //app.text("Yc: "+y,0,300,0);
        //app.text("Zc: "+z,0,350,0);
        app.strokeWeight(1);
        app.stroke(0,255,0);
        app.line(app.width/2,app.height/2-30,app.width/2,app.height/2+30);
        app.line(app.width/2-30,app.height/2,app.width/2+30,app.height/2);
        app.hint(ENABLE_DEPTH_TEST);
        app.popMatrix();

    }
}
