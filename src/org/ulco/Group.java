package org.ulco;

import java.util.Vector;

public class Group extends GraphicsObject{
    protected int m_ID;
    private Vector<GraphicsObject> m_objectList;

    public Group() {
        m_objectList = new Vector<GraphicsObject>();
        m_ID = ID.getInstance().generate();
    }

    public Group(String json) {
        m_objectList = new Vector<GraphicsObject>();
        String str = json.replaceAll("\\s+","");
        int objectsIndex = str.indexOf("objects");
        int groupsIndex = str.indexOf("groups");
        int endIndex = str.lastIndexOf("}");

        parseObjects(str.substring(objectsIndex + 9, groupsIndex - 2));
        parseGroups(str.substring(groupsIndex + 8, endIndex - 1));
    }

    public void add(Object object) {
            addObject((GraphicsObject)object);
    }

    private void addObject(GraphicsObject object) {
        m_objectList.add(object);
    }

    public Group copy() {
        Group g = new Group();

        for (Object o : m_objectList) {
            GraphicsObject element = (GraphicsObject) (o);

            g.addObject(element.copy());
        }
        return g;
    }

    public int getID() {
        return m_ID;
    }

    @Override
    boolean isClosed(Point pt, double distance) {
        return false;
    }

    public void move(Point delta) {
        Group g = new Group();

        for (Object o : m_objectList) {
            GraphicsObject element = (GraphicsObject) (o);

            element.move(delta);
        }
    }

    private void parseGroups(String groupsStr) {
        while (!groupsStr.isEmpty()) {
            int separatorIndex = Utility.searchSeparator(groupsStr);
            String groupStr;

            if (separatorIndex == -1) {
                groupStr = groupsStr;
            } else {
                groupStr = groupsStr.substring(0, separatorIndex);
            }
            m_objectList.add((Group) JSON.parse(groupStr));
            if (separatorIndex == -1) {
                groupsStr = "";
            } else {
                groupsStr = groupsStr.substring(separatorIndex + 1);
            }
        }
    }

    protected void parseObjects(String objectsStr) {
        while (!objectsStr.isEmpty()) {
            int separatorIndex = Utility.searchSeparator(objectsStr);
            String objectStr;

            if (separatorIndex == -1) {
                objectStr = objectsStr;
            } else {
                objectStr = objectsStr.substring(0, separatorIndex);
            }
            m_objectList.add((GraphicsObject) JSON.parse(objectStr));
            if (separatorIndex == -1) {
                objectsStr = "";
            } else {
                objectsStr = objectsStr.substring(separatorIndex + 1);
            }
        }
    }

    public int size() {
        int size = 0;
        for(GraphicsObject gO: m_objectList){
            if(gO instanceof Group){
                Group group = (Group) gO;
                size += group.size();
            }else {
                size++;
            }
        }
        return size;
    }

    public String toJson() {
        String str = "{ type: group, objects : { ";
        String groupe = "";
        for (int i = 0; i < m_objectList.size(); ++i) {

            GraphicsObject element = m_objectList.elementAt(i);
            if(element instanceof Group){
                groupe = element.toJson();
            }else {
                str += element.toJson();
                if (i < m_objectList.size() - 1) {
                    str += ", ";
                }
            }

        }
        str += " }, groups : { "+ groupe;

        return str + " } }";
    }

    public String toString() {
        String str = "";

        String groupe = "";
        for (int i = 0; i < m_objectList.size(); ++i) {
            GraphicsObject element = m_objectList.elementAt(i);
            if(element instanceof Group){
               groupe += element.toString();
            }else {
                if (!str.equals("")) {
                    str += ", ";
                }else {
                    str += "group[[";
                }
                str += element.toString();
            }
        }
        str += "],[";
        str += groupe;
        return str + "]]";
    }


}
