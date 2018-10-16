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
 * Represents a sentence which has been analyzed
 */
public class Sentence {

	private final String text;
	private final List<Word> words;

	public Sentence(String text, List<Word> words) {
		super();
		this.text = text;
		this.words = words;
	}

	public int getCharacterCount() {
		return words.stream().map(Word::getCharacters).mapToInt(i -> i).sum();
	}

	public int getComplexWordCount() {
		return (int) words.stream().filter(Word::isComplex).count();
	}

	public int getSyllableCount() {
		return words.stream().map(Word::getSylables).mapToInt(i -> i).sum();
	}

	public String getText() {
		return text;
	}

	public int getWordCount() {
		return words.size();
	}

	public List<Word> getWords() {
		return words;
	}

	@Override
	public String toString() {
		return "Sentence [text=" + text + ", words=" + words + ", getCharacterCount()=" + getCharacterCount()
				+ ", getComplexWordCount()=" + getComplexWordCount() + ", getSyllableCount()=" + getSyllableCount()
				+ ", getWordCount()=" + getWordCount() + "]";
	}

}
