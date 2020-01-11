/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.persist.td.rmi;

import com.example.tracker.pdo.MessageType;
import com.example.tracker.pdo.md.OrgUnit;
import com.example.tracker.pdo.td.Message;
import com.example.tracker.persist.td.MessagePersistenceImpl;

import org.tentackle.common.Timestamp;
import org.tentackle.misc.TrackedList;
import org.tentackle.pdo.DomainContext;

import java.rmi.RemoteException;


/**
 * Remote delegate for {@link MessagePersistenceImpl}.
 */
public interface MessageRemoteDelegate
       extends AbstractPersistentTransactionDataRemoteDelegate<Message,MessagePersistenceImpl> {

  // @wurblet inclrmi Include --missingok .$classname/methods

  //<editor-fold defaultstate="collapsed" desc="code 'inclrmi' generated by wurblet Include">//GEN-BEGIN:inclrmi

  Message selectByUniqueDomainKey(DomainContext context, String messageNumber) throws RemoteException;
  boolean isReferencingOrgUnitByOrgUnitId(Long orgUnitId) throws RemoteException;
  String nextMessageNumber(DomainContext context) throws RemoteException;
  TrackedList<Message> findLatest(DomainContext context, int limit, long id) throws RemoteException;
  TrackedList<Message> findBy(DomainContext context, String messageNumber, Timestamp from, Timestamp until, MessageType type, OrgUnit<?> orgUnit, String pattern) throws RemoteException;

  //</editor-fold>//GEN-END:inclrmi

}
