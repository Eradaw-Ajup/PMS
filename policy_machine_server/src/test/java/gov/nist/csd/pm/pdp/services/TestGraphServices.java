/*
 * package gov.nist.csd.pm.pdp.services;
 *
 * import static gov.nist.csd.pm.common.constants.Operations.ASSIGN; import
 * static gov.nist.csd.pm.common.constants.Operations.ASSIGN_TO; import static
 * gov.nist.csd.pm.common.constants.Operations.ASSOCIATE; import static
 * gov.nist.csd.pm.common.constants.Operations.DEASSIGN; import static
 * gov.nist.csd.pm.common.constants.Operations.DEASSIGN_FROM; import static
 * gov.nist.csd.pm.common.constants.Operations.DELETE_NODE; import static
 * gov.nist.csd.pm.common.constants.Operations.DISASSOCIATE; import static
 * gov.nist.csd.pm.common.constants.Operations.UPDATE_NODE; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.O; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.OA; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.PC; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.U; import static
 * gov.nist.csd.pm.graph.model.nodes.NodeType.UA; import static
 * gov.nist.csd.pm.utils.TestUtils.getDatabaseContext; import static
 * org.junit.jupiter.api.Assertions.assertAll; import static
 * org.junit.jupiter.api.Assertions.assertEquals; import static
 * org.junit.jupiter.api.Assertions.assertFalse; import static
 * org.junit.jupiter.api.Assertions.assertThrows; import static
 * org.junit.jupiter.api.Assertions.assertTrue;
 *
 * import java.io.IOException; import java.util.Arrays; import
 * java.util.HashSet; import java.util.Map; import java.util.Random; import
 * java.util.Set; import java.util.UUID;
 *
 * import org.junit.jupiter.api.AfterEach; import
 * org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Disabled;
 * import org.junit.jupiter.api.Test; import org.junit.jupiter.api.TestInstance;
 * import org.junit.jupiter.api.TestInstance.Lifecycle;
 *
 * import gov.nist.csd.pm.common.constants.NodeStoreType; import
 * gov.nist.csd.pm.common.exceptions.PMGraphException; import
 * gov.nist.csd.pm.common.util.NodeUtils; import
 * gov.nist.csd.pm.exceptions.PMException; import
 * gov.nist.csd.pm.graph.model.nodes.Node; import gov.nist.csd.pm.pap.PAP;
 *
 * @TestInstance(Lifecycle.PER_CLASS)
 *
 * @Disabled public class TestGraphServices {
 *
 * @BeforeEach public void setup() throws PMException, IOException {
 * PAP.getPAP(getDatabaseContext()); }
 *
 * @Test public void createStageNodeTest() throws PMException { GraphService
 * service = new GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 *
 * // check the exception when name or type is null. assertAll(() ->
 * assertThrows(IllegalArgumentException.class, () -> service.createNode(null,
 * null, null, null)), () -> assertThrows(IllegalArgumentException.class, () ->
 * service.createNode(null, OA, null, null)), () ->
 * assertThrows(IllegalArgumentException.class, () ->
 * service.createNode("test_node", null, null, null))); // create node Node
 * newNode = service.createNode("test", UA, null, NodeStoreType.SN);
 * assertTrue(newNode.getID() != 0, "node does not created"); }
 *
 * @Test public void updateStageNodeTest() throws PMException { GraphService
 * service = new GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b"); // check
 * exception when node id is null and node does not exist assertAll( () ->
 * assertThrows(IllegalArgumentException.class, () -> service.updateNode(0,
 * "update_node_no_id", null, null)), () -> assertThrows(PMException.class, ()
 * -> service.updateNode(new Random().nextLong(), "updated_name",
 * NodeUtils.toProperties("updatedKey", "updatedValue"), null)));
 *
 * service.updateNode(1, "updateNode", NodeUtils.toProperties("updatedKey",
 * "updatedValue"), NodeStoreType.SN); Node node = service.getNode(1);
 * assertEquals(node.getName(), "updateNode", "node name does not match");
 * assertEquals(node.getProperties(), NodeUtils.toProperties("updatedKey",
 * "updatedValue"), "properties do not match"); }
 *
 * @Test
 *
 * @Disabled public void deleteStageNodeTest() throws PMException { GraphService
 * service = new GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 * service.deleteNode(1, NodeStoreType.SN);
 * assertFalse(PAP.getPAP().getGraphPAP().exists(1)); }
 *
 * @Test public void createAssignmentTest() throws PMException { GraphService
 * service = new GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 *
 * Node pc = service.createNode("pc", PC, null, NodeStoreType.SN); Node ua =
 * service.createNode("ua", UA, null, NodeStoreType.SN); Node u =
 * service.createNode("u", U, null, NodeStoreType.SN);
 *
 * assertAll(() -> assertThrows(IllegalArgumentException.class, () ->
 * service.stageNodeAssign(0, 0)), () ->
 * assertThrows(IllegalArgumentException.class, () ->
 * service.stageNodeAssign(123, 0)), () -> assertThrows(PMGraphException.class,
 * () -> service.stageNodeAssign(new Random().nextLong(), new
 * Random().nextLong())), () -> assertThrows(PMGraphException.class, () ->
 * service.stageNodeAssign(pc.getID(), new Random().nextLong())));
 *
 * assertThrows(PMException.class, () -> service.stageNodeAssign(pc.getID(),
 * ua.getID()));
 *
 * service.stageNodeAssign(ua.getID(), pc.getID());
 * service.stageNodeAssign(u.getID(), ua.getID());
 *
 * }
 *
 * @Test public void deassignStageNodeTest() throws PMException { GraphService
 * service = new GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 *
 * Node pc = service.createNode("pc", PC, null, NodeStoreType.SN); Node ua =
 * service.createNode("ua", UA, null, NodeStoreType.SN);
 *
 * assertAll(() -> assertThrows(IllegalArgumentException.class, () ->
 * service.deleteStageAssignment(0, 0)), () ->
 * assertThrows(IllegalArgumentException.class, () ->
 * service.deleteStageAssignment(123, 0)), () ->
 * assertThrows(PMGraphException.class, () -> service.deleteStageAssignment(new
 * Random().nextLong(), new Random().nextLong())), () ->
 * assertThrows(PMGraphException.class, () ->
 * service.deleteStageAssignment(pc.getID(), new Random().nextLong())));
 *
 * service.deleteStageAssignment(ua.getID(), pc.getID());
 * assertTrue(PAP.getPAP().getGraphPAP().getChildren(pc.getID()).isEmpty());
 *
 * }
 *
 * @Test public void createStageAssociationTest() throws PMException {
 * GraphService service = new
 * GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 *
 * Node pc = service.createNode("pc", PC, null, NodeStoreType.SN); Node ua =
 * service.createNode("ua", UA, null, NodeStoreType.SN); Node oa =
 * service.createNode("oa", OA, null, NodeStoreType.SN);
 *
 * assertAll(() -> assertThrows(IllegalArgumentException.class, () ->
 * service.stageNodeAssociate(0, 0, null)), () ->
 * assertThrows(IllegalArgumentException.class, () ->
 * service.stageNodeAssociate(123, 0, null)), () ->
 * assertThrows(PMGraphException.class, () -> service.stageNodeAssociate(new
 * Random().nextLong(), new Random().nextLong(), null)), () ->
 * assertThrows(PMGraphException.class, () ->
 * service.stageNodeAssociate(pc.getID(), new Random().nextLong(), null)));
 *
 * service.stageNodeAssociate(ua.getID(), oa.getID(), new
 * HashSet<>(Arrays.asList("r")));
 *
 * assertTrue(PAP.getPAP().getGraphPAP().getSourceAssociations(ua.getID()).
 * containsKey(oa.getID()));
 * assertTrue(PAP.getPAP().getGraphPAP().getSourceAssociations(ua.getID()).get(
 * oa.getID()).contains("r")); service.stageNodeAssociate(ua.getID(),
 * oa.getID(), new HashSet<>(Arrays.asList("w")));
 * assertTrue(PAP.getPAP().getGraphPAP().getSourceAssociations(ua.getID()).get(
 * oa.getID()).contains("w"));
 *
 * }
 *
 * @Test public void dissociateStageNodeTest() throws PMException { GraphService
 * service = new GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b"); Node pc =
 * service.createNode("pc", PC, null, NodeStoreType.SN); Node ua =
 * service.createNode("ua", UA, null, NodeStoreType.SN); Node oa =
 * service.createNode("oa", OA, null, NodeStoreType.SN);
 *
 * assertAll(() -> assertThrows(IllegalArgumentException.class, () ->
 * service.deleteStageAssociation(0, 0)), () ->
 * assertThrows(IllegalArgumentException.class, () ->
 * service.deleteStageAssociation(123, 0)), () ->
 * assertThrows(PMGraphException.class, () -> service.deleteStageAssociation(new
 * Random().nextLong(), new Random().nextLong())), () ->
 * assertThrows(PMGraphException.class, () ->
 * service.deleteStageAssociation(pc.getID(), new Random().nextLong())));
 *
 * // dissociate ua and oa service.deleteStageAssociation(ua.getID(),
 * oa.getID());
 * assertFalse(PAP.getPAP().getGraphPAP().getSourceAssociations(ua.getID()).
 * containsKey(oa.getID())); }
 *
 * @Test public void getChildrenTest() throws PMException { GraphService service
 * = new GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 *
 * Node o = service.createNode("pc", O, null, NodeStoreType.SN); Node ua =
 * service.createNode("ua", UA, null, NodeStoreType.SN); Node oa =
 * service.createNode("oa", OA, null, NodeStoreType.SN);
 *
 * assertThrows(PMException.class, () -> service.getChildren(new
 * Random().nextLong()));
 *
 * Set<Node> children = service.getChildren(oa.getID()); assertEquals(1,
 * children.size()); assertEquals(children.iterator().next().getID(),
 * o.getID()); }
 *
 * @Test public void getNode() throws PMException { GraphService service = new
 * GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b"); Node pc =
 * service.createNode("pc", PC, null, NodeStoreType.SN); Node ua =
 * service.createNode("ua", UA, null, NodeStoreType.SN); Node oa =
 * service.createNode("oa", OA, null, NodeStoreType.SN);
 *
 * assertThrows(PMException.class, () -> service.getParents(new
 * Random().nextLong()));
 *
 * Node node = service.getNode(pc.getID()); assertTrue(node != null,
 * "return null"); }
 *
 * @Test public void getParentsTest() throws PMException { GraphService service
 * = new GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b"); Node oa =
 * service.createNode("ua", UA, null, NodeStoreType.SN); Node o =
 * service.createNode("o", O, null, NodeStoreType.SN);
 * assertThrows(PMException.class, () -> service.getParents(new
 * Random().nextLong()));
 *
 * Set<Node> parents = service.getParents(o.getID()); assertEquals(1,
 * parents.size()); assertEquals(parents.iterator().next().getID(), oa.getID());
 * }
 *
 * @Test public void getSourceAssociationsTest() throws PMException {
 * GraphService service = new
 * GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 *
 * Node ua = service.createNode("ua", UA, null, NodeStoreType.SN); Node oa =
 * service.createNode("oa", OA, null, NodeStoreType.SN);
 * assertThrows(PMException.class, () -> service.getSourceAssociations(new
 * Random().nextLong())); service.stageNodeAssociate(ua.getID(), oa.getID(), new
 * HashSet<>(Arrays.asList("r")));
 *
 * Map<Long, Set<String>> assocs = service.getSourceAssociations(ua.getID());
 * assertTrue(assocs.containsKey(oa.getID()));
 * assertTrue(assocs.get(ua.getID()).contains("r")); }
 *
 * @Test public void getTargetAssociationsTest() throws PMException {
 * GraphService service = new
 * GraphService("b50eb672-5e50-4f7f-8354-c7ba3423d41b");
 * assertThrows(PMException.class, () -> service.getTargetAssociations(new
 * Random().nextLong()));
 *
 * // Map<Long, Set<String>> assocs =
 * service.getTargetAssociations(PAP.getPAP().getSuperOA().getID()); //
 * assertTrue(assocs.containsKey(PAP.getPAP().getSuperUA2().getID())); //
 * assertTrue(assocs.get(PAP.getPAP().getSuperUA2().getID()).contains("*")); }
 *
 * @AfterEach void teardown() throws PMException { Set<Node> nodes =
 * PAP.getPAP().getGraphPAP().search(null, null,
 * NodeUtils.toProperties("owner","b50eb672-5e50-4f7f-8354-c7ba3423d41b")); for
 * (Node node : nodes) { PAP.getPAP().getGraphPAP().deleteNode(node.getID()); }
 * } }
 */