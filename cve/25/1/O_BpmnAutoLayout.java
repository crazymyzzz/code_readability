
/**
 * Auto layouts a {@link BpmnModel}.
 * 

 */
public class BpmnAutoLayout {

  

  protected void handleSequenceFlow() {

    Hashtable<String, Object> edgeStyle = new Hashtable<String, Object>();
    edgeStyle.put(mxConstants.STYLE_ORTHOGONAL, true);
    edgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);
    edgeStyle.put(mxConstants.STYLE_ENTRY_X, 0.0);
    edgeStyle.put(mxConstants.STYLE_ENTRY_Y, 0.5);
    graph.getStylesheet().putCellStyle(STYLE_SEQUENCEFLOW, edgeStyle);

    Hashtable<String, Object> boundaryEdgeStyle = new Hashtable<String, Object>();
    boundaryEdgeStyle.put(mxConstants.STYLE_EXIT_X, 0.5);
    boundaryEdgeStyle.put(mxConstants.STYLE_EXIT_Y, 1.0);
    boundaryEdgeStyle.put(mxConstants.STYLE_ENTRY_X, 0.5);
    boundaryEdgeStyle.put(mxConstants.STYLE_ENTRY_Y, 1.0);
    boundaryEdgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.orthConnector);
    graph.getStylesheet().putCellStyle(STYLE_BOUNDARY_SEQUENCEFLOW, boundaryEdgeStyle);

    for (SequenceFlow sequenceFlow : sequenceFlows.values()) {
      Object sourceVertex = generatedVertices.get(sequenceFlow.getSourceRef());
      Object targetVertex = generatedVertices.get(sequenceFlow.getTargetRef());

      String style = null;

      if (handledFlowElements.get(sequenceFlow.getSourceRef()) instanceof BoundaryEvent) {
        // Sequence flow out of boundary events are handled in a
        // different way,
        // to make them visually appealing for the eye of the dear end
        // user.
        style = STYLE_BOUNDARY_SEQUENCEFLOW;
      } else {
        style = STYLE_SEQUENCEFLOW;
      }

      Object sequenceFlowEdge = graph.insertEdge(cellParent, sequenceFlow.getId(), "", sourceVertex, targetVertex, style);
      generatedSequenceFlowEdges.put(sequenceFlow.getId(), sequenceFlowEdge);
    }
  }

  protected void handleAssociations() {

    Hashtable<String, Object> edgeStyle = new Hashtable<String, Object>();
    edgeStyle.put(mxConstants.STYLE_ORTHOGONAL, true);
    edgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);
    edgeStyle.put(mxConstants.STYLE_ENTRY_X, 0.0);
    edgeStyle.put(mxConstants.STYLE_ENTRY_Y, 0.5);
    graph.getStylesheet().putCellStyle(STYLE_SEQUENCEFLOW, edgeStyle);

    Hashtable<String, Object> boundaryEdgeStyle = new Hashtable<String, Object>();
    boundaryEdgeStyle.put(mxConstants.STYLE_EXIT_X, 0.5);
    boundaryEdgeStyle.put(mxConstants.STYLE_EXIT_Y, 1.0);
    boundaryEdgeStyle.put(mxConstants.STYLE_ENTRY_X, 0.5);
    boundaryEdgeStyle.put(mxConstants.STYLE_ENTRY_Y, 1.0);
    boundaryEdgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.orthConnector);
    graph.getStylesheet().putCellStyle(STYLE_BOUNDARY_SEQUENCEFLOW, boundaryEdgeStyle);

    for (Association association : associations.values()) {
      Object sourceVertex = generatedVertices.get(association.getSourceRef());
      Object targetVertex = generatedVertices.get(association.getTargetRef());

      String style = null;

      if (handledFlowElements.get(association.getSourceRef()) instanceof BoundaryEvent) {
        // Sequence flow out of boundary events are handled in a different way,
        // to make them visually appealing for the eye of the dear end user.
        style = STYLE_BOUNDARY_SEQUENCEFLOW;
      } else {
        style = STYLE_SEQUENCEFLOW;
      }

      Object associationEdge = graph.insertEdge(cellParent, association.getId(), "", sourceVertex, targetVertex, style);
      generatedAssociationEdges.put(association.getId(), associationEdge);
    }
  }

  
}
