/* Generated By:JJTree: Do not edit this line. OCluster.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.Map;

public class OCluster extends SimpleNode {
  protected String clusterName;
  protected Integer clusterNumber;

  public OCluster(int id) {
    super(id);
  }

  public OCluster(OrientSql p, int id) {
    super(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(OrientSqlVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

  @Override
  public String toString(String prefix) {
    return super.toString(prefix);
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {
    if(clusterName!=null) {
      builder.append("cluster:" + clusterName);
    }else{
      builder.append("cluster:" + clusterNumber);
    }
  }
}
/* JavaCC - OriginalChecksum=d27abf009fe7db482fbcaac9d52ba192 (do not edit this line) */