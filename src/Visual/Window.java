package Visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import Calculus.*;

public class Window extends JFrame {

    private Graph graph;

    public Window() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Differentiation Calculator");
        this.setMinimumSize(new Dimension(1400, 900));
        this.setPreferredSize(new Dimension(1400, 900));
        this.setLayout(new BorderLayout());

        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        {
            try {
                UIManager.setLookAndFeel(looks[3].getClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }

        JPanel menu1 = new JPanel();
        {
            // menu1.setPreferredSize(new Dimension(1200, 150));
            menu1.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JPanel cameraMenu = new JPanel();
            {// create component
                cameraMenu.setLayout(new GridLayout(1, 2));
                JButton resetCameraPosition = new JButton("Reset Camera to Default");
                resetCameraPosition.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        graph.getCamera().resetCameraPosition();
                        graph.getCamera().resetCameraZoom();
                    }
                });
                cameraMenu.add(resetCameraPosition);
            }
            {// set layout constraints
                c.weightx = 1;
                c.fill = GridBagConstraints.BOTH;
                c.gridwidth = 2;
                c.gridx = 0;
                c.gridy = 0;
            }
            menu1.add(cameraMenu, c);

            JTextField inputfunc1 = new JTextField("Enter function 1 here", 40);
            {
                Font font1 = new Font(Font.SERIF, Font.PLAIN, 22);
                inputfunc1.setFont(font1);
                inputfunc1.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        Polynomial f1 = Polynomial.createPolynomial(inputfunc1.getText());
                        Polynomial fprime1 = Differentiator.getDerivative(f1);
                        Polynomial fprimeprime1 = Differentiator.getDerivative(fprime1);
                        graph.setF1(null);
                        graph.setF3(null);
                        graph.setf1(f1);
                        graph.setfprime1(fprime1);
                        graph.setfprimeprime1(fprimeprime1);
                        repaint();
                    }
                });
            }
            {// set layout constraints
                c.gridwidth = 1;
                c.weightx = 1;
                c.gridx = 0;
                c.gridy = 1;
            }
            menu1.add(inputfunc1, c);

            JPanel showHideMenu1 = new JPanel();
            {
                showHideMenu1.setLayout(new GridLayout(1, 4));
                JButton showButtonF = new JButton("Show/Hide integral area");
                showButtonF.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        graph.toggleVisibilityF1();
                    }
                });
                showButtonF.setBackground(Color.ORANGE);
                showHideMenu1.add(showButtonF);

                JButton showButtonf = new JButton("Show/Hide original function");
                showButtonf.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        graph.toggleVisibilityf1();
                    }
                });
                showButtonf.setBackground(Color.RED);
                showHideMenu1.add(showButtonf);

                JButton showButtonfprime = new JButton("Show/Hide first derivative");
                showButtonfprime.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        graph.toggleVisibilityfprime1();
                    }
                });
                showButtonfprime.setBackground(Color.GREEN);
                showHideMenu1.add(showButtonfprime);

                JButton showButtonfprimeprime = new JButton("Show/Hide second derivative");
                showButtonfprimeprime.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        graph.toggleVisibilityfprimeprime1();
                    }
                });
                showButtonfprimeprime.setBackground(Color.BLUE);
                showHideMenu1.add(showButtonfprimeprime);

            }
            {// set layout constraints
                c.gridwidth = 1;
                c.weightx = 1;
                c.gridx = 0;
                c.gridy = 2;
            }
            menu1.add(showHideMenu1, c);

            JButton integrate1 = new JButton("Integrate between two x values (signed area)");
            integrate1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent event) {
                    String x1str = JOptionPane.showInputDialog(integrate1.getParent(), "What is the first value?",
                            null);
                    String x2str = JOptionPane.showInputDialog(integrate1.getParent(), "What is the second value?",
                            null);

                    if (x1str == null || x2str == null) { // cancel button was pressed
                        return;
                    }

                    try {
                        Double x1 = Double.parseDouble(x1str);
                        Double x2 = Double.parseDouble(x2str);

                        graph.setF1(Integrator.getIndefiniteIntegral(graph.getf1()));
                        graph.setF1xvalues(x1, x2);

                    } catch (Exception e) {
                        System.err.println(new Exception("Invalid x values for integration"));
                    }
                }
            });
            {// set layout constraints
                c.ipady = 10;
                c.gridwidth = 1;
                c.weightx = 1;
                c.gridx = 0;
                c.gridy = 3;
            }
            menu1.add(integrate1, c);

            JTextField inputfunc2 = new JTextField("Enter function 2 here", 40);
            {
                Font font1 = new Font(Font.SERIF, Font.PLAIN, 22);
                inputfunc2.setFont(font1);
                inputfunc2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        Polynomial f2 = Polynomial.createPolynomial(inputfunc2.getText());
                        Polynomial fprime2 = Differentiator.getDerivative(f2);
                        Polynomial fprimeprime2 = Differentiator.getDerivative(fprime2);
                        graph.setF2(null);
                        graph.setF3(null);
                        graph.setf2(f2);
                        graph.setfprime2(fprime2);
                        graph.setfprimeprime2(fprimeprime2);
                        repaint();
                    }
                });
            }
            {// set layout constraints
                c.ipady = 0;
                c.gridwidth = 1;
                c.weightx = 1;
                c.gridx = 1;
                c.gridy = 1;
            }
            menu1.add(inputfunc2, c);

            JPanel showHideMenu2 = new JPanel();
            {
                showHideMenu2.setLayout(new GridLayout(1, 4));
                JButton showButtonF = new JButton("Show/Hide integral area");
                showButtonF.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        graph.toggleVisibilityF2();
                    }
                });
                showButtonF.setBackground(Color.ORANGE);
                showHideMenu2.add(showButtonF);

                JButton showButtonf = new JButton("Show/Hide original function");
                showButtonf.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        graph.toggleVisibilityf2();
                    }
                });
                showButtonf.setBackground(Color.RED);
                showHideMenu2.add(showButtonf);

                JButton showButtonfprime = new JButton("Show/Hide first derivative");
                showButtonfprime.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        graph.toggleVisibilityfprime2();
                    }
                });
                showButtonfprime.setBackground(Color.GREEN);
                showHideMenu2.add(showButtonfprime);

                JButton showButtonfprimeprime = new JButton("Show/Hide second derivative");
                showButtonfprimeprime.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent event) {
                        graph.toggleVisibilityfprimeprime2();
                    }
                });
                showButtonfprimeprime.setBackground(Color.BLUE);
                showHideMenu2.add(showButtonfprimeprime);

            }
            {// set layout constraints
                c.gridwidth = 1;
                c.weightx = 1;
                c.gridx = 1;
                c.gridy = 2;
            }
            menu1.add(showHideMenu2, c);

            JButton integrate2 = new JButton("Integrate between two x values (signed area)");
            integrate2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent event) {
                    String x1str = JOptionPane.showInputDialog(integrate2.getParent(), "What is the first x value?",
                            null);
                    String x2str = JOptionPane.showInputDialog(integrate2.getParent(), "What is the second x value?",
                            null);

                    if (x1str == null || x2str == null) { // cancel button was pressed
                        return;
                    }

                    try {
                        Double x1 = Double.parseDouble(x1str);
                        Double x2 = Double.parseDouble(x2str);

                        graph.setF2(Integrator.getIndefiniteIntegral(graph.getf2()));
                        graph.setF2xvalues(x1, x2);

                    } catch (Exception e) {
                        System.err.println(new Exception("Invalid x values for integration"));
                    }
                }
            });
            {// set layout constraints
                c.ipady = 10;
                c.gridwidth = 1;
                c.weightx = 1;
                c.gridx = 1;
                c.gridy = 3;
            }
            menu1.add(integrate2, c);

            JButton integrate3 = new JButton("Integrate between the two functions, between two x values");
            integrate3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent event) {
                    String x1str = JOptionPane.showInputDialog(integrate3.getParent(), "What is the first x value?",
                            null);
                    String x2str = JOptionPane.showInputDialog(integrate3.getParent(), "What is the second x value?",
                            null);
                    String chooseHigherstr = JOptionPane.showInputDialog(integrate3.getParent(),
                            "Which function is on top? (f1/f2) \n [if left blank or invalid input given, f1 will be assumed above]",
                            null);

                    if (x1str == null || x2str == null) { // cancel button was pressed or nothing was given for an input
                        return;
                    }

                    try {
                        Double x1 = Double.parseDouble(x1str);
                        Double x2 = Double.parseDouble(x2str);
                        boolean isf1Highest;
                        switch (chooseHigherstr) {
                            case "f1":
                                isf1Highest = true;
                                break;
                            case "f2":
                                isf1Highest = false;
                                break;
                            default:
                                isf1Highest = true;
                                break;
                        }
                        if (isf1Highest) {
                            graph.setF3(Integrator.getIndefiniteIntegral(graph.getf1(), graph.getf2()));
                        } else {
                            graph.setF3(Integrator.getIndefiniteIntegral(graph.getf2(), graph.getf1()));
                        }
                        graph.setF3xvalues(x1, x2);

                    } catch (Exception e) {
                        System.err.println(new Exception("Invalid x values for integration"));
                    }
                }
            });
            {// set layout constraints
                c.gridwidth = 2;
                c.ipady = 10;
                c.weightx = 1;
                c.gridx = 0;
                c.gridy = 4;
            }
            menu1.add(integrate3, c);

            JSlider xAxisSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
            {
                xAxisSlider.setMinorTickSpacing(2);
                xAxisSlider.setMajorTickSpacing(10);
                xAxisSlider.setPaintTicks(true);
                xAxisSlider.setPaintLabels(true);
                xAxisSlider.setInverted(true);
                xAxisSlider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(javax.swing.event.ChangeEvent e) {
                        JSlider source = (JSlider) e.getSource();
                        if (source.getValueIsAdjusting()) {
                            graph.setXStretch((double) Math.max(1, source.getValue()) / 100);
                        }
                    }
                });
            }
            {// set layout constraints
                c.gridwidth = 2;
                c.gridx = 0;
                c.gridy = 5;
                c.insets = new Insets(0, 0, 0, 60);
            }
            menu1.add(xAxisSlider, c);

        }

        this.add(menu1, BorderLayout.NORTH);


        this.graph = new Graph();
        graph.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                graph.getCamera().setMouseWheelEvent(event);
                graph.getCamera().mouseWheelMoved();
                repaint();
            }

        });
        this.add(graph, BorderLayout.CENTER);

        this.setVisible(true);

    }

    public void update() {
        graph.getCamera().updateCamera();
        repaint();
    }
}