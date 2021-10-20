package venusians.data.board.tiles;

import venusians.data.board.IntPoint;

public class TileSlot extends MapSlot {
	public TileKind kind = ExtraTileKind.OCEAN;
	public int rollValue = -1;

	/**
	 * Creates a new tile slot.
	 * 
	 * @param position The position of this slot
	 * @param kind What kind of slot it is. Only ResourceCard or ExtraTileKind is allowed.
	 * @param rollValue The value needed to gather this resource. -1 if it can't be gathered.
	 */
	public TileSlot(IntPoint position, TileKind kind, int rollValue) {
		super(position);
		this.kind = kind;
		this.rollValue = rollValue;
	}

	@Override
	public TileSlot clone() {
		return new TileSlot(position, kind, rollValue);
	}
}
