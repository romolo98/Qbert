package br.ol.qbert.dlv;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("celleVisitate")
public class CelleVisitate {
	@Param(0)
    private  int y;
    @Param(1)
    private int x;
    @Param(2)
    private int z;

    @Override
    public String toString() {
        return "celleVisitate{" +
                "y=" + y +
                ",x=" + x +
                ",z="+ z +
                '}';
    }

    public CelleVisitate(int y, int x,int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CelleVisitate(){
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
}
