/*
 *
 *  *  Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  *
 *  * For more information: http://orientdb.com
 *
 */

package com.orientechnologies.orient.core.storage.impl.local.paginated.wal;

import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.cas.OEmptyWALRecord;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.cas.OWriteableWALRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Lomakin (a.lomakin-at-orientdb.com)
 * @since 25.04.13
 */
public class OWALRecordsFactory {
  private final Map<Byte, Class> idToTypeMap = new HashMap<>();
  private final Map<Class, Byte> typeToIdMap = new HashMap<>();

  public static final OWALRecordsFactory INSTANCE = new OWALRecordsFactory();

  public byte[] toStream(OWriteableWALRecord walRecord) {
    int contentSize = walRecord.serializedSize() + 1;
    byte[] content = new byte[contentSize];

    if (walRecord instanceof OUpdatePageRecord)
      content[0] = 0;
    else if (walRecord instanceof OFuzzyCheckpointStartRecord)
      content[0] = 1;
    else if (walRecord instanceof OFuzzyCheckpointEndRecord)
      content[0] = 2;
    else if (walRecord instanceof OFullCheckpointStartRecord)
      content[0] = 4;
    else if (walRecord instanceof OCheckpointEndRecord)
      content[0] = 5;
    else if (walRecord instanceof OAtomicUnitStartRecord)
      content[0] = 8;
    else if (walRecord instanceof OAtomicUnitEndRecord)
      content[0] = 9;
    else if (walRecord instanceof OFileCreatedWALRecord)
      content[0] = 10;
    else if (walRecord instanceof ONonTxOperationPerformedWALRecord)
      content[0] = 11;
    else if (walRecord instanceof OFileDeletedWALRecord)
      content[0] = 12;
    else if (walRecord instanceof OFileTruncatedWALRecord)
      content[0] = 13;
    else if (walRecord instanceof OEmptyWALRecord)
      content[0] = 14;
    else if (typeToIdMap.containsKey(walRecord.getClass())) {
      content[0] = typeToIdMap.get(walRecord.getClass());
    } else
      throw new IllegalArgumentException(walRecord.getClass().getName() + " class cannot be serialized.");

    walRecord.toStream(content, 1);

    return content;
  }

  public OWriteableWALRecord fromStream(byte[] content) {
    OWriteableWALRecord walRecord;
    switch (content[0]) {
    case 0:
      walRecord = new OUpdatePageRecord();
      break;
    case 1:
      walRecord = new OFuzzyCheckpointStartRecord();
      break;
    case 2:
      walRecord = new OFuzzyCheckpointEndRecord();
      break;
    case 4:
      walRecord = new OFullCheckpointStartRecord();
      break;
    case 5:
      walRecord = new OCheckpointEndRecord();
      break;
    case 8:
      walRecord = new OAtomicUnitStartRecord();
      break;
    case 9:
      walRecord = new OAtomicUnitEndRecord();
      break;
    case 10:
      walRecord = new OFileCreatedWALRecord();
      break;
    case 11:
      walRecord = new ONonTxOperationPerformedWALRecord();
      break;
    case 12:
      walRecord = new OFileDeletedWALRecord();
      break;
    case 13:
      walRecord = new OFileTruncatedWALRecord();
      break;
    case 14:
      walRecord = new OEmptyWALRecord();
      break;
    default:
      if (idToTypeMap.containsKey(content[0]))
        try {
          walRecord = (OWriteableWALRecord) idToTypeMap.get(content[0]).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
          throw new IllegalStateException("Cannot deserialize passed in record", e);
        }
      else
        throw new IllegalStateException("Cannot deserialize passed in wal record.");
    }

    walRecord.fromStream(content, 1);

    return walRecord;
  }

  public void registerNewRecord(byte id, Class<? extends OWriteableWALRecord> type) {
    typeToIdMap.put(type, id);
    idToTypeMap.put(id, type);
  }
}
