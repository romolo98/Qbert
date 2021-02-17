package br.ol.qbert.infra;

import br.ol.qbert.actor.QBert;
import br.ol.qbert.infra.Actor.Axis;
import br.ol.qbert.infra.Actor.State;

/**
 * Harmless class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Harmless extends Actor {
    
    protected final QBert qbert;
    
    public Harmless(int id, Scene scene, Axis topAxis, QBert qbert, 
        int jumpWaitUntil, PlayField playField, int minLevelFrames) {
        
        super(id, scene, topAxis, playField);
        this.qbert = qbert;
        this.jumpWaitUntil = jumpWaitUntil;
        this.minLevelFrames = minLevelFrames;
    }

    public void onCaught() {
        // implement your code here
    }
    
    
}
