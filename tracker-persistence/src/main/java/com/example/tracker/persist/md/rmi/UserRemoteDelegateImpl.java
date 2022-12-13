/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.persist.md.rmi;

import com.example.tracker.pdo.md.User;
import com.example.tracker.persist.md.UserPersistenceImpl;

import org.tentackle.dbms.rmi.RemoteDbSessionImpl;

import java.rmi.RemoteException;

/**
 * Remote delegate implementation for {@link UserPersistenceImpl}.
 */
public class UserRemoteDelegateImpl
       extends OrgUnitRemoteDelegateImpl<User,UserPersistenceImpl>
       implements UserRemoteDelegate {

  /**
   * Creates the remote delegate for {@link UserPersistenceImpl}.
   *
   * @param session the RMI session
   * @param persistenceClass the persistence implementation class
   * @param pdoClass the pdo interface class
   */
  public UserRemoteDelegateImpl(RemoteDbSessionImpl session, Class<UserPersistenceImpl> persistenceClass, Class<User> pdoClass) {
    super(session, persistenceClass, pdoClass);
  }

  // @wurblet inclrmi Include --missingok .$classname/methods

  //<editor-fold defaultstate="collapsed" desc="code 'inclrmi' generated by wurblet Include">//GEN-BEGIN:inclrmi


  @Override
  public String selectPasswordHash(User obj) throws RemoteException {
    try {
      obj.setSession(getSession());
      return obj.selectPasswordHash();
    }
    catch (Exception e) {
      throw createException(e);
    }
  }

  @Override
  public void updatePasswordHash(String password, User obj) throws RemoteException {
    try {
      obj.setSession(getSession());
      obj.updatePasswordHash(password);
    }
    catch (Exception e) {
      throw createException(e);
    }
  }

  //</editor-fold>//GEN-END:inclrmi

}
