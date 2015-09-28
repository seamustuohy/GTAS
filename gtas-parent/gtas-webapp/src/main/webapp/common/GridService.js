/**
 * some common methods for controllers using grids
 */
app.service("gridService", function ($http, $q) {
  'use strict';

  return ({
    colorHits: colorHits,
    calculateGridHeight: calculateGridHeight
  });
    
  function calculateGridHeight(numRows) {
     var rowHeight = 30;
     var headerHeight = 30;
     return {
        height: (numRows * rowHeight + 3 * headerHeight) + "px"
     };
  }

  function colorHits(grid, row, col, rowRenderIndex, colRenderIndex) {
    if (grid.getCellValue(row ,col) > 0) {
      return 'red';
    } else {
      return 'green';
    }
  }
});