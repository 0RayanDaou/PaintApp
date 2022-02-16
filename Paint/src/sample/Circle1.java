package sample;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Circle1 extends Shapes {
    private GraphicsContext graphicsContext;
    private Color cpLine;
    private Color cpFill;

    private double centerX, centerY, endX, endY;

    private Circle circle = new Circle();

    public Circle1(){}

    public void setGraphicsContext(GraphicsContext graphicsContext){
        this.graphicsContext = graphicsContext;
    }

    public void setColor(Color color){
        cpLine = color;
    }

    public void setFill(Color color){
        cpFill = color;
    }

    public void setCenterPoint(double centerX, double centerY){
        this.centerX = centerX;
        this.centerY = centerY;

        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
    }

    public void setEndPoint(double endX, double endY){
        this.endX = endX;
        this.endY = endY;
    }

    public void setRadius(){
        circle.setRadius((Math.abs(endX - centerX) + Math.abs(endY - centerY)) / 2);
    }

    public void setRadius(double radius){
        circle.setRadius(radius);
    }

    public boolean containsPoint(Point2D point){
        return circle.contains(point);
    }

    public void check(){
        if(centerX > endX) {
            circle.setCenterX(endX);
        }
        if(centerY > endY) {
            circle.setCenterY(endY);
        }
    }

    public double getRadius(){
        return circle.getRadius();
    }

    public double getCenterX(){
        return circle.getCenterX();
    }

    public double getCenterY(){
        return circle.getCenterY();
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

        graphicsContext.fillOval(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), circle.getRadius());
        graphicsContext.strokeOval(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), circle.getRadius());
    }
}
