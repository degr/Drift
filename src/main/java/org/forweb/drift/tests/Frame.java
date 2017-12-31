package org.forweb.drift.tests;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.inventory.engine.BasicEngine;
import org.forweb.drift.entity.drift.inventory.engine.ShuntingEngine;
import org.forweb.drift.entity.drift.inventory.generator.BasicGenerator;
import org.forweb.drift.entity.drift.inventory.gun.Laser;
import org.forweb.drift.entity.drift.inventory.system.BasicRepairSystem;
import org.forweb.drift.entity.drift.inventory.system.radar.MinorRadar;
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

    private JPanel drawingPanel;
    private Timer gameTimer;


    List<PolygonalObject> objectList = new ArrayList<>();

    private Frame() {
        gameTimer = new Timer(1000/60, new GameUpdater());
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
        gameTimer.start();
        Falcon spaceShip = new Falcon(100, 100);
        spaceShip.getSlot(0).mount(new BasicEngine());
        spaceShip.getSlot(1).mount(new ShuntingEngine());
        spaceShip.getSlot(2).mount(new ShuntingEngine());
        spaceShip.getSlot(3).mount(new BasicGenerator());
        spaceShip.getSlot(4).mount(new MinorRadar());
        spaceShip.getSlot(5).mount(new BasicRepairSystem());
        spaceShip.getSlot(6).mount(new Laser());
        spaceShip.getSlot(7).mount(new Laser());
        Listener executor = new Executor(spaceShip);
        TestsKeyListener keyListener = new TestsKeyListener();
        //spaceShip.setAngle(new Angle(Math.PI / 5));
        objectList.add(spaceShip);
        keyListener.addListener(executor);
        addKeyListener(keyListener);
    }

    //  This method redraws the GUI display.
    private void updateDisplay() {
        Graphics g = drawingPanel.getGraphics();
        int width = drawingPanel.getWidth() - 1;
        int height = drawingPanel.getHeight() - 1;

        g.clearRect(0, 0, width, height);

        for (PolygonalObject object : objectList) {
            object.update();
            if(object.getX() > width) {
                object.setX(0);
            }
            if(object.getY() > height) {
                object.setY(0);
            }
            if(object.getX() < 0) {
                object.setX(width);
            }
            if(object.getY() < 0) {
                object.setY(height);
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