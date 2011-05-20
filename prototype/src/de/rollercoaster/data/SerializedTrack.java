package de.rollercoaster.data;

import java.io.File;
import de.rollercoaster.data.xml.*;
import de.rollercoaster.mathematics.Curve;
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

    
    private void parseData() {
        List<Pillar> pillars = getPillars();
        
        //TODO: Create curve from pillars
    }
    
    private List<Pillar> getPillars() {
        de.rollercoaster.data.xml.Track trackData = rollercoasterData.getTrack();
        de.rollercoaster.data.xml.PillarList pillarData = trackData.getPillarList();
        
        return pillarData.getPillar();
    }

    public Curve getCurve() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
