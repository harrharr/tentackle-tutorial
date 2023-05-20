/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.persist.td;

import com.example.tracker.common.Constants;
import com.example.tracker.pdo.MessageType;
import com.example.tracker.pdo.md.OrgUnit;
import com.example.tracker.pdo.td.Message;
import com.example.tracker.pdo.td.persist.MessagePersistence;
import com.example.tracker.persist.AbstractPersistentTransactionData;
import com.example.tracker.persist.td.rmi.MessageRemoteDelegate;

import org.tentackle.common.Freezable;
import org.tentackle.common.RemoteMethod;
import org.tentackle.common.StringHelper;
import org.tentackle.common.Timestamp;
import org.tentackle.dbms.PreparedStatementWrapper;
import org.tentackle.dbms.Query;
import org.tentackle.dbms.ResultSetWrapper;
import org.tentackle.dbms.StatementId;
import org.tentackle.misc.TrackedList;
import org.tentackle.ns.NumberSourceFactory;
import org.tentackle.pdo.DomainContext;
import org.tentackle.pdo.PersistentObjectService;
import org.tentackle.persist.NormText;
import org.tentackle.persist.PersistentObjectClassVariables;
import org.tentackle.session.PersistenceException;
import org.tentackle.session.Session;
import org.tentackle.sql.Backend;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Persistence implementation for Message.
 */
@PersistentObjectService(Message.class)
public class MessagePersistenceImpl extends AbstractPersistentTransactionData<Message, MessagePersistenceImpl> implements MessagePersistence {

  @Serial
  private static final long serialVersionUID = 1L;

  // @wurblet classVariables ClassVariables

  //<editor-fold defaultstate="collapsed" desc="code 'classVariables' generated by wurblet ClassVariables">//GEN-BEGIN:classVariables

  /** Variables common to all instances of MessagePersistenceImpl. */
  public static final PersistentObjectClassVariables<Message, MessagePersistenceImpl> CLASSVARIABLES =
            PersistentObjectClassVariables.create(
                    Message.class,
                    MessagePersistenceImpl.class,
                    "m",
                    null,
                    null);

  @Override
  public PersistentObjectClassVariables<Message, MessagePersistenceImpl> getClassVariables() {
    return CLASSVARIABLES;
  }

  //</editor-fold>//GEN-END:classVariables


  // @wurblet columnNames ColumnNames

  //<editor-fold defaultstate="collapsed" desc="code 'columnNames' generated by wurblet ColumnNames">//GEN-BEGIN:columnNames


  /** database column name for 'messageNumber'. */
  public static final String CN_MESSAGENUMBER = "message_no";

  /** database column name for 'when'. */
  public static final String CN_WHEN = "mwhen";

  /** database column name for 'messageType'. */
  public static final String CN_MESSAGETYPE = "mtype";

  /** database column name for 'refersToClassId'. */
  public static final String CN_REFERSTOCLASSID = "ref_class_id";

  /** database column name for 'refersToPdoId'. */
  public static final String CN_REFERSTOPDOID = "ref_pdo_id";

  /** database column name for 'refersToText'. */
  public static final String CN_REFERSTOTEXT = "ref_txt";

  /** database column name for 'orgUnitId'. */
  public static final String CN_ORGUNITID = "orgunit_id";

  /** database column name for 'text'. */
  public static final String CN_TEXT = "mtext";

  //</editor-fold>//GEN-END:columnNames


  // @wurblet declare Declare

  //<editor-fold defaultstate="collapsed" desc="code 'declare' generated by wurblet Declare">//GEN-BEGIN:declare


  /** message number. */
  private String messageNumber;

  /** timestamp of message. */
  private Timestamp when;

  /** the message type. */
  private MessageType messageType;

  /** class id if message refers to a PDO. */
  private Integer refersToClassId;

  /** object id if message refers to a PDO. */
  private Long refersToPdoId;

  /** refers text or toString of refersToPdo. */
  private String refersToText;

  /** ID of the responsible orgunit. */
  private Long orgUnitId;

  /** message text. */
  private String text;

  //</editor-fold>//GEN-END:declare


  //<editor-fold defaultstate="collapsed" desc="constructors">

  /**
   * Creates a persistent object for a PDO.
   *
   * @param pdo the persistent domain object
   * @param context the domain context
   */
  public MessagePersistenceImpl(Message pdo, DomainContext context) {
    super(pdo, context);
  }

  /**
   * Creates a persistent object for a PDO.
   *
   * @param pdo the persistent domain object
   * @param session the session
   */
  public MessagePersistenceImpl(Message pdo, Session session) {
    super(pdo, session);
  }

  /**
   * Creates a persistent object for a PDO.
   *
   * @param pdo the persistent domain object
   */
  public MessagePersistenceImpl(Message pdo) {
    super(pdo);
  }

  /**
   * Creates a persistent object.
   */
  public MessagePersistenceImpl() {
    super();
  }

  //</editor-fold>

  // @wurblet methods MethodsImpl

  //<editor-fold defaultstate="collapsed" desc="code 'methods' generated by wurblet MethodsImpl">//GEN-BEGIN:methods


  @Override
  public MessageRemoteDelegate getRemoteDelegate() {
    return (MessageRemoteDelegate) super.getRemoteDelegate();
  }

  @Override
  public boolean isRootEntity() {
    return true;
  }

  @Override
  public boolean isTableSerialProvided() {
    return true;
  }

  @Override
  public boolean isNormTextProvided() {
    return true;
  }

  @Override
  public boolean isTracked() {
    return true;
  }

  @Override
  public void getFields(ResultSetWrapper rs) {
    super.getFields(rs);
    if (rs.configureSection(CLASSVARIABLES)) {
      rs.configureColumn(CN_NORMTEXT);
      rs.configureColumn(CN_TABLESERIAL);
      rs.configureColumn(CN_MESSAGENUMBER);
      rs.configureColumn(CN_WHEN);
      rs.configureColumn(CN_MESSAGETYPE);
      rs.configureColumn(CN_REFERSTOCLASSID);
      rs.configureColumn(CN_REFERSTOPDOID);
      rs.configureColumn(CN_REFERSTOTEXT);
      rs.configureColumn(CN_ORGUNITID);
      rs.configureColumn(CN_TEXT);
      rs.configureColumn(CN_ID);
      rs.configureColumn(CN_SERIAL);
    }
    setNormText(rs.getString());
    setTableSerial(rs.getLong());
    messageNumber = rs.getString();
    when = rs.getTimestamp();
    messageType = MessageType.toInternal(rs.getString());
    refersToClassId = rs.getInteger();
    refersToPdoId = rs.getALong();
    refersToText = rs.getString();
    orgUnitId = rs.getALong();
    text = rs.getString();
    setId(rs.getLong());
    setSerial(rs.getLong());
  }

  @Override
  public int setFields(PreparedStatementWrapper st) {
    int ndx = super.setFields(st);
    st.setString(++ndx, getNormText());
    st.setLong(++ndx, getTableSerial());
    st.setString(++ndx, messageNumber);
    st.setTimestamp(++ndx, when);
    st.setString(++ndx, StringHelper.trim(messageType == null ? null : messageType.toExternal(), 30));
    st.setInteger(++ndx, refersToClassId);
    st.setLong(++ndx, refersToPdoId);
    st.setString(++ndx, StringHelper.trim(refersToText, 30));
    st.setLong(++ndx, orgUnitId);
    st.setString(++ndx, StringHelper.trim(text, 1024));
    st.setLong(++ndx, getId());
    st.setLong(++ndx, getSerial());
    return ndx;
  }

  @Override
  public String createInsertSql(Backend backend) {
    return Backend.SQL_INSERT_INTO + getTableName() + Backend.SQL_LEFT_PARENTHESIS +
           CN_NORMTEXT + Backend.SQL_COMMA +
           CN_TABLESERIAL + Backend.SQL_COMMA +
           CN_MESSAGENUMBER + Backend.SQL_COMMA +
           CN_WHEN + Backend.SQL_COMMA +
           CN_MESSAGETYPE + Backend.SQL_COMMA +
           CN_REFERSTOCLASSID + Backend.SQL_COMMA +
           CN_REFERSTOPDOID + Backend.SQL_COMMA +
           CN_REFERSTOTEXT + Backend.SQL_COMMA +
           CN_ORGUNITID + Backend.SQL_COMMA +
           CN_TEXT + Backend.SQL_COMMA +
           CN_ID + Backend.SQL_COMMA +
           CN_SERIAL +
           Backend.SQL_INSERT_VALUES +
           Backend.SQL_PAR_COMMA.repeat(11) +
           Backend.SQL_PAR + Backend.SQL_RIGHT_PARENTHESIS;
  }

  @Override
  public String createUpdateSql(Backend backend) {
    return Backend.SQL_UPDATE + getTableName() + Backend.SQL_SET +
           CN_NORMTEXT + Backend.SQL_EQUAL_PAR_COMMA +
           CN_TABLESERIAL + Backend.SQL_EQUAL_PAR_COMMA +
           CN_MESSAGENUMBER + Backend.SQL_EQUAL_PAR_COMMA +
           CN_WHEN + Backend.SQL_EQUAL_PAR_COMMA +
           CN_MESSAGETYPE + Backend.SQL_EQUAL_PAR_COMMA +
           CN_REFERSTOCLASSID + Backend.SQL_EQUAL_PAR_COMMA +
           CN_REFERSTOPDOID + Backend.SQL_EQUAL_PAR_COMMA +
           CN_REFERSTOTEXT + Backend.SQL_EQUAL_PAR_COMMA +
           CN_ORGUNITID + Backend.SQL_EQUAL_PAR_COMMA +
           CN_TEXT + Backend.SQL_EQUAL_PAR_COMMA +
           CN_SERIAL + Backend.SQL_EQUAL + CN_SERIAL + Backend.SQL_PLUS_ONE +
           Backend.SQL_WHERE + CN_ID + Backend.SQL_EQUAL_PAR +
           Backend.SQL_AND + CN_SERIAL + Backend.SQL_EQUAL_PAR;
  }

  @Override
  public String getMessageNumber()    {
    return messageNumber;
  }

  @Override
  public void setMessageNumber(String messageNumber) {
    if (!Objects.equals(this.messageNumber, messageNumber)) {
      setModified(true);
      this.messageNumber = messageNumber;
    }
  }

  @Override
  public Timestamp getWhen()    {
    return when;
  }

  @Override
  public void setWhen(Timestamp when) {
    Timestamp.setUTC(when, false);
    Freezable.freeze(when);
    if (!Objects.equals(this.when, when)) {
      setModified(true);
      this.when = when;
    }
  }

  @Override
  public MessageType getMessageType()    {
    return messageType;
  }

  @Override
  public void setMessageType(MessageType messageType) {
    if (!Objects.equals(this.messageType, messageType)) {
      setModified(true);
      this.messageType = messageType;
    }
  }

  @Override
  public Integer getRefersToClassId()    {
    return refersToClassId;
  }

  @Override
  public void setRefersToClassId(Integer refersToClassId) {
    if (!Objects.equals(this.refersToClassId, refersToClassId)) {
      setModified(true);
      this.refersToClassId = refersToClassId;
    }
  }

  @Override
  public Long getRefersToPdoId()    {
    return refersToPdoId;
  }

  @Override
  public void setRefersToPdoId(Long refersToPdoId) {
    if (!Objects.equals(this.refersToPdoId, refersToPdoId)) {
      setModified(true);
      this.refersToPdoId = refersToPdoId;
    }
  }

  @Override
  public String getRefersToText()    {
    return refersToText;
  }

  @Override
  public void setRefersToText(String refersToText) {
    if (!Objects.equals(this.refersToText, refersToText)) {
      setModified(true);
      this.refersToText = refersToText;
    }
  }

  @Override
  public Long getOrgUnitId()    {
    return orgUnitId;
  }

  @Override
  public void setOrgUnitId(Long orgUnitId) {
    if (!Objects.equals(this.orgUnitId, orgUnitId)) {
      setModified(true);
      this.orgUnitId = orgUnitId;
    }
  }

  @Override
  public String getText()    {
    return text;
  }

  @Override
  public void setText(String text) {
    if (!Objects.equals(this.text, text)) {
      setModified(true);
      this.text = text;
    }
  }

  /**
   * Copies all attributes from a snapshot back to this object.
   *
   * @param snapshot the snapshot object
   */
  protected void revertAttributesToSnapshot(MessagePersistenceImpl snapshot) {
    super.revertAttributesToSnapshot(snapshot);
    messageNumber = snapshot.messageNumber;
    when = snapshot.when;
    messageType = snapshot.messageType;
    refersToClassId = snapshot.refersToClassId;
    refersToPdoId = snapshot.refersToPdoId;
    refersToText = snapshot.refersToText;
    orgUnitId = snapshot.orgUnitId;
    text = snapshot.text;
  }

  @Override
  public StringBuilder createAttributesNormText() {
    StringBuilder buf = super.createAttributesNormText();
    if (buf == null) {
      buf = new StringBuilder();
    }
    buf.append(getMessageNumber()).append(',');
    buf.append(getRefersToText()).append(',');
    buf.append(getText()).append(',');
    return buf;
  }

  // selects by unique domain key
  // @wurblet selectByUniqueDomainKey PdoSelectUnique messageNumber

  //</editor-fold>//GEN-END:methods

  //<editor-fold defaultstate="collapsed" desc="code 'selectByUniqueDomainKey' generated by wurblet PdoSelectUnique/MethodsImpl">//GEN-BEGIN:selectByUniqueDomainKey


  private static final StatementId SELECT_BY_UNIQUE_DOMAIN_KEY_STMT = new StatementId();

  @Override
  public Message selectByUniqueDomainKey(String messageNumber) {
    if (getSession().isRemote())  {
      try {
        Message obj = getRemoteDelegate().selectByUniqueDomainKey(getDomainContext(), messageNumber);
        configureRemoteObject(getDomainContext(), obj);
        return obj;
      }
      catch (RemoteException e) {
        throw PersistenceException.createFromRemoteException(this, e);
      }
    }
    PreparedStatementWrapper st = getPreparedStatement(SELECT_BY_UNIQUE_DOMAIN_KEY_STMT,
      b -> {
        StringBuilder sql = createSelectAllInnerSql(b);
        sql.append(Backend.SQL_AND);
        sql.append(getColumnName(CN_MESSAGENUMBER));
        sql.append(Backend.SQL_EQUAL_PAR);
        b.buildSelectSql(sql, false, 0, 0);
        return sql.toString();
      }
    );
    int ndx = 1;
    st.setString(ndx, messageNumber);
    return executeFirstPdoQuery(st);
  }

  //</editor-fold>//GEN-END:selectByUniqueDomainKey


  // @wurblet relations PdoRelations

  //<editor-fold defaultstate="collapsed" desc="code 'relations' generated by wurblet PdoRelations">//GEN-BEGIN:relations


  @Override
  @SuppressWarnings("unchecked")
  public OrgUnit<?> getOrgUnit()  {
    return orgUnitId == null || orgUnitId == 0 ? null : (OrgUnit<?>) on(OrgUnit.class).selectCached(orgUnitId);
  }

  @Override
  public void setOrgUnit(OrgUnit<?> orgUnit)  {
    setOrgUnitId(orgUnit == null ? null : orgUnit.getId());
  }

  @Override
  public StringBuilder createRelationsNormText() {
    StringBuilder buf = super.createRelationsNormText();
    if (buf == null) {
      buf = new StringBuilder();
    }
    buf.append(getOrgUnit()).append(',');
    return buf;
  }

  // Determines whether any Message references given OrgUnit
  // @wurblet isReferencingOrgUnitByOrgUnitId PdoIsReferencing orgUnitId

  //</editor-fold>//GEN-END:relations

  //<editor-fold defaultstate="collapsed" desc="code 'isReferencingOrgUnitByOrgUnitId' generated by wurblet PdoIsReferencing/PdoRelations">//GEN-BEGIN:isReferencingOrgUnitByOrgUnitId

  public boolean isReferencingOrgUnitByOrgUnitId(Long orgUnitId) {
    if (getSession().isRemote())  {
      try {
        return getRemoteDelegate().isReferencingOrgUnitByOrgUnitId(orgUnitId);
      }
      catch (RemoteException e) {
        throw PersistenceException.createFromRemoteException(getSession(), e);
      }
    }
    PreparedStatementWrapper st = getPreparedStatement(IS_REFERENCING_ORG_UNIT_BY_ORG_UNIT_ID_STMT,
      b -> {
        StringBuilder sql = createSelectIdInnerSql(false);
        sql.append(Backend.SQL_AND);
        sql.append(CN_ORGUNITID);
        sql.append(Backend.SQL_EQUAL_PAR);
        b.buildSelectSql(sql, false, 1, 0);
        return sql.toString();
      }
    );
    int ndx = getBackend().setLeadingSelectParameters(st, 1, 0);
    st.setLong(ndx++, orgUnitId);
    getBackend().setTrailingSelectParameters(st, ndx, 1, 0);
    try (ResultSetWrapper rs = st.executeQuery()) {
      return rs.next();
    }
  }

  private static final StatementId IS_REFERENCING_ORG_UNIT_BY_ORG_UNIT_ID_STMT = new StatementId();

  //</editor-fold>//GEN-END:isReferencingOrgUnitByOrgUnitId



  @RemoteMethod
  @Override
  public String nextMessageNumber() {

    // @wurblet nextMessageNumber RemoteMethod

    //<editor-fold defaultstate="collapsed" desc="code 'nextMessageNumber' generated by wurblet RemoteMethod">//GEN-BEGIN:nextMessageNumber

    if (getSession().isRemote())  {
      try {
        return getRemoteDelegate().nextMessageNumber(getDomainContext());
      }
      catch (RemoteException e) {
        throw PersistenceException.createFromRemoteException(this, e);
      }
    }

    //</editor-fold>//GEN-END:nextMessageNumber


    return Long.toString(NumberSourceFactory.getInstance().getNumberSource(Message.POOL_NAME, Constants.POOL_REALM).popNumber());
  }


  @Override
  @RemoteMethod
  public TrackedList<Message> findBy(
          String messageNumber,
          Timestamp from, Timestamp until,
          MessageType type,
          OrgUnit<?> orgUnit,
          String pattern) {

    // @wurblet findBy RemoteMethod

    //<editor-fold defaultstate="collapsed" desc="code 'findBy' generated by wurblet RemoteMethod">//GEN-BEGIN:findBy

    if (getSession().isRemote())  {
      try {
        TrackedList<Message> list = getRemoteDelegate().findBy(getDomainContext(), messageNumber, from, until, type, orgUnit, pattern);
        configureRemoteObjects(getDomainContext(), list);
        return list;
      }
      catch (RemoteException e) {
        throw PersistenceException.createFromRemoteException(this, e);
      }
    }

    //</editor-fold>//GEN-END:findBy


    Query query = createQuery();

    if (messageNumber != null) {
      query.add(" AND " + getColumnName(CN_MESSAGENUMBER) + "=?", messageNumber);
    }
    if (from != null) {
      query.add(" AND " + getColumnName(CN_WHEN) + ">=?", from);
    }
    if (until != null) {
      query.add(" AND " + getColumnName(CN_WHEN) + "<=?", until);
    }
    if (type != null) {
      query.add(" AND " + getColumnName(CN_MESSAGETYPE) + "=?", type);
    }
    if (orgUnit != null) {
      query.add(" AND " + getColumnName(CN_ORGUNITID) + "=?", orgUnit.getId());
    }
    NormText nt = new NormText(pattern);
    if (!nt.isMatchingAll()) {
      query.add(" AND " + getColumnName(CN_NORMTEXT) + (nt.isInverted() ? " NOT LIKE ?" : " LIKE ?"), nt.getPattern());
    }
    query.add(" ORDER BY " + getTableAlias() + "." + CN_ID);

    return executeQuery(query);
  }

}
