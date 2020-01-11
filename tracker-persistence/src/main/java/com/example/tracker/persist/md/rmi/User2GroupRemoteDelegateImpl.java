/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.persist.md.rmi;

import com.example.tracker.pdo.md.User2Group;
import com.example.tracker.persist.md.User2GroupPersistenceImpl;

import org.tentackle.dbms.rmi.RemoteDbSessionImpl;
import org.tentackle.misc.TrackedList;
import org.tentackle.pdo.DomainContext;

import java.rmi.RemoteException;


/**
 * Remote delegate implementation for {@link User2GroupPersistenceImpl}.
 */
public class User2GroupRemoteDelegateImpl
       extends AbstractPersistentMasterDataRemoteDelegateImpl<User2Group,User2GroupPersistenceImpl>
       implements User2GroupRemoteDelegate {


  /**
   * Creates the remote delegate for {@link User2GroupPersistenceImpl}.
   *
   * @param session the RMI session
   * @param persistenceClass the persistence implementation class
   * @param pdoClass the pdo interface class
   */
  public User2GroupRemoteDelegateImpl(RemoteDbSessionImpl session, Class<User2GroupPersistenceImpl> persistenceClass, Class<User2Group> pdoClass) {
    super(session, persistenceClass, pdoClass);
  }

  // @wurblet inclrmi Include --missingok .$classname/methods

  //<editor-fold defaultstate="collapsed" desc="code 'inclrmi' generated by wurblet Include">//GEN-BEGIN:inclrmi


  @Override
  public TrackedList<User2Group> selectByUserGroupId(DomainContext context, long userGroupId) throws RemoteException {
    try {
      setDomainContext(context);
      return dbObject.selectByUserGroupId(userGroupId);
    }
    catch (RuntimeException e) {
      throw createException(e);
    }
  }

  @Override
  public TrackedList<User2Group> selectByUserId(DomainContext context, long userId) throws RemoteException {
    try {
      setDomainContext(context);
      return dbObject.selectByUserId(userId);
    }
    catch (RuntimeException e) {
      throw createException(e);
    }
  }

  //</editor-fold>//GEN-END:inclrmi

}
