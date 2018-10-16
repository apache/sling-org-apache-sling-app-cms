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

import java.util.List;

/**
 * Represents a block of text which has been analyzed
 */
public class Text {

	private final String original;
	private final List<Sentence> sentences;

	public Text(String original, List<Sentence> sentences) {
		super();
		this.original = original;
		this.sentences = sentences;
	}

	public int getCharacterCount() {
		return sentences.stream().map(Sentence::getCharacterCount).mapToInt(i -> i).sum();
	}

	public int getComplexWordCount() {
		return sentences.stream().map(Sentence::getComplexWordCount).mapToInt(i -> i).sum();
	}

	public String getOriginal() {
		return original;
	}

	public List<Sentence> getSentences() {
		return sentences;
	}

	public int getSyllableCount() {
		return sentences.stream().map(Sentence::getSyllableCount).mapToInt(i -> i).sum();
	}

	public int getWordCount() {
		return sentences.stream().map(Sentence::getWordCount).mapToInt(i -> i).sum();
	}

	@Override
	public String toString() {
		return "Text [original=" + original + ", sentences=" + sentences + ", getCharacterCount()="
				+ getCharacterCount() + ", getComplexWordCount()=" + getComplexWordCount() + ", getSyllableCount()="
				+ getSyllableCount() + ", getWordCount()=" + getWordCount() + "]";
	}

}
