/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.bullet.collision.shapes;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple point, line, triangle or quad collisionShape based on one to four points-
 * @author normenhansen
 */
public class SimplexCollisionShape extends CollisionShape {

    private Vector3f vector1, vector2, vector3, vector4;

    public SimplexCollisionShape() {
    }

    public SimplexCollisionShape(Vector3f point1, Vector3f point2, Vector3f point3, Vector3f point4) {
        vector1 = point1;
        vector2 = point2;
        vector3 = point3;
        vector4 = point4;
        createShape();
    }

    public SimplexCollisionShape(Vector3f point1, Vector3f point2, Vector3f point3) {
        vector1 = point1;
        vector2 = point2;
        vector3 = point3;
        createShape();
    }

    public SimplexCollisionShape(Vector3f point1, Vector3f point2) {
        vector1 = point1;
        vector2 = point2;
        createShape();
    }

    public SimplexCollisionShape(Vector3f point1) {
        vector1 = point1;
        createShape();
    }

    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(vector1, "simplexPoint1", null);
        capsule.write(vector2, "simplexPoint2", null);
        capsule.write(vector3, "simplexPoint3", null);
        capsule.write(vector4, "simplexPoint4", null);
    }

    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        vector1 = (Vector3f) capsule.readSavable("simplexPoint1", null);
        vector2 = (Vector3f) capsule.readSavable("simplexPoint2", null);
        vector3 = (Vector3f) capsule.readSavable("simplexPoint3", null);
        vector4 = (Vector3f) capsule.readSavable("simplexPoint4", null);
        createShape();
    }

    protected void createShape() {
        if (vector4 != null) {
            objectId = createShape(vector1, vector2, vector3, vector4);
//            objectId = new BU_Simplex1to4(Converter.convert(vector1), Converter.convert(vector2), Converter.convert(vector3), Converter.convert(vector4));
        } else if (vector3 != null) {
            objectId = createShape(vector1, vector2, vector3);
//            objectId = new BU_Simplex1to4(Converter.convert(vector1), Converter.convert(vector2), Converter.convert(vector3));
        } else if (vector2 != null) {
            objectId = createShape(vector1, vector2);
//            objectId = new BU_Simplex1to4(Converter.convert(vector1), Converter.convert(vector2));
        } else {
            objectId = createShape(vector1);
//            objectId = new BU_Simplex1to4(Converter.convert(vector1));
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Created Shape {0}", Long.toHexString(objectId));
//        objectId.setLocalScaling(Converter.convert(getScale()));
//        objectId.setMargin(margin);
        setScale(scale);
        setMargin(margin);
    }
    
    private native long createShape(Vector3f vector1);
    
    private native long createShape(Vector3f vector1, Vector3f vector2);
    
    private native long createShape(Vector3f vector1, Vector3f vector2, Vector3f vector3);

    private native long createShape(Vector3f vector1, Vector3f vector2, Vector3f vector3, Vector3f vector4);
}
