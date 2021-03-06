package nl.rug.search.odr.ws.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.ws.SessionBeanRepository;
import nl.rug.search.odr.ws.assembler.RelationshipTypeAssembler;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTOList;
import nl.rug.search.odr.ws.security.SkipAuthentication;
import nl.rug.search.odr.ws.security.SkipGroupVerification;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Path("/")
public class RelationshipTypeResource {
    
    private RelationshipTypeLocal rtl = SessionBeanRepository
            .getInstance()
            .lookup(RelationshipTypeLocal.class);
    
    @GET
    @Path("/relationshiptypes")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @SkipAuthentication
    @SkipGroupVerification
    public RelationshipTypeDTOList getCommonRelationshipTypes() {
        return new RelationshipTypeDTOList(RelationshipTypeAssembler
                .assemble(rtl.getPublicTypes()));
    }
}
