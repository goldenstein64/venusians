package venusians.data.board.tiles;

import venusians.data.board.IntPoint;

public class PortSlot extends MapSlot {
	public PortKind kind;
	public int portDirection;

	/**
	 * Creates a new port slot
	 * 
	 * @param position The position of this slot
	 * @param kind The kind of slot this is. Only ResourceCard or AnyPort is allowed
	 * @param portDirection Which direction this port is facing, described by an integer value between 0 and 5.
	 */
	public PortSlot(IntPoint position, PortKind kind, int portDirection) {
		super(position);
		this.kind = kind;
		this.portDirection = portDirection;
	}

	@Override
	public PortSlot clone() {
		return new PortSlot(position, kind, portDirection);
	}
}
