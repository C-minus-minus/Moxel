import com.jogamp.opengl.math.geom.AABBox;
import processing.core.PApplet;
import processing.core.PVector;

public class Chunk {

    Block[][][] blocks;
    SubChunk[] subChunks;

    AABBox aabBox;

    World world;
    TextureManager textureManager;
    PVector chunkPosition;
    int render = 30;

    public Chunk(World world,TextureManager textureManager,PVector chunkPosition){

        this.world = world;
        this.textureManager = textureManager;
        this.chunkPosition = chunkPosition;
        genTerrain();
    }

    //  create terrain
    public void genTerrain(){

        //  set up blocks
        blocks = new Block[16][256][16];
        for(int x=0;x<16;x++){
            for(int y=0;y<256;y++){
                for(int z=0;z<16;z++){
                    float t = world.app.noise((x+chunkPosition.x*16)*.05f,y*.05f,(z+chunkPosition.y*16)*.05f);
                    blocks[x][y][z] = new Block(t>.5?"grass":"air");
                }
            }
        }

    }

    //  generate meshes
    public void genMeshes(){
        subChunks = new SubChunk[16];
        for(int i=0;i<16;i++){
            subChunks[i] = new SubChunk(this,i);
            subChunks[i].genMesh();
        }
    }

    public void draw(PApplet app,PVector playerPos,Player player){

        //  adjust player position to chunk space
        PVector adjustedPlayer = playerPos.copy();
        adjustedPlayer.x/=100;
        adjustedPlayer.y/=100;
        adjustedPlayer.z/=100;

        //  if player is close enough to this chunk
        if(distance(adjustedPlayer)<render){

            //  for all sub chunks
            for(SubChunk subChunk:subChunks){

                //  if player is close enough to this subChunk
                if(subChunk.distance(adjustedPlayer)<render){

                    //  draw subchunk
                    subChunk.draw(app);
                }
            }
        }
    }

    public void setBlock(int x,int y,int z,String type){
        try{
            blocks[x][y][z] = new Block(type);
            subChunks[y/16].genMesh();
        }catch (Exception e){}

    }

    private float distance(PVector playerPos){

        //  get coords of center of chunk
        int centerX = (int)(chunkPosition.x*16+8);
        int centerZ = (int)(chunkPosition.y*16+8);
        return PApplet.dist(centerX,centerZ,playerPos.x,playerPos.z);
    }
}
