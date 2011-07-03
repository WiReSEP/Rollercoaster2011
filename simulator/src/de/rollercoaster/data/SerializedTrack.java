package de.rollercoaster.data;

import java.io.File;
import de.rollercoaster.data.xml.*;
import de.rollercoaster.mathematics.BezierCurve;
import de.rollercoaster.mathematics.Curve;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.rollercoaster.mathematics.Vector3d;

public class SerializedTrack implements Track {

    private final File file;
    private RollerCoaster rollercoasterData;
    private Curve curve;

    public SerializedTrack(File file) {
        this.file = file;

        read();
    }

    private void read() {
        try {
            readDataFromFile();
            parseData();
        } catch (JAXBException jbe) {
            throw new RuntimeException(jbe);
        }
    }

    private void readDataFromFile() throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(new Class[]{RollerCoaster.class});
        Unmarshaller um = ctx.createUnmarshaller();
        this.rollercoasterData = (RollerCoaster) um.unmarshal(file);
    }

    private void parseData() {
        List<Pillar> pillars = getPillars();
        List<Vector3d> points = new ArrayList<Vector3d>();
        List<Double> orientations = new ArrayList<Double>();

        for (Pillar pillar : pillars) {
            Vector3d position = new Vector3d(pillar.getPosX(), pillar.getPosZ(), pillar.getPosY());
            double yawAngle = pillar.getYawAngle();

            points.add(position);
            orientations.add(Math.PI / 180 * yawAngle);
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
