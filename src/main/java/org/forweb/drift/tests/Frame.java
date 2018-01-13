package org.forweb.drift.tests;

import org.forweb.drift.entity.drift.PolygonalAsteroid;
import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.engine.BasicEngine;
import org.forweb.drift.entity.drift.inventory.item.engine.ShuntingEngine;
import org.forweb.drift.entity.drift.inventory.item.generator.BasicGenerator;
import org.forweb.drift.entity.drift.inventory.item.gun.Laser;
import org.forweb.drift.entity.drift.inventory.item.system.BasicRepairSystem;
import org.forweb.drift.entity.drift.inventory.item.system.radar.MinorRadar;
import org.forweb.drift.entity.drift.spaceships.Falcon;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.drift.tests.impact.ContactListener;
import org.forweb.drift.tests.impact.Impact;
import org.forweb.drift.utils.DriftWorld;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.forweb.geometry.misc.Angle;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Frame extends JFrame {

    private final int width = 1200;
    private final int height = 800;
    private final int scale = 3;
    private final int worldWidth = width / scale;
    private final int worldHeight = height / scale;
    private Falcon spaceShip;
    private java.util.List<Impact> contacts;
    private World world;

    private JPanel drawingPanel;
    private Timer gameTimer;
    private final int fps = 1000 / 60;


    private Frame() {
        gameTimer = new Timer(fps, new GameUpdater());

        world = new DriftWorld();
        contacts = new ArrayList<>();
        world.setContactListener(new ContactListener(contacts));
        world.setAutoClearForces(true);
/*
        BodyDef def = new BodyDef();


        PolygonShape shape = new PolygonShape();
        shape.set(new Vec2[]{
                new Vec2(-10d, -10d), new Vec2(-10d, 10d), new Vec2(20, 0)
        }, new Vec2[]{
                new Vec2(-10d, -10d), new Vec2(-10d, 10d), new Vec2(20, 0)
        }.length);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 4.0f;
        fixture.friction = 0.2f;
        fixture.restitution = 0f;
        fixture.isSensor = false;


        def.type = BodyType.DYNAMIC;
        def.position = new Vec2(0, 0);
        def.angle = 0;
        def.linearVelocity = new Vec2(0, 0);
        def.angularVelocity = 0;
        def.linearDamping = 3;
        def.angularDamping = 0;
        def.allowSleep = false;
        def.awake = true;
        def.fixedRotation = false;
        def.bullet = false;
        def.active = true;
        def.gravityScale = 1;

        Body body = world.createBody(def);
        body.createFixture(fixture);
        for(int i = 0; i < 1000; i++) {
            world.step(16, 8, 4);
            body = world.getBodyList();
            while(body != null) {
                body.applyForce(body.getLinearVelocity().add(new Vec2(1, 0)), body.getWorldPoint(new Vec2(0, 0)));
                //System.out.println(body.getLinearVelocity().length());
                body = body.getNext();
            }
        }*/



        Falcon spaceShip = new Falcon(world, 140, 150);
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
        PolygonalObjectEntity entity = new PolygonalObjectEntity(
                /*new Vec2[]{
                        new Vec2(10, 10),
                        new Vec2(10, -10),
                        new Vec2(-10, -10),
                        new Vec2(-10, 10)
                },*/ 200D, 150d, 0d/*, 0d*/
        );
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 3; j++) {
                entity.setX(i * 20);
                entity.setY(j * 20);
                //new PolygonalAsteroid(world, i * 20, j * 20);
               // Body asteroidBody = asteroid.getBody();
                //asteroidBody.setLinearVelocity(new Vec2(0, 0));
               // asteroidBody.setAngularVelocity(0);
            }
        }

        keyListener.addListener(executor);
        addKeyListener(keyListener);
        gameTimer.start();

    }

    //  This method redraws the GUI display.
    private void updateDisplay() {
        world.step(fps, 8, 3);
        for(Impact impact : contacts) {
            Body b1 = impact.getContact().getFixtureA().getBody();
            Body b2 = impact.getContact().getFixtureB().getBody();
            PolygonalObject object1 = (PolygonalObject) b1.getUserData();
            PolygonalObject object2 = (PolygonalObject) b2.getUserData();
            double d[] = impact.getImpulse().normalImpulses;
            double impulseStrength = new Vec2(d[0], d[1]).length() / 2;
            object1.setHealth(object1.getHealth() - (int)impulseStrength);
            object2.setHealth(object2.getHealth() - (int)impulseStrength);
        }
        contacts.clear();
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
        Body body = spaceShip.getBody();
        g.drawString("Energy: " + spaceShip.getEnergy(), 10, 10);
        g.drawString("Energy Regeneration: " + spaceShip.getEnergyRegeneration(), 10, 25);
        g.drawString("Rotation: " + angle(Math.PI - body.getAngularVelocity()), 10, 40);
        g.drawString("Angle: " + angle(body.getAngle()), 10, 55);
        g.drawString("Angular speed: " + angle(body.getAngularVelocity()), 10, 70);
        g.drawString("Vector: " + ((int)((body.getLinearVelocity().x) * fps)) + "/" + ((int)(body.getLinearVelocity().y * fps)), 10, 85);
        double speed = Math.sqrt(body.getLinearVelocity().x * body.getLinearVelocity().x + body.getLinearVelocity().y * body.getLinearVelocity().y);
        g.drawString("Speed: " + (speed * fps) * 3600 / 1000, 10, 100);
        g.drawString("Health: " + (spaceShip.getHealth()), 10, 115);


       body = world.getBodyList();
       int scale = 1;
       while(body != null) {
           PolygonalObject object = (PolygonalObject) body.getUserData();
           if(object.getHealth() < 0 || !object.isAlive()) {
               object.destroy();
               Body toDestroy = body;
               body = body.getNext();
               world.destroyBody(toDestroy);
               continue;
           }
           Fixture fixture = body.getFixtureList();
           if (object != null) {
               object.update();
           }
           while(fixture != null) {
               org.jbox2d.collision.shapes.Shape shape = fixture.getShape();
               PolygonShape polygon = (PolygonShape)shape;
               Vec2[] points = polygon.getVertices();
               int count = polygon.getVertexCount();
               for (int i = 0; i < count; i++) {
                   Vec2 a = points[i];
                   Vec2 b = i == count - 1 ? points[0] : points[i + 1];
                   a = body.getWorldPoint(a);
                   b = body.getWorldPoint(b);
                   g.drawLine((int) a.x * scale, (int) a.y * scale, (int) b.x * scale, (int) b.y * scale);
                   g.drawString(object.getHealth() + "", (int)body.getPosition().x, (int)body.getPosition().y);
               }

               fixture = fixture.getNext();
           }
           Vec2 position = body.getPosition();
           boolean needTransform = false;
           if(position.x > width) {
               needTransform = true;
               position.x = 0;
           } else if(position.x < 0){
               needTransform = true;
               position.x = width;
           }
           if(position.y > height) {
               needTransform = true;
               position.y = 0;
           } else if(position.y < 0){
               needTransform = true;
               position.y = height;
           }
           if(needTransform) {
               body.setTransform(position, body.getAngle());
           }
           body = body.getNext();
       }
        DriftWorld driftWorld = (DriftWorld)world;
       Iterator<DriftWorld.RayWrapper> iterator = driftWorld.rays.iterator();
       while(iterator.hasNext()) {
           DriftWorld.RayWrapper ray = iterator.next();
           g.drawLine((int)ray.ray.p1.x,(int)ray.ray.p1.y,(int)ray.ray.p2.x,(int)ray.ray.p2.y);
           ray.iteration++;
           if(ray.iteration > 6) {
               iterator.remove();
           }
       }
    }

    private float angle(Angle angle) {
        return angle(angle.doubleValue());
    }

    private float angle(double value) {
        return ((int) (value * 10 / Math.PI * 180)) / 10f;
    }
}