package de.rollercoaster.data;

import com.jme3.math.Vector3f;
import java.io.File;
import de.rollercoaster.data.xml.*;
import de.rollercoaster.mathematics.Curve;
import de.rollercoaster.mathematics.CurvePoint;
import de.rollercoaster.mathematics.DummyCurve;
import de.rollercoaster.mathematics.DummyCurvePoint;
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
        } catch (JAXBException jbe) {
            throw new RuntimeException(jbe);
        }
    }

    private void readDataFromFile() throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(new Class[]{RollerCoaster.class});
        Unmarshaller um = ctx.createUnmarshaller();
        this.rollercoasterData = (RollerCoaster) um.unmarshal(file);
    }

    
    private Vector3f vectorize(double x, double y, double z) {
        return new Vector3f((float)x, (float)y, (float) z);
    }
    
    private void parseData() {
        List<Pillar> pillars = getPillars();
        List<CurvePoint> points = new ArrayList<CurvePoint>(); 
        
        for (int i=0; i < pillars.size(); i++) {
            Pillar current = pillars.get(i);
            Pillar next = i < pillars.size() - 1 ? pillars.get(i+1) : pillars.get(0);
                   
            Vector3f position = vectorize(current.getPosX(), current.getPosY(), current.getPosZ());
            Vector3f nextPosition = vectorize(next.getPosX(), next.getPosY(), next.getPosZ());
            
           Vector3f direction = nextPosition.subtract(position).normalize(); 
           Vector3f up = vectorize(current.getYawX(), current.getYawY(), current.getYawZ());
           
           Vector3f left = direction.cross(up).normalize();
       
           points.add(new DummyCurvePoint(position, direction, left, up));
        }
        
        this.curve = new DummyCurve(points);
    }
    
    private List<Pillar> getPillars() {
        de.rollercoaster.data.xml.Track trackData = rollercoasterData.getTrack();
        de.rollercoaster.data.xml.PillarList pillarData = trackData.getPillarList();
        
        return pillarData.getPillar();
    }
    
    public Curve getCurve() {
        return this.curve;
    }
}
