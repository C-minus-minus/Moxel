import processing.core.PApplet;
import processing.core.PImage;

public class TextureManager {

    PImage texture;
    PImage textures;
    PImage big;

    PImage grassFront,grassTop,grassBottom;

    public TextureManager(PApplet app){

        try{
            texture = app.loadImage("res/largeStone.jpg");
            textures = app.loadImage("res/textures.png");
        }catch (Exception e){}

        //  get grass front
        PImage tmp = textures.get(32*4,32*4,32,32);
        grassFront = new PImage(32*16,32*16);
        for(int i=0;i<16;i++){
            for(int a=0;a<16;a++){
                grassFront.set(i*32,a*32,tmp);

            }
        }

        //  get grass top
        tmp = textures.get(32*2,32*4,32,32);
        grassTop = new PImage(32*16,32*16);
        for(int i=0;i<16;i++){
            for(int a=0;a<16;a++){
                grassTop.set(i*32,a*32,tmp);

            }
        }

        //  get grass bottom
        tmp = textures.get(32*2,32*0,32,32);
        grassBottom = new PImage(32*16,32*16);
        for(int i=0;i<16;i++){
            for(int a=0;a<16;a++){
                grassBottom.set(i*32,a*32,tmp);

            }
        }

        /*
        big = new PImage(textures.width*16,textures.height*16);
        for(int i=0;i<16;i++){
            for(int a=0;a<16;a++){

                //  get current cell
                tmp = textures.get(i*32,a*32,32,32);

                for(int b=0;b<16;b++){
                    for(int c=0;c<16;c++){
                        big.set(i*32+b*32,a*32+c*32,tmp);
                    }
                }
            }
        }*/
    }

    public PImage getTexture(String type,int face){
        if(type.equals("grass")){
            switch(face){
                case 4:
                    return grassTop;
                case 5:
                    return grassBottom;
                default:
                    return grassFront;
            }
        }
        else if(type.equals("stone")){
            return texture;
        }

        return texture;
    }


}
