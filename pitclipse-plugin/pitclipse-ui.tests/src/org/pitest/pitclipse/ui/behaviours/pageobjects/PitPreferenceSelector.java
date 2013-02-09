package org.pitest.pitclipse.ui.behaviours.pageobjects;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.pitest.pitclipse.core.PitCoreActivator;
import org.pitest.pitclipse.core.PitExecutionMode;

public class PitPreferenceSelector {

	private final SWTWorkbenchBot bot;

	public PitPreferenceSelector(SWTWorkbenchBot bot) {
		this.bot = bot;
	}

	public void setPitExecutionMode(PitExecutionMode mode) {
		activatePreferenceShell();
		expandPitPreferences();
		selectExecutionMode(mode);
		close();
	}

	private void selectExecutionMode(PitExecutionMode mode) {
		// The workaround for Eclipse bug 344484.didn't seem to work here
		// so for now we'll set the property directly. We have assertions
		// on reading back the property which should suffice
		PitCoreActivator.getDefault().setExecutionMode(mode);
	}

	private void close() {
		bot.button("OK").click();
	}

	private void expandPitPreferences() {
		bot.tree().getTreeItem("Pitest").select().expand();
	}

	private void activatePreferenceShell() {
		SWTBotShell shell = bot.shell("Preferences");
		shell.activate();
	}

	public PitExecutionMode getPitExecutionMode() {
		activatePreferenceShell();
		try {
			expandPitPreferences();
			return getActiveExecutionMode();
		} finally {
			close();
		}
	}

	private PitExecutionMode getActiveExecutionMode() {
		for (PitExecutionMode mode : PitExecutionMode.values()) {
			if (bot.radio(mode.getLabel()).isSelected()) {
				return mode;
			}
		}
		return null;
	}

	public boolean isPitRunInParallel() {
		activatePreferenceShell();
		try {
			expandPitPreferences();
			return bot.checkBox("Mutation tests run in parallel").isChecked();
		} finally {
			close();
		}
	}

	public boolean isIncrementalAnalysisEnabled() {
		activatePreferenceShell();
		try {
			expandPitPreferences();
			return bot.checkBox("Use incremental analaysis").isChecked();
		} finally {
			close();
		}
	}

}
