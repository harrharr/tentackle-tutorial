/*
 * Generated by operation wizard from template ${.current_template_name}.
 */

package ${persistenceImplPackage};

import ${operationPackage}.${operationInterface};
<#if persistencePackage != persistenceImplPackage>
import ${persistencePackage}.${persistenceInterface};
</#if>
<#if remoteEnabled == "true">
import ${remotePackage}.${remoteInterface};
</#if>

import java.io.Serial;
<#if remoteEnabled == "true">
import java.rmi.RemoteException;
</#if>
import org.tentackle.pdo.DomainContext;
import org.tentackle.pdo.PersistentOperationService;
import org.tentackle.persist.AbstractPersistentOperation;
import org.tentackle.persist.PersistentOperationClassVariables;
import org.tentackle.pdo.Operation;
import org.tentackle.session.PersistenceException;
import org.tentackle.session.Session;

/**
 * Persistence implementation for {@link ${operationInterface}}.
 */
@PersistentOperationService(${operationInterface}.class)
<#if abstractOperation == "true">
public class ${persistenceImplementation}<T extends ${operationInterface}<T>, P extends ${persistenceImplementation}<T, P>>
       extends ${superPersistenceImplementation}<T, P>
       implements ${persistenceInterface}<T> {
<#else>
public class ${persistenceImplementation}
       extends ${superPersistenceImplementation}<${operationInterface}, ${persistenceImplementation}>
       implements ${persistenceInterface} {
</#if>

  @Serial
  private static final long serialVersionUID = 1L;

  //<editor-fold defaultstate="collapsed" desc="classvariables">

<#if abstractOperation == "true">
  private static final PersistentOperationClassVariables CLASSVARIABLES =
          new PersistentOperationClassVariables(${operationInterface}.class, ${persistenceImplementation}.class);

  @Override
  public PersistentOperationClassVariables<T, P> getClassVariables() {
    return CLASSVARIABLES;
  }
<#else>
  private static final PersistentOperationClassVariables<${operationInterface}, ${persistenceImplementation}> CLASSVARIABLES =
          new PersistentOperationClassVariables<>(${operationInterface}.class, ${persistenceImplementation}.class);

  @Override
  public PersistentOperationClassVariables<${operationInterface}, ${persistenceImplementation}> getClassVariables() {
    return CLASSVARIABLES;
  }
</#if>

  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="constructors">

  /**
   * Creates a persistent object for an operation.
   *
   * @param operation the operation object
   * @param context the domain context
   */
<#if abstractOperation == "true">
  public ${persistenceImplementation}(T operation, DomainContext context) {
<#else>
  public ${persistenceImplementation}(${operationInterface} operation, DomainContext context) {
</#if>
    super(operation, context);
  }

  /**
   * Creates a persistent object for an operation.
   *
   * @param operation the operation object
   * @param session the session
   */
<#if abstractOperation == "true">
  public ${persistenceImplementation}(T operation, Session session) {
<#else>
  public ${persistenceImplementation}(${operationInterface} operation, Session session) {
</#if>
    super(operation, session);
  }

  /**
   * Creates a persistent object for an operation.
   *
   * @param operation the operation object
   */
<#if abstractOperation == "true">
  public ${persistenceImplementation}(T operation) {
<#else>
  public ${persistenceImplementation}(${operationInterface} operation) {
</#if>
    super(operation);
  }

  /**
   * Creates a persistent object.
   */
  public ${persistenceImplementation}() {
    super();
  }

  //</editor-fold>

<#if remoteEnabled == "true">
  //<editor-fold defaultstate="collapsed" desc="remote">

  <#if abstractOperation == "true">
  @Override
  public ${operationInterface}RemoteDelegate<T,P> getRemoteDelegate() {
    return (${operationInterface}RemoteDelegate<T,P>) super.getRemoteDelegate();
  }
  <#else>
  @Override
  public ${operationInterface}RemoteDelegate getRemoteDelegate() {
    return (${operationInterface}RemoteDelegate) super.getRemoteDelegate();
  }
  </#if>

  //</editor-fold>
</#if>
}
