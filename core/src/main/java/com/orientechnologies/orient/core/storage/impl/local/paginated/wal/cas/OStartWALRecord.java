package com.orientechnologies.orient.core.storage.impl.local.paginated.wal.cas;

import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OLogSequenceNumber;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OWALRecord;

import java.util.concurrent.atomic.AtomicReference;

public class OStartWALRecord implements OWALRecord {
  private volatile OLogSequenceNumber lsn;

  @Override
  public OLogSequenceNumber getLsn() {
    return lsn;
  }

  @Override
  public void setLsn(OLogSequenceNumber lsn) {
    this.lsn = lsn;
  }

  @Override
  public boolean casLSN(OLogSequenceNumber currentLSN, OLogSequenceNumber newLSN) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDistance(int distance) {
  }

  @Override
  public void setDiskSize(int diskSize) {
  }

  @Override
  public int getDistance() {
    return 0;
  }

  @Override
  public int getDiskSize() {
    return OCASWALPage.RECORDS_OFFSET;
  }
}
