import processing.core.PApplet;
import processing.core.PVector;

public class World {

    Chunk chunk;
    TextureManager textureManager;
    PApplet app;
    Chunk[][] chunks;
    int width,height;

    Player player;

    public World(PApplet app) {

        this.app = app;
        textureManager = new TextureManager(app);

        player = new Player(app,this);
        this.chunk = new Chunk(this,textureManager,new PVector(0,0));


        width = 4;
        height = 4;

        //  generating chunks
        System.out.println("Generating chunks...");
        this.chunks = new Chunk[width][height];
        for(int i=0;i<width;i++){
            for(int a=0;a<height;a++){
                this.chunks[i][a] = new Chunk(this,textureManager,new PVector(i,a));
            }
        }

        System.out.println("Generating meshes....");
        for(int i=0;i<width;i++){
            for(int a=0;a<height;a++){
                this.chunks[i][a].genMeshes();
            }
        }

        //  drawing first frame
        System.out.println("Drawing first frame.....");
    }

    public String getBlockType(PVector chunkPos,int x,int y,int z){
        try{
            return chunks[(int)chunkPos.x][(int)chunkPos.y].blocks[x][y][z].type;
        }catch (Exception e){
            return "air";
        }
    }

    public void draw(){

        //this.chunk.draw(app,player.position);
        for(int i=0;i<width;i++){
            for(int a=0;a<height;a++) {
                app.pushMatrix();
                app.translate(i*100*16,0,a*100*16);
                this.chunks[i][a].draw(app,player.position,player);
                app.popMatrix();
            }
        }
    }

    public void setBlock(int x,int y,int z,String type){

        //  get chunk
        try{
            int chunkX = x/16;
            int chunkZ = z/16;
            int bX = x%16;
            int bZ = z%16;
            chunks[chunkX][chunkZ].setBlock(bX,y,bZ,type);
        }catch (Exception e){}
    }
}
