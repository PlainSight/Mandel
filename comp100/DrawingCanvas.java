package comp100;

import java.awt.*;
import javax.swing.JComponent;
import java.io.File;

/** A DrawingCanvas is an area for drawing shapes, text, and images.
    It uses double buffering for smoother graphics. In order to use the double
    buffering, you should first draw all the shapes with the redraw argument set
    to false, then call display().
    <P>
    It provides methods for
    <UL>
    <LI> drawing (outline or filled) shapes
    <LI> inverting the colour of lines and rectangles
    <LI> drawing text
    <LI> loading images
    <LI> redisplaying, and clearing the canvas
    <LI> clearing rectangular section of the canvas
    <LI> changing the foreground colour and the fontsize
    </UL>
    <P>

    All the methods that require coordinates, width, and height have two
    versions, one (the standard) which requires all arguments to be int's,
    and another (non-standard) which allows the arguments to be doubles.
    However, as the Java Canvas class only accepts int, we recommend that
    you call these methods with int.*/

// It is not clear what class DrawingCanvas should extend
//  For java 1.2 we had to have a heavyweight than lightweight component
// or we get NullPointerExceptions from
// modal dialog boxes invoked from mouse handlers on the component.

// Component works on netbsd with java 1.4.2, but not windows with java 1.4.2
// public class DrawingCanvas extends Component {  

//JPanel works on netbsd with java 1.4.2, but not windows with java 1.4.2
//public class DrawingCanvas extends JPanel { 

//Canvas seems to work on both netbsd with java 1.4.2, and windows with java 
// 1.4.2
public class DrawingCanvas extends Component {

    private Image imgBuf, visibleBuf;
    private Graphics imgGraphic, visibleGraphic;

    /** Maximum width of the canvas */
    public static final int MaxX =  1280;
    /** Maximum height of the canvas */
    public static final int MaxY =  1024;

    public void addNotify() {
	super.addNotify();
	this.setBackground(Color.white);
	imgBuf = createImage(MaxX, MaxY);          // Can only be done by peer
	imgGraphic = imgBuf.getGraphics();
	imgGraphic.setPaintMode();
	imgGraphic.setColor(Color.black);
	visibleBuf = createImage(MaxX, MaxY);
	visibleGraphic = visibleBuf.getGraphics();
	visibleGraphic.setPaintMode();
	visibleGraphic.setColor(Color.white);
	visibleGraphic.fillRect(0,0,MaxX,MaxY);
	visibleGraphic.setColor(Color.black);
	display();
    }

    private int printCount = 0;

    public void print(Graphics g) {
	super.print(g);
    }

    public void paint(Graphics g) {
	g.drawImage(visibleBuf, 0, 0, null);
    }

    /** Set the current font size. */
    public void setFontSize(int size) {
	imgGraphic.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, size));
    }

    public void update(Graphics g) {            // Stops component being cleared
	paint(g);
    }

    public Dimension getPreferredSize() {
	return new Dimension(Math.min(640, MaxX), Math.min(480, MaxY));
    }

    public Dimension getMaximumSize() {
	return new Dimension(MaxX, MaxY);
    }

    /** Redisplay the entire canvas area, including all the shapes that have
     *  not yet been redisplayed. 
     */
    public void display() {
	Dimension d = getSize();
	display(0, 0, d.width, d.height);
    }

    /** Redisplay the canvas area, including all the shapes that have not yet
     *  been redisplayed. 
     *  <BR>
     *  The area left edge is <I>x</I>, top edge is <I>y</I>. The area has the
     *  given <I>width</I> and <I>height</I>.
     */
    public void display(int x, int y, int width, int height) {
	java.awt.Shape clip = visibleGraphic.getClip();
	visibleGraphic.setClip(x, y, width + 1, height + 1);
	visibleGraphic.drawImage(imgBuf, 0, 0, null);
	visibleGraphic.setClip(clip);
	repaint();
    }

    /** Get the Graphics object that is the backing store of the image, so that
	programs can do more complicated operations on the image than are
	provided by this class.<BR>
	Standard usage would be to get the graphics object, call methods on it,
	and then call the display() method on the DrawingCanvas to update the 
	visible image with the modifications.*/
    public Graphics getBackingGraphics() {
	return imgGraphic;
    }

    /** Clear and redisplay the canvas area. 
     *	This has exactly the same effect as: clear(true)
     */
    public void clear() {
	Color save = imgGraphic.getColor();
	imgGraphic.setColor(Color.white);
	imgGraphic.fillRect(0, 0, MaxX, MaxY);
	imgGraphic.setColor(save);
	display();
    }

    /** 
     * Clear the canvas area.<BR>
     * With an argument of false, do not redisplay the canvas yet. 
     */
    public void clear(boolean redraw) {
	Color save = imgGraphic.getColor();
	imgGraphic.setColor(Color.white);
	imgGraphic.fillRect(0, 0, MaxX, MaxY);
	imgGraphic.setColor(save);
	if (redraw) display();
    }

    /** Set the current foreground colour - the colour of all subsequent
     *  shapes or text.
     */
    public void setColor(Color c) {
	if (imgGraphic != null)
	    imgGraphic.setColor(c);
	//super.setForeground(c);
    }

    /** Set the current foreground colour - the colour of all subsequent
     *  shapes or text.
     */
    public void setForeground(Color c) {
	if (imgGraphic != null)
	    imgGraphic.setColor(c);
	//super.setForeground(c);
    }

    /** Draw a line between (x1, y1) and (x2, y2) and redisplay the canvas.
     *	This has exactly the same effect as: drawLine(x1, y1, x2, y2, true)
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
	imgGraphic.drawLine(x1, y1, x2, y2);
	display(Math.min(x1, x2), Math.min(y1, y2), 
		Math.abs(x2 - x1) + 1, Math.abs(y2 - y1) + 1);
    }

    /** Draw a line between (x1, y1) and (x2, y2).
	<BR>
	With a fifth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void drawLine(int x1, int y1, int x2, int y2, boolean redraw) {
	imgGraphic.drawLine(x1, y1, x2, y2);
	if (redraw)
	    display(Math.min(x1, x2), Math.min(y1, y2), 
		    Math.abs(x2 - x1) + 1, Math.abs(y2 - y1) + 1);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
	drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }

    public void drawLine(double x1, double y1, double x2, double y2, boolean redraw) {
	drawLine((int) x1, (int) y1, (int) x2, (int) y2, redraw);
    }

    /** Draw an outline rectangle with left edge at <I>x</I>, 
	top edge at <I>y</I> of <I>width</I> and <I>height</I>
	and redisplay the canvas.
     	This has exactly the same effect as: drawRect(x, y, width, height, true)
     */
    public void drawRect(int x, int y, int width, int height) {
	imgGraphic.drawRect(x, y, width, height);
	display(x, y, width, height);
    }

    /** Draw an outline rectangle with left edge at <I>x</I>,
	top edge at <I>y</I> of <I>width</I> and <I>height</I>. 
	<BR>
	With a fifth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void drawRect(int x, int y, int width, int height, boolean redraw) {
	imgGraphic.drawRect(x, y, width, height);
	if (redraw)
	    display(x, y, width, height);
    }

    public void drawRect(double x, double y, double width, double height) {
	drawRect((int) x, (int) y, (int) width, (int) height);
    }

    public void drawRect(double x, double y, double width, double height, boolean redraw) {
	drawRect((int) x, (int) y, (int) width, (int) height, redraw);
    }

    /** Draw a filled rectangle with left edge at <I>x</I>, 
	top edge at <I>y</I> of <I>width</I> and <I>height</I>
	and redisplay the canvas.
     	This has exactly the same effect as: fillRect(x, y, width, height, true)
    */
    public void fillRect(int x, int y, int width, int height) {
	/* The imgGraphic.fillRect only draws the "filling" part of the
	 * rectangle. So we add 1 to the sizes to make it the same size as the
	 * outline rectangle. */
	imgGraphic.fillRect(x, y, width + 1, height + 1);
	display(x, y, width + 1, height + 1);
    }

    /** Draw a filled rectangle with left edge at <I>x</I>,
	top edge at <I>y</I> of <I>width</I> and <I>height</I>. 
	<BR>
	With a fifth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void fillRect(int x, int y, int width, int height, boolean redraw) {
	imgGraphic.fillRect(x, y, width + 1, height + 1);
	if (redraw)
	    display(x, y, width + 1, height + 1);
    }

    public void fillRect(double x, double y, double width, double height) {
	fillRect((int) x, (int) y, (int) width, (int) height);
    }

    public void fillRect(double x, double y, double width, double height, boolean redraw) {
	fillRect((int) x, (int) y, (int) width, (int) height, redraw);
    }

    /** Clear the rectangular region with left edge at <I>x</I>, top edge at
	<I>y</I> of <I>width</I> and <I>height</I>
	and redisplay the canvas.
     	This has exactly the same effect as: clearRect(x, y, width, height, true)
    */
    public void clearRect(int x, int y, int width, int height) {
	Color save = imgGraphic.getColor();
	imgGraphic.setColor(Color.white);
	imgGraphic.fillRect(x, y, width, height);
	imgGraphic.setColor(save);
	display(x, y, width, height);
    }

    /** Clear the rectangular region with left edge at <I>x</I>, top edge at
	<I>y</I> of <I>width</I> and <I>height</I>.
	<BR>
	With a fifth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void clearRect(int x, int y, int width, int height, boolean redraw) {
	Color save = imgGraphic.getColor();
	imgGraphic.setColor(Color.white);
	imgGraphic.fillRect(x, y, width, height);
	imgGraphic.setColor(save);
	if (redraw)
	    display(x, y, width, height);
    }
    public void clearRect(double x, double y, double width, double height) {
	clearRect((int) x, (int) y, (int) width, (int) height);
    }

    public void clearRect(double x, double y, double width, double height, boolean redraw) {
	clearRect((int) x, (int) y, (int) width, (int) height, redraw);
    }

    /** Draw a string with its bottom left corner at (x, y) 
	and redisplay the canvas.
	This has exactly the same effect as: drawString(s, x, y, true)
    */
    public void drawString(String s, int x, int y) {
	imgGraphic.drawString(s, x, y);
	FontMetrics fm = imgGraphic.getFontMetrics();
	display(x, y - fm.getMaxAscent(), 
		fm.stringWidth(s) + fm.getMaxAdvance(), 
		fm.getMaxAscent() + fm.getMaxDescent());
    }

    /** Draw a string with its bottom left corner at (x, y).
	<BR>
	With a fourth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void drawString(String s, int x, int y, boolean redraw) {
	imgGraphic.drawString(s, x, y);
	if (redraw) {
	    FontMetrics fm = imgGraphic.getFontMetrics();
	    display(x, y - fm.getMaxAscent(), 
		    fm.stringWidth(s) + fm.getMaxAdvance(), 
		    fm.getMaxAscent() + fm.getMaxDescent());
	}
    }

    public void drawString(String s, double x, double y) {
	drawString(s, (int) x, (int) y);
    }

    public void drawString(String s, double x, double y, boolean redraw) {
	drawString(s, (int) x, (int) y, redraw);
    }


    /** Draw an outline oval with left edge at <I>x</I>, top edge
	at <I>y</I> of <I>width</I> and <I>height</I>
	and redisplay the canvas.
     	This has exactly the same effect as: drawOval(x, y, width, height, true)
    */
    public void drawOval(int x, int y, int width, int height) {
	imgGraphic.drawOval(x, y, width, height);
	display(x, y, width, height);
    }

    /** Draw an outline oval with left edge at <I>x</I>, top edge
	at <I>y</I> of <I>width</I> and <I>height</I>.<BR>
	With a fifth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void drawOval(int x, int y, int width, int height, boolean redraw) {
	imgGraphic.drawOval(x, y, width, height);
	if (redraw)
	    display(x, y, width, height);
    }

    public void drawOval(double x, double y, double width, double height) {
	drawOval((int) x, (int) y, (int) width, (int) height);
    }

    public void drawOval(double x, double y, double width, double height, boolean redraw) {
	drawOval((int) x, (int) y, (int) width, (int) height, redraw);
    }

    /** Draw a filled oval with left edge at <I>x</I>, top edge
	at <I>y</I> of <I>width</I> and <I>height</I>
	and redisplay the canvas.
     	This has exactly the same effect as: fillOval(x, y, width, height, true)
    */
    public void fillOval(int x, int y, int width, int height) {
	/* This is implemented differently than for fillRect. For oval, 
	   we don't increase the size. Instead we draw the filling and the
	   outline.*/
	imgGraphic.fillOval(x, y, width, height);
	imgGraphic.drawOval(x, y, width, height);
	display(x, y, width, height);
    }

    /** Draw a filled oval with left edge at <I>x</I>, top edge
	at <I>y</I> of <I>width</I> and <I>height</I>.<BR>
	With a fifth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void fillOval(int x, int y, int width, int height, boolean redraw) {
	imgGraphic.fillOval(x, y, width, height);
	imgGraphic.drawOval(x, y, width, height);
	if (redraw)
	    display(x, y, width, height);
    }

    public void fillOval(double x, double y, double width, double height) {
	fillOval((int) x, (int) y, (int) width, (int) height);
    }

    public void fillOval(double x, double y, double width, double height, boolean redraw) {
	fillOval((int) x, (int) y, (int) width, (int) height, redraw);
    }

    /** Draw an outline arc of an oval and redisplay the canvas.
	The left edge of the oval would be at <I>x</I>, top edge at <I>y</I> of 
	<I>width</I> and <I>height</I>. The arc begins at <I>startAngle</I> and 
	extends for <I>arcAngle</I> degrees.<BR>
	Angles are interpreted such that 0 degrees is at the 3 o'clock position.
	A positive value indicates a counter-clockwise rotation while a negative
	value indicates a clockwise rotation.
	This has exactly the same effect as: 
	drawArc(x, y, width, height, startAngle, arcAngle, true)
    */
    public void drawArc(int x, int y, int width, int height, 
			int startAngle, int arcAngle) {
	imgGraphic.drawArc(x, y, width, height, startAngle, arcAngle);
	display(x, y, width, height);
    }

    /** Draw an outline arc of an oval.
	The left edge of the oval would be at <I>x</I>, top edge at <I>y</I> of 
	<I>width</I> and <I>height</I>. The arc begins at <I>startAngle</I> and 
	extends for <I>arcAngle</I> degrees.<BR>
	Angles are interpreted such that 0 degrees is at the 3 o'clock position.
	A positive value indicates a counter-clockwise rotation while a negative
	value indicates a clockwise rotation. <BR>
	With a seventh argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void drawArc(int x, int y, int width, int height, 
			int startAngle, int arcAngle, boolean redraw) {
	imgGraphic.drawArc(x, y, width, height, startAngle, arcAngle);
	if (redraw)
	    display(x, y, width, height);
    }

    public void drawArc(double x, double y, double width, double height,
			double startAngle, double arcAngle) {
	drawArc((int) x, (int) y, (int) width, (int) height,
		(int) startAngle, (int) arcAngle);
    }

    public void drawArc(double x, double y, double width, double height,
			double startAngle, double arcAngle, boolean redraw) {
	drawArc((int) x, (int) y, (int) width, (int) height,
		(int) startAngle, (int) arcAngle, redraw);
    }

    /** Draw a filled arc of an oval and redisplay the canvas.
	The left edge of the oval would be at <I>x</I>, top edge at <I>y</I> of 
	<I>width</I> and <I>height</I>. The arc begins at <I>startAngle</I> and 
	extends for <I>arcAngle</I> degrees.<BR>
	Angles are interpreted such that 0 degrees is at the 3 o'clock position.
	A positive value indicates a counter-clockwise rotation while a negative
	value indicates a clockwise rotation.
	This has exactly the same effect as: 
	fillArc(x, y, width, height, startAngle, arcAngle, true)
    */
    public void fillArc(int x, int y, int width, int height, 
			int startAngle, int arcAngle) {
	imgGraphic.fillArc(x, y, width, height, startAngle, arcAngle);
	imgGraphic.drawArc(x, y, width, height, startAngle, arcAngle);
	display(x, y, width, height);
    }

    /** Draw a filled arc of an oval.
	The left edge of the oval would be at <I>x</I>, top edge at <I>y</I> of 
	<I>width</I> and <I>height</I>. The arc begins at <I>startAngle</I> and 
	extends for <I>arcAngle</I> degrees.<BR>
	Angles are interpreted such that 0 degrees is at the 3 o'clock position.
	A positive value indicates a counter-clockwise rotation while a negative
	value indicates a clockwise rotation. <BR>
	With a seventh argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void fillArc(int x, int y, int width, int height, 
			int startAngle, int arcAngle, boolean redraw) {
	imgGraphic.fillArc(x, y, width, height, startAngle, arcAngle);
	imgGraphic.drawArc(x, y, width, height, startAngle, arcAngle);
	if (redraw)
	    display(x, y, width, height);
    }

    public void fillArc(double x, double y, double width, double height,
			double startAngle, double arcAngle) {
	fillArc((int) x, (int) y, (int) width, (int) height,
		(int) startAngle, (int) arcAngle);
    }

    public void fillArc(double x, double y, double width, double height,
			double startAngle, double arcAngle, boolean redraw) {
	fillArc((int) x, (int) y, (int) width, (int) height,
		(int) startAngle, (int) arcAngle, redraw);
    }

    /** Invert the colour of the line between (x1, y1) and (x2, y2)
	and redisplay the canvas.
	Inverting again will restore the original colours.
	This has exactly the same effect as: invertLine(x1, y1, x2, y2, true)
    */
    public void invertLine(int x1, int y1, int x2, int y2) {
	imgGraphic.setXORMode(Color.white);
	imgGraphic.drawLine(x1, y1, x2, y2);
	imgGraphic.setPaintMode();
	display(Math.min(x1, x2), Math.min(y1, y2), 
		Math.abs(x2 - x1) + 1, Math.abs(y2 - y1) + 1);
    }

    /** Invert the colour of the line between (x1, y1) and (x2, y2).
	Inverting again will restore the original colours.
	<BR>
	With a fifth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void invertLine(int x1, int y1, int x2, int y2, boolean redraw) {
	imgGraphic.setXORMode(Color.white);
	imgGraphic.drawLine(x1, y1, x2, y2);
	imgGraphic.setPaintMode();
	if (redraw)
	    display(Math.min(x1, x2), Math.min(y1, y2), 
		    Math.abs(x2 - x1) + 1, Math.abs(y2 - y1) + 1);
    }

    public void invertLine(double x1, double y1, double x2, double y2) {
	invertLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    public void invertLine(double x1, double y1, double x2, double y2, boolean redraw) {
	invertLine((int) x1, (int) y1, (int) x2, (int) y2, redraw);
    }

    /** Invert the rectangular region with left edge at <I>x</I>, top edge
	at <I>y</I>of <I>width</I> and <I>height</I>
	and redisplay the canvas. 
	Inverting again will restore the original colours.
	This has exactly the same effect as: 
	invertRect(x, y, width, height, true)
    */
    public void invertRect(int x, int y, int width, int height) {
	imgGraphic.setXORMode(Color.white);
	imgGraphic.drawRect(x, y, width, height);
	imgGraphic.setPaintMode();
	display(x, y, width, height);
    }

    /** Invert the rectangular region with left edge at <I>x</I>, top edge
	at <I>y</I>of <I>width</I> and <I>height</I>.
	Inverting again will restore the original colours.
	<BR>
	With a fifth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void invertRect(int x, int y, int width, int height, boolean redraw) {
	imgGraphic.setXORMode(Color.white);
	imgGraphic.drawRect(x, y, width, height);
	imgGraphic.setPaintMode();
	if (redraw)
	    display(x, y, width, height);
    }

    public void invertRect(double x, double y, double width, double height) {
	invertRect((int) x, (int) y, (int) width, (int) height);
    }

    public void invertRect(double x, double y, double width, double height, boolean redraw) {
	invertRect((int) x, (int) y, (int) width, (int) height, redraw);
    }


    /** Load the image from <I>name</I>, draw it with left edge at <I>x</I>,
	top edge at <I>y</I> scaled to <I>width</I> and <I>height</I>
	and redisplay the canvas.
	This has exactly the same effect as: 
	drawImage(name, x, y, width, height, true)
    */
    public void drawImage(String name, int x, int y, int width, int height) {
	File fh = new File(name);
	if (fh.canRead()) {
	    Image img;
	    MediaTracker media = new MediaTracker(this);
	    img = Toolkit.getDefaultToolkit().getImage(name);
	    media.addImage(img, 0);
	    try {
		media.waitForID(0);
	    } catch (Exception e) {
	    }
	    imgGraphic.drawImage(img, x, y, width, height, this.getBackground(), 
				 this);
	} else {
	    // The file either doesn't exist or we don't have read access
	    imgGraphic.drawRect(x, y, width, height);
	    imgGraphic.drawLine(x, y, width, height);
	    imgGraphic.drawLine(width, y, x, height);
	}
	display();
    }

    /** Load the image from <I>name</I> and draw it with left edge at <I>x</I>,
	top edge at <I>y</I> scaled to <I>width</I> and <I>height</I>.
	<BR>
	With a sixth argument of <I>false</I>, do not redisplay the canvas 
	yet. */
    public void drawImage(String name, int x, int y, int width, int height, boolean redraw) {
	File fh = new File(name);
	if (fh.canRead()) {
	    Image img;
	    MediaTracker media = new MediaTracker(this);
	    img = Toolkit.getDefaultToolkit().getImage(name);
	    media.addImage(img, 0);
	    try {
		media.waitForID(0);
	    } catch (Exception e) {
	    }
	    imgGraphic.drawImage(img, x, y, width, height, this.getBackground(), 
				 this);
	} else {
	    // The file either doesn't exist or we don't have read access
	    imgGraphic.drawRect(x, y, width, height);
	    imgGraphic.drawLine(x, y, width, height);
	    imgGraphic.drawLine(width, y, x, height);
	}
	if (redraw)
	    display();
    }

    public void drawImage(String name, double x, double y, double width, double height) {
	drawImage(name, (int) x, (int) y, (int) width, (int) height);
    }

    public void drawImage(String name, double x, double y, double width, double height,
			  boolean redraw) {
	drawImage(name, (int) x, (int) y, (int) width, (int) height, redraw);
    }
}
