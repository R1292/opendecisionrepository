package nl.rug.search.odr.controller;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.DatabaseCleaner;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.util.SessionUtil;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.DecisionTemplate;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.TemplateComponent;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ConcernLocal;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.project.StakeholderRoleLocal;
import nl.rug.search.odr.project.StateLocal;
import nl.rug.search.odr.user.UserLocal;
import nl.rug.search.odr.viewpoint.Handle;
import nl.rug.search.odr.viewpoint.relationship.InitRelationshipView;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewAssociation;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewNode;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@ViewScoped
public class FillDbController {

    @EJB
    private StakeholderRoleLocal srl;
    @EJB
    private UserLocal ul;
    @EJB
    private ProjectLocal pl;
    @EJB
    private DecisionTemplateLocal dtl;
    @EJB
    private StateLocal sl;
    @EJB
    private ConcernLocal cl;
    @EJB
    private RelationshipTypeLocal rtl;
    private boolean clearDone, rolesDone, statesDone,
            templatesDone, personsDone, projectsDone, iterationsDone,
            decisionsDone, concernsDone, relationshipTypesDone, addAllDone,
            relationshipsDone, relationshipViewDone;
    private static final String DEFAULT_PASSWORD = "12345";
    private static final String OPEN_DECISION_REPOSITORY = "OpenDecisionRepository";
    private static final String QUCK_ADD_FORM = "Quick add form";
    // Roles
    private static final String ROLE_ARCHITECT = "Architect";
    private static final String ROLE_CUSTOMER = "Customer";
    private static final String ROLE_MANAGER = "Manager";
    // Decisiontypes
    private static final String DECISIONTYPE_IDEA = "idea";
    private static final String DECISIONTYPE_APPROVED = "approved";
    private static final String DECISIONTYPE_DECIDED = "decided";
    private static final String DECISIONTYPE_DISCARDED = "discarded";
    private static final String DECISIONTYPE_REJECTED = "rejected";
    private static final String DECISIONTYPE_FORMULATED = "formulate";
    //Relationshiptypes
    private static final String RELATIONSHIPTYPE_CAUSED_BY = "caused by";
    private static final String RELATIONSHIPTYPE_REPLACES = "replaces";
    private static final String RELATIONSHIPTYPE_IS_ALTERNATIVE_FOR = "is alternative for";
    private static final String RELATIONSHIPTYPE_DEPENDS_ON = "depends on";

    public void addPersons() {
        Person p1 = new Person();
        p1.setName("Ben Ripkens");
        p1.setPlainPassword(DEFAULT_PASSWORD);
        p1.setEmail("ben@ripkens.de");
        ul.register(p1);


        Person p2 = new Person();
        p2.setName("Stefan Arians");
        p2.setPlainPassword(DEFAULT_PASSWORD);
        p2.setEmail("s@s.de");
        ul.register(p2);


        Person p3 = new Person();
        p3.setName("Uwe van Heesch");
        p3.setPlainPassword(DEFAULT_PASSWORD);
        p3.setEmail("uwe@uwe.de");
        ul.register(p3);

        Person p4 = new Person();
        p4.setName("Paris Avgeriou");
        p4.setPlainPassword(DEFAULT_PASSWORD);
        p4.setEmail("paris@paris.de");
        ul.register(p4);

        Person p5 = new Person();
        p5.setName("Christian Manteuffel");
        p5.setPlainPassword(DEFAULT_PASSWORD);
        p5.setEmail("christian@christian.de");
        ul.register(p5);

        Person p6 = new Person();
        p6.setName("Martin Verspai");
        p6.setPlainPassword(DEFAULT_PASSWORD);
        p6.setEmail("martin@martin.de");
        ul.register(p6);

        Person p7 = new Person();
        p7.setName("TestUser");
        p7.setPlainPassword(DEFAULT_PASSWORD);
        p7.setEmail("test@decisionrepository.com");
        ul.register(p7);

        clearDone = false;
        personsDone = true;
    }

    public void addProject() {
        Project pro = new Project();
        pro.setName(OPEN_DECISION_REPOSITORY);
        pro.setDescription("The Open Decision Repository (ODR) project was initiated as part of the doctoral research by Uwe van Heesch, supervised by Paris Avgeriou. It is a joint-venture project of the research group Software Engineering and Architecture (SEARCH), which is part of the Department of Mathematics and Computing Science at the University of Groningen/NL and the Software Engineering study programme at the Fontys University of Applied Sciences in Venlo/NL.");

        ProjectMember member = new ProjectMember();
        member.setPerson(ul.getByName("TestUser"));
        member.setRole(getStakeholderRole(ROLE_ARCHITECT));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Ben Ripkens"));
        member.setRole(getStakeholderRole(ROLE_ARCHITECT));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Stefan Arians"));
        member.setRole(getStakeholderRole(ROLE_ARCHITECT));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Uwe van Heesch"));
        member.setRole(getStakeholderRole(ROLE_CUSTOMER));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Paris Avgeriou"));
        member.setRole(getStakeholderRole(ROLE_MANAGER));
        pro.addMember(member);

        pl.persist(pro);

        pro = new Project();
        pro.setName("OpenPatternRepository");
        pro.setDescription("The Open Pattern Repository is a publicly available and freely usable online repository for software engineering patterns and software technologies like frameworks and libraries.");

        member = new ProjectMember();
        member.setPerson(ul.getByName("Christian Manteuffel"));
        member.setRole(getStakeholderRole(ROLE_ARCHITECT));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Martin Verspai"));
        member.setRole(getStakeholderRole(ROLE_ARCHITECT));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Uwe van Heesch"));
        member.setRole(getStakeholderRole(ROLE_CUSTOMER));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Paris Avgeriou"));
        member.setRole(getStakeholderRole(ROLE_MANAGER));
        pro.addMember(member);

        pl.persist(pro);

        clearDone = false;
        projectsDone = true;
    }

    public void login() {
        Person p = null;

        try {
            p = ul.tryLogin("ben@ripkens.de", DEFAULT_PASSWORD);
        } catch (BusinessException ex) {
            throw new RuntimeException(ex);
        }


        AuthenticationUtil.authenticate(p);

        JsfUtil.redirect("/projects.html");
    }

    public void clearDatabase() {
        DatabaseCleaner.bruteForceCleanup();

        SessionUtil.resetSession();

        JsfUtil.refreshPage();

        clearDone = true;
        personsDone = false;
        projectsDone = false;
        decisionsDone = false;
        iterationsDone = false;
        concernsDone = false;
        rolesDone = false;
        statesDone = false;
        templatesDone = false;
        relationshipTypesDone = false;
        relationshipViewDone = false;
    }

    public void addDecisions() {
        Project p = pl.getByName(OPEN_DECISION_REPOSITORY);

        Decision d = new Decision();
        d.setName("Java Programming language");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        Version v = new Version();
        v.setDecidedWhen(getDate(2010, 9, 2));
        v.setDocumentedWhen(getDate(2010, 9, 2));
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        Date previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Tcl");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(getDate(2010, 9, 5));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_IDEA));
        d.addVersion(v);
        v = new Version();
        v.setDecidedWhen(getDate(2010, 9, 6));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_DECIDED));
        d.addVersion(v);
        v = new Version();
        v.setDecidedWhen(getDate(2010, 9, 7));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, 1));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Out of iteration");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(getDate(2010, 9, 26));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_IDEA));
        d.addVersion(v);
        v = new Version();
        v.setDecidedWhen(new Date(getDate(2010, 9, 26).getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_DISCARDED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Out of iteration 2");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(getDate(2010, 9, 26).getTime() + 2));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_IDEA));
        d.addVersion(v);
        p.addDecision(d);


        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("xowiki");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(getDate(2010, 9, 8));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Java Enterprise Edition");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(getDate(2011, 10, 2));
        v.setDocumentedWhen(getDate(2011, 10, 2));
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Glassfish");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);


        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("OPR technology stack");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("JavaServer Faces");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Icefaces");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Java Persistence API");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("MySQL");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(getDate(2010, 10, 28));
        v.setDocumentedWhen(getDate(2010, 10, 20));
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Eclipselink");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Enterprise Java Beans");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Icefaces 1.8");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Icefaces 2.0-beta1");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Icefaces 2.0-beta2");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("JavaServer Faces 1.2");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("JavaServer Faces 2");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Glassfish 2");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 2));
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Glassfish 3");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(getDate(2010, 11, 20));
        v.setDocumentedWhen(getDate(2010, 11, 20));
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Enterprise Java Beans 3");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Enterprise Java Beans 3.1");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Java Enterprise Edition 5");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);


        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Java Enterprise Edition 6");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(getDate(2010, 12, 2));
        v.setDocumentedWhen(getDate(2010, 12, 2));
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Client side image generation");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Apache FOP");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 2));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Apache Batik");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 2));
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Scalable Vector Graphics");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_APPROVED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Server side image generation");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Image generation in Java");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(getDate(2011, 1, 11));
        v.setDocumentedWhen(getDate(2011, 1, 11));
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("HTML 5 Canvas Element");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers().subList(0, p.getMembers().size() - 1));
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Graphviz");
        d.setTemplate(getTemplate(QUCK_ADD_FORM));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 86400001));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState(DECISIONTYPE_REJECTED));
        d.addVersion(v);
        p.addDecision(d);

        pl.merge(p);

        clearDone = false;
        decisionsDone = true;
    }

    public void addIterations() {
        Project p = pl.getByName(OPEN_DECISION_REPOSITORY);

        Iteration it1 = new Iteration();
        it1.setName("Analysis and design");
        it1.setProjectMember(p.getMembers().iterator().next());
        it1.setDocumentedWhen(new Date());
        it1.setStartDate(getDate(2010, 9, 1));
        it1.setEndDate(getDate(2010, 9, 25));

        Iteration it2 = new Iteration();
        it2.setName("Sprint 1");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 1));
        it2.setStartDate(getDate(2010, 9, 27));
        it2.setEndDate(getDate(2010, 10, 9));

        p.addIteration(it1);
        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 2");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 2));
        it2.setStartDate(getDate(2010, 10, 18));
        it2.setEndDate(getDate(2010, 10, 30));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 3");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 3));
        it2.setStartDate(getDate(2010, 11, 1));
        it2.setEndDate(getDate(2010, 11, 13));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 4");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 4));
        it2.setStartDate(getDate(2010, 11, 15));
        it2.setEndDate(getDate(2010, 11, 27));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 5");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 5));
        it2.setStartDate(getDate(2010, 11, 29));
        it2.setEndDate(getDate(2010, 12, 11));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 6");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 6));
        it2.setStartDate(getDate(2010, 12, 13));
        it2.setEndDate(getDate(2011, 1, 9));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 7 / release sprint");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 7));
        it2.setStartDate(getDate(2011, 1, 10));
        it2.setEndDate(getDate(2011, 1, 29));

        p.addIteration(it2);

        pl.merge(p);

        clearDone = false;
        iterationsDone = true;
    }

    private Date getDate(int year, int month, int day) {
        return new GregorianCalendar(year, month - 1, day).getTime();
    }

    public void addStakeholderRoles() {
        StakeholderRole role1 = new StakeholderRole();
        role1.setName(ROLE_ARCHITECT);
        role1.setCommon(true);
        srl.persist(role1);

        StakeholderRole role2 = new StakeholderRole();
        role2.setName(ROLE_MANAGER);
        role2.setCommon(true);
        srl.persist(role2);

        StakeholderRole role3 = new StakeholderRole();
        role3.setName(ROLE_CUSTOMER);
        role3.setCommon(true);
        srl.persist(role3);

        clearDone = false;
        rolesDone = true;
    }

    private StakeholderRole getStakeholderRole(String roleName) {
        Collection<StakeholderRole> roles = srl.getAll();

        for (StakeholderRole role : roles) {
            if (role.getName().equalsIgnoreCase(roleName)) {
                return role;
            }
        }

        throw new RuntimeException("Can't find the role");
    }

    public void addStates() {
        State state = new State();
        state.setActionName(DECISIONTYPE_FORMULATED);
        state.setStatusName(DECISIONTYPE_IDEA);
        state.setInitialState(true);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("propose");
        state.setStatusName("tentative");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("discard");
        state.setStatusName("discarded");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("validate");
        state.setStatusName("decided");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("confirm");
        state.setStatusName(DECISIONTYPE_APPROVED);
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("challenge");
        state.setStatusName("challenged");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("validate");
        state.setStatusName(DECISIONTYPE_REJECTED);
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        clearDone = false;
        statesDone = true;
    }

    private State getState(String statusName) {
        Collection<State> states = sl.getAll();

        for (State state : states) {
            if (state.getStatusName().equalsIgnoreCase(statusName)) {
                return state;
            }
        }

        throw new RuntimeException("Can't find state");
    }

    public void addDecisionTemplates() {
        DecisionTemplate template = new DecisionTemplate();
        template.setName(QUCK_ADD_FORM);
        dtl.persist(template);

        template = new DecisionTemplate();
        template.setName("Demystifying architecture");

        TemplateComponent templateComponent = new TemplateComponent();
        templateComponent.setLabel("Issue");
        templateComponent.setLocalizationReference("Describe the architectural design issue you?e addressing, leaving no questions about why you?e addressing this issue now. Following a minimalist approach, address and document only the issues that need addressing at various points in the life cycle.");
        templateComponent.setOrder(0);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Decision");
        templateComponent.setLocalizationReference("Clearly state the architecture? direction?hat is, the position you?e selected.");
        templateComponent.setOrder(1);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Group");
        templateComponent.setLocalizationReference("You can use a simple grouping?uch as integration, presentation, data, and so on?o help organize the set of decisions. You could also use a more sophisticated architecture ontology, such as John Kyaruzi and Jan van Katwijk?, which includes more abstract categories such as event, calendar, and location.8 For example, using this ontology, you? group decisions that deal with occurrences where the system requires information under event.");
        templateComponent.setOrder(2);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Assumptions");
        templateComponent.setLocalizationReference("Clearly describe the underlying assumptions in the environment in which you?e making the decision?ost, schedule, technology, and so on. Note that environmental constraints (such as accepted technology standards, enterprise architecture, commonly employed patterns, and so on) might limit the alternatives you consider.");
        templateComponent.setOrder(3);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Constraints");
        templateComponent.setLocalizationReference("Capture any additional constraints to the environment that the chosen alternative (the decision) might pose.");
        templateComponent.setOrder(4);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Positions");
        templateComponent.setLocalizationReference("List the positions (viable options or alternatives) you considered. These often require long explanations, sometimes even models and diagrams. This isn? an exhaustive list. However, you don? want to hear the question ?id you think about ... ??during a final review; this leads to loss of credibility and questioning of other architectural decisions. This section also helps ensure that you heard others?opinions; explicitly stating other opinions helps enroll their advocates in your decision.");
        templateComponent.setOrder(5);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Argument");
        templateComponent.setLocalizationReference("Outline why you selected a position, including items such as implementation cost, total ownership cost, time to market, and required development resources?availability. This is probably as important as the decision itself.");
        templateComponent.setOrder(6);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Implications");
        templateComponent.setLocalizationReference("A decision comes with many implications, as the REMAP metamodel denotes. For example, a decision might introduce a need to make other decisions, create new requirements, or modify existing requirements; pose additional constraints to the environment; require renegotiating scope or schedule with customers; or require additional staff training. Clearly understanding and stating your decision? implications can be very effective in gaining buy-in and creating a roadmap for architecture execution.");
        templateComponent.setOrder(7);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Notes");
        templateComponent.setLocalizationReference("Because the decision-making process can take weeks, we?e found it useful to capture notes and issues that the team discusses during the socialization process.");
        templateComponent.setOrder(8);
        template.addComponent(templateComponent);

        dtl.persist(template);

        template = new DecisionTemplate();
        template.setName("Viewpoints for architectural decisions");

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Problem/Issue");
        templateComponent.setLocalizationReference("Describe the architectural design issue you?e addressing, leaving no questions about why you?e addressing this issue now. Following a minimalist approach, address and document only the issues that need addressing at various points in the life cycle.");
        templateComponent.setOrder(0);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Decision");
        templateComponent.setLocalizationReference("Clearly state the architecture? direction?hat is, the position you?e selected.");
        templateComponent.setOrder(1);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Arguments");
        templateComponent.setOrder(3);
        templateComponent.setLocalizationReference("Outline why you selected a position, including items such as implementation cost, total ownership cost, time to market, and required development resources?availability. This is probably as important as the decision itself.");
        template.addComponent(templateComponent);

        dtl.persist(template);

        clearDone = false;
        templatesDone = true;
    }

    private DecisionTemplate getTemplate(String name) {
        for (DecisionTemplate template : dtl.getAll()) {
            if (template.getName().equalsIgnoreCase(name)) {
                return template;
            }
        }

        throw new RuntimeException("Can't find template.");
    }

    public void addConcerns() {
        Project p = pl.getByName(OPEN_DECISION_REPOSITORY);

        Concern r = new Concern();
        r.setName("Web application");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-1");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());


        r = new Concern();
        r.setName("Data integrity");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-2");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        r = new Concern();
        r.setName("Well tested (> 70% test coverage)");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-10");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        r = new Concern();
        r.setName("Portable to any major OS");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-11");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        r = new Concern();
        r.setName("Password encryption");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-12");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        r = new Concern();
        r.setName("OPR corporate layout");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-4");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        pl.merge(p);

        clearDone = false;
        concernsDone = true;
    }

    public void addRelationshipTypes() {
        RelationshipType type = new RelationshipType();
        type.setCommon(true);
        type.setName(RELATIONSHIPTYPE_DEPENDS_ON);
        rtl.persist(type);

        type = new RelationshipType();
        type.setCommon(true);
        type.setName(RELATIONSHIPTYPE_REPLACES);
        rtl.persist(type);

        type = new RelationshipType();
        type.setCommon(true);
        type.setName(RELATIONSHIPTYPE_IS_ALTERNATIVE_FOR);
        rtl.persist(type);

        type = new RelationshipType();
        type.setCommon(true);
        type.setName(RELATIONSHIPTYPE_CAUSED_BY);
        rtl.persist(type);

        type = new RelationshipType();
        type.setCommon(true);
        type.setName("is excluded by");
        rtl.persist(type);

        clearDone = false;
        relationshipTypesDone = true;
    }

    public RelationshipType getRelationshipType(String name) {
        for (RelationshipType r : rtl.getAll()) {
            if (r.getName().equalsIgnoreCase(name)) {
                return r;
            }
        }

        throw new RuntimeException();
    }

    public void addRelationships() {
        Project p = pl.getByName(OPEN_DECISION_REPOSITORY);
        Decision oprTech = getDecision(p.getDecisions(), "OPR technology stack");
        Decision java = getDecision(p.getDecisions(), "Java Programming language");
        Decision tcl = getDecision(p.getDecisions(), "Tcl");
        Decision xowiki = getDecision(p.getDecisions(), "xowiki");
        Decision javaee = getDecision(p.getDecisions(), "Java Enterprise Edition");
        Decision glassfish = getDecision(p.getDecisions(), "Glassfish");
        Decision jsf = getDecision(p.getDecisions(), "JavaServer Faces");
        Decision icefaces = getDecision(p.getDecisions(), "Icefaces");
        Decision jpa = getDecision(p.getDecisions(), "Java Persistence API");
        Decision mysql = getDecision(p.getDecisions(), "MySQL");
        Decision eclipselink = getDecision(p.getDecisions(), "Eclipselink");
        Decision ejb = getDecision(p.getDecisions(), "Enterprise Java Beans");
        Decision ice18 = getDecision(p.getDecisions(), "Icefaces 1.8");
        Decision ice201 = getDecision(p.getDecisions(), "Icefaces 2.0-beta1");
        Decision ice202 = getDecision(p.getDecisions(), "Icefaces 2.0-beta2");
        Decision jsf12 = getDecision(p.getDecisions(), "JavaServer Faces 1.2");
        Decision jsf2 = getDecision(p.getDecisions(), "JavaServer Faces 2");
        Decision glassfish2 = getDecision(p.getDecisions(), "Glassfish 2");
        Decision glassfish3 = getDecision(p.getDecisions(), "Glassfish 3");
        Decision ejb3 = getDecision(p.getDecisions(), "Enterprise Java Beans 3");
        Decision ejb31 = getDecision(p.getDecisions(), "Enterprise Java Beans 3.1");
        Decision jee5 = getDecision(p.getDecisions(), "Java Enterprise Edition 5");
        Decision jee6 = getDecision(p.getDecisions(), "Java Enterprise Edition 6");
        Decision client = getDecision(p.getDecisions(), "Client side image generation");
        Decision fop = getDecision(p.getDecisions(), "Apache FOP");
        Decision batik = getDecision(p.getDecisions(), "Apache Batik");
        Decision svg = getDecision(p.getDecisions(), "Scalable Vector Graphics");
        Decision server = getDecision(p.getDecisions(), "Server side image generation");
        Decision javaImg = getDecision(p.getDecisions(), "Image generation in Java");
        Decision html5 = getDecision(p.getDecisions(), "HTML 5 Canvas Element");
        Decision graphviz = getDecision(p.getDecisions(), "Graphviz");

        RelationshipType causedBy = getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY);
        RelationshipType alternative = getRelationshipType(RELATIONSHIPTYPE_IS_ALTERNATIVE_FOR);
        RelationshipType replaces = getRelationshipType(RELATIONSHIPTYPE_REPLACES);
        RelationshipType depends = getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON);

        createRelationship(java, tcl, alternative);
        createRelationship(java, oprTech, causedBy);
        createRelationship(xowiki, tcl, depends);
        createRelationship(javaee, oprTech, causedBy);
        createRelationship(javaee, java, depends);
        createRelationship(jee5, javaee, causedBy);
        createRelationship(jee5, glassfish2, depends);
        createRelationship(jee6, javaee, causedBy);
        createRelationship(jee6, jee5, replaces);
        createRelationship(jee6, glassfish3, depends);
        createRelationship(glassfish, oprTech, causedBy);
        createRelationship(glassfish2, glassfish, causedBy);
        createRelationship(glassfish2, ice18, causedBy);
        createRelationship(glassfish3, glassfish, causedBy);
        createRelationship(glassfish3, glassfish2, replaces);
        createRelationship(glassfish3, ice202, causedBy);
        createRelationship(jsf, oprTech, causedBy);
        createRelationship(jsf12, jsf, causedBy);
        createRelationship(jsf12, ice18, causedBy);
        createRelationship(jsf2, jsf, causedBy);
        createRelationship(jsf2, jsf12, replaces);
        createRelationship(jsf2, ice202, causedBy);
        createRelationship(icefaces, oprTech, causedBy);
        createRelationship(ice18, icefaces, causedBy);
        createRelationship(ice201, icefaces, causedBy);
        createRelationship(ice201, ice18, replaces);
        createRelationship(ice202, icefaces, causedBy);
        createRelationship(ice202, ice201, replaces);
        createRelationship(ejb, oprTech, causedBy);
        createRelationship(ejb3, ejb, causedBy);
        createRelationship(ejb3, glassfish2, depends);
        createRelationship(ejb31, ejb, causedBy);
        createRelationship(ejb31, ejb3, replaces);
        createRelationship(ejb31, glassfish3, depends);
        createRelationship(jpa, oprTech, causedBy);
        createRelationship(jpa, java, depends);
        createRelationship(eclipselink, oprTech, causedBy);
        createRelationship(eclipselink, java, depends);
        createRelationship(mysql, oprTech, causedBy);
        createRelationship(javaImg, server, causedBy);
        createRelationship(graphviz, server, causedBy);
        createRelationship(client, server, replaces);
        createRelationship(html5, client, causedBy);
        createRelationship(svg, client, causedBy);
        createRelationship(svg, javaImg, replaces);
        createRelationship(svg, graphviz, replaces);
        createRelationship(svg, html5, replaces);
        createRelationship(fop, java, depends);
        createRelationship(fop, svg, causedBy);
        createRelationship(batik, java, depends);
        createRelationship(batik, svg, causedBy);
        createRelationship(javaImg, java, depends);


        pl.merge(p);

        relationshipsDone = true;
        clearDone = false;
    }

    private void createRelationship(Decision source, Decision target, RelationshipType type) {
        Relationship r = new Relationship();
        r.setSource(source.getCurrentVersion());
        r.setTarget(target.getCurrentVersion());
        r.setType(type);
    }

    private Relationship getRelationship(Version source, Version target, RelationshipType type) {
        for (Relationship r : source.getOutgoingRelationships()) {
            if (r.getTarget().equals(target) && r.getType().equals(type)) {
                return r;
            }
        }
        return null;
    }

    private Decision getDecision(Collection<Decision> allDecisions, String name) {
        for (Decision d : allDecisions) {
            if (d.getName().equalsIgnoreCase(name)) {
                return d;
            }
        }

        throw new RuntimeException();
    }

    public void addRelationshipView() {
        Project p = pl.getByName(OPEN_DECISION_REPOSITORY);
        Decision oprTech = getDecision(p.getDecisions(), "OPR technology stack");
        Decision java = getDecision(p.getDecisions(), "Java Programming language");
        Decision tcl = getDecision(p.getDecisions(), "Tcl");
        Decision xowiki = getDecision(p.getDecisions(), "xowiki");
        Decision javaee = getDecision(p.getDecisions(), "Java Enterprise Edition");
        Decision glassfish = getDecision(p.getDecisions(), "Glassfish");
        Decision jsf = getDecision(p.getDecisions(), "JavaServer Faces");
        Decision icefaces = getDecision(p.getDecisions(), "Icefaces");
        Decision jpa = getDecision(p.getDecisions(), "Java Persistence API");
        Decision mysql = getDecision(p.getDecisions(), "MySQL");
        Decision eclipselink = getDecision(p.getDecisions(), "Eclipselink");
        Decision ejb = getDecision(p.getDecisions(), "Enterprise Java Beans");
        Decision ice18 = getDecision(p.getDecisions(), "Icefaces 1.8");
        Decision ice201 = getDecision(p.getDecisions(), "Icefaces 2.0-beta1");
        Decision ice202 = getDecision(p.getDecisions(), "Icefaces 2.0-beta2");
        Decision jsf12 = getDecision(p.getDecisions(), "JavaServer Faces 1.2");
        Decision jsf2 = getDecision(p.getDecisions(), "JavaServer Faces 2");
        Decision glassfish2 = getDecision(p.getDecisions(), "Glassfish 2");
        Decision glassfish3 = getDecision(p.getDecisions(), "Glassfish 3");
        Decision ejb3 = getDecision(p.getDecisions(), "Enterprise Java Beans 3");
        Decision ejb31 = getDecision(p.getDecisions(), "Enterprise Java Beans 3.1");
        Decision jee5 = getDecision(p.getDecisions(), "Java Enterprise Edition 5");
        Decision jee6 = getDecision(p.getDecisions(), "Java Enterprise Edition 6");
        Decision client = getDecision(p.getDecisions(), "Client side image generation");
        Decision fop = getDecision(p.getDecisions(), "Apache FOP");
        Decision batik = getDecision(p.getDecisions(), "Apache Batik");
        Decision svg = getDecision(p.getDecisions(), "Scalable Vector Graphics");
        Decision server = getDecision(p.getDecisions(), "Server side image generation");
        Decision javaImg = getDecision(p.getDecisions(), "Image generation in Java");
        Decision html5 = getDecision(p.getDecisions(), "HTML 5 Canvas Element");
        Decision graphviz = getDecision(p.getDecisions(), "Graphviz");

        InitRelationshipView init = new InitRelationshipView(p);
        RelationshipViewVisualization vis = init.getView();

        RelationshipViewNode node = vis.getNode(icefaces.getCurrentVersion());
        node.setX(920);
        node.setY(1190);
        node.setWidth(80);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(mysql.getCurrentVersion());
        node.setX(480);
        node.setY(890);
        node.setWidth(70);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(jsf2.getCurrentVersion());
        node.setX(1540);
        node.setY(1390);
        node.setWidth(150);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(jsf12.getCurrentVersion());
        node.setX(1130);
        node.setY(1330);
        node.setWidth(160);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(ice202.getCurrentVersion());
        node.setX(1520);
        node.setY(1270);
        node.setWidth(150);
        node.setHeight(50);
        node.setVisible(true);


        node = vis.getNode(ice201.getCurrentVersion());
        node.setX(1400);
        node.setY(1190);
        node.setWidth(150);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(ice18.getCurrentVersion());
        node.setX(1380);
        node.setY(1100);
        node.setWidth(100);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(server.getCurrentVersion());
        node.setX(850);
        node.setY(70);
        node.setWidth(220);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(graphviz.getCurrentVersion());
        node.setX(1160);
        node.setY(70);
        node.setWidth(80);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(client.getCurrentVersion());
        node.setX(1380);
        node.setY(70);
        node.setWidth(210);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(oprTech.getCurrentVersion());
        node.setX(590);
        node.setY(770);
        node.setWidth(170);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(javaee.getCurrentVersion());
        node.setX(890);
        node.setY(470);
        node.setWidth(170);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(svg.getCurrentVersion());
        node.setX(1150);
        node.setY(190);
        node.setWidth(180);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(javaImg.getCurrentVersion());
        node.setX(870);
        node.setY(190);
        node.setWidth(190);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(batik.getCurrentVersion());
        node.setX(1110);
        node.setY(310);
        node.setWidth(110);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(fop.getCurrentVersion());
        node.setX(1260);
        node.setY(360);
        node.setWidth(100);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(html5.getCurrentVersion());
        node.setX(1410);
        node.setY(190);
        node.setWidth(180);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(tcl.getCurrentVersion());
        node.setX(740);
        node.setY(190);
        node.setWidth(70);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(xowiki.getCurrentVersion());
        node.setX(530);
        node.setY(190);
        node.setWidth(70);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(java.getCurrentVersion());
        node.setX(680);
        node.setY(310);
        node.setWidth(210);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(jee5.getCurrentVersion());
        node.setX(1190);
        node.setY(570);
        node.setWidth(180);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(jee6.getCurrentVersion());
        node.setX(1420);
        node.setY(470);
        node.setWidth(180);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(glassfish.getCurrentVersion());
        node.setX(930);
        node.setY(720);
        node.setWidth(80);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(glassfish2.getCurrentVersion());
        node.setX(1240);
        node.setY(670);
        node.setWidth(90);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(glassfish3.getCurrentVersion());
        node.setX(1470);
        node.setY(780);
        node.setWidth(90);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(jpa.getCurrentVersion());
        node.setX(520);
        node.setY(500);
        node.setWidth(160);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(eclipselink.getCurrentVersion());
        node.setX(410);
        node.setY(500);
        node.setWidth(90);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(ejb.getCurrentVersion());
        node.setX(870);
        node.setY(990);
        node.setWidth(170);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(ejb3.getCurrentVersion());
        node.setX(1200);
        node.setY(870);
        node.setWidth(180);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(ejb31.getCurrentVersion());
        node.setX(1190);
        node.setY(990);
        node.setWidth(190);
        node.setHeight(50);
        node.setVisible(true);

        node = vis.getNode(jsf.getCurrentVersion());
        node.setX(890);
        node.setY(1400);
        node.setWidth(140);
        node.setHeight(50);
        node.setVisible(true);

        Relationship rel = getRelationship(html5.getCurrentVersion(), client.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        RelationshipViewAssociation relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1485).setY(190));
        relVieAss.addHandle(new Handle().setX(1485).setY(110));
        relVieAss.setLabelX(1500).setLabelY(150);

        rel = getRelationship(svg.getCurrentVersion(), client.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1290).setY(190));
        relVieAss.addHandle(new Handle().setX(1290).setY(90));
        relVieAss.addHandle(new Handle().setX(1380).setY(90));
        relVieAss.setLabelX(1300).setLabelY(110);

        rel = getRelationship(client.getCurrentVersion(), server.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1480).setY(70));
        relVieAss.addHandle(new Handle().setX(1480).setY(40));
        relVieAss.addHandle(new Handle().setX(940).setY(40));
        relVieAss.addHandle(new Handle().setX(940).setY(70));
        relVieAss.setLabelX(1180).setLabelY(20);


        rel = getRelationship(fop.getCurrentVersion(), svg.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1300).setY(360));
        relVieAss.addHandle(new Handle().setX(1300).setY(234));
        relVieAss.setLabelX(1310).setLabelY(290);

        rel = getRelationship(batik.getCurrentVersion(), svg.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1190).setY(310));
        relVieAss.addHandle(new Handle().setX(1190).setY(234));
        relVieAss.setLabelX(1200).setLabelY(260);

        rel = getRelationship(java.getCurrentVersion(), tcl.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_IS_ALTERNATIVE_FOR));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(780).setY(310));
        relVieAss.addHandle(new Handle().setX(780).setY(234));
        relVieAss.setLabelX(790).setLabelY(270);

        rel = getRelationship(javaImg.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(990).setY(234));
        relVieAss.addHandle(new Handle().setX(990).setY(310));
        relVieAss.addHandle(new Handle().setX(883).setY(310));
        relVieAss.setLabelX(930).setLabelY(290);


        rel = getRelationship(batik.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1110).setY(330));
        relVieAss.addHandle(new Handle().setX(884).setY(330));
        relVieAss.setLabelX(960).setLabelY(320);

        rel = getRelationship(eclipselink.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(450).setY(500));
        relVieAss.addHandle(new Handle().setX(450).setY(330));
        relVieAss.addHandle(new Handle().setX(680).setY(330));
        relVieAss.setLabelX(460).setLabelY(340);

        rel = getRelationship(fop.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1260).setY(380));
        relVieAss.addHandle(new Handle().setX(850).setY(380));
        relVieAss.addHandle(new Handle().setX(850).setY(355));
        relVieAss.setLabelX(920).setLabelY(370);

        rel = getRelationship(jee6.getCurrentVersion(), jee5.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1420).setY(500));
        relVieAss.addHandle(new Handle().setX(1260).setY(500));
        relVieAss.addHandle(new Handle().setX(1260).setY(570));
        relVieAss.setLabelX(1270).setLabelY(510);

        rel = getRelationship(jpa.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(570).setY(500));
        relVieAss.addHandle(new Handle().setX(570).setY(340));
        relVieAss.addHandle(new Handle().setX(680).setY(340));
        relVieAss.setLabelX(580).setLabelY(350);




        rel = getRelationship(svg.getCurrentVersion(), javaImg.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1150).setY(210));
        relVieAss.addHandle(new Handle().setX(1054).setY(210));
        relVieAss.setLabelX(1080).setLabelY(190);



        rel = getRelationship(javaImg.getCurrentVersion(), server.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(940).setY(190));
        relVieAss.addHandle(new Handle().setX(940).setY(114));
        relVieAss.setLabelX(950).setLabelY(140);




        rel = getRelationship(graphviz.getCurrentVersion(), server.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1160).setY(90));
        relVieAss.addHandle(new Handle().setX(1065).setY(90));
        relVieAss.setLabelX(1090).setLabelY(70);



        rel = getRelationship(svg.getCurrentVersion(), graphviz.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1190).setY(190));
        relVieAss.addHandle(new Handle().setX(1190).setY(114));
        relVieAss.setLabelX(1200).setLabelY(150);



        rel = getRelationship(svg.getCurrentVersion(), html5.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1320).setY(200));
        relVieAss.addHandle(new Handle().setX(1410).setY(200));
        relVieAss.setLabelX(1350).setLabelY(180);





        rel = getRelationship(xowiki.getCurrentVersion(), tcl.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(590).setY(215));
        relVieAss.addHandle(new Handle().setX(740).setY(215));
        relVieAss.setLabelX(650).setLabelY(200);




        rel = getRelationship(javaee.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(890).setY(490));
        relVieAss.addHandle(new Handle().setX(760).setY(490));
        relVieAss.addHandle(new Handle().setX(760).setY(355));
        relVieAss.setLabelX(770).setLabelY(470);




        rel = getRelationship(jee5.getCurrentVersion(), javaee.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1190).setY(590));
        relVieAss.addHandle(new Handle().setX(990).setY(590));
        relVieAss.addHandle(new Handle().setX(990).setY(515));
        relVieAss.setLabelX(1010).setLabelY(570);




        rel = getRelationship(glassfish2.getCurrentVersion(), glassfish.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1240).setY(690));
        relVieAss.addHandle(new Handle().setX(960).setY(690));
        relVieAss.addHandle(new Handle().setX(960).setY(720));
        relVieAss.setLabelX(970).setLabelY(670);





        rel = getRelationship(glassfish3.getCurrentVersion(), glassfish2.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1500).setY(780));
        relVieAss.addHandle(new Handle().setX(1500).setY(690));
        relVieAss.addHandle(new Handle().setX(1325).setY(690));
        relVieAss.setLabelX(1450).setLabelY(700);





        rel = getRelationship(javaee.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(890).setY(510));
        relVieAss.addHandle(new Handle().setX(710).setY(510));
        relVieAss.addHandle(new Handle().setX(710).setY(770));
        relVieAss.setLabelX(720).setLabelY(520);




        rel = getRelationship(glassfish.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(930).setY(730));
        relVieAss.addHandle(new Handle().setX(730).setY(730));
        relVieAss.addHandle(new Handle().setX(730).setY(770));
        relVieAss.setLabelX(740).setLabelY(710);




        rel = getRelationship(mysql.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(545).setY(910));
        relVieAss.addHandle(new Handle().setX(610).setY(910));
        relVieAss.addHandle(new Handle().setX(610).setY(814));
        relVieAss.setLabelX(560).setLabelY(870);




        rel = getRelationship(eclipselink.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(440).setY(545));
        relVieAss.addHandle(new Handle().setX(440).setY(790));
        relVieAss.addHandle(new Handle().setX(590).setY(790));
        relVieAss.setLabelX(450).setLabelY(770);




        rel = getRelationship(glassfish3.getCurrentVersion(), glassfish.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1470).setY(810));
        relVieAss.addHandle(new Handle().setX(970).setY(810));
        relVieAss.addHandle(new Handle().setX(970).setY(765));
        relVieAss.setLabelX(990).setLabelY(790);




        rel = getRelationship(ejb3.getCurrentVersion(), ejb.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1200).setY(890));
        relVieAss.addHandle(new Handle().setX(930).setY(890));
        relVieAss.addHandle(new Handle().setX(930).setY(990));
        relVieAss.setLabelX(940).setLabelY(900);




        rel = getRelationship(ejb.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(870).setY(1010));
        relVieAss.addHandle(new Handle().setX(730).setY(1010));
        relVieAss.addHandle(new Handle().setX(730).setY(815));
        relVieAss.setLabelX(750).setLabelY(990);





        rel = getRelationship(ejb31.getCurrentVersion(), glassfish3.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1375).setY(1010));
        relVieAss.addHandle(new Handle().setX(1500).setY(1010));
        relVieAss.addHandle(new Handle().setX(1500).setY(825));
        relVieAss.setLabelX(1440).setLabelY(990);




        rel = getRelationship(ice18.getCurrentVersion(), icefaces.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1380).setY(1120));
        relVieAss.addHandle(new Handle().setX(950).setY(1120));
        relVieAss.addHandle(new Handle().setX(950).setY(1190));
        relVieAss.setLabelX(960).setLabelY(1130);





        rel = getRelationship(jsf12.getCurrentVersion(), ice18.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1230).setY(1330));
        relVieAss.addHandle(new Handle().setX(1230).setY(1140));
        relVieAss.addHandle(new Handle().setX(1380).setY(1140));
        relVieAss.setLabelX(1240).setLabelY(1150);





        rel = getRelationship(ice202.getCurrentVersion(), icefaces.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1520).setY(1290));
        relVieAss.addHandle(new Handle().setX(960).setY(1290));
        relVieAss.addHandle(new Handle().setX(960).setY(1235));
        relVieAss.setLabelX(980).setLabelY(1270);




        rel = getRelationship(icefaces.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(920).setY(1210));
        relVieAss.addHandle(new Handle().setX(660).setY(1210));
        relVieAss.addHandle(new Handle().setX(660).setY(815));
        relVieAss.setLabelX(670).setLabelY(1190);





        rel = getRelationship(jsf.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(891).setY(1410));
        relVieAss.addHandle(new Handle().setX(640).setY(1410));
        relVieAss.addHandle(new Handle().setX(640).setY(815));
        relVieAss.setLabelX(650).setLabelY(1390);




        rel = getRelationship(jsf12.getCurrentVersion(), jsf.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1131).setY(1350));
        relVieAss.addHandle(new Handle().setX(960).setY(1350));
        relVieAss.addHandle(new Handle().setX(960).setY(1401));
        relVieAss.setLabelX(970).setLabelY(1360);





        rel = getRelationship(glassfish2.getCurrentVersion(), ice18.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1321).setY(710));
        relVieAss.addHandle(new Handle().setX(1410).setY(710));
        relVieAss.addHandle(new Handle().setX(1410).setY(1101));
        relVieAss.setLabelX(1350).setLabelY(730);




        rel = getRelationship(glassfish3.getCurrentVersion(), ice202.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1555).setY(800));
        relVieAss.addHandle(new Handle().setX(1580).setY(800));
        relVieAss.addHandle(new Handle().setX(1580).setY(1271));
        relVieAss.setLabelX(1590).setLabelY(1020);





        rel = getRelationship(jsf2.getCurrentVersion(), jsf12.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1570).setY(1391));
        relVieAss.addHandle(new Handle().setX(1570).setY(1350));
        relVieAss.addHandle(new Handle().setX(1284).setY(1350));
        relVieAss.setLabelX(1520).setLabelY(1360);




        rel = getRelationship(java.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(700).setY(355));
        relVieAss.addHandle(new Handle().setX(700).setY(771));
        relVieAss.setLabelX(650).setLabelY(410);





        rel = getRelationship(jpa.getCurrentVersion(), oprTech.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(625).setY(544));
        relVieAss.addHandle(new Handle().setX(625).setY(771));
        relVieAss.setLabelX(570).setLabelY(640);






        rel = getRelationship(jee6.getCurrentVersion(), javaee.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1421).setY(490));
        relVieAss.addHandle(new Handle().setX(1055).setY(490));
        relVieAss.setLabelX(1210).setLabelY(470);







        rel = getRelationship(jee5.getCurrentVersion(), glassfish2.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1270).setY(614));
        relVieAss.addHandle(new Handle().setX(1270).setY(671));
        relVieAss.setLabelX(1280).setLabelY(640);







        rel = getRelationship(ejb3.getCurrentVersion(), glassfish2.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1270).setY(871));
        relVieAss.addHandle(new Handle().setX(1270).setY(714));
        relVieAss.setLabelX(1210).setLabelY(780);







        rel = getRelationship(ejb31.getCurrentVersion(), ejb3.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1281).setY(991));
        relVieAss.addHandle(new Handle().setX(1281).setY(914));
        relVieAss.setLabelX(1290).setLabelY(950);






        rel = getRelationship(ejb31.getCurrentVersion(), ejb.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1191).setY(1015));
        relVieAss.addHandle(new Handle().setX(1034).setY(1015));
        relVieAss.setLabelX(1090).setLabelY(1000);




        rel = getRelationship(ice201.getCurrentVersion(), ice18.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1445).setY(1191));
        relVieAss.addHandle(new Handle().setX(1445).setY(1144));
        relVieAss.setLabelX(1460).setLabelY(1160);






        rel = getRelationship(ice201.getCurrentVersion(), icefaces.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1401).setY(1210));
        relVieAss.addHandle(new Handle().setX(994).setY(1210));
        relVieAss.setLabelX(1110).setLabelY(1190);






        rel = getRelationship(ice202.getCurrentVersion(), ice201.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_REPLACES));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1533).setY(1271));
        relVieAss.addHandle(new Handle().setX(1533).setY(1234));
        relVieAss.setLabelX(1490).setLabelY(1250);






        rel = getRelationship(jsf2.getCurrentVersion(), ice202.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1605).setY(1391));
        relVieAss.addHandle(new Handle().setX(1605).setY(1313));
        relVieAss.setLabelX(1620).setLabelY(1340);





        rel = getRelationship(jee6.getCurrentVersion(), glassfish3.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_DEPENDS_ON));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1520).setY(513));
        relVieAss.addHandle(new Handle().setX(1520).setY(782));
        relVieAss.setLabelX(1530).setLabelY(620);





        rel = getRelationship(jsf2.getCurrentVersion(), jsf.getCurrentVersion(), getRelationshipType(RELATIONSHIPTYPE_CAUSED_BY));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1541).setY(1420));
        relVieAss.addHandle(new Handle().setX(1023).setY(1420));
        relVieAss.setLabelX(1270).setLabelY(1410);






        p.addRelationshipView(vis);

        //set all elements 200px to the left site.
        int reduceX = 200;
        int expandY = 30;

        for (RelationshipViewVisualization viewVis : p.getRelationshipViews()) {
            for (RelationshipViewNode node1 : viewVis.getNodes()) {
                node1.setX(node1.getX() - reduceX);
                node1.setY(node1.getY() + expandY);

            }
            for (RelationshipViewAssociation ass : viewVis.getAssociations()) {
                ass.setLabelX(ass.getLabelX() - reduceX);
                ass.setLabelY(ass.getLabelY() + expandY);
                for (Handle hand : ass.getHandles()) {
                    hand.setX(hand.getX() - reduceX);
                    hand.setY(hand.getY() + expandY);
                }
            }
        }



        pl.merge(p);

        relationshipViewDone = true;
        clearDone = false;
    }

    public boolean isClearDone() {
        return clearDone;
    }

    public void setClearDone(boolean clearDone) {
        this.clearDone = clearDone;
    }

    public boolean isDecisionsDone() {
        return decisionsDone;
    }

    public void setDecisionsDone(boolean decisionsDone) {
        this.decisionsDone = decisionsDone;
    }

    public boolean isIterationsDone() {
        return iterationsDone;
    }

    public void setIterationsDone(boolean iterationsDone) {
        this.iterationsDone = iterationsDone;
    }

    public boolean isPersonsDone() {
        return personsDone;
    }

    public void setPersonsDone(boolean personsDone) {
        this.personsDone = personsDone;
    }

    public boolean isProjectsDone() {
        return projectsDone;
    }

    public void setProjectsDone(boolean projectsDone) {
        this.projectsDone = projectsDone;
    }

    public boolean isConcernsDone() {
        return concernsDone;
    }

    public void setConcernsDone(boolean concernsDone) {
        this.concernsDone = concernsDone;
    }

    public boolean isRolesDone() {
        return rolesDone;
    }

    public void setRolesDone(boolean rolesDone) {
        this.rolesDone = rolesDone;
    }

    public boolean isStatesDone() {
        return statesDone;
    }

    public void setStatesDone(boolean statesDone) {
        this.statesDone = statesDone;
    }

    public boolean isTemplatesDone() {
        return templatesDone;
    }

    public void setTemplatesDone(boolean templatesDone) {
        this.templatesDone = templatesDone;
    }

    public boolean isRelationshipTypesDone() {
        return relationshipTypesDone;
    }

    public void setRelationshipTypesDone(boolean relationshipTypesDone) {
        this.relationshipTypesDone = relationshipTypesDone;
    }

    public void doEverything() {
        DatabaseCleaner.bruteForceCleanup();

        SessionUtil.resetSession();

        addStakeholderRoles();
        addStates();
        addDecisionTemplates();
        addRelationshipTypes();
        addPersons();
        addProject();
        addIterations();
        addDecisions();
        addConcerns();
        addRelationships();
        addRelationshipView();

        clearDone = false;
        addAllDone = true;

        login();
    }

    public boolean isAddAllDone() {
        return addAllDone;
    }

    public boolean isRelationshipsDone() {
        return relationshipsDone;
    }

    public void setRelationshipsDone(boolean relationshipsDone) {
        this.relationshipsDone = relationshipsDone;
    }

    public boolean isRelationshipViewDone() {
        return relationshipViewDone;
    }
}