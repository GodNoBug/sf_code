package jg.graph;

public interface VertexVisitor<V> {
    boolean visit(V v);
}