/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.domain.md;

import com.example.tracker.pdo.md.OrgUnit;
import com.example.tracker.pdo.md.domain.OrgUnitDomain;

import org.tentackle.domain.AbstractDomainObject;
import org.tentackle.pdo.DomainObjectService;

/**
 * Domain implementation for OrgUnit.
 *
 * @param <T> the PDO class
 * @param <D> the domain object class
 */
@DomainObjectService(OrgUnit.class)
public class OrgUnitDomainImpl<T extends OrgUnit<T>, D extends OrgUnitDomainImpl<T, D>>
        extends AbstractDomainObject<T, D> implements OrgUnitDomain<T> {

  /** serial version UID. */
  private static final long serialVersionUID = 1L;

  //<editor-fold defaultstate="collapsed" desc="constructors">

  /**
   * Creates a domain object for a PDO.
   *
   * @param pdo the persistent domain object
   */
  public OrgUnitDomainImpl(T pdo) {
    super(pdo);
  }

  /**
   * Creates a domain object.
   */
  public OrgUnitDomainImpl() {
    super();
  }

  //</editor-fold>

  // @wurblet domainMethods DomainMethods

  //<editor-fold defaultstate="collapsed" desc="code 'domainMethods' generated by wurblet DomainMethods">//GEN-BEGIN:domainMethods


  @Override
  public boolean isUniqueDomainKeyProvided() {
    return true;
  }

  @Override
  public Class<?> getUniqueDomainKeyType() {
    return String.class;
  }

  @Override
  public T findByUniqueDomainKey(Object domainKey) {
    return on().selectByUniqueDomainKey((String) domainKey);
  }

  @Override
  public String getUniqueDomainKey() {
    return me().getName();
  }

  @Override
  public void setUniqueDomainKey(Object domainKey) {
    me().setName((String) domainKey);
  }

  //</editor-fold>//GEN-END:domainMethods
}
