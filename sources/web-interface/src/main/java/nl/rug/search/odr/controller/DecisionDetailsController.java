package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.entities.ComponentValue;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.Requirement;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.ErrorUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@RequestScoped
@ManagedBean
public class DecisionDetailsController {

    // <editor-fold defaultstate="collapsed" desc="POJOs">
    private boolean validRequest;

    private Project project;

    private Decision decision;

    private Version version;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private ProjectLocal pl;

    @EJB
    private DecisionLocal dl;
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="construction">

    @PostConstruct
    public void setUp() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, pl);

        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {
            setUpDecisionSpecific(result);
        } else {
            result.executeErrorAction();
        }
    }




    private void setUpDecisionSpecific(RequestAnalyserDto analyserDto) {
        HttpServletRequest request = analyserDto.getRequest();

        String decisionIdParam = request.getParameter(RequestParameter.DECISION_ID);
        String versionIdParam = request.getParameter(RequestParameter.VERSION_ID);

        long decisionId = -1;
        long versionId = -1;

        try {
            decisionId = Long.parseLong(decisionIdParam);

            if (versionIdParam != null) {
                versionId = Long.parseLong(versionIdParam);
            }
        } catch (NumberFormatException ex) {
            ErrorUtil.showInvalidIdError();
            return;
        }

        decision = analyserDto.getProject().getDecision(decisionId);

        if (decision == null) {
            ErrorUtil.showInvalidIdError();
            return;
        }

        if (versionId == -1) {
            version = decision.getCurrentVersion();
        } else {
            version = decision.getVersion(versionId);

            if (version == null) {
                ErrorUtil.showInvalidIdError();
                return;
            }

        }

        this.project = analyserDto.getProject();

        validRequest = true;
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="getter">

    public boolean isValid() {
        return validRequest;
    }




    public Decision getDecision() {
        return decision;
    }




    public Version getVersion() {
        return version;
    }




    public List<ComponentValue> getValues() {
        List<ComponentValue> values = new ArrayList<ComponentValue>(decision.getValues());

        Collections.sort(values, new ComponentValue.OrderComparator());

        return values;
    }




    public List<ProjectMember> getInitiators() {
        List<ProjectMember> members = new ArrayList<ProjectMember>(version.getInitiators());

        Collections.sort(members, new ProjectMember.NameComparator());

        return members;
    }




    public List<Requirement> getRequirements() {
        return new ArrayList<Requirement>(version.getRequirements());
    }




    public List<Relationship> getRelationships() {
        return new ArrayList<Relationship>(version.getRelationships());
    }




    public String getDecisionName(long versionId) {
        return dl.getByVersion(versionId).getName();
    }

    public List<HistoryDto> getFuture() {
        Collection<Version> versions = decision.getVersions();
        List<HistoryDto> future = new ArrayList<HistoryDto>(versions.size() - 1);

        for(Version eachVersion : versions) {
            if (eachVersion.getId() != version.getId() && eachVersion.getDecidedWhen().compareTo(version.getDecidedWhen()) > 0) {
                HistoryDto dto = new HistoryDto();
                dto.setDecidedWhen(eachVersion.getDecidedWhen());
                dto.setDocumentedWhen(eachVersion.getDocumentedWhen());
                dto.setState(eachVersion.getState());
                dto.setVersionId(eachVersion.getId());
                future.add(dto);
            }
        }

        Collections.sort(future);

        return future;
    }

    public List<HistoryDto> getHistory() {
        Collection<Version> versions = decision.getVersions();
        List<HistoryDto> history = new ArrayList<HistoryDto>(versions.size() - 1);

        for(Version eachVersion : versions) {
            if (eachVersion.getId() != version.getId() && eachVersion.getDecidedWhen().compareTo(version.getDecidedWhen()) < 0) {
                HistoryDto dto = new HistoryDto();
                dto.setDecidedWhen(eachVersion.getDecidedWhen());
                dto.setDocumentedWhen(eachVersion.getDocumentedWhen());
                dto.setState(eachVersion.getState());
                dto.setVersionId(eachVersion.getId());
                history.add(dto);
            }
        }

        Collections.sort(history);

        return history;
    }




    public String getDecisionLink(long versionId) {
        return new QueryStringBuilder().setUrl("decisionDetails.html").
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.DECISION_ID, decision.getId()).
                append(RequestParameter.VERSION_ID, versionId).
                toString();
    }




    public String getUpdateLink() {
        return new QueryStringBuilder().setUrl("manageDecision.html").
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.DECISION_ID, decision.getId()).
                append(RequestParameter.VERSION_ID, version.getId()).
                toString();
    }
    // </editor-fold>



    public static class HistoryDto implements Comparable<HistoryDto>{
        private State state;
        private Date decidedWhen;
        private Date documentedWhen;
        private long versionId;



        public Date getDecidedWhen() {
            return decidedWhen;
        }




        public Date getDocumentedWhen() {
            return documentedWhen;
        }




        public State getState() {
            return state;
        }




        public void setDecidedWhen(Date decidedWhen) {
            this.decidedWhen = decidedWhen;
        }




        public void setDocumentedWhen(Date documentedWhen) {
            this.documentedWhen = documentedWhen;
        }




        public void setState(State state) {
            this.state = state;
        }




        @Override
        public int compareTo(HistoryDto o) {
            return o.decidedWhen.compareTo(decidedWhen);
        }




        public void setVersionId(long versionId) {
            this.versionId = versionId;
        }




        public long getVersionId() {
            return versionId;
        }
    }
}