package gov.nist.csd.pm.pep.resources;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import gov.nist.csd.pm.common.util.JWT;
import gov.nist.csd.pm.common.util.annotations.PMSecure;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pdp.services.GraphService;
import gov.nist.csd.pm.pep.requests.AssignmentRequest;
import gov.nist.csd.pm.pep.requests.AssociationRequest;
import gov.nist.csd.pm.pep.requests.CreateNodeRequest;
import gov.nist.csd.pm.pep.response.ApiResponse;
import gov.nist.csd.pm.pep.response.AssociationsResponse;

@Path("/graph")
@PMSecure
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GraphResource {

	@Path("/nodes")
	@POST
	public Response createNode(CreateNodeRequest request, @HeaderParam("Authorization") String token)
			throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		return ApiResponse.Builder.success()
				.entity(graphService.createNode(request.getName(), request.getType(), request.getProperties(),
						request.getStoreType(), request.getPolicyID(), request.getAttributeID(),
						request.getAuthorityID()))
				.build();
	}

	@Path("/nodes")
	@PUT
	public Response updateNode(CreateNodeRequest request, @HeaderParam("Authorization") String token)
			throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		graphService.updateNode(request.getId(), request.getName(), request.getProperties(), request.getStoreType(),
				request.getPolicyID());
		return ApiResponse.Builder.success().message(ApiResponse.UPDATE_NODE_SUCCESS).build();
	}

	@Path("/nodes/{nodeID}")
	@DELETE
	public Response deleteNode(@PathParam("nodeID") long id, @HeaderParam("Authorization") String token)
			throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		graphService.deleteNode(id);
		return ApiResponse.Builder.success().message(ApiResponse.DELETE_NODE_SUCCESS).build();
	}

	@Path("/nodes")
	@GET
	public Response getNodes(@Context UriInfo uriInfo, @HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(graphService.getNodes()).build();
	}

	@Path("/nodes/all")
	@GET
	public Response getAllNodes(@HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(graphService.getAllNodes()).build();
	}

	@Path("/nodes/{nodeID}")
	@GET
	public Response getNode(@PathParam("nodeID") long nodeID, @HeaderParam("Authorization") String token)
			throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(graphService.getNode(nodeID)).build();
	}

	@Path("/{sourceID}/assignments/{targetID}")
	@POST
	public Response createAssignment(@PathParam("sourceID") long sourceID, @PathParam("targetID") long targetID,
			AssignmentRequest request, @HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		graphService.assign(request.getPolicyID(), sourceID, targetID);
		return ApiResponse.Builder.success().message(ApiResponse.CREATE_ASSIGNMENT_SUCCESS).build();
	}

	@Path("/{sourceID}/assignments/{targetID}")
	@DELETE
	public Response deleteAssignment(@PathParam("sourceID") long sourceID, @PathParam("targetID") long targetID,
			AssignmentRequest request, @HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		graphService.deassign(request.getPolicyID(), sourceID, targetID);
		return ApiResponse.Builder.success().message(ApiResponse.DELETE_ASSIGNMENT_SUCCESS).build();
	}

	@Path("/assignments/{policyID}")
	@GET
	public Response getAssignments(@PathParam("policyID") long policyID, @HeaderParam("Authorization") String token)
			throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(graphService.getAssignments(policyID)).build();
	}

	@Path("/children/{id}")
	@GET
	public Response getChildrens(@PathParam("id") long id, @HeaderParam("Authorization") String token)
			throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(graphService.getChildren(id)).build();
	}

	@Path("/parent/{id}")
	@GET
	public Response getParents(@PathParam("id") long id, @HeaderParam("Authorization") String token)
			throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		return ApiResponse.Builder.success().entity(graphService.getParents(id)).build();
	}

	@Path("/{sourceID}/associations/{targetID}")
	@POST
	public Response createAssociation(@PathParam("sourceID") long sourceID, @PathParam("targetID") long targetID,
			AssociationRequest request, @HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		graphService.associate(request.getPolicyID(), sourceID, targetID, request.getOperations());
		return ApiResponse.Builder.success().message(ApiResponse.CREATE_ASSOCIATION_SUCCESS).build();
	}

	@Path("/{sourceID}/associations/{targetID}")
	@PUT
	public Response updateAssociation(@PathParam("sourceID") long sourceID, @PathParam("targetID") long targetID,
			AssociationRequest request, @HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService();
		graphService.associate(request.getPolicyID(), sourceID, targetID, request.getOperations());
		return ApiResponse.Builder.success().message(ApiResponse.UPDATE_ASSOCIATION_SUCCESS).build();
	}

	@Path("/{sourceID}/associations/{targetID}")
	@DELETE
	public Response deleteAssociation(@PathParam("sourceID") long sourceID, @PathParam("targetID") long targetID,
			AssociationRequest request, @HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService(JWT.getSubject(token));
		graphService.dissociate(request.getPolicyID(), sourceID, targetID);
		return ApiResponse.Builder.success().message(ApiResponse.DELETE_ASSOCIATION_SUCCESS).build();
	}

	// Need to optimize this API and break it into modular format.
	@Path("/associations/{id}")
	@GET
	public Response getAssociations(@PathParam("id") long nodeID, @QueryParam("type") String type,
			@HeaderParam("Authorization") String token) throws PMException {

		try {
			GraphService graphService = new GraphService(JWT.getSubject(token));
			AssociationsResponse associationsResponse = new AssociationsResponse();

			// if the type is source, return the source associations
			// else return the target associations
			if (type.equals("source")) {
				Node node = null;
				long targetID = 0;
				for (Node n : graphService.getParents(nodeID)) {
					if (n.getType().equals(NodeType.UA))
						node = n;
				}

				if (null != node) {
					Set<String> assoc = new HashSet<>();

					for (Entry<Long, Set<String>> id : graphService.getSourceAssociations(node.getID()).entrySet()) {
						targetID = id.getKey();
						assoc.addAll(id.getValue());
					}
					associationsResponse.setAssociations(assoc);

					associationsResponse.getNodes().add(node);
					associationsResponse.getNodes().add(graphService.getNode(targetID));
				}
			} else if (type.equals("target")) {
				Node node = null;
				for (Node n : graphService.getParents(nodeID))
					if (n.getType().equals(NodeType.OA))
						node = n;

				if (null != node) {
					Set<String> assoc = new HashSet<>();
					for (Entry<Long, Set<String>> id : graphService.getSourceAssociations(node.getID()).entrySet()) {
						assoc.addAll(id.getValue());
					}
					associationsResponse.setAssociations(assoc);
				}
			}

			return ApiResponse.Builder.success().entity(associationsResponse).build();
		} catch (PMException e) {
			return ApiResponse.Builder.success().entity(null).build();
		}
	}

	@Path("/{childID}/check-assignment/{parentID}")
	@GET
	public Response checkAssignment(@PathParam("childID") long childID, @PathParam("parentID") long parentID,
			@HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService();
		return ApiResponse.Builder.success().entity(graphService.checkAssignment(childID, parentID)).build();
	}

	@Path("/{sourceID}/check-association/{targetID}")
	@GET
	public Response checkAssociation(@PathParam("sourceID") long sourceID, @PathParam("targetID") long targetID,
			@HeaderParam("Authorization") String token) throws PMException {
		GraphService graphService = new GraphService();
		return ApiResponse.Builder.success().entity(graphService.checkAssociation(sourceID, targetID)).build();
	}
}
