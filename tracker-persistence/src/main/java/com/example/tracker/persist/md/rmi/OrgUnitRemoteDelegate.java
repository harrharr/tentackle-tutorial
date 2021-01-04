/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.persist.md.rmi;

import com.example.tracker.pdo.md.OrgUnit;
import com.example.tracker.persist.md.OrgUnitPersistenceImpl;
import com.example.tracker.persist.rmi.RemoteSecurable;

import org.tentackle.pdo.DomainContext;

import java.rmi.RemoteException;


/**
 * Remote delegate for {@link OrgUnitPersistenceImpl}.
 *
 * @param <T> the pdo interface
 * @param <P> the persistence implementation
 */
public interface OrgUnitRemoteDelegate<T extends OrgUnit<T>, P extends OrgUnitPersistenceImpl<T,P>>
       extends AbstractPersistentMasterDataRemoteDelegate<T,P>, RemoteSecurable {

  // @wurblet inclrmi Include --missingok .$classname/methods

  //<editor-fold defaultstate="collapsed" desc="code 'inclrmi' generated by wurblet Include">//GEN-BEGIN:inclrmi

  T selectByUniqueDomainKey(DomainContext context, String name) throws RemoteException;

  //</editor-fold>//GEN-END:inclrmi


}
