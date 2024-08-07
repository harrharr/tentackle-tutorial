/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.persist.md.rmi;

import com.example.tracker.pdo.MasterData;
import com.example.tracker.persist.md.AbstractPersistentMasterData;

import org.tentackle.dbms.rmi.RemoteDbSessionImpl;
import org.tentackle.persist.rmi.AbstractPersistentObjectRemoteDelegateImpl;

/**
 * Base implementation for master data remote delegates.
 *
 * @param <T> the {@code PersistentDomainObject} class
 * @param <P> the {@code AbstractPersistenObject} class (persistence implementation)
 */
public class AbstractPersistentMasterDataRemoteDelegateImpl<T extends MasterData<T>,
                                                            P extends AbstractPersistentMasterData<T,P>>
       extends AbstractPersistentObjectRemoteDelegateImpl<T,P> {

  /**
   * Creates a delegate on the serverSession socket.
   *
   * @param serverSession the RMI serverSession
   * @param persistenceClass the subclass of AbstractDbObject
   * @param pdoClass the PDO class (interface)
   */
  public AbstractPersistentMasterDataRemoteDelegateImpl(RemoteDbSessionImpl serverSession,
                                                        Class<P> persistenceClass, Class<T> pdoClass) {

    super(serverSession, persistenceClass, pdoClass);
  }
}
