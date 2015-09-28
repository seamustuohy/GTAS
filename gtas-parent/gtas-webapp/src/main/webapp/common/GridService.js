/**
 * some common methods for controllers using grids
 */
app.service("gridService", function ($http, $q) {
  'use strict';

  return ({
    colorHits: colorHits,
    calculateGridHeight: calculateGridHeight
  });
  
  /**
   * Take the number of rows in the grid and calculate the
   * correct 'height' style to show all of the data at once.
   * We use this as part of auto-resizing grids.
   */
  function calculateGridHeight(numRows) {
     var MIN_NUM_ROWS = 10;
     
     var rowHeight = 30;
     var headerHeight = 30;
     var n = numRows >= MIN_NUM_ROWS ? numRows : MIN_NUM_ROWS;
     return {
        height: (n * rowHeight + 3 * headerHeight) + "px"
     };
  }

  /**
   * @return the cell color for the rule hit and watch list hit
   * columns.  cellValue can either be a count or a boolean value.
   */
  function colorHits(grid, row, col, rowRenderIndex, colRenderIndex) {
    if (grid.getCellValue(row ,col) > 0) {
      return 'red';
    }
  }
});