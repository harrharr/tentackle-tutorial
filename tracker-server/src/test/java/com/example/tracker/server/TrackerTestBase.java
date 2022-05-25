/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.server;

import com.example.tracker.common.Constants;
import com.example.tracker.pdo.td.Message;

import org.tentackle.ns.pdo.NumberPool;
import org.tentackle.ns.pdo.NumberRange;
import org.tentackle.pdo.testng.AbstractPdoTest;
import org.tentackle.session.Session;


public abstract class TrackerTestBase extends AbstractPdoTest {

  @Override
  protected void populateDatabase() {
    NumberPool pool = on(NumberPool.class);
    pool.setRealm(Constants.POOL_REALM);
    pool.setName(Message.POOL_NAME);
    pool.setDescription("pool for message numbers");
    pool.setOnline(true);
    NumberRange range = on(NumberRange.class);
    range.setBegin(1000000);
    range.setEnd  (9999999);
    pool.getNumberRangeList().add(range);
    pool.save();
  }

}