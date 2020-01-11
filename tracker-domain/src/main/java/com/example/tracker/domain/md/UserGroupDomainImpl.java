/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.domain.md;

import com.example.tracker.pdo.md.UserGroup;
import com.example.tracker.pdo.md.domain.UserGroupDomain;

import org.tentackle.pdo.DomainObjectService;

/**
 * Domain implementation for UserGroup.
 */
@DomainObjectService(UserGroup.class)
public class UserGroupDomainImpl extends OrgUnitDomainImpl<UserGroup, UserGroupDomainImpl> implements UserGroupDomain {

  /** serial version UID. */
  private static final long serialVersionUID = 1L;

  //<editor-fold defaultstate="collapsed" desc="constructors">

  /**
   * Creates a domain object for a PDO.
   *
   * @param pdo the persistent domain object
   */
  public UserGroupDomainImpl(UserGroup pdo) {
    super(pdo);
  }

  /**
   * Creates a domain object.
   */
  public UserGroupDomainImpl() {
    super();
  }

  //</editor-fold>

  // @wurblet domainMethods DomainMethods

//<editor-fold defaultstate="collapsed" desc="code 'domainMethods' generated by wurblet DomainMethods">//GEN-BEGIN:domainMethods


//</editor-fold>//GEN-END:domainMethods
}
