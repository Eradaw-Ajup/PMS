package gov.nist.csd.pm.pdp.services;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.HttpClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.constants.PolicyType;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.authority.AuthorityType;
import gov.nist.csd.pm.pap.model.Authority;
import gov.nist.csd.pm.pap.model.MNode;
import gov.nist.csd.pm.pap.model.Policy;
import gov.nist.csd.pm.pep.requests.AccessTokenRequest;
import gov.nist.csd.pm.pep.response.AccessToken;
import gov.nist.csd.pm.pep.response.PartialToken;
import gov.nist.csd.pm.pep.response.Setup;
import gov.nist.csd.pm.pep.response.Shares;
import gov.nist.csd.pm.ptl.model.Translation;

public class CryptoService extends Service {

	public CryptoService(String owner) throws PMException {
		super(owner);
	}

	public void updateJSON(String json, long policyID, PolicyStatus status) throws PMException {
		getCryptoPAP().updateJSON(json, policyID, getOwner(), status.name());
	}

	public String getJSON(long id) throws PMException {
		if (0 == id) {
			throw new IllegalArgumentException("Id not found!");
		}
		return getCryptoPAP().getJSON(id, getOwner(), PolicyStatus.ACTIVE);
	}

	public Setup getSetupJSON(long id) throws PMException, IOException, InterruptedException {
		if (0 == id) {
			throw new IllegalArgumentException("Id not found!");
		}
		Policy policy = getPolicyPAP().getPolicy(id);
		if (null == policy) {
			throw new IllegalArgumentException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, id));
		}

		PolicyType type = PolicyType.toPolicyType(policy.getType());
		AuthorityService service = new AuthorityService(getOwner());
		Set<Authority> authorities = service.getAuthorities();
		if (type == PolicyType.NONE) {
			authorities.removeIf(authority -> authority.getAuthorityType().equals(AuthorityType.CUSTOM.name()));

			Authority authority = authorities.iterator().next();

			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(authority.getApiEndpoint() + "/crypto/setup"))
					.header(MessageHelper.CONTENT_TYPE, MessageHelper.APPLICATION_JSON).GET().build();

			HttpResponse<?> response = HttpClient.newHttpClient().send(httpRequest,
					HttpResponse.BodyHandlers.ofString());

			JsonNode jNode = new ObjectMapper().readTree(response.body().toString());

			return new Gson().fromJson(jNode.get("entity").toString(), Setup.class);
		} else {
			Set<MNode> nodes = (Set<MNode>) getGraphPAP().getNodes();
			nodes.removeIf(node -> node.getPolicyID() != policy.getId());

			Set<Long> ids = new HashSet<>();
			for (MNode node : nodes) {
				ids.add(node.getAuthorityID());
			}

			Setup setup = new Setup();
			for (long authorityID : ids) {
				Authority authority = service.getAuthority(authorityID);

				HttpRequest httpRequest = HttpRequest.newBuilder()
						.uri(URI.create(authority.getApiEndpoint() + "/crypto/setup"))
						.header(MessageHelper.CONTENT_TYPE, MessageHelper.APPLICATION_JSON).GET().build();

				HttpResponse<?> response = HttpClient.newHttpClient().send(httpRequest,
						HttpResponse.BodyHandlers.ofString());

				JsonNode jNode = new ObjectMapper().readTree(response.body().toString());

				Setup temp = new Gson().fromJson(jNode.get("entity").toString(), Setup.class);
				setup.getCurve_params().add(temp.getCurve_params().get(0));
				setup.getMaster_public_key().add(temp.getMaster_public_key().get(0));
			}
			return setup;
		}
	}

	public AccessToken getAccessToken(long nodeID, String token, String value)
			throws PMException, IOException, InterruptedException {
		GraphService service = new GraphService(getOwner());
		if (0 == nodeID || !service.exists(nodeID)) {
			throw new IllegalArgumentException(String.format(MessageHelper.NODE_DOES_NOT_EXIST, nodeID));
		}

		MNode node = service.getNode(nodeID);

		Policy policy = getPolicyPAP().getPolicy(node.getPolicyID());

		if (PolicyType.NONE == PolicyType.toPolicyType(policy.getType())) {
			AuthorityService authService = new AuthorityService(getOwner());
			Authority authority = authService.getAuthority(node.getAuthorityID());

			AccessTokenRequest payload = new AccessTokenRequest();
			payload.setAttributeID(node.getAttributeID());
			payload.setAuthorityID(node.getAuthorityID());
			payload.setNode(node);
			payload.setPolicy(new Gson().fromJson(policy.getCryptoJSON(), Translation.class));
			payload.setPolicyType(policy.getType());
			payload.setSecret(authority.getSecret());
			payload.setToken(token);
			payload.setValue(value);

			System.out.println("PAYLOAD: " + new Gson().toJson(payload));

			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(authority.getApiEndpoint() + "/crypto/access-token"))
					.header(MessageHelper.CONTENT_TYPE, MessageHelper.APPLICATION_JSON)
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(payload))).build();

			HttpResponse<?> response = HttpClient.newHttpClient().send(httpRequest,
					HttpResponse.BodyHandlers.ofString());

			JsonNode jNode = new ObjectMapper().readTree(response.body().toString());

			return new Gson().fromJson(jNode.get("entity").toString(), AccessToken.class);
		} else if (PolicyType.MULTI == PolicyType.toPolicyType(policy.getType())) {
			return null;
		}
		return null;
	}

	public Shares getShares(long id) throws PMException, IOException, InterruptedException {
		if (0 == id) {
			throw new IllegalArgumentException("Id not found!");
		}
		Policy policy = getPolicyPAP().getPolicy(id);
		if (null == policy) {
			throw new IllegalArgumentException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, id));
		}

		PolicyType type = PolicyType.toPolicyType(policy.getType());
		if (type == PolicyType.NONE) {
			AuthorityService authorityService = new AuthorityService(getOwner());
			Set<Authority> authorities = authorityService.getAuthorities();

			for (Authority a : authorities)
				System.out.println("name: " + a.getName() + "\ttype: " + a.getAuthorityType());
			authorities.removeIf(authority -> authority.getAuthorityType().equals(AuthorityType.CUSTOM.name()));

			Authority authority = authorities.iterator().next();

			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(authority.getApiEndpoint() + "/crypto/shares/" + id))
					.header(MessageHelper.CONTENT_TYPE, MessageHelper.APPLICATION_JSON).GET().build();

			HttpResponse<?> response = HttpClient.newHttpClient().send(httpRequest,
					HttpResponse.BodyHandlers.ofString());

			JsonNode jNode = new ObjectMapper().readTree(response.body().toString());

			return new Gson().fromJson(jNode.get("entity").toString(), Shares.class);
		} else {
			throw new PMException("Multi-authority is not supported");
		}
	}

	public PartialToken getPartialToken(long id) throws PMException, IOException, InterruptedException {
		if (0 == id) {
			throw new IllegalArgumentException("Id not found!");
		}
		Policy policy = getPolicyPAP().getPolicy(id);
		if (null == policy) {
			throw new IllegalArgumentException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, id));
		}

		PolicyType type = PolicyType.toPolicyType(policy.getType());
		if (type == PolicyType.NONE) {
			AuthorityService authorityService = new AuthorityService(getOwner());
			Set<Authority> authorities = authorityService.getAuthorities();

			for (Authority a : authorities)
				System.out.println("name: " + a.getName() + "\ttype: " + a.getAuthorityType());
			authorities.removeIf(authority -> authority.getAuthorityType().equals(AuthorityType.CUSTOM.name()));

			Authority authority = authorities.iterator().next();

			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(authority.getApiEndpoint() + "/crypto/partial-token/" + id))
					.header(MessageHelper.CONTENT_TYPE, MessageHelper.APPLICATION_JSON).GET().build();

			HttpResponse<?> response = HttpClient.newHttpClient().send(httpRequest,
					HttpResponse.BodyHandlers.ofString());

			JsonNode jNode = new ObjectMapper().readTree(response.body().toString());
			if (null == jNode || null == jNode.get("entity").toString())
				return null;
			else
				return new Gson().fromJson(jNode.get("entity").toString(), PartialToken.class);
		} else {
			throw new PMException("Multi-authority is not supported");
		}
	}
}
