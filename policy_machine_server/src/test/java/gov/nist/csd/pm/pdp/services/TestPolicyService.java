/*
 * package gov.nist.csd.pm.pdp.services;
 *
 * import static gov.nist.csd.pm.graph.model.nodes.NodeType.O; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.OA; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.PC; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.U; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.UA; import static
 * gov.nist.csd.pm.utils.TestUtils.getDatabaseContext; import static
 * org.junit.Assert.assertNotEquals; import static
 * org.junit.jupiter.api.Assertions.assertThrows; import static
 * org.junit.jupiter.api.Assertions.assertTrue;
 *
 * import java.io.IOException; import java.util.ArrayList; import
 * java.util.HashMap; import java.util.HashSet; import java.util.List; import
 * java.util.Map; import java.util.Set;
 *
 * import org.junit.jupiter.api.AfterEach; import
 * org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Disabled;
 * import org.junit.jupiter.api.Test; import org.junit.jupiter.api.TestInstance;
 * import org.junit.jupiter.api.TestInstance.Lifecycle;
 *
 * import com.pm.abe_jni.exceptions.AbeException;
 *
 * import gov.nist.csd.pm.common.constants.NodeStoreType; import
 * gov.nist.csd.pm.common.exceptions.PMGraphException; import
 * gov.nist.csd.pm.exceptions.PMException; import
 * gov.nist.csd.pm.graph.model.nodes.Node; import gov.nist.csd.pm.pap.PAP;
 * import gov.nist.csd.pm.pap.model.MNode; import
 * gov.nist.csd.pm.pap.model.Policy; import
 * gov.nist.csd.pm.pep.requests.AssociationJSON; import
 * gov.nist.csd.pm.rap.model.Attribute; import
 * gov.nist.csd.pm.rap.model.Resource; import
 * gov.nist.csd.pm.rap.services.ResourceAccessPointService;
 *
 * @TestInstance(Lifecycle.PER_CLASS)
 *
 * @Disabled public class TestPolicyService {
 *
 * @BeforeEach public void setup() throws PMException, IOException {
 * PAP.getPAP(getDatabaseContext()); }
 *
 * @Test
 *
 * @Disabled public void importPolicyTest() throws PMException { PolicyService
 * policyService = new PolicyService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 * assertThrows(IllegalArgumentException.class, () ->
 * policyService.importPolicy(null, null));
 *
 * Policy policy = new Policy(); policy.setName("test");
 * policy.setDescription("for testing"); MNode pc = new MNode(1, "testPC", PC,
 * null); MNode oa = new MNode(2, "testOA", OA, null); MNode ua = new MNode(3,
 * "testUA", UA, null); MNode u = new MNode(4, "testU", U, null); MNode o = new
 * MNode(5, "testO", O, null);
 *
 * List<Node> nodes = new ArrayList<>(); nodes.add(pc); nodes.add(oa);
 * nodes.add(ua); nodes.add(o); nodes.add(u); policy.setNodes(nodes);
 *
 * List<Long> assigList1 = new ArrayList<>(); assigList1.add(0, oa.getID());
 * assigList1.add(1, pc.getID());
 *
 * List<Long> assigList2 = new ArrayList<>(); assigList2.add(0, ua.getID());
 * assigList2.add(1, pc.getID());
 *
 * List<Long> assigList3 = new ArrayList<>(); assigList3.add(0, o.getID());
 * assigList3.add(1, oa.getID());
 *
 * List<Long> assigList4 = new ArrayList<>(); assigList4.add(0, u.getID());
 * assigList4.add(1, ua.getID());
 *
 * List<List<Long>> assignments = new ArrayList<List<Long>>();
 * assignments.add(assigList1); assignments.add(assigList2);
 * assignments.add(assigList3); assignments.add(assigList4);
 *
 * policy.setAssignments(assignments);
 *
 * List<AssociationJSON> associations = new ArrayList<>(); Set<String>
 * operations = new HashSet<String>(); operations.add("*"); AssociationJSON
 * association = new AssociationJSON(ua.getID(), oa.getID(), operations);
 * associations.add(association);
 *
 * policy.setAssociations(associations); String name = "test_policy";
 * policyService.importPolicy(policy, name);
 *
 * }
 *
 * @Test
 *
 * @Disabled public void exportPolicyTest() throws PMException {
 *
 * PolicyService policyService = new
 * PolicyService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 * assertThrows(PMGraphException.class, () -> policyService.exportPolicy(0));
 * Policy policy = policyService.exportPolicy(1); assertTrue(policy != null,
 * "invalid policy"); }
 *
 * @Test public void publishPolicyTest() throws PMException, IOException,
 * AbeException { ResourceAccessPointService resourceAccessPointService = new
 * ResourceAccessPointService("b50eb672-5e50-4f7f-8354-c7ba3423d41b"); Resource
 * resource1 = new Resource(); resource1.setName(null); resource1.setType(null);
 * resource1.setAttributes(null); assertThrows(IllegalArgumentException.class,
 * () -> resourceAccessPointService.createResource(resource1, null));
 *
 * Resource resource = new Resource(); resource.setName("crypto-key");
 * resource.setStatus("un-protected"); resource.setType("TEXT");
 *
 * Map<String, String> properties = new HashMap<>(); properties.put("ua",
 * "colonel");
 *
 * Attribute attribute = new Attribute(); attribute.setKey("network");
 * attribute.setValue("private-network"); Attribute attribute1 = new
 * Attribute(); attribute1.setKey("location"); attribute1.setValue("ait-pune");
 * Attribute attribute2 = new Attribute(); attribute2.setKey("time");
 * attribute2.setValue("10:00AM-09:00PM"); Attribute attribute3 = new
 * Attribute(); attribute3.setKey("rank"); attribute3.setValue("rank>=major");
 * attribute3.setProperties(properties);
 *
 * Set<Attribute> attrs = new HashSet<>(); attrs.add(attribute);
 * attrs.add(attribute1); attrs.add(attribute2); attrs.add(attribute3);
 *
 * resource.setAttributes(attrs);
 *
 * long id = resourceAccessPointService.createResource(resource, null);
 * assertNotEquals(0, id);
 *
 * GraphService graphService = new
 * GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b"); Node pc =
 * graphService.createNode("network-authentication", PC, null,
 * NodeStoreType.SN); Node pc1 =
 * graphService.createNode("location-authentication", PC, null,
 * NodeStoreType.SN); Node pc2 =
 * graphService.createNode("access-time-authentication", PC, null,
 * NodeStoreType.SN); Node pc3 = graphService.createNode("rank-verification",
 * PC, null, NodeStoreType.SN);
 *
 * Node ua = graphService.createNode("192.198.47.188", UA, null,
 * NodeStoreType.SN); Node ua1 =
 * graphService.createNode("confidential-facility", UA, null, NodeStoreType.SN);
 * Node ua2 = graphService.createNode("access-time", UA, null,
 * NodeStoreType.SN); Node ua3 = graphService.createNode("colonel", UA, null,
 * NodeStoreType.SN);
 *
 * Node u = graphService.createNode("super", U, null, NodeStoreType.SN);
 *
 * Node onodes = graphService.getNodes().stream().filter(n ->
 * n.getType().equals(O)) .filter(n ->
 * (n.getName().equals(resource.getName()))).findAny().get();
 *
 * Node oaNode = graphService.getNodes().stream().filter(n ->
 * n.getType().equals(OA)) .filter(n ->
 * (n.getName().equals(attribute.getValue()))).findAny().get();
 *
 * Node oaNode1 = graphService.getNodes().stream().filter(n ->
 * n.getType().equals(OA)) .filter(n ->
 * (n.getName().equals(attribute1.getValue()))).findAny().get();
 *
 * Node oaNode2 = graphService.getNodes().stream().filter(n ->
 * n.getType().equals(OA)) .filter(n ->
 * (n.getName().equals(attribute2.getValue()))).findAny().get();
 *
 * Node oaNode3 = graphService.getNodes().stream().filter(n ->
 * n.getType().equals(OA)) .filter(n ->
 * (n.getName().equals(attribute3.getValue()))).findAny().get();
 *
 *
 * graphService.stageNodeAssign(oaNode.getID(), pc.getID());
 * graphService.stageNodeAssign(oaNode1.getID(), pc1.getID());
 * graphService.stageNodeAssign(oaNode2.getID(), pc2.getID());
 * graphService.stageNodeAssign(oaNode3.getID(), pc3.getID());
 *
 * graphService.stageNodeAssign(ua.getID(), pc.getID());
 * graphService.stageNodeAssign(ua1.getID(), pc1.getID());
 * graphService.stageNodeAssign(ua2.getID(), pc2.getID());
 * graphService.stageNodeAssign(ua3.getID(), pc3.getID());
 *
 * graphService.stageNodeAssign(u.getID(), ua.getID());
 * graphService.stageNodeAssign(u.getID(), ua1.getID());
 * graphService.stageNodeAssign(u.getID(), ua2.getID());
 * graphService.stageNodeAssign(u.getID(), ua3.getID());
 *
 * graphService.stageNodeAssign(onodes.getID(), oaNode.getID());
 * graphService.stageNodeAssign(onodes.getID(), oaNode1.getID());
 * graphService.stageNodeAssign(onodes.getID(), oaNode2.getID());
 * graphService.stageNodeAssign(onodes.getID(), oaNode3.getID());
 *
 * Set<String> opertaion = new HashSet<>(); opertaion.add("*");
 *
 * graphService.stageNodeAssociate(ua.getID(), oaNode.getID(), opertaion);
 * graphService.stageNodeAssociate(ua1.getID(), oaNode1.getID(), opertaion);
 * graphService.stageNodeAssociate(ua2.getID(), oaNode2.getID(), opertaion);
 * graphService.stageNodeAssociate(ua3.getID(), oaNode3.getID(), opertaion);
 *
 * List<Node> pcNodes = new ArrayList<>(); pcNodes.add(pc); pcNodes.add(pc1);
 * pcNodes.add(pc2); pcNodes.add(pc3);
 *
 * PolicyService service = new
 * PolicyService("9083f322-c303-4fdb-8894-22a83eae33d7");
 *
 * assertNotEquals(null, service.publishPolicy("crypto-key", "crypto-key",
 * pcNodes)); }
 *
 * @AfterEach public void teardown() {
 *
 * } }
 */