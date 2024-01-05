package Visual;

import Calculus.*;

import java.awt.*;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

public class Graph extends JPanel {
    private Polynomial F1;
    private Polynomial f1;
    private Polynomial fprime1;
    private Polynomial fprimeprime1;
    private double F1x1, F1x2; // integration between two x values
    private Polynomial F2;
    private Polynomial f2;
    private Polynomial fprime2;
    private Polynomial fprimeprime2;
    private double F2x1, F2x2; // integration between two x values
    private Polynomial F3;
    private double F3x1, F3x2; // integration between two functions

    private Camera camera;
    private float zoom;
    private double[] scales = new double[120];
    private double xStretch = 1;

    private double leftWorldX, rightWorldX, topWorldY, bottomWorldY;

    public Graph() {
        camera = new Camera(this);
        for (int i = 0; i < scales.length / 3; i++) {
            int index = i * 3;
            scales[index] = 0.000002 * Math.pow(10, i);
            scales[index + 1] = 0.000005 * Math.pow(10, i);
            scales[index + 2] = 0.00001 * Math.pow(10, i);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        this.zoom = (float) camera.getZoom(); // reduce typing
        leftWorldX = camera.getTranslatedX(0);
        rightWorldX = camera.getTranslatedX(getWidth());
        topWorldY = camera.getTranslatedY(0);
        bottomWorldY = camera.getTranslatedY(getHeight());

        g2.setColor(Color.white);
        g2.fillRect(0, 0, 2000, 1000);

        // offset the graphics in relation to the pos of the camera, to give the
        // illusion of movement
        camera.translateCamera(g2);

        // highlight the integral area on the graph first to avoid layer clashes
        // F(x)
        g2.setColor(Color.YELLOW);
        drawIntegralArea(F1, f1, F1x1, F1x2, g2);
        g2.setColor(Color.MAGENTA);
        drawIntegralArea(F2, f2, F2x1, F2x2, g2);
        g2.setColor(Color.GREEN);
        drawIntegralArea(F3, f1, f2, F3x1, F3x2, g2);

        // x, y axis lines
        drawAxes(g2);
        // x, y grid lines
        drawGrid(g2);

        // draw functions on the graph
        g2.setStroke(new BasicStroke((float) 2.4 / zoom));
        // FUNCTION 1
        // f''(x)
        g2.setColor(Color.blue);
        drawPolynomial(fprimeprime1, g2);
        // f'(x)
        g2.setColor(Color.green);
        drawPolynomial(fprime1, g2);
        // f(x)
        g2.setColor(Color.red);
        drawPolynomial(f1, g2);

        // FUNCTION 2
        // f''(x)
        g2.setColor(Color.blue);
        drawPolynomial(fprimeprime2, g2);
        // f'(x)
        g2.setColor(Color.green);
        drawPolynomial(fprime2, g2);
        // f(x)
        g2.setColor(Color.red);
        drawPolynomial(f2, g2);

    }

    private void drawAxes(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3 / zoom));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.draw(new Line2D.Double(-99999999, 0, 99999999, 0));
        g2.draw(new Line2D.Double(0, -99999999, 0, 99999999));
    }

    private void drawGrid(Graphics2D g2) {
        // diving by zoom ensures all line thicknesses are constant, no matter the zoom
        // on the camera
        double scale = getNearestScale((float) (100 / zoom)); // the grid lines should not be spaced apart more than
                                                               // 100 pixels

        g2.setStroke(new BasicStroke(1 / zoom));
        float textSize = 28 / zoom;
        Font base = new Font(Font.SERIF, Font.PLAIN, 1);
        Font resized = base.deriveFont(textSize);
        g2.setFont(resized);

        // Y LINES
        for (double x1 = leftWorldX - (leftWorldX % scale); x1 < rightWorldX; x1 += scale) {
            g2.draw(new Line2D.Double(x1, topWorldY, x1, bottomWorldY));
            g2.drawString(Double.toString(roundDecimals(x1 * xStretch, 6)), (float) (x1), (float) -2 / zoom);
        }
        // X LINES
        for (double y1 = topWorldY - (topWorldY % scale); y1 < bottomWorldY; y1 += scale) {
            g2.draw(new Line2D.Double(leftWorldX, y1, rightWorldX, y1));
            g2.drawString(Double.toString(roundDecimals(-y1, 6)), (float) 0, (float) y1 + (-2 / zoom));
        }

    }

    private void drawPolynomial(Polynomial func, Graphics2D g2) {
        if (func == null || func.getVisibility() == false) {
            return;
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // give it a lower transparency
        double deltaX = 1 / zoom * xStretch;
        for (double x1 = leftWorldX * xStretch; x1 < rightWorldX * xStretch; x1 += deltaX) {
            double y1 = func.getPolynomialOutput(x1);
            double x2 = x1 + deltaX;
            double y2 = func.getPolynomialOutput(x2);
            g2.draw(new Line2D.Double(x1 / xStretch, -y1, x2 / xStretch, -y2));
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

    }

    private void drawIntegralArea(Polynomial F, Polynomial f, double x1, double x2, Graphics2D g2) {
        if (F == null || F.getVisibility() == false) {
            return;
        }
        if (x1 > x2) { // swap the two values if x1 is larger than x2
            double tempx1 = x1;
            x1 = x2;
            x2 = tempx1;
        }

        {// shade in the area between the lines x-axis, x = x1, x = x2 and f(x)
            g2.setStroke(new BasicStroke((float) (1 / zoom)));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f)); // give it a lower transparency
            double deltaX = 1 / zoom * xStretch;
            for (double ax1 = Math.max(x1, leftWorldX * xStretch); ax1 < Math.min(x2,
                    rightWorldX * xStretch); ax1 += deltaX) {
                double ay1 = f.getPolynomialOutput(ax1);
                g2.draw(new Line2D.Double(ax1 / xStretch, 0, ax1 / xStretch, -ay1));
            }
        }

        {// label this highlighted region with the calculated area
            double area = Integrator.getDefiniteIntegral(F, x1, x2);
            float textSize = 50 / zoom;
            Font base = new Font(Font.SANS_SERIF, Font.PLAIN, 1);
            Font resized = base.deriveFont(textSize);
            g2.setFont(resized);
            g2.setColor(Color.BLACK);
            double xmid = (x1 + x2) / 2;
            g2.drawString(Double.toString(area), (float) (xmid / xStretch),
                    (float) (-f.getPolynomialOutput(xmid / 2)));
        }

        { // draw the lines x = x1 and x = x2
            double x1y1 = 0;
            double x1y2 = f.getPolynomialOutput(x1);
            double x2y1 = 0;
            double x2y2 = f.getPolynomialOutput(x2);

            float dashPeriodx1 = (float) Math.max(0.0001, Math.abs(x1y2) / 14);
            float[] dashPatternx1 = { dashPeriodx1 * 0.6f, dashPeriodx1 * 0.4f };
            g2.setStroke(
                    new BasicStroke(3 / zoom, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPatternx1,
                            0.0f));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2.setColor(Color.BLACK);
            g2.draw(new Line2D.Double(x1 / xStretch, x1y1, x1 / xStretch, -x1y2));

            float dashPeriodx2 = (float) Math.max(0.0001, Math.abs(x2y2) / 14);
            float[] dashPatternx2 = { dashPeriodx2 * 0.6f, dashPeriodx2 * 0.4f };
            g2.setStroke(
                    new BasicStroke(3 / zoom, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPatternx2,
                            0.0f));
            g2.setColor(Color.BLACK);
            g2.draw(new Line2D.Double(x2 / xStretch, x2y1, x2 / xStretch, -x2y2));
        }

    }

    private void drawIntegralArea(Polynomial F, Polynomial f1, Polynomial f2, double x1, double x2, Graphics2D g2) {
        if (F == null || F.getVisibility() == false) {
            return;
        }
        if (x1 > x2) { // swap the two values if x1 is larger than x2
            double tempx1 = x1;
            x1 = x2;
            x2 = tempx1;
        }

        { // shade in the area between the lines x-axis, x = x1, x = x2 and f(x)
            g2.setStroke(new BasicStroke((float) 1 / zoom));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f)); // give it a lower transparency
            double deltaX = 1 / zoom * xStretch;

            for (double ax1 = Math.max(x1, leftWorldX * xStretch); ax1 < Math.min(x2,
                    rightWorldX * xStretch); ax1 += deltaX) {
                double ay1 = f1.getPolynomialOutput(ax1);
                double ay2 = f2.getPolynomialOutput(ax1);
                g2.draw(new Line2D.Double(ax1 / xStretch, -ay1, ax1 / xStretch, -ay2));
            }
        }

        {// label this highlighted region with the calculated area
            double area = Integrator.getDefiniteIntegral(F, x1, x2);
            float textSize = 50 / zoom;
            Font base = new Font(Font.SANS_SERIF, Font.PLAIN, 1);
            Font resized = base.deriveFont(textSize);
            g2.setFont(resized);
            g2.setColor(Color.BLACK);
            double xmid = (x1 + x2) / 2;
            g2.drawString(Double.toString(area), (float) (xmid / xStretch),
                    (float) -((f1.getPolynomialOutput(xmid) + f2.getPolynomialOutput(xmid)) / 2));
        }

        {// draw the lines x = x1 and x = x2 bounding the area 
            
            double x1y1 = f2.getPolynomialOutput(x1);
            double x1y2 = f1.getPolynomialOutput(x1);
            double x2y1 = f2.getPolynomialOutput(x2);
            double x2y2 = f1.getPolynomialOutput(x2);
            

            float dashPeriodx1 = (float) Math.max(0.0001, Math.abs(x1y1 - x1y2) / 14);
            float[] dashPatternx1 = { dashPeriodx1 * 0.6f, dashPeriodx1 * 0.4f };
            g2.setStroke(
                    new BasicStroke(3 / zoom, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPatternx1,
                            0.0f));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2.setColor(Color.BLACK);
            g2.draw(new Line2D.Double(x1 / xStretch, -x1y1, x1 / xStretch, -x1y2));

            float dashPeriodx2 = (float) Math.max(0.0001, Math.abs(x2y1 - x2y2) / 14);
            float[] dashPatternx2 = { dashPeriodx2 * 0.6f, dashPeriodx2 * 0.4f };
            g2.setStroke(
                    new BasicStroke(3 / zoom, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPatternx2,
                            0.0f));
            g2.setColor(Color.BLACK);
            g2.draw(new Line2D.Double(x2 / xStretch, -x2y1, x2 / xStretch, -x2y2));
        }
    }

    private double getNearestScale(float zoom) {
        // scales[] is an ordered array so use a binary search to find the closest scale
        // to fit to the desired zoom amount
        int index = java.util.Arrays.binarySearch(scales, zoom);
        if (index >= 0) {
            // scale found
            return scales[index];
        } else {
            // scale not found, but closest value found
            return scales[Math.min(-(index + 1), 99)];
        }
    }

    private double roundDecimals(double n, int nDecimalPlaces) {
        double offset = (int) Math.pow(10, nDecimalPlaces);
        return Math.round(n * offset) / offset;
    }

    // TOGGLEABLES
    public void toggleVisibilityF1() {
        if (F1 != null) {
            F1.toggleVisibility();
        }
    }

    public void toggleVisibilityF1(boolean onOrOff) {
        if (F1 != null) {
            if (F1.getVisibility() != onOrOff) {
                F1.toggleVisibility();
            }
        }
    }

    public void toggleVisibilityf1() {
        if (f1 != null) {
            f1.toggleVisibility();
        }
    }

    public void toggleVisibilityfprime1() {
        if (fprime1 != null) {
            fprime1.toggleVisibility();
        }
    }

    public void toggleVisibilityfprimeprime1() {
        if (fprimeprime1 != null) {
            fprimeprime1.toggleVisibility();
        }
    }

    public void toggleVisibilityF2() {
        if (F2 != null) {
            F2.toggleVisibility();
        }
    }

    public void toggleVisibilityF2(boolean onOrOff) {
        if (F2 != null) {
            if (F2.getVisibility() != onOrOff) {
                F2.toggleVisibility();
            }
        }
    }

    public void toggleVisibilityf2() {
        if (f2 != null) {
            f2.toggleVisibility();
        }
    }

    public void toggleVisibilityfprime2() {
        if (fprime2 != null) {
            fprime2.toggleVisibility();
        }
    }

    public void toggleVisibilityfprimeprime2() {
        if (fprimeprime2 != null) {
            fprimeprime2.toggleVisibility();
        }
    }

    // SETTERS
    public void setF1(Polynomial F1) {
        this.F1 = F1;
    }

    public void setF2(Polynomial F2) {
        this.F2 = F2;
    }

    public void setF3(Polynomial F3) {
        this.F3 = F3;
    }

    public void setF1xvalues(double x1, double x2) {
        this.F1x1 = x1;
        this.F1x2 = x2;
    }

    public void setF2xvalues(double x1, double x2) {
        this.F2x1 = x1;
        this.F2x2 = x2;
    }

    public void setF3xvalues(double x1, double x2) {
        this.F3x1 = x1;
        this.F3x2 = x2;
    }

    public void setf1(Polynomial f1) {
        this.f1 = f1;
    }

    public void setf2(Polynomial f2) {
        this.f2 = f2;
    }

    public void setfprime1(Polynomial fprime1) {
        this.fprime1 = fprime1;
    }

    public void setfprime2(Polynomial fprime2) {
        this.fprime2 = fprime2;
    }

    public void setfprimeprime1(Polynomial fprimeprime1) {
        this.fprimeprime1 = fprimeprime1;
    }

    public void setfprimeprime2(Polynomial fprimeprime2) {
        this.fprimeprime2 = fprimeprime2;
    }

    public void setXStretch(double xStretch) {
        this.xStretch = xStretch;
    }

    // GETTERS
    public Polynomial getf1() {
        return this.f1;
    }

    public Polynomial getf2() {
        return this.f2;
    }

    public Camera getCamera() {
        return camera;
    }
}
