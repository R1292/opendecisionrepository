package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "RelationshipType.getAll",
                query= "SELECT r FROM RelationshipType r")
})
@Entity
public class RelationshipType extends BaseEntity<RelationshipType> {

    private static final long serialVersionUID = 1l;

    @Id
    private Long id;

    private String name;

    private boolean common;

    private String description;


    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null");
        }

        this.id = id;
    }




    public boolean isCommon() {
        return common;
    }




    public void setCommon(boolean common) {
        this.common = common;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        StringValidator.isValid(name);
        this.name = name;
    }




    public String getDescription() {
        return description;
    }




    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public boolean isPersistable() {
        return name != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[] {name, common, description};
    }
}
