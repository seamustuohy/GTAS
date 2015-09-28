app.service("gridService", function ($http, $q) {
  'use strict';

  return ({
    colorHits: colorHits
  });
    
  function colorHits(grid, row, col, rowRenderIndex, colRenderIndex) {
    if (grid.getCellValue(row ,col) > 0) {
      return 'red';
    } else {
      return 'green';
    }
  }
});