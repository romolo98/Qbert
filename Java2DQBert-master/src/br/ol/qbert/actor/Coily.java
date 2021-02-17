package br.ol.qbert.actor;

import static br.ol.qbert.infra.Actor.Axis.Z_AXIS;
import static br.ol.qbert.infra.Actor.State.*;
import br.ol.qbert.infra.Enemy;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.dlv.HandlerDlv;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.HudInfo;
import br.ol.qbert.infra.Scene;
import br.ol.qbert.infra.Actor.Axis;
import br.ol.qbert.infra.Actor.State;

import static br.ol.qbert.infra.ScoreInfo.*;

import java.io.FileWriter;
import java.io.IOException;

import br.ol.qbert.scene.Level;

/**
 * Coily class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Coily extends Enemy {
    
    // in this implementation, coily has priority to be revived
    protected int reviveTime;
    private HandlerDlv handler;
    private String own_facts = "encoding/ballPurple.txt";
    
    public Coily(int id, Scene scene, QBert qbert, PlayField playField,HandlerDlv hdlv) {
        super(id, scene, Axis.Z_AXIS, qbert, 40, playField, 15);
        handler = hdlv;
    }

    @Override
    public void init() {
        addFrame("coily_0", 8, 28);
        addFrame("coily_1", 8, 28);
        addFrame("coily_2", 8, 28); 
        addFrame("coily_3", 8, 28);
        addFrame("coily_4", 8, 28); 
        addFrame("coily_5", 8, 28);
        addFrame("coily_6", 8, 28); 
        addFrame("coily_7", 8, 28);
        reviveTime = 15;
    }

    public void hatch(int x, int y, int z) {
        set(x, y, z, 0, 0, 0);
        state = State.IDLE;
    }
    
    @Override
    public void reset() {
        fall1(1, 1, 7, 0);
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
    }
    
    @Override
    public void updateIdle() {
        int qx = qbert.location[0] >> 4;
        int qy = qbert.location[1] >> 4;
        int qz = qbert.location[2] >> 4;

        int x = location[0] >> 4;
        int y = location[1] >> 4;
        int z = location[2] >> 4;

        int dif[] = new int[3];
        dif[0] = qx - x;
        dif[1] = qy - y;
        dif[2] = qz - z;
        dif[0] = dif[0] > 0 ? 1 : dif[0] < 0 ? -1 : 0;
        dif[1] = dif[1] > 0 ? 1 : dif[1] < 0 ? -1 : 0;
        dif[2] = dif[2] > 0 ? 1 : dif[2] < 0 ? -1 : 0;
        target[xIndex] = 0;
        target[yIndex] = 0;
        
        boolean jumped = false;
        int choice = 0;
        
        // will fall trying to catch the Q*Bert with flying disc
        if (x - 1 == qbert.liftFlyingDiscX && y == qbert.liftFlyingDiscY) {
            jumpX(-1);
            jumped = true;
        }
        else if (x == qbert.liftFlyingDiscX && y - 1 == qbert.liftFlyingDiscY) {
            jumpY(-1);
            jumped = true;
        }
        // chase Q*Bert but stay in the playfield
        else {
            if (dif[xIndex] != 0 && 
                !playField.isOut(upAxis, x + dif[xIndex], y)) {
                // mark jumpX as available
                choice |= 0x01;
            }
            if (dif[yIndex] != 0 && 
                !playField.isOut(upAxis, x, y + dif[yIndex])) {
                // mark jumpY as available
                choice |= 0x02;
            }
        }
        
        // if both jumpX and jumpY are available, then select one randomly
        if (choice == 3) {
            choice = Math.random() <= 0.5 ? 1 : 2;
        }
        
        if (choice == 1) {
            jumpX(dif[xIndex]);
            jumped = true;
        }
        else if (choice == 2) {
            jumpY(dif[yIndex]);
            jumped = true;
        }

        if (jumped) {
            Audio.playSound("jump4");
        }
    }

    @Override
    public void updateDead() {
        reviveTime--;
    }
    
    @Override
    public void onFall() {
        Audio.playSound("coily_fall");
        HudInfo.addScore(SCORE_LURING_COILY_OVER_EDGE);
        if (scene instanceof Level) {
            Level level = (Level) scene;
            level.killAllCreaturesExceptCoilyAndGreenBall();
        }
    }

    @Override
    public void onDead() {
        reviveTime = 90;
    }
    
    public boolean needsRevive() {
        return reviveTime <= 0;
    }
    
}
