/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.cms.readability;

/**
 * Represents a word which has been analyzed
 */
public class Word {

    private final String original;
    private final String normalized;
    private final int sylables;
    private final boolean complex;
    private final int characters;

    public Word(String original, String normalized, int sylables, boolean complex) {
        super();
        this.original = original;
        this.characters = original.trim().length();
        this.normalized = normalized;
        this.sylables = sylables;
        this.complex = complex;
    }

    public int getCharacters() {
        return characters;
    }

    public String getNormalized() {
        return normalized;
    }

    public String getOriginal() {
        return original;
    }

    public int getSylables() {
        return sylables;
    }

    public boolean isComplex() {
        return complex;
    }

    @Override
    public String toString() {
        return "Word [original=" + original + ", normalized=" + normalized + ", sylables=" + sylables + ", complex="
                + complex + ", characters=" + characters + "]";
    }

}
