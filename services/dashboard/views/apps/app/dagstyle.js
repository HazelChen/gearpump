/*
 * Licensed under the Apache License, Version 2.0
 * See accompanying LICENSE file.
 */
'use strict';
angular.module('dashboard.apps.appmaster')

  .factory('dagStyle', function () {
    var fontFace = "'lato','helvetica neue','segoe ui',arial";
    return {
      newOptions: function (flags) {
        var levelSeparation = 85;
        return {
          hover: true,
          width: '100%',
          height: ((levelSeparation + 20) * flags.depth) + 'px',
          hierarchicalLayout: {
            layout: 'direction',
            direction: 'UD',
            levelSeparation: levelSeparation
          },
          stabilize: true /* stabilize positions before displaying */,
          freezeForStabilization: true,
          nodes: {
            shape: 'dot',
            fontSize: 13,
            fontFace: fontFace,
            fontStrokeColor: '#fff',
            fontStrokeWidth: 5
          },
          edges: {
            style: 'arrow',
            labelAlignment: 'line-center',
            fontSize: 11,
            fontFace: fontFace,
            widthSelectionMultiplier: 1,
            opacity: 0.75
          },
          tooltip: {
            fontSize: 12,
            fontFace: fontFace,
            fontColor: "#000",
            color: {
              border: "#eee",
              background: "#fff"
            }
          }
        };
      },
      newData: function () {
        return {
          nodes: new vis.DataSet(),
          edges: new vis.DataSet()
        };
      },
      nodeRadiusRange: function () {
        return [2, 16];
      },
      edgeWidthRange: function () {
        return [0.5, 4];
      },
      edgeArrowSizeRange: function() {
        return [0.5, 0.1];
      }
    };
  })
;
