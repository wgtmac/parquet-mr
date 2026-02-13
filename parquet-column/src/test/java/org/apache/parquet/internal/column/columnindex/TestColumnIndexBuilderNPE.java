/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.parquet.internal.column.columnindex;

import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.INT32;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;

import java.lang.reflect.Method;
import org.apache.parquet.column.statistics.Statistics;
import org.apache.parquet.schema.PrimitiveType;
import org.junit.Test;

public class TestColumnIndexBuilderNPE {

  @Test
  public void testClearNPE() throws Exception {
    PrimitiveType type = new PrimitiveType(REQUIRED, INT32, "test");
    ColumnIndexBuilder builder = ColumnIndexBuilder.getBuilder(type, Integer.MAX_VALUE);

    // Create statistics (dummy)
    Statistics<?> stats = Statistics.getBuilderForReading(type).build();
    // Add stats with null sizeStats -> sets histograms to null in the builder
    builder.add(stats, null);

    // Call private clear() via reflection
    // This is expected to throw InvocationTargetException wrapping NullPointerException if the bug exists
    Method clearMethod = ColumnIndexBuilder.class.getDeclaredMethod("clear");
    clearMethod.setAccessible(true);
    clearMethod.invoke(builder);
  }
}
