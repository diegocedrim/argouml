// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.model;

/**
 * The interface that the ModelImplementation must implement. This is the
 * class finding all helpers and factories.
 */
public interface ModelImplementation {
    /**
     * Get the event pump.
     *
     * @return the current ModelEventPump.
     */
    ModelEventPump getModelEventPump();

    /**
     * Getter for ActivityGraphsFactory.
     *
     * @return the factory
     */
    ActivityGraphsFactory getActivityGraphsFactory();

    /**
     * Getter for the ActivityGraphsHelper.
     *
     * @return the instance of the helper
     */
    ActivityGraphsHelper getActivityGraphsHelper();

    /**
     * Getter for CollaborationsFactory.
     *
     * @return the factory
     */
    CollaborationsFactory getCollaborationsFactory();

    /**
     * Getter for CollaborationsHelper.
     *
     * @return the helper
     */
    CollaborationsHelper getCollaborationsHelper();

    /**
     * Getter for CommonBehaviorFactory.
     *
     * @return the factory
     */
    CommonBehaviorFactory getCommonBehaviorFactory();

    /**
     * Getter for CommonBehaviorHelper.
     *
     * @return the helper
     */
    CommonBehaviorHelper getCommonBehaviorHelper();

    /**
     * Getter for CoreFactory.
     *
     * @return the factory
     */
    CoreFactory getCoreFactory();

    /**
     * Getter for CoreHelper.
     *
     * @return The helper.
     */
    CoreHelper getCoreHelper();

    /**
     * Getter for DataTypesFactory.
     *
     * @return the factory
     */
    DataTypesFactory getDataTypesFactory();

    /**
     * Getter for DataTypesHelper.
     *
     * @return the helper.
     */
    DataTypesHelper getDataTypesHelper();

    /**
     * Getter for ExtensionMechanismsFactory.
     *
     * @return the factory instance.
     */
    ExtensionMechanismsFactory getExtensionMechanismsFactory();

    /**
     * Getter for ExtensionMechanismsHelper.
     *
     * @return the helper
     */
    ExtensionMechanismsHelper getExtensionMechanismsHelper();

    /**
     * Getter for ModelManagementFactory.
     *
     * @return the factory
     */
    ModelManagementFactory getModelManagementFactory();

    /**
     * Getter for ModelManagementHelper.
     *
     * @return The model management helper.
     */
    ModelManagementHelper getModelManagementHelper();

    /**
     * Getter for StateMachinesFactory.
     *
     * @return the factory
     */
    StateMachinesFactory getStateMachinesFactory();

    /**
     * Getter for StateMachinesHelper.
     *
     * @return the helper
     */
    StateMachinesHelper getStateMachinesHelper();

    /**
     * Getter for UmlFactory.
     *
     * @return the factory
     */
    UmlFactory getUmlFactory();

    /**
     * Getter for UmlHelper.
     *
     * @return the helper
     */
    UmlHelper getUmlHelper();

    /**
     * Getter for UseCasesFactory.
     *
     * @return the factory
     */
    UseCasesFactory getUseCasesFactory();

    /**
     * Getter for UseCasesHelper.
     *
     * @return the helper
     */
    UseCasesHelper getUseCasesHelper();
}
