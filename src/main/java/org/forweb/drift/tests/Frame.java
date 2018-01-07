package org.forweb.drift.tests;

import org.forweb.drift.entity.drift.PolygonalAsteroid;
import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.World;
import org.forweb.drift.entity.drift.inventory.item.engine.BasicEngine;
import org.forweb.drift.entity.drift.inventory.item.engine.ShuntingEngine;
import org.forweb.drift.entity.drift.inventory.item.generator.BasicGenerator;
import org.forweb.drift.entity.drift.inventory.item.gun.Laser;
import org.forweb.drift.entity.drift.inventory.item.system.BasicRepairSystem;
import org.forweb.drift.entity.drift.inventory.item.system.radar.MinorRadar;
import org.forweb.drift.entity.drift.spaceships.Falcon;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.drift.utils.MassUtils;
import org.forweb.drift.utils.PolygonalUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.*;
import org.forweb.geometry.shapes.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class Frame extends JFrame {

    private final int width = 1200;
    private final int height = 800;
    private final int scale = 3;
    private final int worldWidth = width / scale;
    private final int worldHeight = height / scale;
    private Falcon spaceShip;

    private JPanel drawingPanel;
    private Timer gameTimer;
    private final int fps = 1000/60;
    private World world;



    private Frame() {

        gameTimer = new Timer(fps, new GameUpdater());




        world = new World(worldWidth, worldHeight);
        Falcon spaceShip = new Falcon(140, 150);
        spaceShip.getSlot(0).mount(new BasicEngine());
        spaceShip.getSlot(1).mount(new ShuntingEngine());
        spaceShip.getSlot(2).mount(new ShuntingEngine());
        spaceShip.getSlot(3).mount(new BasicGenerator());
        spaceShip.getSlot(4).mount(new MinorRadar());
        spaceShip.getSlot(5).mount(new BasicRepairSystem());
        spaceShip.getSlot(6).mount(new Laser());
        spaceShip.getSlot(7).mount(new Laser());
        this.spaceShip = spaceShip;
        drawingPanel = new DrawPanel(spaceShip, world, fps);
        drawingPanel.setBorder(new LineBorder(Color.black));
        drawingPanel.setPreferredSize(new Dimension(width, height));
        getContentPane().setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.add(drawingPanel);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        setTitle("Frame Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, width + 10, height + 10);
        setVisible(true);

        Listener executor = new Executor(spaceShip);
        TestsKeyListener keyListener = new TestsKeyListener();
        //spaceShip.setAngle(new Angle(Math.PI / 5));
        world.add(spaceShip);
        PolygonalObjectEntity entity = new PolygonalObjectEntity(
              new Point[]{
                      new Point(10, 10),
                      new Point(10, -10),
                      new Point(-10, -10),
                      new Point(-10, 10)
                     /* new Point(50, 15),
                      new Point(4, 62),
                      new Point(-40, -2),
                      new Point(-58, -86),
                      new Point(-10, -49),
                      new Point(60, -23)*/
              }, 200D, 150d, 0d, 0d
        );
        PolygonalAsteroid asteroid = new PolygonalAsteroid(entity);
        asteroid.setVector(new Vector(0, 0));
        asteroid.setRotation(new Angle(0));
        world.add(asteroid);

        keyListener.addListener(executor);
        addKeyListener(keyListener);
        gameTimer.start();

    }

    //  This method redraws the GUI display.
    private void updateDisplay() {
        world.update();
        drawingPanel.repaint();
    }

    public static void main(String args[]) {
        new Frame();
    }

    class GameUpdater implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            updateDisplay();
        }
    }


}

class DrawPanel extends JPanel {

    private final int fps;
    PolygonalSpaceShip spaceShip;
    World world;

    public DrawPanel(PolygonalSpaceShip spaceShip, World world, int fps) {
        this.spaceShip = spaceShip;
        this.world = world;
        this.fps = fps;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() - 1;
        int height = getHeight() - 1;
        g.clearRect(0, 0, width, height);
        g.drawString("Energy: " + spaceShip.getEnergy(), 10, 10);
        g.drawString("Energy Regeneration: " + spaceShip.getEnergyRegeneration(), 10, 25);
        g.drawString("Rotation: " + angle(Math.PI - spaceShip.getRotation().doubleValue()), 10, 40);
        g.drawString("Angle: " + angle(spaceShip.getAngle()), 10, 55);
        g.drawString("Vector: " + ((int)((spaceShip.getVector().x) * fps)) + "/" + ((int)(spaceShip.getVector().y * fps)), 10, 70);
        double speed = Math.sqrt(spaceShip.getVector().x * spaceShip.getVector().x + spaceShip.getVector().y * spaceShip.getVector().y);
        g.drawString("Speed: " + (speed * fps) * 3600 / 1000, 10, 85);


        for (PolygonalObject object : world.getObjects()) {
            object.draw(g);
        }
    }

    private int angle(Angle angle) {
        return angle(angle.doubleValue());
    }
    private int angle(double value) {
        return (int)(value / Math.PI * 180);
    }
}