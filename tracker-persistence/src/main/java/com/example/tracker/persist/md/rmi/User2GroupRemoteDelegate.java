/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.persist.md.rmi;

import com.example.tracker.pdo.md.User2Group;
import com.example.tracker.persist.md.User2GroupPersistenceImpl;

import org.tentackle.misc.TrackedList;
import org.tentackle.pdo.DomainContext;

import java.rmi.RemoteException;

/**
 * Remote delegate for {@link User2GroupPersistenceImpl}.
 */
public interface User2GroupRemoteDelegate
       extends AbstractPersistentMasterDataRemoteDelegate<User2Group,User2GroupPersistenceImpl> {

  // @wurblet inclrmi Include --missingok .$classname/methods

  //<editor-fold defaultstate="collapsed" desc="code 'inclrmi' generated by wurblet Include">//GEN-BEGIN:inclrmi

  TrackedList<User2Group> selectByUserGroupId(DomainContext context, long userGroupId) throws RemoteException;
  TrackedList<User2Group> selectByUserId(DomainContext context, long userId) throws RemoteException;

  //</editor-fold>//GEN-END:inclrmi

}
