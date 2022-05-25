/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.pdo.md.persist;

import com.example.tracker.pdo.md.OrgUnit;

import org.tentackle.bind.Bindable;
import org.tentackle.misc.ShortLongText;
import org.tentackle.pdo.DomainKey;
import org.tentackle.pdo.PersistentObject;
import org.tentackle.session.Persistent;
import org.tentackle.validate.validator.NotNull;


/**
 * Persistence interface for OrgUnit.
 *
 * @param <T> the orgunit type
 */
public interface OrgUnitPersistence<T extends OrgUnit<T>> extends PersistentObject<T>, ShortLongText {

  // @wurblet attributeNames AttributeNames

  //<editor-fold defaultstate="collapsed" desc="code 'attributeNames' generated by wurblet AttributeNames">//GEN-BEGIN:attributeNames


  /** attribute name for database column 'name'. */
  String AN_NAME = "name";

  /** attribute name for database column 'ocomment'. */
  String AN_COMMENT = "comment";

  //</editor-fold>//GEN-END:attributeNames


  // @wurblet columnLenghts ColumnLengths

  //<editor-fold defaultstate="collapsed" desc="code 'columnLenghts' generated by wurblet ColumnLengths">//GEN-BEGIN:columnLenghts


  /** maximum number of characters for 'name'. */
  int CL_NAME = 20;

  /** maximum number of characters for 'comment'. */
  int CL_COMMENT = 256;

  //</editor-fold>//GEN-END:columnLenghts


  // @wurblet methods Methods

  //<editor-fold defaultstate="collapsed" desc="code 'methods' generated by wurblet Methods">//GEN-BEGIN:methods


  /**
   * Gets the attribute name.
   *
   * @return short name
   */
  @Persistent(ordinal=5, comment="short name")
  @NotNull(message="{ @('please enter the name') }")
  @Bindable(options="UC,MAXCOLS=20")
  @DomainKey
  String getName();

  /**
   * Sets the attribute name.
   *
   * @param name short name
   */
  @Bindable
  void setName(String name);

  /**
   * Gets the attribute comment.
   *
   * @return optional comment
   */
  @Persistent(ordinal=6, comment="optional comment")
  @Bindable(options="MAXCOLS=256")
  String getComment();

  /**
   * Sets the attribute comment.
   *
   * @param comment optional comment
   */
  @Bindable
  void setComment(String comment);

  /**
   * Selects OrgUnit by its unique domain key.
   *
   * @param name short name
   * @return the OrgUnit, null if no such PDO
   */
  T selectByUniqueDomainKey(String name);

  //</editor-fold>//GEN-END:methods


  // @wurblet relations Relations

//<editor-fold defaultstate="collapsed" desc="code 'relations' generated by wurblet Relations">//GEN-BEGIN:relations


//</editor-fold>//GEN-END:relations

}
