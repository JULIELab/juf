/**
 * This is JUFIT, the Jena UMLS Filter Copyright (C) 2015-2023 JULIE LAB
 * Authors: Johannes Hellrich and Sven Buechel and Christina Lohr
 *
 * This program is free software, see the accompanying LICENSE file for details.
 */
package de.julielab.umlsfilter.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;

import org.junit.Test;

import de.julielab.umlsfilter.delemmatizer.Delemmatizer;
import de.julielab.umlsfilter.delemmatizer.TestPair;

@SuppressWarnings("deprecation")
public class TestRewriteShortFormLongForm {

	TestPair[] pairsShortFormLongForm = {
			new TestPair("HSF (shock transcription factor)",
					"HSF (shock transcription factor)"),
			new TestPair("shock transcription factor (HSF)",
					"shock transcription factor (HSF)"),
			new TestPair("HSF (Heat shock transcription factor)",
					"HSF (Heat shock transcription factor)", "HSF",
					"Heat shock transcription factor"),
			new TestPair("Selective Sorotonin Reuptake Inhabitors (SSRIs)",
					"Selective Sorotonin Reuptake Inhabitors (SSRIs)",
					"Selective Sorotonin Reuptake Inhabitors", "SSRIs"),
			new TestPair(
					"reparación de múltiples desgarros de manguito rotador del hombro durante procedimiento de revisión (procedimiento)",
					"reparación de múltiples desgarros de manguito rotador del hombro durante procedimiento de revisión (procedimiento)"),
			new TestPair("Heat shock transcription factor (HSF)",
					"Heat shock transcription factor (HSF)",
					"Heat shock transcription factor", "HSF"),
			new TestPair("Äpfel Mörder (ÄM)", "Äpfel Mörder (ÄM)",
					"Äpfel Mörder", "ÄM"),
			new TestPair("ÄM Mörder (ÄM)", "ÄM Mörder (ÄM)"),
			new TestPair(
					"C-SSRS Pediatric/Cognitively Impaired Lifetime/Recent - Number of Actual Suicide Attempts (Lifetime)",
					"C-SSRS Pediatric/Cognitively Impaired Lifetime/Recent - Number of Actual Suicide Attempts (Lifetime)"), };

	TestPair[] pairsShortFormLongFormDestructive = {
			new TestPair("HSF (shock transcription factor)",
					"HSF (shock transcription factor)"),
			new TestPair("shock transcription factor (HSF)",
					"shock transcription factor (HSF)"),
			new TestPair("HSF (Heat shock transcription factor)", "HSF",
					"Heat shock transcription factor"),
			new TestPair("Selective Sorotonin Reuptake Inhabitors (SSRIs)",
					"Selective Sorotonin Reuptake Inhabitors", "SSRIs"),
			new TestPair(
					"reparación de múltiples desgarros de manguito rotador del hombro durante procedimiento de revisión (procedimiento)",
					"reparación de múltiples desgarros de manguito rotador del hombro durante procedimiento de revisión (procedimiento)"),
			new TestPair("Heat shock transcription factor (HSF)",
					"Heat shock transcription factor", "HSF"),
			new TestPair("Äpfel Mörder (ÄM)", "Äpfel Mörder", "ÄM"),
			new TestPair("ÄM Mörder (ÄM)", "ÄM Mörder (ÄM)"),
			new TestPair(
					"C-SSRS Pediatric/Cognitively Impaired Lifetime/Recent - Number of Actual Suicide Attempts (Lifetime)",
					"C-SSRS Pediatric/Cognitively Impaired Lifetime/Recent - Number of Actual Suicide Attempts (Lifetime)"), };

	@Test
	public void testAddShortFormLongForm() throws IOException {
		final Rule r = new RewriteShortFormLongForm(false);
		for (final TestPair z : pairsShortFormLongForm) {
			final TermContainer termContainer = new TermContainer(z.input,
					Delemmatizer.LANGUAGE_ENLGLISH, false);
			assertEquals(z.expected, new HashSet<>(
					r.apply(termContainer).getUnsuppressedTermStrings()));
		}
	}

	@Test
	public void testContainsAsToken() {
		assertTrue(RewriteShortFormLongForm.containsAsToken("a and b", "a"));
		assertTrue(RewriteShortFormLongForm.containsAsToken("a and b", "and"));
		assertTrue(RewriteShortFormLongForm.containsAsToken("a and b", "b"));
		assertFalse(RewriteShortFormLongForm.containsAsToken("a and b", "c"));
		assertFalse(RewriteShortFormLongForm.containsAsToken("rr", "r"));
		assertFalse(RewriteShortFormLongForm.containsAsToken("c and b", "a"));
	}

	@Test
	public void testDestructive() throws IOException {
		final Rule r = new RewriteShortFormLongForm(true);
		for (final TestPair z : pairsShortFormLongFormDestructive) {
			final TermContainer termContainer = new TermContainer(z.input,
					Delemmatizer.LANGUAGE_ENLGLISH, false);
			assertEquals(z.expected, new HashSet<>(
					r.apply(termContainer).getUnsuppressedTermStrings()));
		}
	}

	@Test
	public void testGetParenthesesContet() {
		final RewriteShortFormLongForm r = new RewriteShortFormLongForm(false);
		assertEquals(null, r.getParenthesesContent("foo"));
		assertEquals("foo", r.getParenthesesContent("(foo)"));
	}

	@Test
	public void testLongAndShortFormCompatible() {
		assertTrue(
				RewriteShortFormLongForm.longAndShortFormCompatible("rr", "r"));
		assertFalse(RewriteShortFormLongForm.longAndShortFormCompatible("rr",
				"rrr"));
		assertFalse(RewriteShortFormLongForm
				.longAndShortFormCompatible("ro abx", "ro"));
		assertTrue(RewriteShortFormLongForm.longAndShortFormCompatible("ro abx",
				"ra"));
	}
}
