package nl.rug.search.odr.controller.decision;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RelationshipStepInput {

    private Decision decision;

    private String type;

    private String version;

    private Relationship relationship;

    private final Date decidedWhen;


    public RelationshipStepInput(Relationship relationship, Decision decision, String version, String type, Date decidedWhen) {
        this.decision = decision;
        this.type = type;
        this.version = version;
        this.relationship = relationship;
        this.decidedWhen = decidedWhen;

        
        if (version == null) {
            this.version = decision.getCurrentVersion().getId().toString();
        }

        Version v = decision.getVersion(Long.parseLong(this.version));

        if (v.getDecidedWhen().after(decidedWhen)) {
             JsfUtil.addJavascriptCall("odr.toggling.slideDown('#decisionAfterInformation');");
        }
    }




    public Relationship getRelationship() {
        if (relationship == null) {
            relationship = new Relationship();
        }
        
        return relationship;
    }



    public Decision getDecision() {
        return decision;
    }




    public void setDecision(Decision decision) {
        this.decision = decision;
    }




    public List<SelectItem> getVersions() {
        List<Version> versions = new ArrayList<Version>(decision.getVersions());

        Collections.sort(versions, Collections.reverseOrder(new Version.DecidedWhenComparator()));
        
        List<SelectItem> items = new ArrayList<SelectItem>(versions.size());

        SimpleDateFormat format = new SimpleDateFormat(
                JsfUtil.evaluateExpressionGet("#{common['format.date.time']}", String.class));

        String afterMessage = JsfUtil.evaluateExpressionGet("#{form['decision.wizard.decided.after']}", String.class);

        for (Version currentVersion : versions) {
            if (currentVersion.isRemoved()) {
                continue;
            }

            String label = currentVersion.getState().getStatusName().
                    concat(" - ").
                    concat(format.format(currentVersion.getDecidedWhen()));

            if (decidedWhen.before(currentVersion.getDecidedWhen())) {
                label = label.concat(" ").
                        concat(afterMessage);
            }

            items.add(new SelectItem(currentVersion.getId(), label));
        }

        return items;
    }




    public String getType() {
        return type;
    }




    public void setType(String type) {
        this.type = type;
    }




    public String getVersion() {
        return version;
    }




    public void setVersion(String version) {
        this.version = version;

        Version v = decision.getVersion(Long.parseLong(this.version));

        if (v.getDecidedWhen().after(decidedWhen)) {
             JsfUtil.addJavascriptCall("odr.toggling.slideDown('#decisionAfterInformation');");
        }
    }
}
