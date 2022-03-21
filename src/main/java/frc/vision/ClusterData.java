package frc.vision;

public class ClusterData
{
  float datum;
  int label;
  float center;

  ClusterData(float datum, int label, float center)
  {
    this.datum = datum;
    this.label = label;
    this.center = center;
  }

  public String toString()
  {
   return "[Cluster] datum " + datum + ", label " + label + ", center " + center;
  }
}  
