/*
 * Copyright 2005-2014 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.apache.activemq.artemis.tests.compatibility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyRun {

   public static final String SNAPSHOT = "ARTEMIS-SNAPSHOT";
   public static final String ONE_FIVE = "ARTEMIS-155";
   public static final String ONE_FOUR = "ARTEMIS-140";
   public static final String TWO_FOUR = "ARTEMIS-240";
   public static final String HORNETQ_235 = "HORNETQ-235";
   public static final String HORNETQ_247 = "HORNETQ-247";

   public static final String WORD_START = "**SERVER STARTED**";

   public static Binding binding = new Binding();
   public static GroovyShell shell = new GroovyShell(binding);

   // Called with reflection
   public static void doMain(String script, String... arg) throws Throwable {
      int i = 0;
      for (String a : arg) {
         System.out.println("[" + (i++) + "]=" + a);
      }
      System.out.println();

      evaluate(script, "arg", arg);

      System.out.println(WORD_START);
   }

   /**
    * This can be called from the scripts as well.
    *  The scripts will use this method instead of its own groovy method.
    *  As a classloader operation needs to be done here.
    */
   public static void evaluate(String script,
                               String argVariableName,
                               String[] arg) throws URISyntaxException, IOException {
      URL scriptURL = GroovyRun.class.getClassLoader().getResource(script);
      if (scriptURL == null) {
         throw new RuntimeException("cannot find " + script);
      }
      URI scriptURI = scriptURL.toURI();

      binding.setVariable(argVariableName, arg);

      shell.evaluate(scriptURI);
   }

   // Called with reflection
   public static void execute(String script) throws Throwable {
      shell.evaluate(script);
   }

   public static void assertNotNull(Object value) {
      if (value == null) {
         throw new RuntimeException("Null value");
      }
   }

   public static void assertNull(Object value) {
      if (value != null) {
         throw new RuntimeException("Expected Null value");
      }
   }

   public static void assertTrue(boolean  value) {
      if (!value) {
         throw new RuntimeException("Expected true");
      }
   }

   public static void assertEquals(Object value1, Object value2) {
      if (!value1.equals(value2)) {
         throw new RuntimeException(value1 + "!=" + value2);
      }
   }

   public static void assertEquals(int value1, int value2) {
      if (value1 != value2) {
         throw new RuntimeException(value1 + "!=" + value2);
      }
   }

   public static void assertEquals(byte[] value1, byte[] value2) {

      assertEquals(value1.length, value2.length);

      for (int i = 0; i < value1.length; i++) {
         assertEquals(value1[i], value2[i]);
      }
   }


   public static byte getSamplebyte(final long position) {
      return (byte) ('a' + position % ('z' - 'a' + 1));
   }
}

