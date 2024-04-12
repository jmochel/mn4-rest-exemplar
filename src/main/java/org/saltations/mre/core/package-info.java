/**
 * the classes and functionality here deals with the following concepts
 * <dl>
 *   <dt>Core Object</dt>
 *   <dd>An object that contains all of the <em>core</em> content attributes needed to represent the domain concept and
 *   implements a <em>core interface</em> that allows getting and modifying the value of those attributes</dd>
 *   <dt>Entity</dt>
 *   <dd>An object contains all of the <em>core</em> content attributes needed to represent a domain concept AND the
 *   meta data needed to identify and track this particular object (At the very least a single unique ID).
 *   See {@link org.saltations.mre.layer.Entity} interface</dd>
 *   <dt>Entity Mapper</dt>
 *   <dd>Service providing the functionality to map between <em>core objects</em> and corresponding <em>entities</em>
 *   meta data needed to identify and track this particular object (At the very least a single unique ID).
 *   See {@link org.saltations.mre.layer.EntityMapper} interface</dd>
 *   <dt>Entity Repository</dt>
 *   <dd>Service that provides operations for inserting, updating, finding and deleting  <em>entities</em>.
 *   Insertion Is done from a provided <em>core object</em>.
 *   See base class {@link org.saltations.mre.layer.EntityRepoBase}</dd>
 *   <dt>Entity Service</dt>
 *   <dd>Service that provides operations for creating, finding, updating, patching and deleting <em>entities</em>.
 *   Resulting changes are stored to be via an <em>entity repository</em> within transactions.
 *   Creation is done from a provided <em>core object</em>. Patching is done from a provided
 *  <a href="https://datatracker.ietf.org/doc/html/rfc7386">JSON Merge Patch</a>.
 *   See base class {@link org.saltations.mre.layer.EntityServiceBase}</dd>
 *   <dt>Entity Controller</dt>
 *   <dd>Resource controller that exposes basic REST API endpoints for creating, finding, replacing, patching and deleting <em>entities</em>.
 *   Creation is done from a provided <em>core object</em>. Patching is done from a provided
 *  <a href="https://datatracker.ietf.org/doc/html/rfc7386">JSON Merge Patch</a>.
 *   See base class {@link org.saltations.mre.layer.EntityControllerBase}</dd>
 * </dl>
 */
package org.saltations.mre.core;
