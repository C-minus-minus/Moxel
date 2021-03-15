
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Plane {

    //  vertices
    PVector a,b,c,d;
    int width,height;
    PImage texture;
    int tint = 0;
    int face;

    public Plane(PVector a,PVector b,PVector c,PVector d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Plane(int x1,int y1,int x2,int y2,int face,int down,int through,PImage texture){
        this.face = face;
        this.texture = texture;
        if(face/2 == 0){
            this.a = new PVector(x1,y1+(16*down),through+(face%2));
            this.b = new PVector(x2,y1+(16*down),through+(face%2));
            this.c = new PVector(x2,y2+(16*down),through+(face%2));
            this.d = new PVector(x1,y2+(16*down),through+(face%2));
            tint = 200;
        }
        else if(face/2 == 1){
            this.a = new PVector(through+(face%2),y1+(16*down),x1);
            this.b = new PVector(through+(face%2),y1+(16*down),x2);
            this.c = new PVector(through+(face%2),y2+(16*down),x2);
            this.d = new PVector(through+(face%2),y2+(16*down),x1);
            tint = 150;
            /*
            this.a = new PVector(through+(face%2),y1+(15*down),x1);
            this.b = new PVector(through+(face%2),y1+(15*down),x2);
            this.c = new PVector(through+(face%2),y2+(15*down),x2);
            this.d = new PVector(through+(face%2),y2+(15*down),x1);
*/
        }
        else{
            this.a = new PVector(x1,through+(16*down)+(face%2),y1);
            this.b = new PVector(x2,through+(16*down)+(face%2),y1);
            this.c = new PVector(x2,through+(16*down)+(face%2),y2);
            this.d = new PVector(x1,through+(16*down)+(face%2),y2);
            tint = 100;
        }

        this.width = (int)PApplet.dist(x1,y1,x2,y1);
        this.height = (int)PApplet.dist(x2,y1,x2,y2);
    }

    //  don't ever fuck with this, some how I got it to work on the first try
    public double distance(PVector pos,PApplet app){

        //  find closest distance to a-b line segment
        double abd = PApplet.dist(a.x,a.y,a.z,b.x,b.y,b.z);
        double apd = PApplet.dist(a.x,a.y,a.z,pos.x,pos.y,pos.z);
        double bpd = PApplet.dist(b.x,b.y,b.z,pos.x,pos.y,pos.z);

        //  if point is outside line
        double h1x,h1y,h1z;
        double A = Math.acos((apd*apd+abd*abd-bpd*bpd)/(2*apd*abd));
        double B = Math.acos((bpd*bpd+abd*abd-apd*apd)/(2*abd*bpd));
        if(A>PApplet.PI/2){
            h1x = a.x;
            h1y = a.y;
            h1z = a.z;
        }
        else if(B>PApplet.PI/2){
            h1x = b.x;
            h1y = b.y;
            h1z = b.z;
        }
        else{
            //  find distance to line segment
            double abps = (abd+apd+bpd) / 2;
            double areaABP = Math.sqrt(abps*(abps-abd)*(abps-apd)*(abps-bpd));
            double h1 = (areaABP * 2) / abd;

            //  find coord of h1
            double h1d = Math.sqrt(apd*apd-h1*h1);
            double t1 = h1d/abd;
            h1x = (1-t1)*a.x+t1*b.x;
            h1y = (1-t1)*a.y+t1*b.y;
            h1z = (1-t1)*a.z+t1*b.z;
        }


        app.fill(255,0,0);
        app.pushMatrix();
        app.translate((float)h1x*100,(float)h1y*100,(float)h1z*100);
        //app.sphere(50);
        app.popMatrix();

        //  find closest distance to c-d line segment
        double cdd = PApplet.dist(c.x,c.y,c.z,d.x,d.y,d.z);
        double cpd = PApplet.dist(c.x,c.y,c.z,pos.x,pos.y,pos.z);
        double dpd = PApplet.dist(d.x,d.y,d.z,pos.x,pos.y,pos.z);

        //  if point is outside line
        double h2x,h2y,h2z;
        double C = Math.acos((cpd*cpd+cdd*cdd-dpd*dpd)/(2*cdd*cpd));
        double D = Math.acos((dpd*dpd+cdd*cdd-cpd*cpd)/(2*cdd*dpd));
        if(C>PApplet.PI/2){
            h2x = c.x;
            h2y = c.y;
            h2z = c.z;
        }
        else if(D>PApplet.PI/2){
            h2x = d.x;
            h2y = d.y;
            h2z = d.z;
        }else {

            double cdps = (cdd+cpd+dpd)/2;
            double areaCDP = Math.sqrt(cdps*(cdps-cdd)*(cdps-cpd)*(cdps-dpd));
            double h2 = (areaCDP*2)/cdd;

            //  find coord of h2
            double h2d = Math.sqrt(cpd*cpd-h2*h2);
            double t2 = h2d/cdd;
            h2x = (1-t2)*c.x+t2*d.x;
            h2y = (1-t2)*c.y+t2*d.y;
            h2z = (1-t2)*c.z+t2*d.z;
        }





        app.fill(255,0,0);
        app.pushMatrix();
        app.translate((float)h2x*100,(float)h2y*100,(float)h2z*100);
        //app.sphere(50);
        app.popMatrix();

        //  find closest distance to h1-h2 line segment
        double h1h2d = PApplet.dist((float)h1x,(float)h1y,(float)h1z,(float)h2x,(float)h2y,(float)h2z);
        double h1pd = PApplet.dist((float)h1x,(float)h1y,(float)h1z,pos.x,pos.y,pos.z);
        double h2pd = PApplet.dist((float)h2x,(float)h2y,(float)h2z,pos.x,pos.y,pos.z);

        //  if point is outside line
        double h3x,h3y,h3z,answer;
        double H1 = Math.acos((h1pd*h1pd+h1h2d*h1h2d-h2pd*h2pd)/(2*h1pd*h1h2d));
        double H2 = Math.acos((h2pd*h2pd+h1h2d*h1h2d-h1pd*h1pd)/(2*h2pd*h1h2d));
        if(H1>PApplet.PI/2){
            h3x = h1x;
            h3y = h1y;
            h3z = h1z;
            answer = PApplet.dist((float)h1x,(float)h1y,(float)h1z,pos.x,pos.y,pos.z);
        }
        else if(H2>PApplet.PI/2){
            h3x = h2x;
            h3y = h2y;
            h3z = h2z;
            answer = PApplet.dist((float)h2x,(float)h2y,(float)h2z,pos.x,pos.y,pos.z);
        }
        else{
            double h1h2ps = (h1h2d+h1pd+h2pd)/2;
            double h1h2pArea = Math.sqrt(h1h2ps*(h1h2ps-h1h2d)*(h1h2ps-h1pd)*(h1h2ps-h2pd));
            answer = (h1h2pArea*2) / h1h2d;

            //  find coord of h3
            double h3d = Math.sqrt(h1h2d*h1h2d-answer*answer);
            double t3 = h3d/h1h2d;
            h3x = (1-t3)*h1x+t3*h2x;
            h3y = (1-t3)*h1y+t3*h2y;
            h3z = (1-t3)*h1z+t3*h2z;
        }


        app.pushMatrix();
        app.fill(0,0,255);
        app.translate((float)h3x*100,(float)h3y*100,(float)h3z*100);
        //app.sphere(50);
        app.popMatrix();

        //  for the love of god please don't be a bug
        return answer;
    }
}
