import java.util.*;
import comp100.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.*;
import java.lang.*;

public class tests implements MouseListener
{
    public static void main(String[] args)
    {
        tests abc = new tests();
        abc.method();
    }
    
    private double minx = -2;
    private double maxx = 1;
    private double miny = -1.5;
    private double maxy = 1.5;
    
    public void mouseReleased(MouseEvent e)
    {
        double newx = (maxx-minx)*e.getX()/1000 + minx;
        double newy = (maxy-miny)*e.getY()/1000 + miny;
        maxx = (newx + maxx)/2;
        minx = (newx + minx)/2;
        maxy = (newy + maxy)/2;
        miny = (newy + miny)/2;

        draw();
    }
    
    public void mousePressed(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    private DrawingCanvas canvas;
    
    public void method()
    {
        canvas = new DrawingCanvas();
        JFrame frame = new JFrame("RTS2");
        frame.setSize(1000,1000);
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);
        canvas.addMouseListener(this);
        
        draw();
    }
    
    private void draw()
    {
        for (double xc = 0; xc < 1000; xc += 1)
        {
            for (double yc= 0; yc < 1000; yc += 1)
            {
                double x0 = (xc/1000)*(maxx - minx) + minx;
                double y0 = (yc/1000)*(maxy - miny) + miny;
                
                double x = 0;
                double y = 0;
                
                int iteration = 0;
                int max_iteration = 512;
                
                while ( x*x + y*y <= (2*2) && iteration < max_iteration )
                {
                    double xtemp = x*x - y*y + x0;
                    y = 2*x*y + y0;
                    
                    x = xtemp;
                    
                    iteration++;
                }
                
                if (iteration == max_iteration )
                {
                    Color cat = new Color(0,0,0);
                    canvas.setColor(cat);
                }
                else
                {
                    if(iteration > 255)
                        iteration = 255;
                    //Color cat = new Color(iteration,16*iteration%16, 32*iteration%8);
                    Color cat = new Color(iteration,16*iteration%64,32*iteration%128);
                    canvas.setColor(cat);
                }
                
                canvas.drawRect(xc, yc, 1, 1);
            }
        }
    }
}