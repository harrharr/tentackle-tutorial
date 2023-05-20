/*
 * Generated by PDO wizard.
 */

package ${persistencePackage};

import ${pdoPackage}.${pdoInterface};
import java.util.Collection;
import java.util.List;
import org.tentackle.bind.Bindable;
import org.tentackle.common.Binary;
import org.tentackle.common.BMoney;
import org.tentackle.common.DMoney;
import org.tentackle.common.Date;
import org.tentackle.common.Time;
import org.tentackle.common.Timestamp;
import org.tentackle.misc.TrackedList;
import org.tentackle.pdo.DomainContext;
import org.tentackle.pdo.DomainKey;
import org.tentackle.pdo.PersistentObject;
import org.tentackle.session.Persistent;


/**
 * Persistence interface for ${pdoInterface}.
 */
<#if pdoInheritance != "NONE" && pdoInheritance != "EMBEDDED">
public interface ${persistenceInterface}<T extends ${pdoInterface}<T>> extends ${superPersistenceInterface}<T> {
<#else>
public interface ${persistenceInterface} extends ${superPersistenceInterface}<${pdoInterface}> {
</#if>

  // @wurblet attributeNames AttributeNames

  // @wurblet columnLengths ColumnLengths

  // @wurblet methods Methods

  // @wurblet relations Relations

}
