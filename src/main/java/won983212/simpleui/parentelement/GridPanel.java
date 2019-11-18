package won983212.simpleui.parentelement;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

public class GridPanel extends UIPanel {
	private ArrayList<CellLength> columns = new ArrayList<CellLength>();
	private ArrayList<CellLength> rows = new ArrayList<CellLength>();
	
	@Override
	public void add(UIObject obj) {
		super.add(obj);
	}
	
	public void addColumn(LengthDefinition column) {
		columns.add(new CellLength(column, 0));
	}
	
	public void addEmptyColumn() {
		columns.add(new CellLength(new LengthDefinition(LengthType.ALLOCATED, 1), 0));
	}
	
	public void addRow(LengthDefinition row) {
		rows.add(new CellLength(row, 0));
	}
	
	public void addEmptyRow() {
		rows.add(new CellLength(new LengthDefinition(LengthType.ALLOCATED, 1), 0));
	}
	
	private void measureMaxSize() {
		for(CellLength c : columns)
			c.maxDesiredLength = 0;
		for(CellLength r : rows)
			r.maxDesiredLength = 0;
		for(UIObject obj : uiList) {
			Dimension desired = obj.getLayoutMinSize();
			CellLength column = columns.get(obj.layoutX);
			column.maxDesiredLength = Math.max(column.maxDesiredLength, desired.width / obj.layoutXSpan);
			CellLength row = rows.get(obj.layoutY);
			row.maxDesiredLength = Math.max(row.maxDesiredLength, desired.height / obj.layoutYSpan);
		}
	}
	
	private int[] calculateActualLength(int totalLen, ArrayList<CellLength> cellLen) {
		int[] arr = new int[cellLen.size()];
		double scale = 0;
		for(int i=0;i<cellLen.size();i++) {
			CellLength cell = cellLen.get(i);
			if(cell.lengthDef.type == LengthType.FIXED)
				arr[i] = (int) cell.lengthDef.argument;
			else if(cell.lengthDef.type == LengthType.AUTO)
				arr[i] = cell.maxDesiredLength;
			if(cell.lengthDef.type != LengthType.ALLOCATED)
				totalLen -= arr[i];
			else
				scale += cell.lengthDef.argument; 
		}
		scale = totalLen / scale;
		for(int i=0;i<cellLen.size();i++) {
			CellLength cell = cellLen.get(i);
			if(cell.lengthDef.type == LengthType.ALLOCATED)
				arr[i] = (int) (scale * cell.lengthDef.argument);
		}
		return arr;
	}
	
	private double calculateMinLength(ArrayList<CellLength> cellLen) {
		double length = 0;
		double cellSize = 0;
		double totalCellScales = 0;
		for(int i=0;i<cellLen.size();i++) {
			CellLength cell = cellLen.get(i);
			if(cell.lengthDef.type == LengthType.FIXED)
				length += cell.lengthDef.argument;
			else if(cell.lengthDef.type == LengthType.AUTO)
				length += cell.maxDesiredLength;
			else if(cell.lengthDef.type == LengthType.ALLOCATED) {
				cellSize = Math.max(cellSize, cell.maxDesiredLength / cell.lengthDef.argument);
				totalCellScales += cell.lengthDef.argument;
			}
		}
		length += totalCellScales * cellSize;
		return length;
	}
	
	@Override
	public Dimension measureMinSize() {
		Dimension size = new Dimension();
		measureMaxSize();
		size.width = (int) calculateMinLength(columns);
		size.height = (int) calculateMinLength(rows);
		return size;
	}

	@Override
	public void layout() {
		measureMaxSize();
		Rectangle rect = getRelativeBounds();
		int[] widths = calculateActualLength(rect.width, columns);
		int[] heights = calculateActualLength(rect.height, rows);
		int[] stackedX = new int[widths.length+1];
		int[] stackedY = new int[heights.length+1];
		
		// calculate stackedX or Y
		stackedX[0] = 0;
		stackedY[0] = 0;
		for(int i=1;i<stackedX.length;i++)
			stackedX[i] = stackedX[i-1]+widths[i-1];
		for(int i=1;i<stackedY.length;i++)
			stackedY[i] = stackedY[i-1]+heights[i-1];
		
		for(UIObject obj : uiList) {
			Rectangle available = new Rectangle(stackedX[obj.layoutX], stackedY[obj.layoutY], 0, 0);
			available.width = stackedX[obj.layoutX + obj.layoutXSpan] - available.x;
			available.height = stackedY[obj.layoutY + obj.layoutYSpan] - available.y;
			obj.arrange(available);
		}
	}
	
	public static enum LengthType {
		FIXED,
		AUTO,
		ALLOCATED
	}
	
	public static class LengthDefinition {
		public double argument;
		public LengthType type;
		
		public LengthDefinition(LengthType type, double arg) {
			this.type = type;
			this.argument = arg;
		}
	}
	
	public static class CellLength {
		public LengthDefinition lengthDef;
		public int maxDesiredLength;
		
		public CellLength(LengthDefinition lenDef, int desired) {
			this.lengthDef = lenDef;
			this.maxDesiredLength = desired;
		}
	}
}
