package org.forweb.drift.tests;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.inventory.item.engine.BasicEngine;
import org.forweb.drift.entity.drift.inventory.item.engine.ShuntingEngine;
import org.forweb.drift.entity.drift.inventory.item.generator.BasicGenerator;
import org.forweb.drift.entity.drift.inventory.item.gun.Laser;
import org.forweb.drift.entity.drift.inventory.item.system.BasicRepairSystem;
import org.forweb.drift.entity.drift.inventory.item.system.radar.MinorRadar;
import org.forweb.drift.entity.drift.spaceships.Falcon;

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
    private final Falcon spaceShip;

    private JPanel drawingPanel;
    private Timer gameTimer;
    private final int fps = 1000/60;


    List<PolygonalObject> objectList = new ArrayList<>();

    private Frame() {
        gameTimer = new Timer(fps, new GameUpdater());
        drawingPanel = new JPanel();
        drawingPanel.setBorder(new LineBorder(Color.black));
        drawingPanel.setPreferredSize(new Dimension(width, height));
        JPanel centerPanel = new JPanel();
        centerPanel.add(drawingPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        setTitle("Frame Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, width + 10, height + 10);
        setVisible(true);
        Falcon spaceShip = new Falcon(100, 100);
        spaceShip.getSlot(0).mount(new BasicEngine());
        spaceShip.getSlot(1).mount(new ShuntingEngine());
        spaceShip.getSlot(2).mount(new ShuntingEngine());
        spaceShip.getSlot(3).mount(new BasicGenerator());
        spaceShip.getSlot(4).mount(new MinorRadar());
        spaceShip.getSlot(5).mount(new BasicRepairSystem());
        spaceShip.getSlot(6).mount(new Laser());
        spaceShip.getSlot(7).mount(new Laser());
        this.spaceShip = spaceShip;
        Listener executor = new Executor(spaceShip);
        TestsKeyListener keyListener = new TestsKeyListener();
        //spaceShip.setAngle(new Angle(Math.PI / 5));
        objectList.add(spaceShip);
        keyListener.addListener(executor);
        addKeyListener(keyListener);
        gameTimer.start();
    }

    //  This method redraws the GUI display.
    private void updateDisplay() {
        drawingPanel.setDoubleBuffered(true);
        Graphics g = drawingPanel.getGraphics();
        int width = drawingPanel.getWidth() - 1;
        int height = drawingPanel.getHeight() - 1;
        g.clearRect(0, 0, width, height);
        g.drawString("Energy: " + spaceShip.getEnergy(), 10, 10);
        g.drawString("Energy Regeneration: " + spaceShip.getEnergyRegeneration(), 10, 25);
        g.drawString("Rotation: " + spaceShip.getRotation().doubleValue(), 10, 40);
        g.drawString("Angle: " + spaceShip.getAngle().doubleValue(), 10, 55);
        g.drawString("Vector: " + ((int)((spaceShip.getVector().x) * fps)) + "/" + ((int)(spaceShip.getVector().y * fps)), 10, 70);
        double speed = Math.sqrt(spaceShip.getVector().x * spaceShip.getVector().x + spaceShip.getVector().y * spaceShip.getVector().y);
        g.drawString("Speed: " + (speed * fps) * 3600 / 1000, 10, 85);


        for (PolygonalObject object : objectList) {
            object.update();
            if(object.getX() * 3 > width) {
                object.setX(0);
            }
            if(object.getY() * 3 > height) {
                object.setY(0);
            }
            if(object.getX() * 3 < 0) {
                object.setX(width / 3);
            }
            if(object.getY() * 3 < 0) {
                object.setY(height / 3);
            }

            object.draw(g);
        }
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