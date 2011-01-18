package de.uniba.wiai.lspi.chord.service.impl;

public interface EntriesEventListener{
	public final int ENTRY_ADDED = 0;
	public final int ENTRY_ALL_ADDED = 1;
	public final int ENTRY_REMOVED = 2;
	public final int ENTRY_ALL_REMOVED = 3;
	public final int ENTRY_OTHER = 4;
	public void newEvent(int event, Object obj);
}