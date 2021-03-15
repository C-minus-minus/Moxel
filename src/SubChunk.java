import com.jogamp.opengl.math.geom.Frustum;
import processing.core.*;

import java.util.ArrayList;

public class SubChunk {

    Chunk chunk;
    int index;

    ArrayList<Plane> meshes;
    ArrayList<PShape> meshShapes;
    PShape group;

    public SubChunk(Chunk chunk, int index){

        this.chunk = chunk;
        this.index = index;
        //meshShapes = new ArrayList<>();
    }

    public void draw(PApplet app){
        app.shape(group);
        /*
        for(PShape shape:meshShapes){
            app.shape(shape);
        }*/

        /*
        for(Plane mesh:meshes){
            app.beginShape();
            app.noFill();
            app.tint(mesh.tint);
            app.noStroke();
            app.texture(mesh.texture);
            app.textureMode(app.NORMAL);
            app.textureWrap(app.REPEAT);
            app.vertex(mesh.a.x*100,mesh.a.y*100,mesh.a.z*100,0,0);
            app.vertex(mesh.b.x*100,mesh.b.y*100,mesh.b.z*100,mesh.width,0);
            app.vertex(mesh.c.x*100,mesh.c.y*100,mesh.c.z*100,mesh.width,mesh.height);
            app.vertex(mesh.d.x*100,mesh.d.y*100,mesh.d.z*100,0,mesh.height);
            app.endShape();
        }*/


        /*int centerX = (int)(chunk.chunkPosition.x*16+8);
        int centerZ = (int)(chunk.chunkPosition.y*16+8);
        int centerY = index*16+8;
        app.pushMatrix();
        app.translate(centerX*100,centerY*100,centerZ*100);
        app.noFill();
        app.box(16*100,16*100,16*100);
        app.popMatrix();*/
    }

    public double distance(PVector playerPos){

        //  get coords of center of chunk
        int centerX = (int)(chunk.chunkPosition.x*16+8);
        int centerZ = (int)(chunk.chunkPosition.y*16+8);
        int centerY = index*16+8;
        return PApplet.dist(centerX,centerY,centerZ,playerPos.x,playerPos.y,playerPos.z);

    }

    public void genMesh(){
        meshShapes = new ArrayList<>();
        meshes = new ArrayList<>();
        for(int u=0;u<16;u++){

            //  generate masks
            boolean[][] front = new boolean[16][16];
            boolean[][] back = new boolean[16][16];
            boolean[][] left = new boolean[16][16];
            boolean[][] right = new boolean[16][16];
            boolean[][] top = new boolean[16][16];
            boolean[][] bottom = new boolean[16][16];
            for(int v=0;v<16;v++){
                for(int w=0;w<16;w++){

                    //  top bottom
                    if(index==0&&u==0){
                        top[v][w] = !chunk.blocks[v][16*index+u][w].transparent;
                    }
                    else{
                        top[v][w] = chunk.blocks[v][16*index+u-1][w].transparent&&!chunk.blocks[v][16*index+u][w].transparent;
                    }

                    if(index==15&&u==15){
                        bottom[v][w] = !chunk.blocks[v][16*index+u][w].transparent;
                    }
                    else{
                        bottom[v][w] = chunk.blocks[v][16*index+u+1][w].transparent&&!chunk.blocks[v][16*index+u][w].transparent;
                    }

                    //  front back
                    if(u==0){
                        PVector newPos = chunk.chunkPosition.copy();
                        newPos.y--;
                        String type = chunk.world.getBlockType(newPos,v,16*index+w,15);
                        front[v][w] = type.equals("air")&&!chunk.blocks[v][16*index+w][u].transparent;
                    }
                    else{
                        front[v][w] = chunk.blocks[v][16*index+w][u-1].transparent&&!chunk.blocks[v][16*index+w][u].transparent;
                    }

                    if(u==15){
                        PVector newPos = chunk.chunkPosition.copy();
                        newPos.y++;
                        String type = chunk.world.getBlockType(newPos,v,16*index+w,0);
                        back[v][w] = type.equals("air")&&!chunk.blocks[v][16*index+w][u].transparent;
                    }
                    else{
                        back[v][w] = chunk.blocks[v][16*index+w][u+1].transparent&&!chunk.blocks[v][16*index+w][u].transparent;
                    }

                    //  left right
                    if(u==0){
                        PVector newPos = chunk.chunkPosition.copy();
                        newPos.x--;
                        String type = chunk.world.getBlockType(newPos,15,16*index+w,v);
                        left[v][w] = type.equals("air")&&!chunk.blocks[u][16*index+w][v].transparent;
                        //left[v][w] = !chunk.blocks[u][16*index+w][v].transparent;
                    }
                    else{
                        left[v][w] = chunk.blocks[u-1][16*index+w][v].transparent&&!chunk.blocks[u][16*index+w][v].transparent;
                    }

                    if(u==15){
                        PVector newPos = chunk.chunkPosition.copy();
                        newPos.x++;
                        String type = chunk.world.getBlockType(newPos,0,16*index+w,v);
                        right[v][w] = type.equals("air")&&!chunk.blocks[u][16*index+w][v].transparent;

                        //right[v][w] = !chunk.blocks[u][16*index+w][v].transparent;
                    }
                    else{
                        right[v][w] = chunk.blocks[u+1][16*index+w][v].transparent&&!chunk.blocks[u][16*index+w][v].transparent;
                    }
                }
            }

            //  get meshes from masks
            meshes.addAll(meshFromMask(top,4,index,u,chunk.textureManager.texture));
            meshes.addAll(meshFromMask(bottom,5,index,u,chunk.textureManager.texture));
            meshes.addAll(meshFromMask(front,0,index,u,chunk.textureManager.texture));
            meshes.addAll(meshFromMask(back,1,index,u,chunk.textureManager.texture));
            meshes.addAll(meshFromMask(left,2,index,u,chunk.textureManager.texture));
            meshes.addAll(meshFromMask(right,3,index,u,chunk.textureManager.texture));

        }

        createShapes();
    }

    private ArrayList<Plane> meshFromMask(boolean[][] mask, int face, int down, int through, PImage texture){

        ArrayList<Plane> meshes = new ArrayList<>();

        for(int x=0;x<16;x++){
            for(int y=0;y<16;y++){
                if(mask[x][y]){

                    int x1 = x;
                    int y1 = y;
                    for(;x1<16;x1++){
                        if(!mask[x1][y]){
                            break;
                        }
                    }
                    //x1--;

                    boolean broke = false;
                    for(;y1<16;y1++){
                        for(int x2 = x;x2<x1;x2++){
                            if(!mask[x2][y1]){
                                broke = true;
                                break;
                            }
                        }
                        if(broke){
                            break;
                        }
                    }
                    //y1--;

                    for(int u=x;u<x1;u++){
                        for(int v=y;v<y1;v++){
                            mask[u][v] = false;
                        }
                    }

                    //  add plane to mesh
                    //PImage t = chunk.textureManager.texture;//chunk.textureManager.getTexture("grass",face);
                    PImage t = chunk.textureManager.getTexture("grass",face);

                    meshes.add(new Plane(x,y,x1,y1,face,down,through,t));
                }
            }
        }

        return meshes;
    }

    private void createShapes(){

        PApplet app = chunk.world.app;
        group = app.createShape(app.GROUP);
        for(Plane mesh:meshes){
            PShape shape = app.createShape();
            shape.beginShape();
            shape.noFill();

            //shape.noTint();
            shape.tint(mesh.tint);
            shape.noStroke();
            shape.texture(mesh.texture);
            //shape.texture(mesh.texture.get(0,0,mesh.width*14,mesh.height*14));
            //shape.textureWrap(app.REPEAT);
            shape.vertex(mesh.a.x*100,mesh.a.y*100,mesh.a.z*100,0,0);
            shape.vertex(mesh.b.x*100,mesh.b.y*100,mesh.b.z*100,mesh.width*32,0);
            shape.vertex(mesh.c.x*100,mesh.c.y*100,mesh.c.z*100,mesh.width*32,mesh.height*32);
            shape.vertex(mesh.d.x*100,mesh.d.y*100,mesh.d.z*100,0,mesh.height*32);

            shape.endShape(app.CLOSE);
            meshShapes.add(shape);
            group.addChild(shape);
        }
    }
}
