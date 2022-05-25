/*
 * Generated by PDO wizard.
 */

package ${pdoPackage};

import ${domainPackage}.${domainInterface};
import ${persistencePackage}.${persistenceInterface};
import org.tentackle.pdo.PersistentDomainObject;
import org.tentackle.pdo.Plural;
import org.tentackle.pdo.Singular;
import org.tentackle.session.ClassId;
import org.tentackle.session.TableName;

/*
 * @{
<#if pdoClassId != "">
 * classid   = ${pdoClassId}
</#if>
<#if pdoTablename != "">
 * tablename = ${pdoTablename}
 * mapping   = $model/$tablename.map
<#else>
 * mapping   = $model/$classname.map
</#if>
 * @}
 */

/*
 * @> $mapping
 *
 * # ${shortDescription}
 * name := $classname
<#if pdoTablename != "">
 * table := $tablename
</#if>
<#if pdoClassId != "">
 * id := $classid
</#if>
<#if pdoExtends != "">
 * extends := ${pdoExtends}
</#if>
<#if pdoInheritance != "NONE">
 * inheritance := ${pdoInheritance}
</#if>
 * integrity := $integrity
 *
 * ## attributes
<#if profile == "masterdata">
  <#if pdoExtends == "">
 * [cached, tokenlock]
  <#else>
 * [cached]
  </#if>
</#if>
 *
 * ## indexes
 *
 * ## relations
 *
 * ## validations
 *
 * @<
 */

/**
 * ${shortDescription}.
<#if longDescription != "">
 * <p>
 * ${longDescription}
</#if>
 */
<#if pdoTablename != "">
@TableName(value=/**/"${pdoTablename}"/**/,    // @wurblet < Inject --string $tablename
           mapSchema=/**/false/**/,     // @wurblet < Inject $mapSchema
           prefix=/**/""/**/)       // @wurblet < Inject --string $tablePrefix
</#if>
<#if pdoClassId != "">
@ClassId(/**/${pdoClassId}/**/) // @wurblet < Inject $classid
</#if>
<#if pdoInheritance != "PLAIN">
@Singular("${pdoInterface}")
@Plural("${pdoInterface}s")
</#if>
<#if pdoInheritance != "NONE">
public interface ${pdoInterface}<T extends ${pdoInterface}<T>> extends ${superPdoInterface}<T>, ${persistenceInterface}<T>, ${domainInterface}<T> {
<#else>
public interface ${pdoInterface} extends ${superPdoInterface}<${pdoInterface}>, ${persistenceInterface}, ${domainInterface} {
</#if>

  // @wurblet(fold=expanded) modelComment ModelComment

  // @wurblet uniqueDomainKey UniqueDomainKey

}