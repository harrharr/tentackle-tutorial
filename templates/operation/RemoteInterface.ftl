/*
 * Generated by wurbelizer from template ${.current_template_name}.
 */

package ${remotePackage};

import ${operationPackage}.${operationInterface};
<#if persistenceImplPackage != remotePackage>
import ${persistenceImplPackage}.${persistenceImplementation};
</#if>
<#if superPackage != remotePackage>
import ${superPackage}.${superInterface};
</#if>

import java.rmi.RemoteException;
import java.util.List;
import org.tentackle.common.BMoney;
import org.tentackle.common.Binary;
import org.tentackle.common.DMoney;
import org.tentackle.common.Date;
import org.tentackle.common.Time;
import org.tentackle.common.Timestamp;
import org.tentackle.misc.TrackedList;
import org.tentackle.pdo.DomainContext;

/**
 * Remote delegate interface for {@link ${persistenceImplementation}}.
<#if abstractOperation == "true">
 *
 * @param <T> the pdo interface
 * @param <P> the persistence implementation
</#if>
 */
<#if abstractOperation == "true">
public interface ${remoteInterface}<T extends ${operationInterface}<T>, P extends ${persistenceImplementation}<T, P>>
       extends ${superInterface}<T, P> {
<#else>
public interface ${remoteInterface}
       extends ${superInterface}<${operationInterface}, ${persistenceImplementation}> {
</#if>

  // @wurblet inclrmi Include --missingok .$classname/methods

}
