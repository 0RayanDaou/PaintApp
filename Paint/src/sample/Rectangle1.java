package sample;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Rectangle1 extends Shapes{
    private GraphicsContext graphicsContext;
    private Color cpLine;
    private Color cpFill;

    public double startX, startY, endX, endY, width, height;

    private Rectangle rectangle = new Rectangle();

    public Rectangle1(){ }

    public void setGraphicsContext(GraphicsContext graphicsContext){
        this.graphicsContext = graphicsContext;
    }

    public void setColor(Color color){
        this.cpLine = color;
    }

    public void setFill(Color color){
        this.cpFill = color;
    }

    public void setStartPoint(double startX, double startY){
        this.startX = startX;
        this.startY = startY;

        rectangle.setX(startX);
        rectangle.setY(startY);
    }

    public void setEndPoint(double endX, double endY){
        this.endX = endX;
        this.endY = endY;
    }

    public void setWidth(){
        this.width = Math.abs((endX - startX));

        rectangle.setWidth(Math.abs((endX - startX)));
    }

    public void setHeight(){
        this.height = Math.abs((endY - startY));

        rectangle.setHeight(Math.abs((endY - startY)));
    }

    public boolean containsPoint(Point2D point){
        return rectangle.contains(point);
    }

    public void check(){
        if(getX() > endX) {
            rectangle.setX(endX);
        }
        if(getY() > endY) {
            rectangle.setY(endY);
        }
    }

    public double getX(){
        return rectangle.getX();
    }

    public double getY(){
        return rectangle.getY();
    }

    public double getWidth(){
        return rectangle.getWidth();
    }

    public double getHeight() {
        return rectangle.getHeight();
    }

    public Color getColor(){
        return cpLine;
    }

    public Color getFill(){
        return cpFill;
    }

    public void draw(){
        graphicsContext.setStroke(cpLine);
        graphicsContext.setFill(cpFill);

        graphicsContext.fillRect(getX(), getY(), getWidth(), getHeight());
        graphicsContext.strokeRect(getX(), getY(), getWidth(), getHeight());
    }
}
