package de.rollercoaster.data;

import com.jme3.math.Vector3f;
import java.io.File;
import de.rollercoaster.data.xml.*;
import de.rollercoaster.mathematics.BezierCurve;
import de.rollercoaster.mathematics.Curve;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class SerializedTrack implements Track {
  private final File file;
  private RollerCoaster rollercoasterData;
  private Curve curve;

  public SerializedTrack(File file) {
    this.file = file;
  }

  public void read() {
    try {
      readDataFromFile();
      parseData();
    }
    catch (JAXBException jbe) {
      throw new RuntimeException(jbe);
    }
  }

  private void readDataFromFile() throws JAXBException {
    JAXBContext ctx = JAXBContext.newInstance(new Class[]{RollerCoaster.class});
    Unmarshaller um = ctx.createUnmarshaller();
    this.rollercoasterData = (RollerCoaster) um.unmarshal(file);
  }

  private Vector3f vectorize(double x, double y, double z) {
    return new Vector3f((float) x, (float) z, (float) y);
  }

  private void parseData() {
    List<Pillar> pillars = getPillars();
    List<Vector3f> points = new ArrayList<Vector3f>();
    List<Vector3f> orientations = new ArrayList<Vector3f>();

    for (Pillar pillar : pillars) {
      Vector3f position = vectorize(pillar.getPosX(), pillar.getPosY(), pillar.getPosZ());
      Vector3f up = vectorize(pillar.getYawX(), pillar.getYawY(), pillar.getYawZ());

      points.add(position);
      orientations.add(up);
    }

    this.curve = new BezierCurve(points, orientations);
  }

  private List<Pillar> getPillars() {
    de.rollercoaster.data.xml.Track trackData = rollercoasterData.getTrack();
    de.rollercoaster.data.xml.PillarList pillarData = trackData.getPillarList();

    return pillarData.getPillar();
  }

  @Override
  public Curve getCurve() {
    return this.curve;
  }
}
