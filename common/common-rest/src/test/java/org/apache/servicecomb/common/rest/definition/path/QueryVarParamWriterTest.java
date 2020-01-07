/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.common.rest.definition.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.servicecomb.common.rest.definition.RestParam;
import org.apache.servicecomb.common.rest.definition.path.URLPathBuilder.URLPathStringBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.swagger.models.parameters.QueryParameter;

public class QueryVarParamWriterTest {

  private static QueryVarParamWriter queryVarParamWriterCsv;

  private static QueryVarParamWriter queryVarParamWriterMulti;

  private static QueryVarParamWriter queryVarParamWriterDefault;

  @BeforeClass
  public static void beforeClass() {
    QueryParameter parameter = new QueryParameter();
    parameter.setName("q");
    parameter.setCollectionFormat("csv");
    queryVarParamWriterCsv = new QueryVarParamWriter(
        new RestParam(0, parameter, String[].class));

    parameter = new QueryParameter();
    parameter.setName("q");
    parameter.setCollectionFormat("multi");
    queryVarParamWriterMulti = new QueryVarParamWriter(
        new RestParam(0, parameter, String[].class));

    parameter = new QueryParameter();
    parameter.setName("q");
    queryVarParamWriterDefault = new QueryVarParamWriter(
        new RestParam(0, parameter, String[].class));
  }

  @Test
  public void write() throws Exception {
    URLPathStringBuilder stringBuilder = new URLPathStringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("q", "a");
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a", stringBuilder.build());
  }

  @Test
  public void writeNull() throws Exception {
    URLPathStringBuilder stringBuilder = new URLPathStringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("test", null);
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
  }

  @Test
  public void writeArray() throws Exception {
    URLPathStringBuilder stringBuilder = new URLPathStringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("q", new String[] {"ab", "cd", "ef"});
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab%2Ccd%2Cef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab&q=cd&q=ef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab&q=cd&q=ef", stringBuilder.build());

    // encode space char
    stringBuilder = new URLPathStringBuilder();
    parameters.put("q", new String[] {"a b", " ", "", "ef"});
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a+b%2C+%2C%2Cef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a+b&q=+&q=&q=ef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a+b&q=+&q=&q=ef", stringBuilder.build());

    // pass blank string
    stringBuilder = new URLPathStringBuilder();
    parameters.put("q",  new String[] {""});
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=", stringBuilder.build());

    // pass empty
    stringBuilder = new URLPathStringBuilder();
    parameters.put("q",  new String[] {});
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    // pass null
    parameters.put("q", new String[] {null});
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());

    parameters.put("q", new String[] {null, "ab", null, "cd", null, null, "", null, "ef", null});
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab%2Ccd%2C%2Cef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab&q=cd&q=&q=ef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab&q=cd&q=&q=ef", stringBuilder.build());
  }

  @Test
  public void writeList() throws Exception {
    List<String> queryList = Arrays.asList("ab", "cd", "ef");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("q", queryList);
    URLPathStringBuilder stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab%2Ccd%2Cef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab&q=cd&q=ef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab&q=cd&q=ef", stringBuilder.build());

    // encode space char
    parameters.put("q", Arrays.asList("a b", " ", "", "ef"));
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a+b%2C+%2C%2Cef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a+b&q=+&q=&q=ef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=a+b&q=+&q=&q=ef", stringBuilder.build());

    // pass blank string
    stringBuilder = new URLPathStringBuilder();
    parameters.put("q",  Collections.singletonList(""));
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=", stringBuilder.build());

    // pass empty
    stringBuilder = new URLPathStringBuilder();
    parameters.put("q", new ArrayList<>());
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    // pass null
    parameters.put("q", Collections.singletonList(null));
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("", stringBuilder.build());

    parameters.put("q", Arrays.asList(null, "ab", null, "cd", null, null, "", null, "ef", null));
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterCsv.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab%2Ccd%2C%2Cef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterMulti.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab&q=cd&q=&q=ef", stringBuilder.build());
    stringBuilder = new URLPathStringBuilder();
    queryVarParamWriterDefault.write(stringBuilder, parameters);
    Assert.assertEquals("?q=ab&q=cd&q=&q=ef", stringBuilder.build());
  }
}
