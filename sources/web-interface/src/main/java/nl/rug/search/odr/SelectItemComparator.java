
package nl.rug.search.odr;

import java.util.Comparator;
import javax.faces.model.SelectItem;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class SelectItemComparator implements Comparator<SelectItem>{

    @Override
    public int compare(SelectItem o1, SelectItem o2) {
        return o1.getLabel().compareToIgnoreCase(o2.getLabel());
    }

}
