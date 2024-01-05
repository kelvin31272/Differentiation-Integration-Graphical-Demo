package Visual;

import java.awt.*;
import java.awt.event.*;

public class Camera {

    private Graph graph;
    private MouseWheelEvent mouse;

    public Camera(Graph graph) {
        this.graph = graph;
    }

    /* CAMERA PARAMETERS */
    // BOUNDARY
    private double CAMERA_BORDER_X1 = -99999999;
    private double CAMERA_BORDER_Y1 = -99999999;
    private double CAMERA_BORDER_X2 = 99999999;
    private double CAMERA_BORDER_Y2 = 99999999;

    // POSITION
    private double CAMERA_X = 0;
    private double CAMERA_Y = 0;
    private double DESIRED_CAMERA_X = 0;
    private double DESIRED_CAMERA_Y = 0;
    private double CAMERA_MOVEMENT_SENSITIVITY = 1.2;
    private double CAMERA_MOVEMENT_SPEED = 0.8;

    // ZOOM
    private double CAMERA_ZOOM = 1;
    private double DESIRED_CAMERA_ZOOM = 150; // make this different from CAMERA_ZOOM to create an initial zoom effect
    private double MIN_CAMERA_ZOOM = 0.000001;
    private double MAX_CAMERA_ZOOM = 9999999;

    private double CAMERA_ZOOM_SENSITIVITY = 0.16;
    private double CAMERA_ZOOM_SPEED = 0.6;

    // DEFAULT VARS
    private double DEFAULT_CAMERA_ZOOM = DESIRED_CAMERA_ZOOM;
    private double DEFAULT_CAMERA_X = DESIRED_CAMERA_X;
    private double DEFAULT_CAMERA_Y = DESIRED_CAMERA_Y;

    public void mouseWheelMoved() {
        // change the desired camera zoom based on the mouse wheel movement
        // [-1 == scrolling out, 1 == scolling in]
        int direction = mouse.getWheelRotation();
        if (direction == 1) {
            DESIRED_CAMERA_ZOOM -= CAMERA_ZOOM * CAMERA_ZOOM_SENSITIVITY; // scroll out by decreasing zoom
        } else {
            DESIRED_CAMERA_ZOOM += CAMERA_ZOOM * CAMERA_ZOOM_SENSITIVITY; // scroll in by increasing zoom
        }

        // if the desired camera zoom is now at the limits of zoom capabilities
        // change the desired camera x and y based on the mouse wheel movement
        // [this allows the camera to still be maneuverable when at max/min zoom]
        if (!(isWithin(DESIRED_CAMERA_ZOOM, MIN_CAMERA_ZOOM, MAX_CAMERA_ZOOM))) {
            double deltaZoom;
            // are we scrolling in at max zoom or out at min zoom?
            if (direction == 1) {
                deltaZoom = DESIRED_CAMERA_ZOOM - MIN_CAMERA_ZOOM;
            } else {
                deltaZoom = DESIRED_CAMERA_ZOOM - MAX_CAMERA_ZOOM;
            }
            updatePosition(deltaZoom * CAMERA_ZOOM_SPEED * CAMERA_MOVEMENT_SENSITIVITY);
        }

        // constrain the values so nothing breaks
        DESIRED_CAMERA_ZOOM = constrain(DESIRED_CAMERA_ZOOM, MIN_CAMERA_ZOOM, MAX_CAMERA_ZOOM);
    }

    public void translateCamera(Graphics2D g) {
        g.translate(graph.getWidth() / 2, graph.getHeight() / 2);
        g.scale(CAMERA_ZOOM, CAMERA_ZOOM);
        g.translate(-CAMERA_X, -CAMERA_Y);

    }

    public void untranslateCamera(Graphics2D g) {
        g.translate(-graph.getWidth() / 2, -graph.getHeight() / 2);
        g.scale(-CAMERA_ZOOM, -CAMERA_ZOOM);
        g.translate(CAMERA_X, CAMERA_Y);

    }

    public void updateCamera() {
        // ease the zooming and movement of the camera over timesteps
        // to create a nice smooth camera gliding effect
        // [the amount of easing is determined by CAMERA_ZOOM_SPEED]
        double deltaZoom = DESIRED_CAMERA_ZOOM - CAMERA_ZOOM;
        updateZoom(deltaZoom * CAMERA_ZOOM_SPEED);
        updatePosition(deltaZoom * CAMERA_ZOOM_SPEED);
    }

    private void updateZoom(double deltaZoom) {
        CAMERA_ZOOM += deltaZoom;
        CAMERA_ZOOM = constrain(CAMERA_ZOOM, MIN_CAMERA_ZOOM, MAX_CAMERA_ZOOM);
    }

    private void updatePosition(double deltaZoom) {
        if (mouse == null) {
            return;
        }
        double realMouseX = (graph.getWidth() / 2 - mouse.getX()) / CAMERA_ZOOM;
        double realMouseY = (graph.getHeight() / 2 - mouse.getY()) / CAMERA_ZOOM;
        DESIRED_CAMERA_X -= (realMouseX * deltaZoom) / CAMERA_ZOOM;
        DESIRED_CAMERA_Y -= (realMouseY * deltaZoom) / CAMERA_ZOOM;
        DESIRED_CAMERA_X = constrain(DESIRED_CAMERA_X, CAMERA_BORDER_X1, CAMERA_BORDER_X2);
        DESIRED_CAMERA_Y = constrain(DESIRED_CAMERA_Y, CAMERA_BORDER_Y1, CAMERA_BORDER_Y2);
        double deltaX = DESIRED_CAMERA_X - CAMERA_X;
        double deltaY = DESIRED_CAMERA_Y - CAMERA_Y;
        CAMERA_X += deltaX * CAMERA_MOVEMENT_SPEED;
        CAMERA_Y += deltaY * CAMERA_MOVEMENT_SPEED;
    }

    public void resetCameraPosition() {
        DESIRED_CAMERA_X = DEFAULT_CAMERA_X;
        DESIRED_CAMERA_Y = DEFAULT_CAMERA_Y;
    }

    public void resetCameraZoom() {
        mouse = new MouseWheelEvent(graph, 0, 0, 0, graph.getWidth()/2, graph.getHeight()/2, 0, false, 0, 0, 0);
        DESIRED_CAMERA_ZOOM = DEFAULT_CAMERA_ZOOM;
    }

    // math methods
    private boolean isWithin(double n, double lowerBound, double upperBound) {
        return n >= Math.min(lowerBound, upperBound) && n <= Math.max(lowerBound, upperBound) ? true : false;
    }

    private double constrain(double d, double lower, double upper) {
        if (d < lower) {
            d = lower;
        }
        if (d > upper) {
            d = upper;
        }
        return d;
    }

    // SETTERS
    public void setMouseWheelEvent(MouseWheelEvent me) {
        this.mouse = me;
    }

    // GETTERS
    public double getTranslatedX(double mouseX) {
        double worldX = (mouseX - graph.getWidth() / 2) / CAMERA_ZOOM + CAMERA_X;
        return worldX;
    }

    public double getTranslatedY(double mouseY) {
        double worldY = (mouseY - graph.getHeight() / 2) / CAMERA_ZOOM + CAMERA_Y;
        return worldY;
    }

    public double getUntranslatedX(double worldX) {
        double mouseX = ((worldX - CAMERA_X) * CAMERA_ZOOM) + (graph.getWidth() / 2);
        return mouseX;
    }

    public double getUntranslatedY(double worldY) {
        double mouseY = ((worldY - CAMERA_Y) * CAMERA_ZOOM) + (graph.getWidth() / 2);
        return mouseY;
    }

    public double getZoom() {
        return CAMERA_ZOOM;
    }
}
