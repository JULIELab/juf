/**
 * This is JUFIT, the Jena UMLS Filter Copyright (C) 2015-2023 JULIE LAB
 * Authors: Johannes Hellrich and Sven Buechel and Christina Lohr
 *
 * This program is free software, see the accompanying LICENSE file for details.
 */

package de.julielab.provider;

import com.google.common.base.Joiner;

public class ProvidedTerm {
	private static final Joiner JOINER = Joiner.on("|");
	private final String cui;
	private final String term;
	private final String languageLong;
	private final boolean chemicalOrDrug;
	private final String originalMRCONSO;

	/*
	 * Dumb container
	 */
	public ProvidedTerm(final String cui, final String term,
			final String language, final boolean isChemicalOrDrug,
			final String originalMRCONSO)
	{
		this.cui = cui;
		this.term = term;
		languageLong = language;
		chemicalOrDrug = isChemicalOrDrug;
		this.originalMRCONSO = originalMRCONSO;
	}

	@Override
	public boolean equals(final Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ProvidedTerm other = (ProvidedTerm) obj;
		if (chemicalOrDrug != other.chemicalOrDrug)
			return false;
		if (languageLong == null) {
			if (other.languageLong != null)
				return false;
		} else if (!languageLong.equals(other.languageLong))
			return false;
		if (originalMRCONSO == null) {
			if (other.originalMRCONSO != null)
				return false;
		} else if (!originalMRCONSO.equals(other.originalMRCONSO))
			return false;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}

	public String getCui() {
		return cui;
	}

	public String getLanguageLong() {
		return languageLong;
	}

	public String getOriginalMRCONSO() {
		return originalMRCONSO;
	}

	public String getTerm() {
		return term;
	}

	public String getUpdatedMRCONSO(final String cleanedTerm,
			final String rule) {
		final String[] split = originalMRCONSO.replaceAll("\\|\\|", "| |")
				.replaceAll("\\|$", "| ").split("\\|");
		split[UMLSTermProvider.TERM_INDEX] = cleanedTerm;
		split[UMLSTermProvider.SUI_INDEX] = String.format("%s+%s",
				split[UMLSTermProvider.SUI_INDEX], rule);
		return JOINER.join(split).trim().replaceAll("\\| \\|", "||");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (chemicalOrDrug ? 1231 : 1237);
		result = (prime * result)
				+ ((languageLong == null) ? 0 : languageLong.hashCode());
		result = (prime * result)
				+ ((originalMRCONSO == null) ? 0 : originalMRCONSO.hashCode());
		result = (prime * result) + ((term == null) ? 0 : term.hashCode());
		return result;
	}

	public boolean isChemicalOrDrug() {
		return chemicalOrDrug;
	}

	@Override
	public String toString() {
		return "WrappedTerm [term=" + term + ", language=" + languageLong
				+ ", chemicalOrDrug=" + chemicalOrDrug + ", originalMRCONSO="
				+ originalMRCONSO + "]";
	}

}
