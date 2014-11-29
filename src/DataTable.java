import java.util.ArrayList;


public class DataTable {
	ArrayList<ArrayList<Integer>> table;
	ArrayList<String> header;
	int numRows;
	int numColumns;

	public DataTable()
	{
		 table = new ArrayList<ArrayList<Integer>>();
		 header = new ArrayList<String>();
		 numRows = 0;
	     numColumns = 0;
	}
	

	void addColumnValue(int columnHeader)
	{
		expandColumn(numColumns++, columnHeader);
	}
	
	void addRowValue(int... val)
	{
		//expandRow(numRows+1);//, columnHeader);
		for(int i = 0; i < val.length; ++i)
		{
			table.add(new ArrayList<Integer>());
			//table.set(numRows, new ArrayList<Integer>());
			table.get(numRows).add(val[i]);
		}
		numRows++;

	}
	
	void insertValue(int rowNumber, int columnNumber, int value)
	{
		if(rowNumber >= numRows || columnNumber >= numColumns)
		{
			expandRow(rowNumber);
			expandColumn(columnNumber);
			table.get(rowNumber).set(columnNumber, value);	
		}
		else
		{
			table.get(rowNumber).set(columnNumber, value);	
		}
	}
	
	
	void expandRow(int newRowSize)
	{
		int sizeBefore = numRows;
		//if(sizeBefore == 0) 
		if(newRowSize > numRows)
		{
			numRows = newRowSize;
		}
		if(newRowSize == numRows)
		{
			numRows = newRowSize++;
		}
		for(int i = sizeBefore; i < newRowSize; i++)
		{
			table.add(new ArrayList<Integer>());
			expandColumnOfRow(table.get(i), numColumns);
		}

	
	}
	
	int getValue(int rowNumber, int columnNumber)
	{
		if(!(rowNumber < numRows && columnNumber < numColumns))
		{
			return -1;
		}
		return (int) table.get(rowNumber).get(columnNumber);
	}
	
	ArrayList<Integer> getRow(int rowNumber)
	{
		if(!(rowNumber < numRows ))
		{
			return null;
		}
		return table.get(rowNumber);
	}
	
	ArrayList<Integer> getColumn(int columnNumber)
	{
		if(!(columnNumber < numColumns ))
		{
			return null;
		}
		ArrayList<Integer> column = new ArrayList<Integer>();
		for(ArrayList<Integer> row  : table){			
				column.add(row.get(columnNumber));			
		}
		return column;
	}
	
	void expandColumnOfRow(ArrayList<Integer> row, int newColSize)
	{
		int sizeBefore = numColumns;
		if(newColSize > numColumns)
		{
			numColumns = newColSize;
		}
		if(newColSize == numColumns)
		{
			numColumns = newColSize++;
		}
		for(int i = sizeBefore; i < numColumns; i++)
		{
			row.add(0);
		}
	}
	
	void expandColumn(int newColSize)
	{
		
		for(ArrayList<Integer> row  : table){
			expandColumnOfRow(row,newColSize);
		}	
	}
	
	void expandColumn(int newColSize, int columnHeader)
	{
		int sizeBefore = numColumns;
		if(newColSize > numColumns)
		{
			numColumns = newColSize;
		}
		if(newColSize == numColumns)
		{
			numColumns = newColSize++;
		}
		int currRowNum = 0;
		for(ArrayList<Integer> row  : table){
			for(int i = sizeBefore; i < numColumns; i++)
			{
				if(currRowNum == 0)
				{
					row.add(columnHeader);
				}
				else
				{
					row.add(0);
				}
			}
			currRowNum++;
		}		
	}
	

}
