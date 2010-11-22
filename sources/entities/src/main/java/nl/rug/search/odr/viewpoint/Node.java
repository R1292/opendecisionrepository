package nl.rug.search.odr.viewpoint;

import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.entities.BaseEntity;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Node extends BaseEntity<Node> {

    private static final long serialVersionUID = 1l;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Long id;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Version version;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private int x;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private int y;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private boolean visible;




    public Version getVersion() {
        return version;
    }




    public void setVersion(Version version) {
        this.version = version;
    }




    public int getX() {
        return x;
    }




    public void setX(int x) {
        this.x = x;
    }




    public int getY() {
        return y;
    }




    public void setY(int y) {
        this.y = y;
    }




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        this.id = id;
    }




    public boolean isVisible() {
        return visible;
    }




    public void setVisible(boolean visible) {
        this.visible = visible;
    }




    @Override
    public boolean isPersistable() {
        return version != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{x, y, visible};
    }



//    public s
}