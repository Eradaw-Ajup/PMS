package gov.nist.csd.pm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MNode;
import gov.nist.csd.pm.pdp.services.GraphService;

public class App {
	private static GraphService graphService ;
	
	
    public static void main(String[] args) throws PMException {
    	graphService = new GraphService();
//    	System.out.println("BEFORE");
//    	Set <MNode> node = getNode();
//        node.forEach(s -> System.out.println(s));
//        System.out.println("------------------------------");
//        
//        System.out.println();
////        create();
////        update();
////        delete();
//        System.out.println("AFTER");
//        node = getNode();
//        node.forEach(s -> System.out.println(s));
//        System.out.println("------------------------------");
    	
    	System.out.println("BEFORE");
    	Set <MAssignment> node = getAssignments();
        node.forEach(s -> System.out.println(s));
        System.out.println("------------------------------");
        
        System.out.println();
//        createAssignment();
//        updateAssignment();
//        deleteAssignment();
        System.out.println("AFTER");
    	node = getAssignments();
        node.forEach(s -> System.out.println(s));
        System.out.println("------------------------------");
    	
    }
   
    private static void updateAssignment() throws PMException {
		// TODO Auto-generated method stub
		graphService.updateAssignment(2, 2, 1, "NORMAL");
	}

	private static void deleteAssignment() throws PMException {
		// TODO Auto-generated method stub
		graphService.deassign(2, 2, 1);
	}

	private static void createAssignment() throws PMException {
		// TODO Auto-generated method stub
		graphService.assign(2, 2, 1);
	}

	public static void create() throws PMException {
    	Map <String, String> mp = new HashMap<String, String>();
    	mp.put("K1", "V1");
        MNode node = graphService.createNode("Node-2", "OA", mp, "NORMAL", 1, 3, 2);  

    }
    public static void update() throws PMException {
    	MNode node = graphService.getNode(1);
    	System.out.println(node);
    	graphService.updateNode(2, "node68882", node.getProperties(), "NORMAL", 1);
    }
    public static Set<MNode> getNode() throws PMException {
    	return graphService.getAllNodes();
    }
    public static void delete() throws PMException {
    	graphService.deleteNode(3);
    }

    
    public static Set<MAssignment> getAssignments() throws PMException {
//    	get assignments with the specified policy id
//    	System.out.println(graphService.getAssignment(1));
    	return graphService.getAssignments(2);
    }

}