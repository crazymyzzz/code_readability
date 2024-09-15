@@ -268,7 +268,7 @@ public class BpmnAutoLayout {
     boundaryEdgeStyle.put(mxConstants.STYLE_EXIT_Y, 1.0);
     boundaryEdgeStyle.put(mxConstants.STYLE_ENTRY_X, 0.5);
     boundaryEdgeStyle.put(mxConstants.STYLE_ENTRY_Y, 1.0);
-    boundaryEdgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.orthConnector);
+    boundaryEdgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.OrthConnector);
     graph.getStylesheet().putCellStyle(STYLE_BOUNDARY_SEQUENCEFLOW, boundaryEdgeStyle);
 
     for (SequenceFlow sequenceFlow : sequenceFlows.values()) {
@@ -306,7 +306,7 @@ public class BpmnAutoLayout {
     boundaryEdgeStyle.put(mxConstants.STYLE_EXIT_Y, 1.0);
     boundaryEdgeStyle.put(mxConstants.STYLE_ENTRY_X, 0.5);
     boundaryEdgeStyle.put(mxConstants.STYLE_ENTRY_Y, 1.0);
-    boundaryEdgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.orthConnector);
+    boundaryEdgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.OrthConnector);
     graph.getStylesheet().putCellStyle(STYLE_BOUNDARY_SEQUENCEFLOW, boundaryEdgeStyle);
 
     for (Association association : associations.values()) {
