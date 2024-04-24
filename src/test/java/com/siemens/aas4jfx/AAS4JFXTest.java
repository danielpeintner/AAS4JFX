package com.siemens.aas4jfx;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.core.DeserializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.xml.XmlDeserializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class AAS4JFXTest {

    @Test
    public void testOk1() throws IOException, DeserializationException {
        new XmlDeserializer().read(AAS4JFXTest.class.getResource("/AAS_Template_for_AID.aas.xml").openStream());
    }

    @Test
    public void testFail1() {
        try {
            new XmlDeserializer().read(AAS4JFXTest.class.getResource("/AAS_Template_for_AID_Invalid.aas.xml").openStream());
            fail("Expected to fail");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
