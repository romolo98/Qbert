package br.ol.qbert.dlv;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("scelta")
public class Scelta {

    @Param(0)
    private String scelta;

    @Override
    public String toString() {
    	return scelta;
    }

    public Scelta(String s) {
        scelta = s;
    }
    
    public Scelta() {
    	scelta = "";
    }
    
    public String getScelta() {
        return scelta;
    }

    public void setScelta(String scelta) {
        this.scelta = scelta;
    }
}
