/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.pdo.md;

import com.example.tracker.pdo.md.domain.UserGroupDomain;
import com.example.tracker.pdo.md.persist.UserGroupPersistence;

import org.tentackle.pdo.Plural;
import org.tentackle.pdo.Singular;
import org.tentackle.session.ClassId;
import org.tentackle.session.TableName;

/*
 * @{
 * tablename = md.usergroups
 * classid   = 1002
 * mapping   = $model/$tablename.map
 * @}
 */

/*
 * @> $mapping
 *
 * # User Group
 * name := $classname
 * table := $tablename
 * id := $classid
 * extends := OrgUnit
 * integrity := $integrity
 *
 * ## attributes
 * [cached]
 *
 * ## indexes
 *
 * ## relations
 * User2Group:
 *    relation = composite list,
 *    name = nmLinks,
 *    method = UserGroupId,
 *    nm = User Users
 *
 * ## validations
 *
 * @<
 */

/**
 * User Group.
 * <p>
 * Group of users to define security roles
 */
@TableName(value =/**/"md.usergroups"/**/, // @wurblet < Inject --string $tablename
           mapSchema =/**/false/**/,      // @wurblet < Inject $mapSchema
           prefix =/**/""/**/)            // @wurblet < Inject --string $tablePrefix
@ClassId(/**/1002/**/)                    // @wurblet < Inject $classid
@Singular("UserGroup")
@Plural("UserGroups")
public interface UserGroup extends OrgUnit<UserGroup>, UserGroupPersistence, UserGroupDomain {

  // @wurblet(fold=expanded) modelComment ModelComment

  //<editor-fold defaultstate="expanded" desc="code 'modelComment' generated by wurblet ModelComment">//GEN-BEGIN:modelComment

  /*
   * -------------------------------------------------------------------------------------------------------------------
   *
   * UserGroup is referenced by:
   *
   * Message via orgUnitId as orgUnit [1:1]
   * User via User2Group as NmLinks [N:M]
   *
   *
   * UserGroup is a root entity
   *
   *
   * UserGroup is a composite with the components:
   *    + User2Group via u2g.userGroupId as nmLinks [N:M] to User as users
   *
   *
   * UserGroup is referencing the following entities:
   *
   * User from User2Group via userId [N:M]
   *
   *
   * Components of UserGroup are not deeply referenced
   *
   *
   * UserGroup is not extended
   *
   * -------------------------------------------------------------------------------------------------------------------
   */

  //</editor-fold>//GEN-END:modelComment


  // @wurblet uniqueDomainKey UniqueDomainKey

  //<editor-fold defaultstate="collapsed" desc="code 'uniqueDomainKey' generated by wurblet UniqueDomainKey">//GEN-BEGIN:uniqueDomainKey

  // UserGroup inherits from OrgUnit

  //</editor-fold>//GEN-END:uniqueDomainKey

}
