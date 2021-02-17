package br.ol.qbert.actor;

import br.ol.qbert.infra.Enemy;
import br.ol.qbert.infra.PlayField;

import static br.ol.qbert.infra.Actor.Axis.Z_AXIS;

import java.io.FileWriter;
import java.io.IOException;

import br.ol.qbert.dlv.HandlerDlv;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.Scene;
import br.ol.qbert.infra.Actor.Axis;
import br.ol.qbert.infra.Actor.State;

/**
 * BallPurple class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BallPurple extends Enemy {
    
    private final Coily coily;
    private HandlerDlv handler;
    private String own_facts = "encoding/ballPurple.txt";
	
    
    public BallPurple(int id, Scene scene, QBert qbert, 
        PlayField playField, Coily coily, HandlerDlv hdlv) {
        super(id, scene, Axis.Z_AXIS, qbert, 50, playField, 30);
        this.coily = coily;
        handler = hdlv;
    }

    @Override
    public void init() {
        addFrame("ball_coily_0", 7, 6);
        addFrame("ball_coily_1", 7, 6);
        addFrame("ball_coily_0", 7, 6);
        addFrame("ball_coily_1", 7, 6);
        addFrame("ball_coily_0", 7, 6);
        addFrame("ball_coily_1", 7, 6);
        addFrame("ball_coily_0", 7, 6);
        addFrame("ball_coily_1", 7, 6);
    }

    @Override
    public void reset() {
        fall1b(0);
    }
    
    public void resetFacts() throws IOException {
    	FileWriter writer = new FileWriter(own_facts);
    	writer.append("");
		writer.flush();
		writer.close();
    }
    
    public void updateFacts() throws IOException {
    	if (location[yIndex]%16==0 && location[xIndex]%16==0 && location[zIndex]%16==0) {
	    	FileWriter writer = new FileWriter(own_facts);
			writer.append(this.getPosizione());
	        writer.flush();
			writer.close();
	    }
    }
    
    
    @Override
    public void updateIdle() {
        if (Math.random() < 0.5) {
            jumpX(1);
        }
        else {
            jumpY(1);
        }
        Audio.playSound("jump2");

        // coily born
        int x = location[xIndex] >> 4;
        int y = location[yIndex] >> 4;
        int z = location[zIndex] >> 4;
        if (x + y == 8) {
          coily.hatch(x, y, z);
          kill(false);
        }
    }
    
    @Override
    public void checkOutOfScreen() {
        final int maxSpriteSize = 32;
        if (state != State.DEAD && (
            (upAxis == Axis.X_AXIS && spx > 256 + maxSpriteSize) || 
            (upAxis == Axis.Y_AXIS && spx < 0 - maxSpriteSize) || 
            (upAxis == Axis.Z_AXIS && spy > 240 + maxSpriteSize))) {
            kill(true);
            
        }
    }
    
    @Override
    public void updateJumping () {
    	if (jumpCount < 16) {
            location[xIndex] += target[xIndex];
            location[yIndex] += target[yIndex];
            zOrder = (location[0] >> 4) + (location[1] >> 4);
        }
        jumpCount++;
        if (jumpCount == 16) {
            int col = location[xIndex] >> 4;
            int row = location[yIndex] >> 4;
            
            // check flying disc
            if (upAxis == Z_AXIS && scene instanceof br.ol.qbert.scene.Level && 
                ((br.ol.qbert.scene.Level) scene).isOnFlyingDisc(
                    this, col, row)) {
                
                liftFlyingDiscX = col;
                liftFlyingDiscY = row;
                state = State.FLYING_DISC;
                fallingVelocity = 5;
                return;
            }
                
            // fall from playField Z up axis
            if (upAxis == Axis.Z_AXIS && 
                (col == 0 || row == 0 || col + row > 8)) {
                
                state = State.FALLING2;
                fallingVelocity = col + row > 8 ? 5 : 2;
                onFall();
                try {
					this.resetFacts();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            // fall from playField X or Y up axis
            else if (upAxis != Axis.Z_AXIS && (col + row > 7)) {
                state = State.FALLING2;
                fallingVelocity = 5;
                onFall();
            }
            else {
                currentFrame &= 0xfffffffe; 
                onStepOnPlayfield();
                try {
					this.updateFacts();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        else if (jumpCount == jumpWaitUntil) {
            state = State.IDLE;
        }
        
        if (location[yIndex]%16==0 && location[yIndex]>=16 && location[yIndex] <=112 && location[xIndex]%16==0 && location[xIndex]>=16 && location[xIndex] <= 112 && location[zIndex]%16==0 && location[zIndex]>=16 && location[zIndex] <= 112) {
            try {
	        	this.updateFacts();
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
     
}
