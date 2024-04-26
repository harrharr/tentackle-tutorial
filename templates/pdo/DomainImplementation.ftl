/*
 * Generated by PDO wizard from template ${.current_template_name}.
 */

package ${domainImplPackage};

import ${pdoPackage}.${pdoInterface};
<#if domainPackage != domainImplPackage>
import ${domainPackage}.${domainInterface};
</#if>

import java.io.Serial;
import org.tentackle.domain.AbstractDomainObject;
import org.tentackle.pdo.DomainException;
import org.tentackle.pdo.DomainObjectService;

/**
 * Domain implementation for {@link ${pdoInterface}}.
 */
@DomainObjectService(${pdoInterface}.class)
<#if pdoInheritance != "NONE" && pdoInheritance != "EMBEDDED">
public class ${domainImplementation}<T extends ${pdoInterface}<T>, D extends ${domainImplementation}<T, D>>
       extends ${superDomainImplementation}<T, D>
       implements ${domainInterface}<T> {
<#else>
public class ${domainImplementation}
       extends ${superDomainImplementation}<${pdoInterface}, ${domainImplementation}>
       implements ${domainInterface} {
</#if>

  @Serial
  private static final long serialVersionUID = 1L;

  //<editor-fold defaultstate="collapsed" desc="constructors">

  /**
   * Creates a domain object for a PDO.
   *
   * @param pdo the persistent domain object
   */
<#if pdoInheritance != "NONE" && pdoInheritance != "EMBEDDED">
  public ${domainImplementation}(T pdo) {
<#else>
  public ${domainImplementation}(${pdoInterface} pdo) {
</#if>
    super(pdo);
  }

  /**
   * Creates a domain object.
   */
  public ${domainImplementation}() {
    super();
  }

  //</editor-fold>

  // @wurblet domainMethods DomainMethods

}
