package org.ulco;

import java.util.Iterator;
import java.util.Vector;

public class Document {
    public Vector<Layer> m_layers;
    public Document() {
        m_layers = new Vector<Layer>();
    }

    public Document(String json) {
        m_layers = new Vector<Layer>();
        String str = json.replaceAll("\\s+", "");
        int layersIndex = str.indexOf("layers");
        int endIndex = str.lastIndexOf("}");

        parseLayers(str.substring(layersIndex + 8, endIndex));
    }

//    public Document(Point origin, int line, int column, double length) {
//        m_layers = new Vector<Layer>();
//
//        Layer layer = createLayer();
//
//        for (int indexX = 0; indexX < column; ++indexX) {
//            for (int indexY = 0; indexY < line; ++indexY) {
//                layer.add(new Square(new Point(origin.getX() + indexX * length, origin.getY() + indexY * length), length));
//            }
//        }
//    }
//
//    public Document(Point center, int number, double radius, double delta) {
//        m_layers = new Vector<Layer>();
//
//        Layer layer = createLayer();
//
//        for (int index = 0; index < number; ++index) {
//            layer.add(new Circle(center, radius + index * delta));
//        }
//    }

    public Layer createLayer() {
        Layer layer = new Layer();

        m_layers.add(layer);
        return layer;
    }

    public int getLayerNumber() {
        return m_layers.size();
    }

    public int getObjectNumber() {
        int size = 0;

        for (int i = 0; i < m_layers.size(); ++i) {
            size += m_layers.elementAt(i).getObjectNumber();
        }
        return size;
    }

    private void parseLayers(String layersStr) {
        while (!layersStr.isEmpty()) {
            int separatorIndex = Utility.searchSeparator(layersStr);
            String layerStr;

            if (separatorIndex == -1) {
                layerStr = layersStr;
            } else {
                layerStr = layersStr.substring(0, separatorIndex);
            }
            m_layers.add((Layer) JSON.parse(layerStr));
            if (separatorIndex == -1) {
                layersStr = "";
            } else {
                layersStr = layersStr.substring(separatorIndex + 1);
            }
        }
    }

}
