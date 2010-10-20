package nl.rug.search.odr.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class StakeholderRole extends BaseEntity<StakeholderRole> {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private static final long serialVersionUID = 1L;
   
    @Column
    private String name;
    @Column
    private boolean common;

    @Override
    public Long getId(){
        return id;
    }

    @Override
    public void setId(Long id){
        if(id == null){
            throw new BusinessException("Please provide an id");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        StringValidator.isValid(name);
        
        this.name = name;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    @Override
    protected Object[] getCompareData() {
       return new Object[]{name, common};
    }

    @Override
    public boolean isPersistable() {
        return name != null;
    }
}
