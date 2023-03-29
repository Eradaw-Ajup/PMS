package gov.nist.csd.pm.pdp.prohibitions;

import java.util.List;

import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.model.Prohibition;

public interface ProhibitionMachine {
	/**
	 * Create a new prohibition.
	 *
	 * @param prohibition The prohibition to be created.
	 * @return id of the prohibition created.
	 * @throws PMException if there is an error creating a prohibition.
	 */
	int add(Prohibition prohibition) throws PIPException;

	/**
	 * Get a list of all prohibitions
	 *
	 * @return a list of all prohibitions
	 * @throws PMException if there is an error getting the prohibitions.
	 */
	List<Prohibition> getAll() throws PIPException;

	/**
	 * Retrieve a Prohibition and return the Object representing it.
	 *
	 * @param prohibitionId The id of the Prohibition to retrieve.
	 * @return the Prohibition with the given id.
	 * @throws PMException if there is an error getting the prohibition with the
	 *                     given id.
	 */
	Prohibition get(int prohibitionId) throws PIPException;

	/**
	 * Get all of the prohibitions a given entity is the direct subject of. The
	 * subject can be a user, user attribute, or process.
	 *
	 * @param subject the name of the subject to get the prohibitions for.
	 * @return The list of prohibitions the given entity is the subject of.
	 */
	List<Prohibition> getProhibitionsFor(String subject) throws PIPException;

	/**
	 * Update the prohibition with the given prohibitionId. Prohibition names cannot
	 * be updated.
	 *
	 * @param prohibitionId the name of the prohibition to update.
	 * @param prohibition   The prohibition to update.
	 * @throws PMException if there is an error updating the prohibition.
	 */
	void update(int prohibitionId, Prohibition prohibition) throws PIPException;

	/**
	 * Delete the prohibition, and remove it from the data structure.
	 *
	 * @param prohibitionId The Id of the prohibition to delete.
	 * @throws PMException if there is an error deleting the prohibition.
	 */
	void delete(int prohibitionId) throws PIPException;
}
