package br.ol.qbert.dlv;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("celleAdiacenti")
public class CelleAdiacenti {

	@Param(0)
    private  int y;
    @Param(1)
    private int x;
    @Param(2)
    private int z;
    @Param(3)
    private String s;

    @Override
    public String toString() {
        return "celleAdiacenti{" +
                "y=" + y +
                ",x=" + x +
                ",z="+ z +
                ",s="+ s +
                '}';
    }

    public CelleAdiacenti(int y, int x,int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CelleAdiacenti(){
        this.x=16;
        this.y=16;
        this.z=112;
    }
    public int getX() {
        return x;
    }
    
    public int getZ() {
    	return z;
    }
    
    public void setZ(int z) {
    	this.z = z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void setS(String s) {
    	this.s = s;
    }
    
    public String getS() {
    	return s;
    }
}
